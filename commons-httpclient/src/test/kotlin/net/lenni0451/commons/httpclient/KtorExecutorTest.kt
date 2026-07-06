package net.lenni0451.commons.httpclient

import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import net.lenni0451.commons.httpclient.executor.extra.KtorExecutor
import net.lenni0451.commons.httpclient.server.TestWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
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
