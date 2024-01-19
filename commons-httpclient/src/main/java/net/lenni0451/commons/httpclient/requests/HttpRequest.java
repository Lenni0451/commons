package net.lenni0451.commons.httpclient.requests;

import net.lenni0451.commons.httpclient.HeaderStore;
import net.lenni0451.commons.httpclient.RetryHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest extends HeaderStore<HttpRequest> {

    private final String method;
    private final URL url;
    private FollowRedirects followRedirects = FollowRedirects.NOT_SET;
    private CookieManager cookieManager;
    private boolean cookieManagerSet = false;
    private RetryHandler retryHandler = new RetryHandler();
    private boolean retryHandlerSet = false;

    public HttpRequest(@Nonnull final String method, @Nonnull final String url) throws MalformedURLException {
        this(method, new URL(url));
    }

    public HttpRequest(@Nonnull final String method, @Nonnull final URL url) {
        this.method = method;
        this.url = url;
    }

    /**
     * @return The request method
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * @return The request url
     */
    public URL getURL() {
        return this.url;
    }

    /**
     * @return If redirects should be followed
     */
    public FollowRedirects getFollowRedirects() {
        return this.followRedirects;
    }

    /**
     * Set if redirects should be followed.
     *
     * @param followRedirects If redirects should be followed
     * @return This instance for chaining
     */
    public HttpRequest setFollowRedirects(final boolean followRedirects) {
        return this.setFollowRedirects(followRedirects ? FollowRedirects.FOLLOW : FollowRedirects.IGNORE);
    }

    /**
     * Set if redirects should be followed.
     *
     * @param followRedirects If redirects should be followed
     * @return This instance for chaining
     */
    public HttpRequest setFollowRedirects(@Nonnull final FollowRedirects followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    /**
     * @return If the cookie manager is set
     */
    public boolean isCookieManagerSet() {
        return this.cookieManagerSet;
    }

    /**
     * Unset the cookie manager.
     *
     * @return This instance for chaining
     */
    public HttpRequest unsetCookieManager() {
        this.cookieManager = null;
        this.cookieManagerSet = false;
        return this;
    }

    /**
     * @return The set cookie manager
     */
    @Nullable
    public CookieManager getCookieManager() {
        return this.cookieManager;
    }

    /**
     * Set the cookie manager to use for this request.
     *
     * @param cookieManager The cookie manager to use
     * @return This instance for chaining
     */
    public HttpRequest setCookieManager(@Nullable final CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        this.cookieManagerSet = true;
        return this;
    }

    /**
     * @return If the retry handler is set
     */
    public boolean isRetryHandlerSet() {
        return this.retryHandlerSet;
    }

    /**
     * Unset the retry handler.
     *
     * @return This instance for chaining
     */
    public HttpRequest unsetRetryHandler() {
        this.retryHandler = new RetryHandler();
        this.retryHandlerSet = false;
        return this;
    }

    /**
     * @return The set retry handler
     */
    public RetryHandler getRetryHandler() {
        return this.retryHandler;
    }

    /**
     * Set the retry handler to use for this request.
     *
     * @param retryHandler The retry handler to use
     * @return This instance for chaining
     */
    public HttpRequest setRetryHandler(@Nonnull final RetryHandler retryHandler) {
        this.retryHandler = retryHandler;
        this.retryHandlerSet = true;
        return this;
    }


    public enum FollowRedirects {
        NOT_SET, FOLLOW, IGNORE
    }

}
