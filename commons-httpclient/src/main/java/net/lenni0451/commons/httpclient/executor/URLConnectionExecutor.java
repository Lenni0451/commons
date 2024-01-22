package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.proxy.SingleProxySelector;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class URLConnectionExecutor extends RequestExecutor {

    public URLConnectionExecutor(final HttpClient client) {
        super(client);
    }

    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        CookieManager cookieManager = request.isCookieManagerSet() ? request.getCookieManager() : this.client.getCookieManager();
        HttpURLConnection connection = this.openConnection(request, cookieManager);
        return this.executeRequest(connection, cookieManager, request);
    }

    private HttpURLConnection openConnection(final HttpRequest request, final CookieManager cookieManager) throws IOException {
        SingleProxySelector proxySelector = null;
        if (this.client.getProxyHandler().isProxySet()) proxySelector = this.client.getProxyHandler().getProxySelector();
        try {
            if (proxySelector != null) proxySelector.set();
            URL url = request.getURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            this.setupConnection(connection, cookieManager, request);
            connection.connect();
            return connection;
        } finally {
            if (proxySelector != null) proxySelector.reset();
        }
    }

    private void setupConnection(@Nonnull final HttpURLConnection connection, @Nullable final CookieManager cookieManager, @Nonnull final HttpRequest request) throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        if (request instanceof HttpContentRequest) {
            HttpContent content = ((HttpContentRequest) request).getContent();
            if (content != null) {
                headers.put(Headers.CONTENT_TYPE, Collections.singletonList(content.getContentType().toString()));
                headers.put(Headers.CONTENT_LENGTH, Collections.singletonList(String.valueOf(content.getContentLength())));
            }
        }
        HttpRequestUtils.setHeaders(connection, HttpRequestUtils.mergeHeaders(
                HttpRequestUtils.getCookieHeaders(cookieManager, request.getURL()),
                headers,
                this.client.getHeaders(),
                request.getHeaders()
        ));

        connection.setConnectTimeout(this.client.getConnectTimeout());
        connection.setReadTimeout(this.client.getReadTimeout());
        connection.setRequestMethod(request.getMethod());
        connection.setDoInput(true);
        connection.setDoOutput(request instanceof HttpContentRequest && ((HttpContentRequest) request).getContent() != null);
        switch (request.getFollowRedirects()) {
            case NOT_SET:
                connection.setInstanceFollowRedirects(this.client.isFollowRedirects());
                break;
            case FOLLOW:
                connection.setInstanceFollowRedirects(true);
                break;
            case IGNORE:
                connection.setInstanceFollowRedirects(false);
                break;
        }
    }

    private HttpResponse executeRequest(@Nonnull final HttpURLConnection connection, @Nullable final CookieManager cookieManager, @Nonnull final HttpRequest request) throws IOException {
        try {
            if (connection.getDoOutput()) {
                OutputStream os = connection.getOutputStream();
                os.write(((HttpContentRequest) request).getContent().getAsBytes());
                os.flush();
            }
            byte[] body = HttpRequestUtils.readBody(connection);

            HttpResponse response = new HttpResponse(
                    request.getURL(),
                    connection.getResponseCode(),
                    body,
                    connection
                            .getHeaderFields()
                            .entrySet()
                            .stream()
                            .filter(e -> e.getKey() != null)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );
            HttpRequestUtils.updateCookies(cookieManager, request.getURL(), connection.getHeaderFields());
            return response;
        } finally {
            connection.disconnect();
        }
    }

}
