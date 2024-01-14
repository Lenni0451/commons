package net.lenni0451.commons.httpclient;

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
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class HttpClient implements HttpRequestBuilder {

    private final Map<String, List<String>> defaultHeaders = new HashMap<>();
    @Nullable
    private CookieManager cookieManager = new CookieManager();
    private boolean followRedirects = true;
    private int connectTimeout = 10_000;
    private int readTimeout = 10_000;

    public Map<String, List<String>> getDefaultHeaders() {
        return Collections.unmodifiableMap(this.defaultHeaders);
    }

    public HttpClient addDefaultHeader(final String name, final String value) {
        this.defaultHeaders.computeIfAbsent(name.toLowerCase(Locale.ROOT), n -> new ArrayList<>()).add(value);
        return this;
    }

    public HttpClient setDefaultHeader(final String name, final String value) {
        List<String> values = new ArrayList<>();
        values.add(value);
        this.defaultHeaders.put(name.toLowerCase(Locale.ROOT), values);
        return this;
    }

    public HttpClient removeDefaultHeader(final String name) {
        this.defaultHeaders.remove(name.toLowerCase(Locale.ROOT));
        return this;
    }

    public HttpClient clearDefaultHeaders() {
        this.defaultHeaders.clear();
        return this;
    }

    @Nullable
    public CookieManager getCookieManager() {
        return this.cookieManager;
    }

    public HttpClient setCookieManager(@Nullable final CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        return this;
    }

    public boolean isFollowRedirects() {
        return this.followRedirects;
    }

    public HttpClient setFollowRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public HttpClient setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public HttpClient setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public <R> R execute(final HttpRequest request, final Function<HttpResponse, R> responseHandler) throws IOException {
        return responseHandler.apply(this.execute(request));
    }

    public HttpResponse execute(final HttpRequest request, final Consumer<HttpResponse> responseHandler) throws IOException {
        HttpResponse response = this.execute(request);
        responseHandler.accept(response);
        return response;
    }

    public HttpResponse execute(final HttpRequest request) throws IOException {
        URL url = request.getURL();
        CookieManager cookieManager = request.isCookieManagerSet() ? request.getCookieManager() : this.cookieManager;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        HttpRequestUtils.setHeaders(connection, HttpRequestUtils.mergeHeaders(
                HttpRequestUtils.getCookieHeaders(cookieManager, url),
                this.defaultHeaders,
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
                ((HttpContentRequest) request).getContent().writeContent(os);
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
