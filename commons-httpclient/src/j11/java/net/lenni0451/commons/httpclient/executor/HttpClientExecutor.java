package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.proxy.ProxyType;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;
import net.lenni0451.commons.httpclient.utils.URLWrapper;
import net.lenni0451.commons.httpclient.utils.stream.CloseListenerInputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This executor uses the Java 11 HttpClient to execute requests.<br>
 * <b>Make sure you are running Java 11 or higher before loading this class!</b><br>
 * The safest way to access this class is by using Reflection.<br>
 * <br>
 * The used {@link java.net.http.HttpClient} can be customized by passing a user provided client and/or a client customizer to the constructor.<br>
 * Use it together with {@link HttpClient#HttpClient(java.util.function.Function)} (e.g. {@code new HttpClient(c -> new HttpClientExecutor(c, myJavaHttpClient))}).<br>
 * <br>
 * Limitations:
 * <ul>
 *     <li>Only supports HTTP proxies</li>
 *     <li>Only supports HTTP/1.1 and HTTP/2</li>
 *     <li>Client level settings (redirects, proxy, SSL, cookies, connect timeout) are not applied to user provided clients</li>
 * </ul>
 */
public class HttpClientExecutor extends RequestExecutor {

    @Nullable
    private final java.net.http.HttpClient customHttpClient;
    @Nullable
    private final Consumer<java.net.http.HttpClient.Builder> clientCustomizer;

    public HttpClientExecutor(final HttpClient client) {
        this(client, null, null);
    }

    /**
     * @param client           The http client
     * @param clientCustomizer A customizer that is applied to the {@link java.net.http.HttpClient.Builder} after the default configuration
     */
    public HttpClientExecutor(final HttpClient client, @Nullable final Consumer<java.net.http.HttpClient.Builder> clientCustomizer) {
        this(client, null, clientCustomizer);
    }

    /**
     * @param client           The http client
     * @param customHttpClient A user provided client which is used for all requests.
     *                         Its lifecycle is managed by the user and client level settings are not applied to it.
     */
    public HttpClientExecutor(final HttpClient client, @Nullable final java.net.http.HttpClient customHttpClient) {
        this(client, customHttpClient, null);
    }

    /**
     * @param client           The http client
     * @param customHttpClient A user provided client which is used for all requests.
     *                         Its lifecycle is managed by the user and client level settings are not applied to it.
     * @param clientCustomizer A customizer that is applied to the {@link java.net.http.HttpClient.Builder} after the default configuration.
     *                         It is ignored if a user provided client is used.
     */
    public HttpClientExecutor(final HttpClient client, @Nullable final java.net.http.HttpClient customHttpClient, @Nullable final Consumer<java.net.http.HttpClient.Builder> clientCustomizer) {
        super(client);
        this.customHttpClient = customHttpClient;
        this.clientCustomizer = clientCustomizer;
    }

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        if (this.customHttpClient != null) {
            //The lifecycle of user provided clients is managed by the user
            return this.executeRequest(this.customHttpClient, request, null);
        }
        ExecutorService executor = Executors.newCachedThreadPool();
        java.net.http.HttpClient httpClient = null;
        boolean close = true;
        try {
            httpClient = this.buildClient(request, executor);
            HttpResponse response = this.executeRequest(httpClient, request, this.closeListener(executor, httpClient));
            if (request.isStreamedResponse()) close = false; //Closing the http client would also close the input stream
            return response;
        } finally {
            if (close) this.closeListener(executor, httpClient).close();
        }
    }

    private HttpResponse executeRequest(final java.net.http.HttpClient httpClient, final HttpRequest request, @Nullable final CloseListenerInputStream.CloseListener closeListener) throws IOException {
        java.net.http.HttpRequest httpRequest = this.buildRequest(request);
        if (request.isStreamedResponse()) {
            java.net.http.HttpResponse<InputStream> response = this.send(httpClient, httpRequest, BodyHandlers.ofInputStream());
            InputStream inputStream = closeListener == null ? response.body() : new CloseListenerInputStream(response.body(), closeListener);
            return new HttpResponse(new URLWrapper(response.uri()).toURL(), response.statusCode(), inputStream, response.headers().map());
        } else {
            java.net.http.HttpResponse<byte[]> response = this.send(httpClient, httpRequest, BodyHandlers.ofByteArray());
            return new HttpResponse(new URLWrapper(response.uri()).toURL(), response.statusCode(), response.body(), response.headers().map());
        }
    }

    private java.net.http.HttpClient buildClient(final HttpRequest request, final Executor executor) throws IOException {
        java.net.http.HttpClient.Builder builder = java.net.http.HttpClient.newBuilder().executor(executor);
        CookieManager cookieManager = this.getCookieManager(request);
        if (cookieManager != null) builder.cookieHandler(cookieManager);
        if (this.isIgnoreInvalidSSL(request)) builder.sslContext(IgnoringTrustManager.makeIgnoringSSLContext());
        builder.connectTimeout(Duration.ofMillis(this.client.getConnectTimeout()));
        switch (request.getFollowRedirects()) {
            case NOT_SET:
                builder.followRedirects(this.client.isFollowRedirects() ? Redirect.NORMAL : Redirect.NEVER);
                break;
            case FOLLOW:
                builder.followRedirects(Redirect.NORMAL);
                break;
            case IGNORE:
                builder.followRedirects(Redirect.NEVER);
                break;
        }
        if (this.client.getProxyHandler().isProxySet()) {
            if (!ProxyType.HTTP.equals(this.client.getProxyHandler().getProxyType())) {
                throw new UnsupportedOperationException("The Java 11 HttpClient only supports HTTP proxies");
            }
            builder.proxy(this.client.getProxyHandler().getProxySelector());
            if (this.client.getProxyHandler().getUsername() != null && this.client.getProxyHandler().getPassword() != null) {
                builder.authenticator(this.client.getProxyHandler().getProxyAuthenticator());
            }
        }
        if (this.clientCustomizer != null) this.clientCustomizer.accept(builder);
        return builder.build();
    }

    private java.net.http.HttpRequest buildRequest(final HttpRequest request) throws IOException {
        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder();
        builder.uri(new URLWrapper(request.getURL()).toURI());
        builder.timeout(Duration.ofMillis(this.client.getReadTimeout()));
        java.net.http.HttpRequest.BodyPublisher bodyPublisher;
        if (request instanceof HttpContentRequest && ((HttpContentRequest) request).hasContent()) {
            HttpContent content = ((HttpContentRequest) request).getContent();
            if (request.isStreamedRequest()) {
                InputStream inputStream = content.getAsStream();
                bodyPublisher = BodyPublishers.ofInputStream(() -> inputStream);
            } else {
                bodyPublisher = BodyPublishers.ofByteArray(content.getAsBytes());
            }
        } else {
            bodyPublisher = BodyPublishers.noBody();
        }
        builder.method(request.getMethod(), bodyPublisher);
        for (Map.Entry<String, List<String>> entry : this.getHeaders(request, null).entrySet()) {
            if (entry.getKey().equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
                //Java 11 HttpClient does not allow manually setting the content length
                continue;
            }
            for (String value : entry.getValue()) {
                builder.header(entry.getKey(), value);
            }
        }
        return builder.build();
    }

    private <T> java.net.http.HttpResponse<T> send(final java.net.http.HttpClient httpClient, final java.net.http.HttpRequest httpRequest, final java.net.http.HttpResponse.BodyHandler<T> bodyHandler) throws IOException {
        try {
            return httpClient.send(httpRequest, bodyHandler);
        } catch (InterruptedException e) {
            throw new IOException("Request interrupted", e);
        }
    }

    private CloseListenerInputStream.CloseListener closeListener(final ExecutorService executor, final java.net.http.HttpClient httpClient) {
        return () -> {
            executor.shutdownNow();
            if (httpClient instanceof Closeable) ((Closeable) httpClient).close();
        };
    }

}
