package net.lenni0451.commons.httpclient;

import com.sun.net.httpserver.HttpServer;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.executor.extra.ReactorNettyExecutor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReactorNettyExecutorTest {

    private static HttpServer server;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/largeHeaders", exchange -> {
            String value = String.join("", Collections.nCopies(3 * 1024, "x"));
            for (int cookie = 0; cookie < 64; cookie++) {
                exchange.getResponseHeaders().add(HttpHeaders.SET_COOKIE, "cookie" + cookie + "=" + value);
            }
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        });
        server.createContext("/oversizedHeaders", exchange -> {
            String value = String.join("", Collections.nCopies(272 * 1024, "x"));
            exchange.getResponseHeaders().add(HttpHeaders.SET_COOKIE, "cookie=" + value);
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        });
        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    @AfterAll
    static void stopServer() {
        server.stop(0);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void acceptsHeadersLargerThanNettyDefault(final boolean streamedResponse) throws IOException {
        HttpClient client = new HttpClient(ReactorNettyExecutor::new);
        HttpResponse response = client.get(baseUrl + "/largeHeaders")
                .setStreamedResponse(streamedResponse)
                .execute();
        try (InputStream content = response.getContent().getAsStream()) {
            assertEquals(200, response.getStatusCode());
            assertEquals(64, response.getHeader(HttpHeaders.SET_COOKIE).size());
            assertEquals(-1, content.read());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void rejectsHeadersLargerThanDefaultLimit(final boolean streamedResponse) {
        HttpClient client = new HttpClient(ReactorNettyExecutor::new);
        assertThrows(IOException.class, () -> client.get(baseUrl + "/oversizedHeaders")
                .setStreamedResponse(streamedResponse)
                .execute());
    }

}
