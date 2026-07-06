package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.content.impl.StringContent;
import net.lenni0451.commons.httpclient.executor.extra.OkHttpExecutor;
import net.lenni0451.commons.httpclient.server.TestWebServer;
import okhttp3.Dispatcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OkHttpExecutorTest {

    private static TestWebServer server;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws IOException {
        server = new TestWebServer();
        baseUrl = "http://127.0.0.1:" + server.bind();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    private static HttpClient client() {
        return new HttpClient(OkHttpExecutor::new);
    }

    @Test
    void getWithBodyThrowsIOException() {
        //OkHttp rejects a body on GET with an unchecked exception; it must be wrapped in an IOException
        assertThrows(IOException.class, () -> client().contentRequest("GET", baseUrl + "/echo")
                .setContent(new StringContent("body"))
                .execute());
    }

    @Test
    void nonAsciiHeaderThrowsIOException() {
        //OkHttp rejects non-ASCII header values with an unchecked exception; it must be wrapped in an IOException
        assertThrows(IOException.class, () -> client().get(baseUrl + "/echo")
                .setHeader("X-Info", "münchen")
                .execute());
    }

    @Test
    void redirectCookiePreserved() throws IOException {
        //A cookie set on the redirect hop must be stored and sent to the redirect target (handled by the cookie jar)
        HttpResponse response = client().get(baseUrl + "/redirectCookie").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertTrue(response.getContent().getAsString().contains("rc=1"));
    }

    @Test
    void customizerInjectedDispatcherIsNotShutDown() throws IOException {
        Dispatcher shared = new Dispatcher();
        HttpClient client = new HttpClient(c -> new OkHttpExecutor(c, builder -> builder.dispatcher(shared)));
        client.get(baseUrl + "/constant").execute();
        //The executor must not shut down a dispatcher it does not own
        assertFalse(shared.executorService().isShutdown());
        //Subsequent requests must keep working
        assertEquals("test", client.get(baseUrl + "/constant").execute().getContent().getAsString());
    }

}
