package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.exceptions.RetryExceededException;
import net.lenni0451.commons.httpclient.executor.ExecutorType;
import net.lenni0451.commons.httpclient.executor.RequestExecutor;
import net.lenni0451.commons.httpclient.handler.HttpResponseHandler;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.CookieManager;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

public class HttpClient extends HeaderStore<HttpClient> implements HttpRequestBuilder {

    private RequestExecutor executor;
    @Nullable
    private CookieManager cookieManager = new CookieManager();
    private boolean followRedirects = true;
    private int connectTimeout = 10_000;
    private int readTimeout = 10_000;
    private RetryHandler retryHandler = new RetryHandler();
    private ProxyHandler proxyHandler = new ProxyHandler();
    private boolean ignoreInvalidSSL = false;

    /**
     * Create a new http client with the default executor.
     */
    public HttpClient() {
        this(ExecutorType.AUTO);
    }

    /**
     * Create a new http client with the given executor type.<br>
     * Some executor types may not be supported by your Java version. Make sure to check {@link ExecutorType#isAvailable()} before using them.
     *
     * @param executorType The executor type to use
     */
    public HttpClient(@Nonnull final ExecutorType executorType) {
        this(executorType::makeExecutor);
    }

    /**
     * Create a new http client with the given executor.<br>
     * You'll only need this if you want to use a custom executor.
     * For using an official executor use {@link ExecutorType} with {@link #HttpClient(ExecutorType)}.
     *
     * @param executorSupplier The supplier for the executor to use
     */
    public HttpClient(@Nonnull final Function<HttpClient, RequestExecutor> executorSupplier) {
        this.setExecutor(executorSupplier);
    }

    /**
     * Set the executor to use for all requests.
     *
     * @param executorSupplier The supplier for the executor to use
     * @return This instance for chaining
     */
    public HttpClient setExecutor(@Nonnull final Function<HttpClient, RequestExecutor> executorSupplier) {
        RequestExecutor executor = executorSupplier.apply(this);
        if (executor == null) throw new NullPointerException("The executor supplier returned null");
        this.executor = executor;
        return this;
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
    @Nonnull
    public RetryHandler getRetryHandler() {
        return this.retryHandler;
    }

    /**
     * Set the retry handler for all requests.
     *
     * @param retryHandler The retry handler
     * @return This instance for chaining
     */
    public HttpClient setRetryHandler(@Nonnull final RetryHandler retryHandler) {
        this.retryHandler = retryHandler;
        return this;
    }

    /**
     * @return The proxy handler
     */
    @Nonnull
    public ProxyHandler getProxyHandler() {
        return this.proxyHandler;
    }

    /**
     * Set the proxy handler for all requests.
     *
     * @param proxyHandler The proxy handler
     * @return This instance for chaining
     */
    public HttpClient setProxyHandler(@Nonnull final ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
        return this;
    }

    /**
     * @return Whether invalid SSL certificates should be ignored
     */
    public boolean isIgnoreInvalidSSL() {
        return this.ignoreInvalidSSL;
    }

    /**
     * Set whether invalid SSL certificates should be ignored.
     *
     * @param ignoreInvalidSSL Whether invalid SSL certificates should be ignored
     * @return This instance for chaining
     */
    public HttpClient setIgnoreInvalidSSL(final boolean ignoreInvalidSSL) {
        this.ignoreInvalidSSL = ignoreInvalidSSL;
        return this;
    }

    /**
     * Execute a request that is also a response handler and return the handled response.<br>
     * This is just a shortcut for {@link #execute(HttpRequest, HttpResponseHandler)}.
     *
     * @param requestAndHandler The request that is also the response handler
     * @param <T>               The type of the request and response handler
     * @param <R>               The return type of the response handler
     * @return The handled response
     * @throws IOException If an I/O error occurs
     */
    public <T extends HttpRequest & HttpResponseHandler<R>, R> R executeAndHandle(final T requestAndHandler) throws IOException {
        return this.execute(requestAndHandler, requestAndHandler);
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
     * @throws IOException            If an I/O error occurs
     * @throws RetryExceededException If the maximum header retry count was exceeded
     * @throws IllegalStateException  If the maximum retry count was exceeded but no exception was thrown
     */
    public HttpResponse execute(final HttpRequest request) throws IOException {
        RetryHandler retryHandler = request.isRetryHandlerSet() ? request.getRetryHandler() : this.retryHandler;

        for (int connects = 0; connects <= retryHandler.getMaxConnectRetries(); connects++) {
            try {
                HttpResponse response = null;
                for (int headers = 0; headers <= retryHandler.getMaxHeaderRetries(); headers++) {
                    response = this.executor.execute(request);
                    Optional<String> retryAfter = response.getFirstHeader(HttpHeaders.RETRY_AFTER);
                    if (retryAfter.isPresent()) {
                        if (headers >= retryHandler.getMaxHeaderRetries()) break;
                        Long delay = this.parseSecondsOrHttpDate(retryAfter.get());
                        if (delay == null) {
                            //An invalid retry after header. Treat as no retry
                            return response;
                        }
                        if (delay > 0) {
                            Thread.sleep(delay);
                        }
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

    @Nullable
    private Long parseSecondsOrHttpDate(final String value) {
        try {
            Instant date = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(value));
            return date.toEpochMilli() - Instant.now().toEpochMilli();
        } catch (DateTimeParseException ignored) {
        }
        try {
            int seconds = Integer.parseInt(value);
            return (long) seconds * 1000;
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    @Override
    public <T extends HttpRequest> T bind(T request) {
        request.bind(this);
        return request;
    }

    @Override
    public String toString() {
        return "HttpClient#" + this.executor.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(this));
    }

}
