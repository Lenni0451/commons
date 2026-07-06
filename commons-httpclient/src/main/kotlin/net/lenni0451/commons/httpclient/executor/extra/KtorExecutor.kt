package net.lenni0451.commons.httpclient.executor.extra

import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutgoingContent
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.lenni0451.commons.httpclient.HttpClient
import net.lenni0451.commons.httpclient.HttpResponse
import net.lenni0451.commons.httpclient.content.HttpContent
import net.lenni0451.commons.httpclient.executor.RequestExecutor
import net.lenni0451.commons.httpclient.proxy.ProxyType
import net.lenni0451.commons.httpclient.proxy.ThreadedProxyAuthenticator
import net.lenni0451.commons.httpclient.requests.HttpContentRequest
import net.lenni0451.commons.httpclient.requests.HttpRequest
import net.lenni0451.commons.httpclient.utils.URLWrapper
import net.lenni0451.commons.httpclient.utils.stream.CloseListenerInputStream
import java.io.IOException
import java.net.CookieManager
import java.nio.charset.StandardCharsets
import java.util.Base64
import io.ktor.client.HttpClient as KtorClient

/**
 * Executor that uses Ktor to execute requests.<br>
 * Make sure to add the dependency (including a client engine) to your project before using this executor.<br>
 * By default the engine is discovered from the classpath by Ktor itself.<br>
 * <br>
 * The used Ktor client can be customized by passing an engine factory, a user provided client and/or a config block to the constructor.<br>
 * Use it together with [HttpClient(Function)][HttpClient] (e.g. `HttpClient { KtorExecutor(it, engineFactory = CIO) }`).<br>
 * <br>
 * Limitations:
 * <ul>
 *     <li>Ignoring invalid SSL certificates is engine specific and therefore not supported</li>
 *     <li>Proxy support depends on the used engine</li>
 *     <li>Engine configuration (e.g. the proxy) is not applied to user provided clients</li>
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
        val cookieManager = this.getCookieManager(request)
        val proxyHandler = this.client.proxyHandler
        //HTTP proxy authentication is sent preemptively, SOCKS proxies authenticate through the global java.net.Authenticator
        val useThreadedAuthenticator = proxyHandler.isProxySet && proxyHandler.isAuthenticationSet && ProxyType.HTTP != proxyHandler.proxyType
        val httpClient = this.buildClient(request)
        var close = true
        try {
            if (useThreadedAuthenticator) ThreadedProxyAuthenticator.setAuthentication(proxyHandler.username, proxyHandler.password)
            val requestBuilder = this.buildRequest(request, cookieManager)
            return runBlocking {
                val response = httpClient.request(requestBuilder)
                val url = URLWrapper.ofURL(response.request.url.toString()).toURL()
                val headers = convertHeaders(response.headers)
                updateCookies(cookieManager, url, headers)
                if (request.isStreamedResponse) {
                    val inputStream = CloseListenerInputStream(response.bodyAsChannel().toInputStream()) { httpClient.close() }
                    close = false
                    HttpResponse(url, response.status.value, inputStream, headers)
                } else {
                    HttpResponse(url, response.status.value, response.body<ByteArray>(), headers)
                }
            }
        } catch (e: IOException) {
            throw e
        } catch (e: InterruptedException) {
            throw e
        } catch (t: Throwable) {
            throw this.unwrap(t)
        } finally {
            if (useThreadedAuthenticator) ThreadedProxyAuthenticator.clearAuthentication()
            if (close) httpClient.close()
        }
    }

    private fun buildClient(request: HttpRequest): KtorClient {
        val follow = this.isFollowRedirects(request)
        val connectTimeout = this.client.connectTimeout.toLong()
        val readTimeout = this.client.readTimeout.toLong()
        val proxyHandler = this.client.proxyHandler
        val clientConfig = this.clientConfig
        val configure: HttpClientConfig<*>.() -> Unit = {
            followRedirects = follow
            expectSuccess = false
            install(HttpTimeout) {
                connectTimeoutMillis = connectTimeout
                socketTimeoutMillis = readTimeout
            }
            if (proxyHandler.isProxySet) {
                //The engine configuration is ignored by Ktor if a user provided client is used
                engine {
                    proxy = proxyHandler.toJavaProxy()
                }
            }
            clientConfig?.invoke(this)
        }
        return when {
            this.baseClient != null -> this.baseClient.config(configure)
            this.engineFactory != null -> KtorClient(this.engineFactory, configure)
            else -> KtorClient(configure)
        }
    }

    private fun buildRequest(request: HttpRequest, cookieManager: CookieManager?): HttpRequestBuilder {
        val builder = HttpRequestBuilder()
        builder.method = HttpMethod.parse(request.method)
        builder.url(request.getURL().toString())
        if (request is HttpContentRequest && request.hasContent()) {
            builder.setBody(this.toRequestBody(request.content!!, request.isStreamedRequest))
        }
        //Content headers are excluded because Ktor does not allow setting them directly, they are taken from the request body instead
        this.setHeaders(
            this.getHeaders(request, cookieManager, false),
            { name, value -> builder.headers[name] = value },
            { name, value -> builder.headers.append(name, value) }
        )
        val proxyHandler = this.client.proxyHandler
        if (proxyHandler.isProxySet && proxyHandler.isAuthenticationSet && ProxyType.HTTP == proxyHandler.proxyType) {
            //Ktor has no built-in support for proxy authentication, sending the header preemptively works for HTTP proxies
            val credentials = Base64.getEncoder().encodeToString("${proxyHandler.username}:${proxyHandler.password}".toByteArray(StandardCharsets.ISO_8859_1))
            builder.headers[HttpHeaders.ProxyAuthorization] = "Basic $credentials"
        }
        return builder
    }

    private fun toRequestBody(content: HttpContent, streamed: Boolean): OutgoingContent {
        val mediaType = ContentType.parse(content.type.toString())
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

    private fun convertHeaders(headers: Headers): Map<String, List<String>> {
        val map = HashMap<String, MutableList<String>>()
        headers.forEach { name, values -> map.getOrPut(name) { ArrayList() }.addAll(values) }
        return map
    }

    private fun unwrap(t: Throwable): IOException {
        var cause: Throwable? = t
        while (cause != null) {
            if (cause is IOException) return cause
            cause = cause.cause
        }
        return IOException("Failed to execute request", t)
    }

}
