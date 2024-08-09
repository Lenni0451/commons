package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.content.StreamedHttpContent;
import net.lenni0451.commons.httpclient.proxy.ProxyType;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;
import net.lenni0451.commons.httpclient.utils.URLWrapper;

import javax.annotation.Nonnull;
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

/**
 * This executor uses the Java 11 HttpClient to execute requests.<br>
 * <b>Make sure you are running Java 11 or higher before loading this class!</b><br>
 * The safest way to access this class is by using Reflection.
 */
public class HttpClientExecutor extends RequestExecutor {

    public HttpClientExecutor(final HttpClient client) {
        super(client);
    }

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        java.net.http.HttpClient httpClient = null;
        try {
            httpClient = this.buildClient(request, executor);
            java.net.http.HttpRequest httpRequest = this.buildRequest(request);
            java.net.http.HttpResponse<byte[]> response;
            try {
                response = httpClient.send(httpRequest, BodyHandlers.ofByteArray());
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
            return new HttpResponse(new URLWrapper(response.uri()).toURL(), response.statusCode(), response.body(), response.headers().map());
        } finally {
            executor.shutdownNow();
            if (httpClient instanceof Closeable) ((Closeable) httpClient).close();
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
        return builder.build();
    }

    private java.net.http.HttpRequest buildRequest(final HttpRequest request) throws IOException {
        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder();
        builder.uri(new URLWrapper(request.getURL()).toURI());
        builder.timeout(Duration.ofMillis(this.client.getReadTimeout()));
        if (request instanceof HttpContentRequest && ((HttpContentRequest) request).hasContent()) {
            HttpContent content = ((HttpContentRequest) request).getContent();
            if (content instanceof StreamedHttpContent) {
                InputStream inputStream = ((StreamedHttpContent) content).getInputStream();
                builder.method(request.getMethod(), BodyPublishers.ofInputStream(() -> inputStream));
            } else {
                builder.method(request.getMethod(), BodyPublishers.ofByteArray(content.getAsBytes()));
            }
        } else {
            builder.method(request.getMethod(), BodyPublishers.noBody());
        }
        for (Map.Entry<String, List<String>> entry : this.getHeaders(request, null).entrySet()) {
            if (entry.getKey().equalsIgnoreCase(Headers.CONTENT_LENGTH)) continue; //Java 11 HttpClient does not allow manually setting the content length
            for (String value : entry.getValue()) builder.header(entry.getKey(), value);
        }
        return builder.build();
    }

}
