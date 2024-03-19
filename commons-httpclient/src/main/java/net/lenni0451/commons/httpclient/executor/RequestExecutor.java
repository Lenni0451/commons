package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.CookieManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RequestExecutor {

    @Nonnull
    protected final HttpClient client;

    public RequestExecutor(@Nonnull final HttpClient client) {
        this.client = client;
    }

    @Nonnull
    public abstract HttpResponse execute(@Nonnull final HttpRequest request) throws IOException, InterruptedException;

    @Nullable
    protected final CookieManager getCookieManager(@Nonnull final HttpRequest request) {
        return request.isCookieManagerSet() ? request.getCookieManager() : this.client.getCookieManager();
    }

    protected final boolean isIgnoreInvalidSSL(@Nonnull final HttpRequest request) {
        return request.isIgnoreInvalidSSLSet() ? request.getIgnoreInvalidSSL() : this.client.isIgnoreInvalidSSL();
    }

    protected final Map<String, List<String>> getHeaders(@Nonnull final HttpRequest request, @Nullable final CookieManager cookieManager) throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        if (request instanceof HttpContentRequest) {
            HttpContent content = ((HttpContentRequest) request).getContent();
            if (content != null) {
                headers.put(Headers.CONTENT_TYPE, Collections.singletonList(content.getContentType().toString()));
                headers.put(Headers.CONTENT_LENGTH, Collections.singletonList(String.valueOf(content.getContentLength())));
            }
        }
        return HttpRequestUtils.mergeHeaders(
                HttpRequestUtils.getCookieHeaders(cookieManager, request.getURL()),
                headers,
                this.client.getHeaders(),
                request.getHeaders()
        );
    }

}
