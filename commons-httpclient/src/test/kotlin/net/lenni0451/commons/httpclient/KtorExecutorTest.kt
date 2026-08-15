package net.lenni0451.commons.httpclient

import com.sun.net.httpserver.HttpServer
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import net.lenni0451.commons.httpclient.executor.extra.KtorExecutor
import net.lenni0451.commons.httpclient.proxy.ProxyType
import net.lenni0451.commons.httpclient.server.TestWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.net.InetSocketAddress
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import io.ktor.client.HttpClient as KtorClient

class KtorExecutorTest {

    companion object {
        private lateinit var server: TestWebServer
        private lateinit var baseUrl: String

        @JvmStatic
        @BeforeAll
        fun startServer() {
            server = TestWebServer()
            baseUrl = "http://127.0.0.1:" + server.bind()
        }

        @JvmStatic
        @AfterAll
        fun stopServer() {
            server.stop()
        }
    }

    @Test
    fun engineFactory() {
        val client = HttpClient { c -> KtorExecutor(c, engineFactory = CIO) }
        val response = client.get("$baseUrl/constant").execute()
        assertEquals("test", response.content.asString)
    }

    @Test
    fun clientConfig() {
        val client = HttpClient { c ->
            KtorExecutor(c, clientConfig = {
                defaultRequest { header("X-Custom", "ktor-config") }
            })
        }
        val response = client.get("$baseUrl/headerEcho?name=X-Custom").execute()
        assertEquals("ktor-config", response.content.asString)
    }

    @Test
    fun zeroReadTimeout() {
        //A read timeout of 0 (meaning infinite) must not crash the Ktor timeout plugin
        val client = HttpClient { c -> KtorExecutor(c, engineFactory = CIO) }
        client.readTimeout = 0
        client.connectTimeout = 0
        assertEquals("test", client.get("$baseUrl/constant").execute().content.asString)
    }

    @Test
    @Timeout(15)
    fun incrementalStreaming() {
        //execute() must return before the whole body arrives, proving the streamed response is not buffered in memory
        val latch = CountDownLatch(1)
        val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
        server.createContext("/stream") { exchange ->
            exchange.sendResponseHeaders(200, 0)
            val os = exchange.responseBody
            os.write("hello".toByteArray())
            os.flush()
            latch.await()
            os.close()
            exchange.close()
        }
        server.executor = Executors.newSingleThreadExecutor()
        server.start()
        try {
            val client = HttpClient { c -> KtorExecutor(c, engineFactory = CIO) }
            client.readTimeout = 10000
            val response = client.get("http://127.0.0.1:${server.address.port}/stream").setStreamedResponse(true).execute()
            val stream = response.content.asStream
            val buffer = ByteArray(5)
            var read = 0
            while (read < 5) {
                val r = stream.read(buffer, read, 5 - read)
                if (r < 0) break
                read += r
            }
            assertEquals("hello", String(buffer, 0, read))
            stream.close()
        } finally {
            latch.countDown()
            server.stop(0)
        }
    }

    @Test
    fun redirectCookiePreserved() {
        //A cookie set on the redirect hop must be stored and sent to the redirect target (handled by the HttpCookies plugin)
        val client = HttpClient { c -> KtorExecutor(c, engineFactory = CIO) }
        val body = client.get("$baseUrl/redirectCookie").execute().content.asString
        assertTrue(body.contains("rc=1"))
    }

    @Test
    fun proxyWithBaseClientThrows() {
        //A proxy can not be applied to a user provided client, so the executor must fail loudly instead of bypassing it
        val baseClient = KtorClient {}
        try {
            val client = HttpClient { c -> KtorExecutor(c, baseClient = baseClient) }
            client.proxyHandler.setProxy(ProxyType.HTTP, "127.0.0.1", 1)
            assertThrows(UnsupportedOperationException::class.java) { client.get("$baseUrl/constant").execute() }
        } finally {
            baseClient.close()
        }
    }

    @Test
    fun baseClient() {
        val baseClient = KtorClient {
            defaultRequest { header("X-Custom", "ktor-base") }
        }
        try {
            val client = HttpClient { c -> KtorExecutor(c, baseClient = baseClient) }
            assertEquals("ktor-base", client.get("$baseUrl/headerEcho?name=X-Custom").execute().content.asString)
            //The engine of the user provided client must still be usable for further requests
            assertEquals("ktor-base", client.get("$baseUrl/headerEcho?name=X-Custom").execute().content.asString)
        } finally {
            baseClient.close()
        }
    }

}
