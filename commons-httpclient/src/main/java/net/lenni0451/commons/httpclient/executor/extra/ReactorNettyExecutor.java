package net.lenni0451.commons.httpclient.executor.extra;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.ssl.SslContextBuilder;
import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.executor.RequestExecutor;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;
import net.lenni0451.commons.httpclient.utils.URLWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient.RequestSender;
import reactor.netty.http.client.HttpClient.ResponseReceiver;
import reactor.netty.tcp.SslProvider;
import reactor.netty.transport.ProxyProvider;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.CookieManager;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Executor that uses reactor-netty to execute requests.<br>
 * Make sure to add the dependency to your project before using this executor.<br>
 * <br>
 * Limitations:
 * <ul>
 *     <li>Does not support streaming (request and response)</li>
 * </ul>
 */
public class ReactorNettyExecutor extends RequestExecutor {

    private static final byte[] EMPTY_BODY = new byte[0];

    public ReactorNettyExecutor(final HttpClient client) {
        super(client);
    }

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull final HttpRequest request) throws IOException {
        CookieManager cookieManager = this.getCookieManager(request);
        reactor.netty.http.client.HttpClient httpClient = this.buildClient(request, cookieManager);
        RequestSender requestSender = httpClient.request(HttpMethod.valueOf(request.getMethod()))
                .uri(URLWrapper.of(request.getURL()).toURI());
        ResponseReceiver<?> responseReceiver = requestSender;
        if (request instanceof HttpContentRequest) {
            HttpContentRequest contentRequest = (HttpContentRequest) request;
            if (contentRequest.getContent() != null) {
                responseReceiver = requestSender.send(ByteBufFlux.fromInbound(Flux.just(contentRequest.getContent().getAsBytes())));
            }
        }
        try {
            return responseReceiver.responseSingle((response, content) -> {
                try {
                    URL resourceURL = URLWrapper.ofURL(response.resourceUrl()).toURL();
                    Map<String, List<String>> responseHeaders = this.convertHeaders(response.responseHeaders());
                    this.updateCookies(cookieManager, resourceURL, responseHeaders);
                    return content.asByteArray().defaultIfEmpty(EMPTY_BODY).map(bytes -> new HttpResponse(
                            resourceURL,
                            response.status().code(),
                            bytes,
                            responseHeaders
                    ));
                } catch (Throwable t) {
                    return Mono.error(t);
                }
            }).blockOptional().orElseThrow(() -> new IOException("Response is null"));
        } catch (Throwable t) {
            Throwable cause = t;
            while (cause != null) {
                if (cause instanceof IOException) throw (IOException) cause;
                cause = cause.getCause();
            }
            throw new IOException("Failed to execute request", t);
        }
    }

    private reactor.netty.http.client.HttpClient buildClient(final HttpRequest request, final CookieManager cookieManager) throws IOException {
        Map<String, List<String>> requestHeaders = this.getHeaders(request, cookieManager);
        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
                .responseTimeout(Duration.ofMillis(this.client.getReadTimeout()))
                .followRedirect(this.isFollowRedirects(request))
                .headers(headers -> this.setHeaders(requestHeaders, headers::set, headers::add));
        if (this.isIgnoreInvalidSSL(request)) {
            httpClient = httpClient.secure(SslProvider.builder().sslContext(SslContextBuilder.forClient().trustManager(new IgnoringTrustManager()).build()).build());
        }
        if (this.client.getProxyHandler().isProxySet()) {
            ProxyHandler proxyHandler = this.client.getProxyHandler();
            httpClient = httpClient.proxy(typeSpec -> {
                ProxyProvider.AddressSpec addressSpec;
                switch (proxyHandler.getProxyType()) {
                    case HTTP:
                        addressSpec = typeSpec.type(ProxyProvider.Proxy.HTTP);
                        break;
                    case SOCKS4:
                        addressSpec = typeSpec.type(ProxyProvider.Proxy.SOCKS4);
                        break;
                    case SOCKS5:
                        addressSpec = typeSpec.type(ProxyProvider.Proxy.SOCKS5);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported proxy type: " + proxyHandler.getProxyType());
                }
                ProxyProvider.Builder builder = addressSpec.socketAddress(proxyHandler.getAddress());
                if (proxyHandler.getUsername() != null) builder.username(proxyHandler.getUsername());
                if (proxyHandler.getPassword() != null) builder.password(s -> proxyHandler.getPassword());
            });
        }
        return httpClient;
    }

    private Map<String, List<String>> convertHeaders(final HttpHeaders headers) {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<String, String> entry : headers.entries()) {
            map.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(entry.getValue());
        }
        return map;
    }

}
