package net.lenni0451.commons.httpclient.requests;

import net.lenni0451.commons.httpclient.HeaderStore;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class HttpRequest extends HeaderStore<HttpRequest> {

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
