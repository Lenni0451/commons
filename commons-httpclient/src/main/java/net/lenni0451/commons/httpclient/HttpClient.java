package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.exceptions.RetryExceededException;
import net.lenni0451.commons.httpclient.executor.RequestExecutor;
import net.lenni0451.commons.httpclient.handler.HttpResponseHandler;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.CookieManager;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.function.Function;

public class HttpClient extends HeaderStore<HttpClient> implements HttpRequestBuilder {

    private final RequestExecutor executor;
    @Nullable
    private CookieManager cookieManager = new CookieManager();
    private boolean followRedirects = true;
    private int connectTimeout = 10_000;
    private int readTimeout = 10_000;
    private RetryHandler retryHandler = new RetryHandler();
    @Nullable
    private ProxyHandler proxyHandler;

    /**
     * Create a new HTTP client with the default executor.<br>
     * In Java 8-10 this will be the URLConnectionExecutor using the URLConnection API.<br>
     * In Java 11+ this will be the HttpClientExecutor using the HttpClient API.
     */
    public HttpClient() {
        this(RequestExecutor::create);
    }

    /**
     * Create a new HTTP client with the default executor or the URLConnectionExecutor if specified.<br>
     * If {@code useURLConnection} is true the URLConnectionExecutor will be used, otherwise the default executor will be used.
     *
     * @param useURLConnection Whether to use the URLConnectionExecutor
     * @see #HttpClient() For more information about the default executor
     */
    public HttpClient(final boolean useURLConnection) {
        this(useURLConnection ? RequestExecutor::createURLConnectionExecutor : RequestExecutor::create);
    }

    private HttpClient(final Function<HttpClient, RequestExecutor> executorSupplier) {
        this.executor = executorSupplier.apply(this);
    }

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
     * @return The proxy handler
     */
    @Nullable
    public ProxyHandler getProxyHandler() {
        return this.proxyHandler;
    }

    /**
     * Set the proxy handler for all requests.
     *
     * @param proxyHandler The proxy handler
     */
    public void setProxyHandler(@Nullable final ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
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
     * @throws IOException            If an I/O error occurs
     * @throws RetryExceededException If the maximum header retry count was exceeded
     * @throws IllegalStateException  If the maximum retry count was exceeded but no exception was thrown
     */
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        RetryHandler retryHandler = request.isRetryHandlerSet() ? request.getRetryHandler() : this.retryHandler;

        for (int connects = 0; connects <= retryHandler.getMaxConnectRetries(); connects++) {
            try {
                HttpResponse response = null;
                for (int headers = 0; headers <= retryHandler.getMaxHeaderRetries(); headers++) {
                    response = this.executor.execute(request);
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

}
