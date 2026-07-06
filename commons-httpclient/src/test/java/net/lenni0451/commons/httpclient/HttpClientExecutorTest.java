package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.executor.HttpClientExecutor;
import net.lenni0451.commons.httpclient.server.TestWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpClientExecutorTest {

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

    @Test
    void customClientCookiesRoundTrip() throws IOException {
        //A user provided java.net.http client has no cookie handler, so cookies are applied per request via headers
        java.net.http.HttpClient customClient = java.net.http.HttpClient.newHttpClient();
        HttpClient client = new HttpClient(c -> new HttpClientExecutor(c, customClient));

        HttpResponse first = client.get(baseUrl + "/cookieEcho").execute();
        assertEquals(StatusCodes.OK, first.getStatusCode());
        assertEquals("<none>", first.getContent().getAsString());

        //The cookie set by the first response must be stored and sent on the second request
        HttpResponse second = client.get(baseUrl + "/cookieEcho").execute();
        assertEquals("session=abc123", second.getContent().getAsString());
    }

}
