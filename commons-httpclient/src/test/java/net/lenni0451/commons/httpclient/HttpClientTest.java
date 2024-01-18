package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.content.impl.StringContent;
import net.lenni0451.commons.httpclient.content.impl.URLEncodedFormContent;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.server.TestWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    private static TestWebServer server;
    private static String baseUrl;
    private HttpClient client;

    @BeforeAll
    static void startServer() throws IOException {
        server = new TestWebServer();
        baseUrl = "http://127.0.0.1:" + server.bind();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setUp() {
        this.client = new HttpClient();
    }

    @Test
    void testGet() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.get(baseUrl + "/echo"));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testErrorGet() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.get(baseUrl + "/response?content=123&code=404"));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(404, response.getStatusCode());
        assertEquals("123", response.getContentAsString());
    }

    @Test
    void testPostString() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.post(baseUrl + "/echo").setContent(new StringContent("Hello World")));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
        assertEquals("Hello World", response.getContentAsString());
    }

    @Test
    void testPostForm() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.post(baseUrl + "/echo").setContent(new URLEncodedFormContent().put("content", "Hello World")));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
        assertEquals("content=Hello+World", response.getContentAsString());
    }

    @Test
    void testEmptyGet() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.get(baseUrl + "/empty"));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
        assertEquals(0, response.getContent().length);
        assertEquals("", response.getContentAsString());
    }

    @Test
    void testEmptyPost() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.post(baseUrl + "/echo"));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
        assertEquals(0, response.getContent().length);
    }

    @Test
    void connectFail() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.get("http://" + System.currentTimeMillis()));
        assertThrows(UnknownHostException.class, () -> this.client.execute(request));
    }

}
