package net.lenni0451.commons.httpclient.executor.extra

import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutgoingContent
import io.ktor.http.renderSetCookieHeader
import io.ktor.util.toMap
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.lenni0451.commons.httpclient.HttpClient
import net.lenni0451.commons.httpclient.HttpResponse
import net.lenni0451.commons.httpclient.constants.HttpHeaders as CommonsHttpHeaders
import net.lenni0451.commons.httpclient.content.HttpContent
import net.lenni0451.commons.httpclient.executor.RequestExecutor
import net.lenni0451.commons.httpclient.proxy.ProxyType
import net.lenni0451.commons.httpclient.proxy.ThreadedProxyAuthenticator
import net.lenni0451.commons.httpclient.requests.HttpContentRequest
import net.lenni0451.commons.httpclient.requests.HttpRequest
import net.lenni0451.commons.httpclient.utils.URLWrapper
import net.lenni0451.commons.httpclient.utils.stream.CloseListenerInputStream
import java.io.IOException
import java.io.InputStream
import java.io.UncheckedIOException
import java.net.CookieManager
import java.net.URI
import java.net.URL
import java.util.Base64
import java.util.concurrent.SynchronousQueue
import io.ktor.client.HttpClient as KtorClient

/**
 * Executor that uses Ktor to execute requests.<br>
 * Make sure to add the dependency (including a client engine) to your project before using this executor.<br>
 * By default the engine is discovered from the classpath by Ktor itself; there is no way to detect a missing engine
 * up front, so requests fail with an {@link IOException} if no engine is available.<br>
 * <br>
 * The used Ktor client can be customized by passing an engine factory, a user provided client and/or a config block to the constructor.<br>
 * Use it together with [HttpClient(Function)][HttpClient] (e.g. `HttpClient { KtorExecutor(it, engineFactory = CIO) }`).<br>
 * <br>
 * Limitations:
 * <ul>
 *     <li>Ignoring invalid SSL certificates is engine specific and therefore not supported</li>
 *     <li>Proxy support depends on the used engine; a proxy can not be applied to a user provided client (an exception is thrown)</li>
 *     <li>The read timeout is mapped to the socket timeout, which some engines (e.g. ktor-client-java) do not honor</li>
 *     <li>SOCKS proxy authentication may not work depending on the engine, as it authenticates on the calling thread</li>
 * </ul>
 *
 * @param client        The http client
 * @param engineFactory The engine factory to use instead of the classpath discovery
 * @param baseClient    A user provided client which is used as the base for all requests (see [io.ktor.client.HttpClient.config]).
 *                      It takes precedence over the engine factory. Its engine is shared and will not be closed by this executor.
 * @param clientConfig  A config block that is applied to the client config after the default configuration
 */
class KtorExecutor @JvmOverloads constructor(
    client: HttpClient,
    private val engineFactory: HttpClientEngineFactory<*>? = null,
    private val baseClient: KtorClient? = null,
    private val clientConfig: (HttpClientConfig<*>.() -> Unit)? = null,
) : RequestExecutor(client) {

    override fun execute(request: HttpRequest): HttpResponse {
        if (this.isIgnoreInvalidSSL(request)) {
            throw UnsupportedOperationException("Ignoring invalid SSL certificates is engine specific and not supported by the Ktor executor")
        }
        val proxyHandler = this.client.proxyHandler
        if (proxyHandler.isProxySet && this.baseClient != null) {
            //Ktor ignores the engine configuration of user provided clients, so the proxy would be silently bypassed
            throw UnsupportedOperationException("A proxy can not be applied to a user provided Ktor client")
        }
        val cookieManager = this.getCookieManager(request)
        //HTTP proxy authentication is sent preemptively, SOCKS proxies authenticate through the global java.net.Authenticator
        val useThreadedAuthenticator = proxyHandler.isProxySet && proxyHandler.isAuthenticationSet && ProxyType.HTTP != proxyHandler.proxyType
        var httpClient: KtorClient? = null
        var close = true
        try {
            if (useThreadedAuthenticator) ThreadedProxyAuthenticator.setAuthentication(proxyHandler.username, proxyHandler.password)
            httpClient = this.buildClient(request, cookieManager)
            val requestBuilder = this.buildRequest(request)
            val client = httpClient
            if (request.isStreamedResponse) {
                val response = this.executeStreamed(client, requestBuilder)
                close = false //The streaming coroutine owns the client and closes it when the response stream is closed
                return response
            }
            return runBlocking {
                val response = client.request(requestBuilder)
                val url = URLWrapper.ofURL(response.request.url.toString()).toURL()
                HttpResponse(url, response.status.value, response.body<ByteArray>(), response.headers.toMap())
            }
        } catch (e: InterruptedException) {
            throw e
        } catch (t: Throwable) {
            throw this.unwrap(t)
        } finally {
            if (useThreadedAuthenticator) ThreadedProxyAuthenticator.clearAuthentication()
            if (close) httpClient?.close()
        }
    }

    private fun executeStreamed(httpClient: KtorClient, requestBuilder: HttpRequestBuilder): HttpResponse {
        //Ktor's high level request() buffers the whole body in memory, so a kept-alive coroutine is used to stream incrementally
        val handoff = SynchronousQueue<Any>()
        val closed = CompletableDeferred<Unit>()
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                httpClient.prepareRequest(requestBuilder).execute { response ->
                    val url = URLWrapper.ofURL(response.request.url.toString()).toURL()
                    val meta = StreamedResponse(url, response.status.value, response.headers.toMap(), response.bodyAsChannel().toInputStream())
                    handoff.put(meta)
                    //Keep the response (and connection) open until the caller closes the returned stream
                    closed.await()
                }
            } catch (t: Throwable) {
                //Only reaches a waiting caller if the response metadata was not handed off yet
                handoff.offer(t)
            } finally {
                httpClient.close()
            }
        }
        val signal = handoff.take()
        if (signal is Throwable) throw this.unwrap(signal)
        val meta = signal as StreamedResponse
        val stream = CloseListenerInputStream(meta.body) {
            closed.complete(Unit)
            scope.cancel()
        }
        return HttpResponse(meta.url, meta.status, stream, meta.headers)
    }

    private fun buildClient(request: HttpRequest, cookieManager: CookieManager?): KtorClient {
        val follow = this.isFollowRedirects(request)
        val connectTimeout = timeoutMillis(this.client.connectTimeout)
        val readTimeout = timeoutMillis(this.client.readTimeout)
        val proxyHandler = this.client.proxyHandler
        val userConfig = this.clientConfig
        val configure: HttpClientConfig<*>.() -> Unit = {
            followRedirects = follow
            expectSuccess = false
            install(HttpTimeout) {
                connectTimeoutMillis = connectTimeout
                socketTimeoutMillis = readTimeout
            }
            if (cookieManager != null) {
                //Let Ktor manage cookies across redirects; the storage bridges to the shared CookieManager
                install(HttpCookies) { storage = CookieManagerStorage(cookieManager) }
            }
            if (proxyHandler.isProxySet) {
                engine {
                    proxy = proxyHandler.toJavaProxy()
                }
            }
            userConfig?.invoke(this)
        }
        return when {
            this.baseClient != null -> this.baseClient.config(configure)
            this.engineFactory != null -> KtorClient(this.engineFactory, configure)
            else -> KtorClient(configure)
        }
    }

    private fun buildRequest(request: HttpRequest): HttpRequestBuilder {
        val builder = HttpRequestBuilder()
        builder.method = HttpMethod.parse(request.method)
        builder.url(request.getURL().toString())
        //Cookies are handled by the HttpCookies plugin; content headers are set from the request body instead of the header map
        val headers = this.getHeaders(request, null, false)
        if (request is HttpContentRequest && request.hasContent()) {
            //Honor a user provided Content-Type header for the body (Ktor derives the sent Content-Type from the body)
            val contentType = headers[CommonsHttpHeaders.CONTENT_TYPE.lowercase()]?.firstOrNull()
            builder.setBody(this.toRequestBody(request.content!!, request.isStreamedRequest, contentType))
        }
        this.setHeaders(
            headers.filterKeys { !it.equals(CommonsHttpHeaders.CONTENT_TYPE, true) && !it.equals(CommonsHttpHeaders.CONTENT_LENGTH, true) },
            { name, value -> builder.headers[name] = value },
            { name, value -> builder.headers.append(name, value) }
        )
        val proxyHandler = this.client.proxyHandler
        if (proxyHandler.isProxySet && proxyHandler.isAuthenticationSet && ProxyType.HTTP == proxyHandler.proxyType) {
            //Ktor has no built-in support for proxy authentication, sending the header preemptively works for HTTP proxies
            val credentials = Base64.getEncoder().encodeToString("${proxyHandler.username}:${proxyHandler.password}".toByteArray(Charsets.ISO_8859_1))
            builder.headers[HttpHeaders.ProxyAuthorization] = "Basic $credentials"
        }
        return builder
    }

    private fun toRequestBody(content: HttpContent, streamed: Boolean, contentType: String?): OutgoingContent {
        val mediaType = ContentType.parse(contentType ?: content.type.toString())
        return if (streamed) {
            object : OutgoingContent.WriteChannelContent() {
                override val contentType: ContentType get() = mediaType
                override val contentLength: Long? get() = if (content.length >= 0) content.length.toLong() else null

                override suspend fun writeTo(channel: ByteWriteChannel) {
                    withContext(Dispatchers.IO) {
                        content.getAsStream().use { inputStream ->
                            val buffer = ByteArray(maxOf(1, content.bufferSize))
                            while (true) {
                                val read = inputStream.read(buffer)
                                if (read < 0) break
                                channel.writeFully(buffer, 0, read)
                            }
                        }
                    }
                }
            }
        } else {
            ByteArrayContent(content.getAsBytes(), mediaType)
        }
    }

    private fun timeoutMillis(value: Int): Long {
        //A non-positive timeout means "infinite" for the other executors; Ktor rejects that value unless the infinite marker is used
        return if (value <= 0) HttpTimeoutConfig.INFINITE_TIMEOUT_MS else value.toLong()
    }

    private fun unwrap(t: Throwable): IOException {
        var cause: Throwable? = t
        while (cause != null) {
            if (cause is IOException) return cause
            if (cause is UncheckedIOException) return cause.cause ?: IOException(cause)
            cause = cause.cause
        }
        return IOException("Failed to execute request", t)
    }

    private class StreamedResponse(
        val url: URL,
        val status: Int,
        val headers: Map<String, List<String>>,
        val body: InputStream,
    )

    /**
     * Bridges Ktor's cookie storage to a [CookieManager] so cookies are sent and stored per host across redirects.
     */
    private class CookieManagerStorage(private val cookieManager: CookieManager) : CookiesStorage {
        override suspend fun get(requestUrl: Url): List<Cookie> {
            val uri = URI(requestUrl.toString())
            val headers = this.cookieManager.get(uri, emptyMap())
            val cookies = ArrayList<Cookie>()
            for ((name, values) in headers) {
                if (!name.equals("Cookie", true)) continue
                for (header in values) {
                    for (pair in header.split(";")) {
                        val trimmed = pair.trim()
                        val idx = trimmed.indexOf('=')
                        if (idx <= 0) continue
                        cookies.add(Cookie(name = trimmed.substring(0, idx), value = trimmed.substring(idx + 1), encoding = CookieEncoding.RAW, domain = requestUrl.host, path = "/"))
                    }
                }
            }
            return cookies
        }

        override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
            val uri = URI(requestUrl.toString())
            this.cookieManager.put(uri, mapOf("Set-Cookie" to listOf(renderSetCookieHeader(cookie))))
        }

        override fun close() {
        }
    }

}
