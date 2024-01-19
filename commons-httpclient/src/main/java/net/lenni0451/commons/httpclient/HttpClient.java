package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.handler.HttpResponseHandler;
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

public class HttpClient extends HeaderStore<HttpClient> implements HttpRequestBuilder {

    @Nullable
    private CookieManager cookieManager = new CookieManager();
    private boolean followRedirects = true;
    private int connectTimeout = 10_000;
    private int readTimeout = 10_000;
    private RetryHandler retryHandler = new RetryHandler();

    /**
     * @return The cookie manager
     */
    @Nullable
    public CookieManager getCookieManager() {
        return this.cookieManager;
    }

    /**
     * Set the cookie manager to use for all requests.<br>
     * If this is null no cookies will be used.
     *
     * @param cookieManager The cookie manager to use
     * @return This instance for chaining
     */
    public HttpClient setCookieManager(@Nullable final CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        return this;
    }

    /**
     * @return Whether redirects should be followed
     */
    public boolean isFollowRedirects() {
        return this.followRedirects;
    }

    /**
     * Set whether redirects should be followed.
     *
     * @param followRedirects Whether redirects should be followed
     * @return This instance for chaining
     */
    public HttpClient setFollowRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    /**
     * @return The connect timeout for all requests in milliseconds
     */
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    /**
     * Set the connect timeout for all requests.
     *
     * @param connectTimeout The connect timeout in milliseconds
     * @return This instance for chaining
     */
    public HttpClient setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * @return The read timeout for all requests in milliseconds
     */
    public int getReadTimeout() {
        return this.readTimeout;
    }

    /**
     * Set the read timeout for all requests.
     *
     * @param readTimeout The read timeout in milliseconds
     * @return This instance for chaining
     */
    public HttpClient setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * @return The retry handler
     */
    public RetryHandler getRetryHandler() {
        return this.retryHandler;
    }

    /**
     * Set the retry handler for all requests.
     *
     * @param retryHandler The retry handler
     * @return This instance for chaining
     */
    public HttpClient setRetryHandler(final RetryHandler retryHandler) {
        this.retryHandler = retryHandler;
        return this;
    }

    /**
     * Execute a request and pass the response to the response handler.<br>
     * The return value of the response handler will be returned.
     *
     * @param request         The request to execute
     * @param responseHandler The response handler
     * @param <R>             The return type of the response handler
     * @return The return value of the response handler
     * @throws IOException If an I/O error occurs
     */
    public <R> R execute(@Nonnull final HttpRequest request, @Nonnull final HttpResponseHandler<R> responseHandler) throws IOException {
        return responseHandler.handle(this.execute(request));
    }

    /**
     * Execute a request and return the response.
     *
     * @param request The request to execute
     * @return The response
     * @throws IOException If an I/O error occurs
     */
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        CookieManager cookieManager = request.isCookieManagerSet() ? request.getCookieManager() : this.cookieManager;
        HttpURLConnection connection;

        for (int connects = 0; connects <= this.retryHandler.getMaxConnectRetries(); connects++) {
            try {
                HttpResponse response = null;
                for (int headers = 0; headers <= this.retryHandler.getMaxHeaderRetries(); headers++) {
                    connection = this.openConnection(request, cookieManager);
                    response = this.executeRequest(connection, cookieManager, request);
                    Optional<String> retryAfter = response.getFirstHeader(Headers.RETRY_AFTER);
                    if (retryAfter.isPresent()) {
                        if (headers >= this.retryHandler.getMaxHeaderRetries()) break;
                        Long delay = HttpRequestUtils.parseSecondsOrHttpDate(retryAfter.get());
                        if (delay == null) return response; //An invalid retry after header. Treat as no retry
                        if (delay > 0) Thread.sleep(delay);
                    } else {
                        return response;
                    }
                }
                if (this.retryHandler.getMaxHeaderRetries() == 0) {
                    if (response == null) throw new IllegalStateException("Response not received but no exception was thrown");
                    return response;
                } else {
                    throw new IOException("Max header retries reached");
                }
            } catch (InterruptedException e) {
                throw new IOException(e);
            } catch (UnknownHostException | SSLException | ProtocolException e) {
                //No need to retry these as they are not going to change
                throw e;
            } catch (IOException e) {
                if (connects >= this.retryHandler.getMaxConnectRetries()) throw e;
            }
        }
        throw new IllegalStateException("Connect retry failed but no exception was thrown");
    }

    private HttpURLConnection openConnection(final HttpRequest request, final CookieManager cookieManager) throws IOException {
        URL url = request.getURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        this.setupConnection(connection, cookieManager, request);
        connection.connect();
        return connection;
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
                this.getHeaders(),
                request.getHeaders()
        ));

        connection.setConnectTimeout(this.connectTimeout);
        connection.setReadTimeout(this.readTimeout);
        connection.setRequestMethod(request.getMethod());
        connection.setDoInput(true);
        connection.setDoOutput(request instanceof HttpContentRequest && ((HttpContentRequest) request).getContent() != null);
        switch (request.getFollowRedirects()) {
            case NOT_SET:
                connection.setInstanceFollowRedirects(this.followRedirects);
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
