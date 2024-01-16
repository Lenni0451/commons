package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.handler.HttpResponseHandler;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
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

@ParametersAreNonnullByDefault
public class HttpClient extends HeaderStore<HttpClient> implements HttpRequestBuilder {

    @Nullable
    private CookieManager cookieManager = new CookieManager();
    private boolean followRedirects = true;
    private int connectTimeout = 10_000;
    private int readTimeout = 10_000;

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
     * Execute a request and pass the response to the response handler.<br>
     * The return value of the response handler will be returned.
     *
     * @param request         The request to execute
     * @param responseHandler The response handler
     * @param <R>             The return type of the response handler
     * @return The return value of the response handler
     * @throws IOException If an I/O error occurs
     */
    public <R> R execute(final HttpRequest request, final HttpResponseHandler<R> responseHandler) throws IOException {
        return responseHandler.handle(this.execute(request));
    }

    /**
     * Execute a request and return the response.
     *
     * @param request The request to execute
     * @return The response
     * @throws IOException If an I/O error occurs
     */
    public HttpResponse execute(final HttpRequest request) throws IOException {
        URL url = request.getURL();
        CookieManager cookieManager = request.isCookieManagerSet() ? request.getCookieManager() : this.cookieManager;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Map<String, List<String>> headers = new HashMap<>();
        if (request instanceof HttpContentRequest && ((HttpContentRequest) request).hasContent()) {
            HttpContentRequest contentRequest = (HttpContentRequest) request;
            headers.put(Headers.CONTENT_TYPE, Collections.singletonList(contentRequest.getContent().getDefaultContentType()));
            headers.put(Headers.CONTENT_LENGTH, Collections.singletonList(String.valueOf(contentRequest.getContent().getContentLength())));
        }
        HttpRequestUtils.setHeaders(connection, HttpRequestUtils.mergeHeaders(
                HttpRequestUtils.getCookieHeaders(cookieManager, url),
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
        connection.connect();
        try {
            if (connection.getDoOutput()) {
                OutputStream os = connection.getOutputStream();
                os.write(((HttpContentRequest) request).getContent().getAsBytes());
                os.flush();
            }
            byte[] body = HttpRequestUtils.readBody(connection);

            HttpResponse response = new HttpResponse(
                    url,
                    connection.getResponseCode(),
                    body,
                    connection
                            .getHeaderFields()
                            .entrySet()
                            .stream()
                            .filter(e -> e.getKey() != null)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );
            HttpRequestUtils.updateCookies(cookieManager, url, connection.getHeaderFields());
            return response;
        } finally {
            connection.disconnect();
        }
    }

}
