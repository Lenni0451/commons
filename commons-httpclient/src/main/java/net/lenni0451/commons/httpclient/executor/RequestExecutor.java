package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.CookieManager;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class RequestExecutor {

    @Nonnull
    protected final HttpClient client;

    public RequestExecutor(@Nonnull final HttpClient client) {
        this.client = client;
    }

    @Nonnull
    public abstract HttpResponse execute(@Nonnull final HttpRequest request) throws IOException, InterruptedException;

    protected final boolean isFollowRedirects(@Nonnull final HttpRequest request) {
        switch (request.getFollowRedirects()) {
            case NOT_SET:
                return this.client.isFollowRedirects();
            case FOLLOW:
                return true;
            case IGNORE:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + request.getFollowRedirects());
        }
    }

    @Nullable
    protected final CookieManager getCookieManager(@Nonnull final HttpRequest request) {
        return request.isCookieManagerSet() ? request.getCookieManager() : this.client.getCookieManager();
    }

    protected final boolean isIgnoreInvalidSSL(@Nonnull final HttpRequest request) {
        return request.isIgnoreInvalidSSLSet() ? request.getIgnoreInvalidSSL() : this.client.isIgnoreInvalidSSL();
    }

    protected final Map<String, List<String>> getHeaders(@Nonnull final HttpRequest request, @Nullable final CookieManager cookieManager) throws IOException {
        return this.getHeaders(request, cookieManager, true);
    }

    protected final Map<String, List<String>> getHeaders(@Nonnull final HttpRequest request, @Nullable final CookieManager cookieManager, final boolean includeContentHeaders) throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        //Add cookie headers first so they can be overridden by client/request headers
        if (cookieManager != null) {
            try {
                Map<String, List<String>> cookieHeaders = cookieManager.get(request.getURL().toURI(), Collections.emptyMap());
                for (Map.Entry<String, List<String>> entry : cookieHeaders.entrySet()) {
                    if (entry.getValue().isEmpty()) continue; //Skip empty headers
                    headers.put(entry.getKey().toLowerCase(), entry.getValue());
                }
            } catch (URISyntaxException e) {
                throw new IOException("Failed to parse URL as URI", e);
            }
        }
        //Add content headers if requested
        if (request instanceof HttpContentRequest && includeContentHeaders) {
            HttpContent content = ((HttpContentRequest) request).getContent();
            if (content != null) {
                headers.put(HttpHeaders.CONTENT_TYPE.toLowerCase(), Collections.singletonList(content.getType().toString()));
                if (content.getLength() < 0) {
                    headers.put(HttpHeaders.CONTENT_LENGTH.toLowerCase(), Collections.singletonList(String.valueOf(content.getLength())));
                }
            }
        }
        //Add client headers
        for (Map.Entry<String, List<String>> entry : this.client.getHeaders().entrySet()) {
            if (entry.getValue().isEmpty()) continue; //Skip empty headers
            headers.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        //Add request headers
        for (Map.Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
            if (entry.getValue().isEmpty()) continue; //Skip empty headers
            headers.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return headers;
    }

    protected final void updateCookies(@Nullable final CookieManager cookieManager, final URL url, final Map<String, List<String>> headers) throws IOException {
        if (cookieManager == null) return;
        try {
            cookieManager.put(url.toURI(), headers);
        } catch (URISyntaxException e) {
            throw new IOException("Failed to parse URL as URI", e);
        }
    }

    protected final void setHeaders(final Map<String, List<String>> headers, final BiConsumer<String, String> setConsumer, final BiConsumer<String, String> addConsumer) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (HttpHeaders.COOKIE.equalsIgnoreCase(entry.getKey())) {
                setConsumer.accept(entry.getKey(), String.join("; ", entry.getValue()));
            } else {
                boolean first = true;
                for (String val : entry.getValue()) {
                    if (first) {
                        first = false;
                        //Use the first value to clear all previous values
                        setConsumer.accept(entry.getKey(), val);
                    } else {
                        addConsumer.accept(entry.getKey(), val);
                    }
                }
            }
        }
    }

}
