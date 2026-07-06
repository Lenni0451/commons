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
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
        assertThrows(IOException.class, () -> client.get("http://127.0.0.1:0").execute());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void headerRetry(final HttpClient client) throws IOException {
        client.setRetryConfig(new RetryConfig(0, 4/*1 initial request + 4 retries*/));
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
        client.setRetryConfig(new RetryConfig(Integer.MAX_VALUE, Integer.MAX_VALUE));
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

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testHead(final HttpClient client) throws IOException {
        HttpResponse response = client.head(baseUrl + "/methodEcho").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(Optional.of("HEAD"), response.getFirstHeader("X-Method"));
        assertEquals("", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testPut(final HttpClient client) throws IOException {
        HttpResponse response = client.put(baseUrl + "/echo")
                .setContent(new StringContent("put body"))
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("put body", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void testDelete(final HttpClient client) throws IOException {
        HttpResponse response = client.delete(baseUrl + "/methodEcho").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("DELETE", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void multiValueRequestHeader(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/headerEcho?name=X-Multi")
                .appendHeader("X-Multi", "a")
                .appendHeader("X-Multi", "b")
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        //Some clients send multiple header lines, some join the values with ", "
        assertEquals("a,b", response.getContent().getAsString().replace(", ", ","));
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void multiValueResponseHeader(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/multiHeader").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        List<String> values = response.getHeader("X-Multi");
        assertNotNull(values);
        //The value order is not guaranteed by all clients
        assertEquals(2, values.size());
        assertTrue(values.contains("first"));
        assertTrue(values.contains("second"));
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void clientHeaderAndRequestOverride(final HttpClient client) throws IOException {
        client.setHeader("X-Custom", "client");
        HttpResponse response = client.get(baseUrl + "/headerEcho?name=X-Custom").execute();
        assertEquals("client", response.getContent().getAsString());

        response = client.get(baseUrl + "/headerEcho?name=X-Custom")
                .setHeader("X-Custom", "request")
                .execute();
        assertEquals("request", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void cookieRoundTrip(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/cookieEcho").execute();
        assertEquals("<none>", response.getContent().getAsString());

        response = client.get(baseUrl + "/cookieEcho").execute();
        assertEquals("session=abc123", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void noCookieManager(final HttpClient client) throws IOException {
        client.setCookieManager(null);
        client.get(baseUrl + "/cookieEcho").execute();
        HttpResponse response = client.get(baseUrl + "/cookieEcho").execute();
        assertEquals("<none>", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void gzipDecodedContent(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/gzip").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("Hello Gzip", response.getDecodedContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void readTimeout(final HttpClient client) {
        client.setReadTimeout(300);
        assertThrows(IOException.class, () -> client.get(baseUrl + "/slow?delay=1500").execute());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void largeContent(final HttpClient client) throws IOException {
        byte[] payload = new byte[256 * 1024];
        new Random(42).nextBytes(payload);
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(new ByteArrayContent(payload))
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertArrayEquals(payload, response.getContent().getAsBytes());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void largeContentStreamed(final HttpClient client) throws IOException {
        byte[] payload = new byte[256 * 1024];
        new Random(42).nextBytes(payload);
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(HttpContent.inputStream(ContentTypes.APPLICATION_OCTET_STREAM, new ByteArrayInputStream(payload), payload.length))
                .setStreamedRequest(true)
                .setStreamedResponse(true)
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertArrayEquals(payload, response.getContent().getAsBytes());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void chunkedStreamedRequest(final HttpClient client) throws IOException {
        byte[] payload = "chunked request".getBytes(StandardCharsets.UTF_8);
        //The content length is unknown, forcing the executors to use chunked transfer encoding
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(HttpContent.inputStream(ContentTypes.APPLICATION_OCTET_STREAM, new ByteArrayInputStream(payload), -1))
                .setStreamedRequest(true)
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("chunked request", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void utf8Content(final HttpClient client) throws IOException {
        String text = "Привет мир 🌍 üöä";
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(new StringContent(text))
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals(text, response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void queryParameters(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/queryEcho?foo=bar&baz=qux").execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("foo=bar&baz=qux", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void noContent204(final HttpClient client) throws IOException {
        HttpResponse response = client.get(baseUrl + "/noContent").execute();
        assertEquals(StatusCodes.NO_CONTENT, response.getStatusCode());
        assertEquals("", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void errorBodyPost(final HttpClient client) throws IOException {
        HttpResponse response = client.post(baseUrl + "/response?content=error&code=500")
                .setContent(new StringContent("ignored"))
                .execute();
        assertEquals(StatusCodes.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("error", response.getContent().getAsString());
    }

    @ParameterizedTest
    @MethodSource(DATA_SOURCE)
    void multiPartPost(final HttpClient client) throws IOException {
        HttpResponse response = client.post(baseUrl + "/echo")
                .setContent(HttpContent.multiPartForm()
                        .addPart("field", HttpContent.string("Hello Part"))
                        .addPart("file", HttpContent.bytes(new byte[]{1, 2, 3}), "data.bin"))
                .execute();
        assertEquals(StatusCodes.OK, response.getStatusCode());
        String body = response.getContent().getAsString();
        assertTrue(body.contains("name=\"field\""));
        assertTrue(body.contains("Hello Part"));
        assertTrue(body.contains("filename=\"data.bin\""));
    }

}
