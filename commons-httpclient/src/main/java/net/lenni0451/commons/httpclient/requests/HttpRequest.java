package net.lenni0451.commons.httpclient.requests;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@ParametersAreNonnullByDefault
public class HttpRequest {

    private final String method;
    private final URL url;
    private final Map<String, List<String>> headers = new HashMap<>();
    private FollowRedirects followRedirects = FollowRedirects.NOT_SET;
    private CookieManager cookieManager;
    private boolean cookieManagerSet = false;

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
     * @return The request headers
     */
    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    /**
     * Add a header to the request.<br>
     * If the header already exists it will be appended to the list.
     *
     * @param name  The name of the header
     * @param value The value of the header
     * @return This instance for chaining
     */
    public HttpRequest addHeader(final String name, final String value) {
        this.headers.computeIfAbsent(name.toLowerCase(Locale.ROOT), n -> new ArrayList<>()).add(value);
        return this;
    }

    /**
     * Set a header to the request.<br>
     * If the header already exists it will be overwritten.
     *
     * @param name  The name of the header
     * @param value The value of the header
     * @return This instance for chaining
     */
    public HttpRequest setHeader(final String name, final String value) {
        List<String> values = new ArrayList<>();
        values.add(value);
        this.headers.put(name.toLowerCase(Locale.ROOT), values);
        return this;
    }

    /**
     * Remove a header from the request.
     *
     * @param name The name of the header
     * @return This instance for chaining
     */
    public HttpRequest removeHeader(final String name) {
        this.headers.remove(name.toLowerCase(Locale.ROOT));
        return this;
    }

    /**
     * Remove all headers from the request.
     *
     * @return This instance for chaining
     */
    public HttpRequest clearHeaders() {
        this.headers.clear();
        return this;
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
    public HttpRequest setFollowRedirects(final FollowRedirects followRedirects) {
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


    public enum FollowRedirects {
        NOT_SET, FOLLOW, IGNORE
    }

}
