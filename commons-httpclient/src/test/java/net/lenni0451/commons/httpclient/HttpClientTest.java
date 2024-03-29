package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.content.impl.ByteArrayContent;
import net.lenni0451.commons.httpclient.content.impl.StringContent;
import net.lenni0451.commons.httpclient.content.impl.URLEncodedFormContent;
import net.lenni0451.commons.httpclient.exceptions.RetryExceededException;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.server.TestWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    void testGet() throws IOException {
        HttpRequest request = this.client.get(baseUrl + "/echo");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
    }

    @Test
    void testErrorGet() throws IOException {
        HttpRequest request = this.client.get(baseUrl + "/response?content=123&code=404");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.NOT_FOUND, response.getStatusCode());
        assertEquals("123", response.getContentAsString());
    }

    @Test
    void testCustomCode() throws IOException {
        HttpRequest request = this.client.get(baseUrl + "/response?content=321&code=432");
        HttpResponse response = this.client.execute(request);
        assertEquals(432, response.getStatusCode());
        assertEquals("321", response.getContentAsString());
    }

    @Test
    void testPostString() throws IOException {
        HttpRequest request = this.client.post(baseUrl + "/echo").setContent(new StringContent("Hello World"));
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("Hello World", response.getContentAsString());
    }

    @Test
    void testPostForm() throws IOException {
        HttpRequest request = this.client.post(baseUrl + "/echo").setContent(new URLEncodedFormContent().put("content", "Hello World"));
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("content=Hello+World", response.getContentAsString());
    }

    @Test
    void testEmptyGet() throws IOException {
        HttpRequest request = this.client.get(baseUrl + "/empty");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(0, response.getContent().length);
        assertEquals("", response.getContentAsString());
    }

    @Test
    void testEmptyPost() throws IOException {
        HttpRequest request = this.client.post(baseUrl + "/echo");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(0, response.getContent().length);
    }

    @Test
    void connectFail() throws IOException {
        HttpRequest request = this.client.get("http://" + System.currentTimeMillis());
        assertThrows(IOException.class, () -> this.client.execute(request));
    }

    @Test
    void headerRetry() throws IOException {
        this.client.setRetryHandler(new RetryHandler(0, 4/*1 initial request + 4 retries*/));
        HttpRequest request = this.client.get(baseUrl + "/retryCookie");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("OK", response.getContentAsString());
    }

    @Test
    void getWithRetry() throws IOException {
        HttpRequest request = this.client.get(baseUrl + "/retryCookie");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertTrue(response.hasHeader(Headers.RETRY_AFTER));
    }

    @Test
    void failingHeaderRetry() throws IOException {
        this.client.setRetryHandler(new RetryHandler(Integer.MAX_VALUE, Integer.MAX_VALUE));
        HttpRequest request = this.client.get(baseUrl + "/retryCookie");
        request.setRetryHandler(new RetryHandler(0, 1));
        assertThrows(RetryExceededException.class, () -> this.client.execute(request));
    }

    @Test
    void contentType() throws IOException {
        HttpRequest request = this.client.post(baseUrl + "/contentType").setContent(new ByteArrayContent("Hello World".getBytes(StandardCharsets.UTF_8)));
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("application/octet-stream", response.getContentAsString());
    }

    @Test
    void followRedirect() throws IOException {
        this.client.setFollowRedirects(true);
        HttpRequest request = this.client.get(baseUrl + "/redirect");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("test", response.getContentAsString());
    }

    @Test
    void dontFollowRedirect() throws IOException {
        this.client.setFollowRedirects(false);
        HttpRequest request = this.client.get(baseUrl + "/redirect");
        HttpResponse response = this.client.execute(request);
        assertEquals(StatusCodes.MOVED_PERMANENTLY, response.getStatusCode());
        assertEquals("redirect", response.getContentAsString());
    }

}
