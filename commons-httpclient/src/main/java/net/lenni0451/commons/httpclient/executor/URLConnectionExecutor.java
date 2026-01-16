package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.proxy.SingleProxySelector;
import net.lenni0451.commons.httpclient.proxy.ThreadedProxyAuthenticator;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Executor that uses {@link HttpURLConnection} to execute requests.<br>
 * This executor is available on all Java versions and is the default executor if no other executor is available.<br>
 * <br>
 * Limitations:
 * <ul>
 *     <li>Only supports HTTP/1.1</li>
 *     <li>Proxy authentication is static and might interfere with concurrent requests using different proxy credentials</li>
 * </ul>
 */
public class URLConnectionExecutor extends RequestExecutor {

    public URLConnectionExecutor(final HttpClient client) {
        super(client);
    }

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        CookieManager cookieManager = this.getCookieManager(request);
        ProxyHandler proxyHandler = this.client.getProxyHandler();
        SingleProxySelector proxySelector = null;
        if (proxyHandler.isProxySet()) {
            proxySelector = proxyHandler.getProxySelector();
        }
        try {
            if (proxyHandler.isProxySet() && proxyHandler.isAuthenticationSet()) {
                ThreadedProxyAuthenticator.setAuthentication(proxyHandler.getUsername(), proxyHandler.getPassword());
            }
            HttpURLConnection connection = this.openConnection(request, cookieManager, proxySelector == null ? null : proxyHandler);
            return this.executeRequest(connection, cookieManager, request);
        } finally {
            if (proxyHandler.isProxySet() && proxyHandler.isAuthenticationSet()) {
                ThreadedProxyAuthenticator.clearAuthentication();
            }
        }
    }

    private HttpURLConnection openConnection(final HttpRequest request, final CookieManager cookieManager, final ProxyHandler proxyHandler) throws IOException {
        URL url = request.getURL();
        HttpURLConnection connection;
        if (proxyHandler == null) {
            connection = (HttpURLConnection) url.openConnection();
        } else {
            connection = (HttpURLConnection) url.openConnection(proxyHandler.toJavaProxy());
        }
        if (this.isIgnoreInvalidSSL(request) && connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            httpsConnection.setSSLSocketFactory(IgnoringTrustManager.makeIgnoringSSLContext().getSocketFactory());
        }
        this.setupConnection(connection, cookieManager, request);
        connection.connect();
        return connection;
    }

    private void setupConnection(final HttpURLConnection connection, @Nullable final CookieManager cookieManager, final HttpRequest request) throws IOException {
        this.setHeaders(this.getHeaders(request, cookieManager), connection::setRequestProperty, connection::addRequestProperty);
        HttpContentRequest contentRequest = request instanceof HttpContentRequest ? (HttpContentRequest) request : null;
        HttpContent content = contentRequest != null ? contentRequest.getContent() : null;

        connection.setConnectTimeout(this.client.getConnectTimeout());
        connection.setReadTimeout(this.client.getReadTimeout());
        connection.setRequestMethod(request.getMethod());
        connection.setDoInput(true);
        if (contentRequest != null && content != null) {
            connection.setDoOutput(true);
            if (request.isStreamedRequest()) {
                if (content.getLength() >= 0) {
                    connection.setFixedLengthStreamingMode(content.getLength());
                } else {
                    connection.setChunkedStreamingMode(0);
                }
            }
        } else {
            connection.setDoOutput(false);
        }
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
        boolean closeConnection = true;
        try {
            if (connection.getDoOutput()) {
                HttpContent content = ((HttpContentRequest) request).getContent();
                try (OutputStream os = connection.getOutputStream()) {
                    content.transferTo(os);
                }
            }

            Map<String, List<String>> headers = new HashMap<>(connection.getHeaderFields());
            headers.remove(null);
            HttpResponse response;
            InputStream body = this.getInputStream(connection);
            if (request.isStreamedResponse()) {
                response = new HttpResponse(request.getURL(), connection.getResponseCode(), body, headers);
                closeConnection = false; //The connection needs to remain open for streamed responses
            } else {
                byte[] bodyContent = this.readAllBytes(body, connection.getContentLength());
                response = new HttpResponse(request.getURL(), connection.getResponseCode(), bodyContent, headers);
            }
            this.updateCookies(cookieManager, request.getURL(), connection.getHeaderFields());
            return response;
        } finally {
            if (closeConnection) connection.disconnect();
        }
    }

    private InputStream getInputStream(final HttpURLConnection connection) throws IOException {
        InputStream is;
        if (connection.getResponseCode() >= 400) {
            is = connection.getErrorStream();
        } else {
            is = connection.getInputStream();
        }
        if (is == null) {
            is = new ByteArrayInputStream(new byte[0]);
        }
        return is;
    }

    private byte[] readAllBytes(final InputStream is, final int contentLength) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) >= 0) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }

}
