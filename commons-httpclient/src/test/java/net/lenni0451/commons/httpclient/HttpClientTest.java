package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.content.impl.ByteArrayContent;
import net.lenni0451.commons.httpclient.content.impl.StringContent;
import net.lenni0451.commons.httpclient.content.impl.URLEncodedFormContent;
import net.lenni0451.commons.httpclient.exceptions.RetryExceededException;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.retry.RetryConfig;
import net.lenni0451.commons.httpclient.server.TestWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static net.lenni0451.commons.httpclient.HttpClientSource.DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

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

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testGet(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/echo").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testErrorGet(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/response?content=123&code=404").execute();
        assertEquals(StatusCodes.NOT_FOUND, response.getStatusCode());
        assertEquals("123", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testCustomCode(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/response?content=321&code=432").execute();
        assertEquals(432, response.getStatusCode());
        assertEquals("321", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testPostString(final HttpClient client) throws IOException {
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(new StringContent("Hello World"))
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("Hello World", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testPostForm(final HttpClient client) throws IOException {
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(new URLEncodedFormContent().put("content", "Hello World"))
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("content=Hello+World", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testEmptyGet(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/empty").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(0, response.getContent().getLength());
        assertEquals("", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testEmptyPost(final HttpClient client) throws IOException {
        HttpResponse response = client.post(baseUrl + "/echo").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(0, response.getContent().getLength());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void connectFail(final HttpClient client) {
        assertThrows(IOException.class, () -> client.get("http://" + System.currentTimeMillis()).execute());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void headerRetry(final HttpClient client) throws IOException {
        client.setRetryHandler(new RetryConfig(0, 4/*1 initial request + 4 retries*/));
        HttpResponse response = client.get(baseUrl + "/retryCookie").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("OK", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void getWithRetry(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/retryCookie").execute();
        assertEquals(StatusCodes.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertTrue(response.hasHeader(HttpHeaders.RETRY_AFTER));
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void failingHeaderRetry(final HttpClient client) throws IOException {
        client.setRetryHandler(new RetryConfig(Integer.MAX_VALUE, Integer.MAX_VALUE));
        HttpRequest request = client.get(baseUrl + "/retryCookie");
        request.setRetryHandler(new RetryConfig(0, 1));
        assertThrows(RetryExceededException.class, request::execute);
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void contentType(final HttpClient client) throws IOException {
        HttpResponse response = client.post(baseUrl + "/contentType")
                .setContent(new ByteArrayContent("Hello World".getBytes(StandardCharsets.UTF_8)))
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("application/octet-stream", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void followRedirect(final HttpClient client) throws IOException {
        client.setFollowRedirects(true);
        HttpResponse response = client.get(baseUrl + "/redirect").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("test", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void dontFollowRedirect(final HttpClient client) throws IOException {
        client.setFollowRedirects(false);
        HttpResponse response = client.get(baseUrl + "/redirect").execute();
        assertEquals(StatusCodes.MOVED_PERMANENTLY, response.getStatusCode());
        assertEquals("redirect", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void streamed(final HttpClient client) throws IOException {
        byte[] payload = "Hello World".getBytes(StandardCharsets.UTF_8);
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(HttpContent.inputStream(ContentTypes.APPLICATION_OCTET_STREAM, new ByteArrayInputStream(payload), payload.length).setBufferSize(1))
                .setStreamedRequest(true)
                .setStreamedResponse(true)
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("Hello World", response.getContent().getAsString());
    }

}
