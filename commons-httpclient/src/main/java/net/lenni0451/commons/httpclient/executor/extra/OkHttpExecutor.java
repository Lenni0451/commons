package net.lenni0451.commons.httpclient.executor.extra;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.executor.RequestExecutor;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.proxy.ProxyType;
import net.lenni0451.commons.httpclient.proxy.ThreadedProxyAuthenticator;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;
import net.lenni0451.commons.httpclient.utils.stream.CloseListenerInputStream;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Executor that uses OkHttp to execute requests.<br>
 * Make sure to add the dependency to your project before using this executor.<br>
 * <br>
 * The used {@link OkHttpClient} can be customized by passing a base client and/or a client customizer to the constructor.<br>
 * Use it together with {@link HttpClient#HttpClient(java.util.function.Function)} (e.g. {@code new HttpClient(c -> new OkHttpExecutor(c, myOkHttpClient))}).<br>
 * <br>
 * Limitations:
 * <ul>
 *     <li>GET and HEAD requests can not have a request body</li>
 * </ul>
 */
public class OkHttpExecutor extends RequestExecutor {

    private static final byte[] EMPTY_BODY = new byte[0];

    @Nullable
    private final OkHttpClient baseClient;
    @Nullable
    private final Consumer<OkHttpClient.Builder> clientCustomizer;

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
        OkHttpClient httpClient = this.buildClient(request);
        boolean close = true;
        try {
            if (useThreadedAuthenticator) ThreadedProxyAuthenticator.setAuthentication(proxyHandler.getUsername(), proxyHandler.getPassword());
            Response response = httpClient.newCall(this.buildRequest(request, cookieManager)).execute();
            try {
                this.updateCookies(cookieManager, response);
                URL url = response.request().url().url();
                Map<String, List<String>> headers = response.headers().toMultimap();
                ResponseBody body = response.body();
                if (request.isStreamedResponse() && body != null) {
                    InputStream inputStream = new CloseListenerInputStream(body.byteStream(), () -> {
                        response.close();
                        this.closeClient(httpClient);
                    });
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
            if (close) this.closeClient(httpClient);
        }
    }

    private OkHttpClient buildClient(final HttpRequest request) throws IOException {
        boolean followRedirects = this.isFollowRedirects(request);
        OkHttpClient.Builder builder = (this.baseClient != null ? this.baseClient.newBuilder() : new OkHttpClient.Builder())
                .connectTimeout(this.client.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(this.client.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(this.client.getReadTimeout(), TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .followSslRedirects(followRedirects);
        if (this.isIgnoreInvalidSSL(request)) {
            builder.sslSocketFactory(IgnoringTrustManager.makeIgnoringSSLContext().getSocketFactory(), new IgnoringTrustManager());
            builder.hostnameVerifier((hostname, session) -> true);
        }
        ProxyHandler proxyHandler = this.client.getProxyHandler();
        if (proxyHandler.isProxySet()) {
            builder.proxy(proxyHandler.toJavaProxy());
            if (proxyHandler.isAuthenticationSet() && ProxyType.HTTP.equals(proxyHandler.getProxyType())) {
                String credentials = Credentials.basic(proxyHandler.getUsername(), proxyHandler.getPassword());
                builder.proxyAuthenticator((route, response) -> {
                    if (response.request().header("Proxy-Authorization") != null) return null; //The credentials were already rejected
                    return response.request().newBuilder().header("Proxy-Authorization", credentials).build();
                });
            }
        }
        if (this.clientCustomizer != null) this.clientCustomizer.accept(builder);
        return builder.build();
    }

    private Request buildRequest(final HttpRequest request, @Nullable final CookieManager cookieManager) throws IOException {
        Request.Builder builder = new Request.Builder();
        try {
            builder.url(request.getURL());
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid request URL", e);
        }
        RequestBody body = null;
        if (request instanceof HttpContentRequest && ((HttpContentRequest) request).hasContent()) {
            body = this.toRequestBody(((HttpContentRequest) request).getContent(), request.isStreamedRequest());
        } else if (this.requiresRequestBody(request.getMethod())) {
            body = RequestBody.create(EMPTY_BODY, null);
        }
        builder.method(request.getMethod(), body);
        this.setHeaders(this.getHeaders(request, cookieManager), builder::header, builder::addHeader);
        return builder.build();
    }

    private RequestBody toRequestBody(final HttpContent content, final boolean streamed) throws IOException {
        MediaType mediaType = MediaType.parse(content.getType().toString());
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
        //Methods that require a request body according to OkHttp
        switch (method) {
            case "POST":
            case "PUT":
            case "PATCH":
            case "PROPPATCH":
            case "REPORT":
                return true;
            default:
                return false;
        }
    }

    private void updateCookies(@Nullable final CookieManager cookieManager, final Response response) throws IOException {
        if (cookieManager == null) return;
        //Walk the redirect chain from oldest to newest to also store cookies set by intermediate responses
        Deque<Response> responses = new ArrayDeque<>();
        for (Response r = response; r != null; r = r.priorResponse()) responses.addFirst(r);
        for (Response r : responses) {
            this.updateCookies(cookieManager, r.request().url().url(), r.headers().toMultimap());
        }
    }

    private void closeClient(final OkHttpClient httpClient) {
        //Clients derived from a user provided client share its resources which must not be closed
        if (this.baseClient != null) return;
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

}
