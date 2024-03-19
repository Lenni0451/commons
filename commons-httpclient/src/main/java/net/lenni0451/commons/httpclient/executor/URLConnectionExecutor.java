package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.proxy.SingleProxySelector;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

public class URLConnectionExecutor extends RequestExecutor {

    public URLConnectionExecutor(final HttpClient client) {
        super(client);
    }

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        CookieManager cookieManager = this.getCookieManager(request);
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
            if (this.isIgnoreInvalidSSL(request) && connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                httpsConnection.setSSLSocketFactory(IgnoringTrustManager.makeIgnoringSSLContext().getSocketFactory());
            }
            this.setupConnection(connection, cookieManager, request);
            connection.connect();
            return connection;
        } finally {
            if (proxySelector != null) proxySelector.reset();
        }
    }

    private void setupConnection(final HttpURLConnection connection, @Nullable final CookieManager cookieManager, final HttpRequest request) throws IOException {
        HttpRequestUtils.setHeaders(connection, this.getHeaders(request, cookieManager));

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

    private HttpResponse executeRequest(final HttpURLConnection connection, @Nullable final CookieManager cookieManager, final HttpRequest request) throws IOException {
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
