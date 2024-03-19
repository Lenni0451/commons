package net.lenni0451.commons.httpclient.requests;

import net.lenni0451.commons.httpclient.HeaderStore;
import net.lenni0451.commons.httpclient.RetryHandler;
import net.lenni0451.commons.httpclient.utils.ResettableStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest extends HeaderStore<HttpRequest> {

    private final String method;
    private final URL url;
    private FollowRedirects followRedirects = FollowRedirects.NOT_SET;
    private final ResettableStorage<CookieManager> cookieManager = new ResettableStorage<>();
    private final ResettableStorage<RetryHandler> retryHandler = new ResettableStorage<>();
    private final ResettableStorage<Boolean> ignoreInvalidSSL = new ResettableStorage<>();

    public HttpRequest(final String method, final String url) throws MalformedURLException {
        this(method, new URL(url));
    }

    public HttpRequest(final String method, final URL url) {
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
        return this.cookieManager.isSet();
    }

    /**
     * Unset the cookie manager.
     *
     * @return This instance for chaining
     */
    public HttpRequest unsetCookieManager() {
        this.cookieManager.unset();
        return this;
    }

    /**
     * @return The set cookie manager
     * @throws IllegalStateException If the cookie manager is not set
     */
    @Nullable
    public CookieManager getCookieManager() {
        return this.cookieManager.get();
    }

    /**
     * Set the cookie manager to use for this request.
     *
     * @param cookieManager The cookie manager to use
     * @return This instance for chaining
     */
    public HttpRequest setCookieManager(@Nullable final CookieManager cookieManager) {
        this.cookieManager.set(cookieManager);
        return this;
    }

    /**
     * @return If the retry handler is set
     */
    public boolean isRetryHandlerSet() {
        return this.retryHandler.isSet();
    }

    /**
     * Unset the retry handler.
     *
     * @return This instance for chaining
     */
    public HttpRequest unsetRetryHandler() {
        this.retryHandler.unset();
        return this;
    }

    /**
     * @return The set retry handler
     * @throws IllegalStateException If the retry handler is not set
     */
    @Nonnull
    public RetryHandler getRetryHandler() {
        return this.retryHandler.get();
    }

    /**
     * Set the retry handler to use for this request.
     *
     * @param retryHandler The retry handler to use
     * @return This instance for chaining
     */
    public HttpRequest setRetryHandler(@Nonnull final RetryHandler retryHandler) {
        this.retryHandler.set(retryHandler);
        return this;
    }

    /**
     * @return If the ignore invalid SSL flag is set
     */
    public boolean isIgnoreInvalidSSLSet() {
        return this.ignoreInvalidSSL.isSet();
    }

    /**
     * Unset the ignore invalid SSL flag.
     *
     * @return This instance for chaining
     */
    public HttpRequest unsetIgnoreInvalidSSL() {
        this.ignoreInvalidSSL.unset();
        return this;
    }

    /**
     * @return The set ignore invalid SSL flag
     * @throws IllegalStateException If the ignore invalid SSL flag is not set
     */
    public boolean getIgnoreInvalidSSL() {
        return this.ignoreInvalidSSL.get();
    }

    /**
     * Set the ignore invalid SSL flag to use for this request.
     *
     * @param ignoreInvalidSSL The ignore invalid SSL flag to use
     * @return This instance for chaining
     */
    public HttpRequest setIgnoreInvalidSSL(final boolean ignoreInvalidSSL) {
        this.ignoreInvalidSSL.set(ignoreInvalidSSL);
        return this;
    }


    public enum FollowRedirects {
        NOT_SET, FOLLOW, IGNORE
    }

}
