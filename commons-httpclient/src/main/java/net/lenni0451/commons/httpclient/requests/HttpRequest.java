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

    public String getMethod() {
        return this.method;
    }

    public URL getURL() {
        return this.url;
    }

    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    public HttpRequest addHeader(final String name, final String value) {
        this.headers.computeIfAbsent(name.toLowerCase(Locale.ROOT), n -> new ArrayList<>()).add(value);
        return this;
    }

    public HttpRequest setHeader(final String name, final String value) {
        List<String> values = new ArrayList<>();
        values.add(value);
        this.headers.put(name.toLowerCase(Locale.ROOT), values);
        return this;
    }

    public HttpRequest removeHeader(final String name) {
        this.headers.remove(name.toLowerCase(Locale.ROOT));
        return this;
    }

    public HttpRequest clearHeaders() {
        this.headers.clear();
        return this;
    }

    public FollowRedirects getFollowRedirects() {
        return this.followRedirects;
    }

    public HttpRequest setFollowRedirects(final boolean followRedirects) {
        return this.setFollowRedirects(followRedirects ? FollowRedirects.FOLLOW : FollowRedirects.IGNORE);
    }

    public HttpRequest setFollowRedirects(final FollowRedirects followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public boolean isCookieManagerSet() {
        return this.cookieManagerSet;
    }

    public void unsetCookieManager() {
        this.cookieManager = null;
        this.cookieManagerSet = false;
    }

    @Nullable
    public CookieManager getCookieManager() {
        return this.cookieManager;
    }

    public HttpRequest setCookieManager(@Nullable final CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        this.cookieManagerSet = true;
        return this;
    }


    public enum FollowRedirects {
        NOT_SET, FOLLOW, IGNORE
    }

}
