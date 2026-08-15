package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.executor.HttpClientExecutor;
import net.lenni0451.commons.httpclient.executor.URLConnectionExecutor;
import net.lenni0451.commons.httpclient.executor.extra.OkHttpExecutor;
import net.lenni0451.commons.httpclient.executor.extra.ReactorNettyExecutor;
import net.lenni0451.commons.httpclient.server.TestWebServer;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecutorCustomizationTest {

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
    void urlConnectionCustomizer() throws IOException {
        HttpClient client = new HttpClient(c -> new URLConnectionExecutor(c, connection -> connection.setRequestProperty("X-Custom", "url-connection")));
        HttpResponse response = client.get(baseUrl + "/headerEcho?name=X-Custom").execute();
        assertEquals("url-connection", response.getContent().getAsString());
    }

    @Test
    void httpClientCustomizer() throws IOException {
        HttpClient client = new HttpClient(c -> new HttpClientExecutor(c, builder -> builder.followRedirects(java.net.http.HttpClient.Redirect.NEVER)));
        client.setFollowRedirects(true);
        //The customizer is applied after the default configuration and overrides the client settings
        HttpResponse response = client.get(baseUrl + "/redirect").execute();
        assertEquals(StatusCodes.MOVED_PERMANENTLY, response.getStatusCode());
    }

    @Test
    void httpClientCustomInstance() throws IOException {
        java.net.http.HttpClient customClient = java.net.http.HttpClient.newBuilder().followRedirects(java.net.http.HttpClient.Redirect.NEVER).build();
        HttpClient client = new HttpClient(c -> new HttpClientExecutor(c, customClient));
        client.setFollowRedirects(true);
        HttpResponse response = client.get(baseUrl + "/redirect").execute();
        assertEquals(StatusCodes.MOVED_PERMANENTLY, response.getStatusCode());
        //The user provided client must still be usable for further requests
        response = client.get(baseUrl + "/constant").execute();
        assertEquals("test", response.getContent().getAsString());
    }

    @Test
    void okHttpCustomizer() throws IOException {
        HttpClient client = new HttpClient(c -> new OkHttpExecutor(c, builder -> builder.addInterceptor(chain ->
                chain.proceed(chain.request().newBuilder().header("X-Custom", "okhttp").build()))));
        HttpResponse response = client.get(baseUrl + "/headerEcho?name=X-Custom").execute();
        assertEquals("okhttp", response.getContent().getAsString());
    }

    @Test
    void okHttpBaseInstance() throws IOException {
        OkHttpClient baseClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder().header("X-Custom", "okhttp-base").build()))
                .build();
        HttpClient client = new HttpClient(c -> new OkHttpExecutor(c, baseClient));
        HttpResponse response = client.get(baseUrl + "/headerEcho?name=X-Custom").execute();
        assertEquals("okhttp-base", response.getContent().getAsString());
        //The dispatcher and connection pool of the user provided client must still be usable
        response = client.get(baseUrl + "/headerEcho?name=X-Custom").execute();
        assertEquals("okhttp-base", response.getContent().getAsString());
    }

    @Test
    void reactorNettyCustomizer() throws IOException {
        HttpClient client = new HttpClient(c -> new ReactorNettyExecutor(c, httpClient ->
                httpClient.headers(headers -> headers.set("X-Custom", "reactor"))));
        HttpResponse response = client.get(baseUrl + "/headerEcho?name=X-Custom").execute();
        assertEquals("reactor", response.getContent().getAsString());
    }

    @Test
    void reactorNettyBaseInstance() throws IOException {
        reactor.netty.http.client.HttpClient baseClient = reactor.netty.http.client.HttpClient.create()
                .headers(headers -> headers.set("X-Custom", "reactor-base"));
        HttpClient client = new HttpClient(c -> new ReactorNettyExecutor(c, baseClient));
        HttpResponse response = client.get(baseUrl + "/headerEcho?name=X-Custom").execute();
        assertEquals("reactor-base", response.getContent().getAsString());
    }

}
