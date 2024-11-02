package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.content.StreamedHttpContent;
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

import java.io.ByteArrayInputStream;
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
        HttpResponse response = this.client.get(baseUrl + "/echo").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
    }

    @Test
    void testErrorGet() throws IOException {
        HttpResponse response = this.client.get(baseUrl + "/response?content=123&code=404").execute();
        assertEquals(StatusCodes.NOT_FOUND, response.getStatusCode());
        assertEquals("123", response.getContentAsString());
    }

    @Test
    void testCustomCode() throws IOException {
        HttpResponse response = this.client.get(baseUrl + "/response?content=321&code=432").execute();
        assertEquals(432, response.getStatusCode());
        assertEquals("321", response.getContentAsString());
    }

    @Test
    void testPostString() throws IOException {
        HttpResponse response = this.client.post(baseUrl + "/echo").setContent(new StringContent("Hello World")).execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("Hello World", response.getContentAsString());
    }

    @Test
    void testPostForm() throws IOException {
        HttpResponse response = this.client.post(baseUrl + "/echo").setContent(new URLEncodedFormContent().put("content", "Hello World")).execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("content=Hello+World", response.getContentAsString());
    }

    @Test
    void testEmptyGet() throws IOException {
        HttpResponse response = this.client.get(baseUrl + "/empty").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(0, response.getContent().length);
        assertEquals("", response.getContentAsString());
    }

    @Test
    void testEmptyPost() throws IOException {
        HttpResponse response = this.client.post(baseUrl + "/echo").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(0, response.getContent().length);
    }

    @Test
    void connectFail() {
        assertThrows(IOException.class, () -> this.client.get("http://" + System.currentTimeMillis()).execute());
    }

    @Test
    void headerRetry() throws IOException {
        this.client.setRetryHandler(new RetryHandler(0, 4/*1 initial request + 4 retries*/));
        HttpResponse response = this.client.get(baseUrl + "/retryCookie").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("OK", response.getContentAsString());
    }

    @Test
    void getWithRetry() throws IOException {
        HttpResponse response = this.client.get(baseUrl + "/retryCookie").execute();
        assertEquals(StatusCodes.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertTrue(response.hasHeader(HttpHeaders.RETRY_AFTER));
    }

    @Test
    void failingHeaderRetry() throws IOException {
        this.client.setRetryHandler(new RetryHandler(Integer.MAX_VALUE, Integer.MAX_VALUE));
        HttpRequest request = this.client.get(baseUrl + "/retryCookie");
        request.setRetryHandler(new RetryHandler(0, 1));
        assertThrows(RetryExceededException.class, request::execute);
    }

    @Test
    void contentType() throws IOException {
        HttpResponse response = this.client.post(baseUrl + "/contentType").setContent(new ByteArrayContent("Hello World".getBytes(StandardCharsets.UTF_8))).execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("application/octet-stream", response.getContentAsString());
    }

    @Test
    void followRedirect() throws IOException {
        this.client.setFollowRedirects(true);
        HttpResponse response = this.client.get(baseUrl + "/redirect").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("test", response.getContentAsString());
    }

    @Test
    void dontFollowRedirect() throws IOException {
        this.client.setFollowRedirects(false);
        HttpResponse response = this.client.get(baseUrl + "/redirect").execute();
        assertEquals(StatusCodes.MOVED_PERMANENTLY, response.getStatusCode());
        assertEquals("redirect", response.getContentAsString());
    }

    @Test
    void streamed() throws IOException {
        byte[] payload = "Hello World".getBytes(StandardCharsets.UTF_8);
        HttpResponse response = this.client.post(baseUrl + "/echo").setContent(new StreamedHttpContent(ContentTypes.APPLICATION_OCTET_STREAM, new ByteArrayInputStream(payload), payload.length).setBufferSize(1)).setStreamedResponse(true).execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("Hello World", response.getContentAsString());
    }

}
