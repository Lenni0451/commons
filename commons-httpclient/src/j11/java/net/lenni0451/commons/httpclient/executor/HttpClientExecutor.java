package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;
import net.lenni0451.commons.httpclient.utils.URLWrapper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.CookieManager;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class HttpClientExecutor extends RequestExecutor {

    protected HttpClientExecutor(final HttpClient client) {
        super(client);
    }

    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        java.net.http.HttpClient httpClient = this.buildClient(request);
        java.net.http.HttpRequest httpRequest = this.buildRequest(request);
        java.net.http.HttpResponse<byte[]> response;
        try {
            response = httpClient.send(httpRequest, BodyHandlers.ofByteArray());
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
        return new HttpResponse(new URLWrapper(response.uri()).toURL(), response.statusCode(), response.body(), response.headers().map());
    }

    private java.net.http.HttpClient buildClient(final HttpRequest request) {
        java.net.http.HttpClient.Builder builder = java.net.http.HttpClient.newBuilder();
        CookieManager cookieManager = request.isCookieManagerSet() ? request.getCookieManager() : this.client.getCookieManager();
        if (cookieManager != null) builder.cookieHandler(cookieManager);
        builder.connectTimeout(Duration.ofMillis(this.client.getConnectTimeout()));
        switch (request.getFollowRedirects()) {
            case NOT_SET:
                builder.followRedirects(this.client.isFollowRedirects() ? Redirect.ALWAYS : Redirect.NORMAL);
                break;
            case FOLLOW:
                builder.followRedirects(Redirect.ALWAYS);
                break;
            case IGNORE:
                builder.followRedirects(Redirect.NEVER);
                break;
        }
        if (this.client.getProxyHandler() != null && this.client.getProxyHandler().getProxy() != null) {
            if (!ProxyHandler.ProxyType.HTTP.equals(this.client.getProxyHandler().getProxyType())) {
                throw new UnsupportedOperationException("Only HTTP proxies are supported with the Java 11 HttpClient");
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
        if (request instanceof HttpContentRequest && ((HttpContentRequest) request).hasContent()) {
            HttpContentRequest contentRequest = (HttpContentRequest) request;
            builder.method(request.getMethod(), BodyPublishers.ofByteArray(contentRequest.getContent().getAsBytes()));
        } else {
            builder.method(request.getMethod(), BodyPublishers.noBody());
        }
        Map<String, List<String>> headers = HttpRequestUtils.mergeHeaders(this.client.getHeaders(), request.getHeaders());
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            for (String value : entry.getValue()) builder.header(entry.getKey(), value);
        }
        return builder.build();
    }

}
