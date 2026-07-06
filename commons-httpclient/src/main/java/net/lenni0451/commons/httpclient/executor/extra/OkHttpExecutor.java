package net.lenni0451.commons.httpclient.executor.extra;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.constants.RequestMethods;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.executor.RequestExecutor;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.proxy.ProxyType;
import net.lenni0451.commons.httpclient.proxy.ThreadedProxyAuthenticator;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;
import net.lenni0451.commons.httpclient.utils.stream.CloseListenerInputStream;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Executor that uses OkHttp to execute requests.<br>
 * Make sure to add the dependency to your project before using this executor.<br>
 * <br>
 * The used {@link OkHttpClient} can be customized by passing a base client and/or a client customizer to the constructor.<br>
 * Use it together with {@link HttpClient#HttpClient(java.util.function.Function)} (e.g. {@code new HttpClient(c -> new OkHttpExecutor(c, myOkHttpClient))}).<br>
 * A single backing client is reused for all requests (per-request settings are applied via {@link OkHttpClient#newBuilder()}), so the
 * connection pool and dispatcher are shared and kept alive; they are never shut down by this executor.<br>
 * <br>
 * Limitations:
 * <ul>
 *     <li>GET and HEAD requests can not have a request body</li>
 * </ul>
 */
public class OkHttpExecutor extends RequestExecutor {

    private static final byte[] EMPTY_BODY = new byte[0];
    //The ignoring SSL factory and trust manager are stateless and can be shared across all requests
    private static volatile SSLSocketFactory ignoringSocketFactory;
    private static final IgnoringTrustManager IGNORING_TRUST_MANAGER = new IgnoringTrustManager();

    @Nullable
    private final OkHttpClient baseClient;
    @Nullable
    private final Consumer<OkHttpClient.Builder> clientCustomizer;
    //Lazily created default client, reused for all requests so its connection pool and dispatcher are shared
    private volatile OkHttpClient defaultClient;

    public OkHttpExecutor(final HttpClient client) {
        this(client, null, null);
    }

    /**
     * @param client           The http client
     * @param clientCustomizer A customizer that is applied to the {@link OkHttpClient.Builder} after the default configuration
     */
    public OkHttpExecutor(final HttpClient client, @Nullable final Consumer<OkHttpClient.Builder> clientCustomizer) {
        this(client, null, clientCustomizer);
    }

    /**
     * @param client     The http client
     * @param baseClient A user provided client which is used as the base for all requests (see {@link OkHttpClient#newBuilder()}).
     *                   Its resources (dispatcher, connection pool, ...) are shared and will not be closed by this executor.
     */
    public OkHttpExecutor(final HttpClient client, @Nullable final OkHttpClient baseClient) {
        this(client, baseClient, null);
    }

    /**
     * @param client           The http client
     * @param baseClient       A user provided client which is used as the base for all requests (see {@link OkHttpClient#newBuilder()}).
     *                         Its resources (dispatcher, connection pool, ...) are shared and will not be closed by this executor.
     * @param clientCustomizer A customizer that is applied to the {@link OkHttpClient.Builder} after the default configuration
     */
    public OkHttpExecutor(final HttpClient client, @Nullable final OkHttpClient baseClient, @Nullable final Consumer<OkHttpClient.Builder> clientCustomizer) {
        super(client);
        this.baseClient = baseClient;
        this.clientCustomizer = clientCustomizer;
    }

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        CookieManager cookieManager = this.getCookieManager(request);
        ProxyHandler proxyHandler = this.client.getProxyHandler();
        //HTTP proxy authentication is handled by OkHttp itself, SOCKS proxies authenticate through the global java.net.Authenticator
        boolean useThreadedAuthenticator = proxyHandler.isProxySet() && proxyHandler.isAuthenticationSet() && !ProxyType.HTTP.equals(proxyHandler.getProxyType());
        OkHttpClient httpClient = this.buildClient(request, cookieManager);
        boolean close = true;
        try {
            if (useThreadedAuthenticator) ThreadedProxyAuthenticator.setAuthentication(proxyHandler.getUsername(), proxyHandler.getPassword());
            Response response = httpClient.newCall(this.buildRequest(request)).execute();
            try {
                URL url = response.request().url().url();
                Map<String, List<String>> headers = response.headers().toMultimap();
                ResponseBody body = response.body();
                if (request.isStreamedResponse() && body != null) {
                    //Cookies are stored by the cookie jar; the connection is returned to the shared pool when the stream is closed
                    InputStream inputStream = new CloseListenerInputStream(body.byteStream(), response::close);
                    close = false;
                    return new HttpResponse(url, response.code(), inputStream, headers);
                } else {
                    return new HttpResponse(url, response.code(), body == null ? EMPTY_BODY : body.bytes(), headers);
                }
            } finally {
                if (close) response.close();
            }
        } finally {
            if (useThreadedAuthenticator) ThreadedProxyAuthenticator.clearAuthentication();
        }
    }

    private OkHttpClient buildClient(final HttpRequest request, @Nullable final CookieManager cookieManager) throws IOException {
        boolean followRedirects = this.isFollowRedirects(request);
        OkHttpClient.Builder builder = this.rootClient().newBuilder()
                .connectTimeout(this.client.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(this.client.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(this.client.getReadTimeout(), TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .followSslRedirects(followRedirects);
        if (cookieManager != null) {
            //A cookie jar participates in OkHttp's own redirect loop, so cookies are sent/stored per host and per hop
            builder.cookieJar(new CookieManagerCookieJar(cookieManager));
        }
        if (this.isIgnoreInvalidSSL(request)) {
            builder.sslSocketFactory(ignoringSocketFactory(), IGNORING_TRUST_MANAGER);
            builder.hostnameVerifier((hostname, session) -> true);
        }
        ProxyHandler proxyHandler = this.client.getProxyHandler();
        if (proxyHandler.isProxySet()) {
            builder.proxy(proxyHandler.toJavaProxy());
            if (proxyHandler.isAuthenticationSet() && ProxyType.HTTP.equals(proxyHandler.getProxyType())) {
                String credentials = Credentials.basic(proxyHandler.getUsername(), proxyHandler.getPassword());
                builder.proxyAuthenticator((route, response) -> {
                    if (response.request().header(HttpHeaders.PROXY_AUTHORIZATION) != null) return null; //The credentials were already rejected
                    return response.request().newBuilder().header(HttpHeaders.PROXY_AUTHORIZATION, credentials).build();
                });
            }
        }
        if (this.clientCustomizer != null) this.clientCustomizer.accept(builder);
        return builder.build();
    }

    private OkHttpClient rootClient() {
        if (this.baseClient != null) return this.baseClient;
        if (this.defaultClient == null) {
            synchronized (this) {
                if (this.defaultClient == null) this.defaultClient = new OkHttpClient();
            }
        }
        return this.defaultClient;
    }

    private Request buildRequest(final HttpRequest request) throws IOException {
        Request.Builder builder = new Request.Builder();
        try {
            builder.url(request.getURL());
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid request URL", e);
        }
        //Cookies are handled by the cookie jar, so they are not injected as a header here
        Map<String, List<String>> headers = this.getHeaders(request, null);
        RequestBody body = null;
        if (request instanceof HttpContentRequest && ((HttpContentRequest) request).hasContent()) {
            //Honor a user provided Content-Type header for the body (OkHttp derives the sent Content-Type from the body)
            String contentType = this.firstHeader(headers, HttpHeaders.CONTENT_TYPE);
            body = this.toRequestBody(((HttpContentRequest) request).getContent(), request.isStreamedRequest(), contentType);
        } else if (this.requiresRequestBody(request.getMethod())) {
            body = RequestBody.create(EMPTY_BODY, null);
        }
        try {
            builder.method(request.getMethod(), body);
            this.setHeaders(headers, builder::header, builder::addHeader);
        } catch (IllegalArgumentException e) {
            //OkHttp rejects e.g. bodies on GET/HEAD or non-ASCII header values with an unchecked exception
            throw new IOException("Invalid request", e);
        }
        return builder.build();
    }

    private RequestBody toRequestBody(final HttpContent content, final boolean streamed, @Nullable final String contentType) throws IOException {
        MediaType mediaType = MediaType.parse(contentType != null ? contentType : content.getType().toString());
        if (streamed) {
            return new RequestBody() {
                @Nullable
                @Override
                public MediaType contentType() {
                    return mediaType;
                }

                @Override
                public long contentLength() {
                    return content.getLength();
                }

                @Override
                public boolean isOneShot() {
                    return !content.canBeStreamedMultipleTimes();
                }

                @Override
                public void writeTo(@Nonnull final BufferedSink sink) throws IOException {
                    content.transferTo(sink.outputStream());
                }
            };
        } else {
            return RequestBody.create(content.getAsBytes(), mediaType);
        }
    }

    private boolean requiresRequestBody(final String method) {
        //Methods that require a request body according to OkHttp (PROPPATCH/REPORT have no RequestMethods constant)
        switch (method) {
            case RequestMethods.POST:
            case RequestMethods.PUT:
            case RequestMethods.PATCH:
            case "PROPPATCH":
            case "REPORT":
                return true;
            default:
                return false;
        }
    }

    @Nullable
    private String firstHeader(final Map<String, List<String>> headers, final String name) {
        List<String> values = headers.get(name.toLowerCase(Locale.ROOT));
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private static SSLSocketFactory ignoringSocketFactory() throws IOException {
        if (ignoringSocketFactory == null) {
            synchronized (OkHttpExecutor.class) {
                if (ignoringSocketFactory == null) ignoringSocketFactory = IgnoringTrustManager.makeIgnoringSSLContext().getSocketFactory();
            }
        }
        return ignoringSocketFactory;
    }


    /**
     * Bridges OkHttp's {@link CookieJar} to a {@link CookieManager} so cookies are sent and stored per host across redirects.
     */
    private static class CookieManagerCookieJar implements CookieJar {
        private final CookieManager cookieManager;

        private CookieManagerCookieJar(final CookieManager cookieManager) {
            this.cookieManager = cookieManager;
        }

        @Nonnull
        @Override
        public List<Cookie> loadForRequest(@Nonnull final HttpUrl url) {
            try {
                Map<String, List<String>> headers = this.cookieManager.get(url.uri(), Collections.emptyMap());
                List<Cookie> cookies = new ArrayList<>();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    if (!"Cookie".equalsIgnoreCase(entry.getKey())) continue;
                    for (String header : entry.getValue()) {
                        for (String pair : header.split(";")) {
                            String trimmed = pair.trim();
                            int idx = trimmed.indexOf('=');
                            if (idx <= 0) continue;
                            cookies.add(new Cookie.Builder()
                                    .name(trimmed.substring(0, idx))
                                    .value(trimmed.substring(idx + 1))
                                    .hostOnlyDomain(url.host())
                                    .path("/")
                                    .build());
                        }
                    }
                }
                return cookies;
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }

        @Override
        public void saveFromResponse(@Nonnull final HttpUrl url, @Nonnull final List<Cookie> cookies) {
            if (cookies.isEmpty()) return;
            List<String> setCookies = new ArrayList<>(cookies.size());
            //Cookie.toString() renders a Set-Cookie compatible string which the CookieManager can parse back
            for (Cookie cookie : cookies) setCookies.add(cookie.toString());
            try {
                this.cookieManager.put(url.uri(), Collections.singletonMap("Set-Cookie", setCookies));
            } catch (IOException ignored) {
            }
        }
    }

}
