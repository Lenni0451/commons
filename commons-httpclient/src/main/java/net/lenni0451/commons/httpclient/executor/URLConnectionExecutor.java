package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.RetryHandler;
import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.exceptions.RetryExceededException;
import net.lenni0451.commons.httpclient.proxy.SingleProxySelector;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class URLConnectionExecutor extends RequestExecutor {

    protected URLConnectionExecutor(final HttpClient client) {
        super(client);
    }

    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        CookieManager cookieManager = request.isCookieManagerSet() ? request.getCookieManager() : this.client.getCookieManager();
        RetryHandler retryHandler = request.isRetryHandlerSet() ? request.getRetryHandler() : this.client.getRetryHandler();
        HttpURLConnection connection;

        for (int connects = 0; connects <= retryHandler.getMaxConnectRetries(); connects++) {
            try {
                HttpResponse response = null;
                for (int headers = 0; headers <= retryHandler.getMaxHeaderRetries(); headers++) {
                    connection = this.openConnection(request, cookieManager);
                    response = this.executeRequest(connection, cookieManager, request);
                    Optional<String> retryAfter = response.getFirstHeader(Headers.RETRY_AFTER);
                    if (retryAfter.isPresent()) {
                        if (headers >= retryHandler.getMaxHeaderRetries()) break;
                        Long delay = HttpRequestUtils.parseSecondsOrHttpDate(retryAfter.get());
                        if (delay == null) return response; //An invalid retry after header. Treat as no retry
                        if (delay > 0) Thread.sleep(delay);
                    } else {
                        return response;
                    }
                }
                if (response == null) throw new IllegalStateException("Response not received but no exception was thrown");
                if (retryHandler.getMaxHeaderRetries() == 0) return response;
                else throw new RetryExceededException(response);
            } catch (InterruptedException e) {
                throw new IOException(e);
            } catch (UnknownHostException | SSLException | ProtocolException e) {
                //No need to retry these as they are not going to change
                throw e;
            } catch (IOException e) {
                if (connects >= retryHandler.getMaxConnectRetries()) throw e;
            }
        }
        throw new IllegalStateException("Connect retry failed but no exception was thrown");
    }

    private HttpURLConnection openConnection(final HttpRequest request, final CookieManager cookieManager) throws IOException {
        SingleProxySelector proxySelector = null;
        if (this.client.getProxyHandler() != null && this.client.getProxyHandler().getProxy() != null) proxySelector = this.client.getProxyHandler().getProxySelector();
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
