package net.lenni0451.commons.httpclient.executor.extra;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.ssl.SslContextBuilder;
import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.executor.RequestExecutor;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.utils.IgnoringTrustManager;
import net.lenni0451.commons.httpclient.utils.URLWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.Connection;
import reactor.netty.http.client.HttpClient.RequestSender;
import reactor.netty.http.client.HttpClient.ResponseReceiver;
import reactor.netty.tcp.SslProvider;
import reactor.netty.transport.ProxyProvider;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.CookieManager;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Executor that uses reactor-netty to execute requests.<br>
 * Make sure to add the dependency to your project before using this executor.<br>
 * <br>
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
                HttpContent content = contentRequest.getContent();
                if (request.isStreamedRequest()) {
                    responseReceiver = requestSender.send((req, outbound) -> outbound.send(this.toRequestBody(content, outbound.alloc())).then());
                } else {
                    responseReceiver = requestSender.send(ByteBufFlux.fromInbound(Flux.just(content.getAsBytes())));
                }
            }
        }
        try {
            if (request.isStreamedResponse()) {
                HttpResponse response = responseReceiver.responseConnection((clientResponse, connection) -> {
                    try {
                        URL resourceURL = URLWrapper.ofURL(clientResponse.resourceUrl()).toURL();
                        Map<String, List<String>> responseHeaders = this.convertHeaders(clientResponse.responseHeaders());
                        this.updateCookies(cookieManager, resourceURL, responseHeaders);
                        return Mono.just(new HttpResponse(
                                resourceURL,
                                clientResponse.status().code(),
                                new ByteBufStreamInputStream(connection.inbound().receive().retain(), connection),
                                responseHeaders
                        ));
                    } catch (Throwable t) {
                        connection.dispose();
                        return Mono.error(t);
                    }
                }).blockFirst();
                if (response == null) throw new IOException("Response is null");
                return response;
            }
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

    private Flux<ByteBuf> toRequestBody(final HttpContent content, final ByteBufAllocator allocator) {
        final int bufferSize = Math.max(1, content.getBufferSize());
        return Mono.fromCallable(content::getAsStream).flatMapMany(inputStream -> Flux.generate(
                () -> new ContentStreamState(inputStream, new byte[bufferSize]),
                (state, sink) -> {
                    try {
                        int read = state.inputStream.read(state.buffer);
                        if (read < 0) {
                            sink.complete();
                        } else {
                            ByteBuf byteBuf = allocator.buffer(read);
                            byteBuf.writeBytes(state.buffer, 0, read);
                            sink.next(byteBuf);
                        }
                    } catch (Throwable t) {
                        sink.error(t);
                    }
                    return state;
                },
                state -> {
                    try {
                        state.inputStream.close();
                    } catch (IOException ignored) {
                    }
                }
        ));
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


    private static class ContentStreamState {
        private final InputStream inputStream;
        private final byte[] buffer;

        private ContentStreamState(final InputStream inputStream, final byte[] buffer) {
            this.inputStream = inputStream;
            this.buffer = buffer;
        }
    }

    private static class ByteBufStreamInputStream extends InputStream {
        private final Stream<ByteBuf> stream;
        private final Iterator<ByteBuf> iterator;
        private final Connection connection;

        private ByteBuf current;
        private boolean closed;

        private ByteBufStreamInputStream(final ByteBufFlux content, final Connection connection) {
            this.stream = content.toStream(1);
            this.iterator = this.stream.iterator();
            this.connection = connection;
        }

        @Override
        public int read() throws IOException {
            if (!this.ensureCurrent()) return -1;
            int value = this.current.readByte() & 0xFF;
            this.releaseIfConsumed();
            return value;
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            if (len == 0) return 0;
            if (!this.ensureCurrent()) return -1;
            int read = Math.min(len, this.current.readableBytes());
            this.current.readBytes(b, off, read);
            this.releaseIfConsumed();
            return read;
        }

        @Override
        public int available() throws IOException {
            if (this.closed) throw new IOException("Stream closed");
            return this.current == null ? 0 : this.current.readableBytes();
        }

        @Override
        public void close() throws IOException {
            if (this.closed) return;
            this.closed = true;

            IOException error = null;
            this.releaseCurrent();
            try {
                this.stream.close();
            } catch (RuntimeException e) {
                error = new IOException("Failed to close response stream", e);
            } finally {
                this.connection.dispose();
            }
            if (error != null) throw error;
        }

        private boolean ensureCurrent() throws IOException {
            if (this.closed) throw new IOException("Stream closed");
            while (this.current == null || !this.current.isReadable()) {
                this.releaseCurrent();
                final boolean hasNext;
                try {
                    hasNext = this.iterator.hasNext();
                } catch (RuntimeException e) {
                    throw unwrap(e);
                }
                if (!hasNext) return false;
                try {
                    this.current = this.iterator.next();
                } catch (RuntimeException e) {
                    throw unwrap(e);
                }
            }
            return true;
        }

        private void releaseIfConsumed() {
            if (this.current != null && !this.current.isReadable()) this.releaseCurrent();
        }

        private void releaseCurrent() {
            if (this.current == null) return;
            this.current.release();
            this.current = null;
        }

        private static IOException unwrap(final RuntimeException e) {
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof IOException) return (IOException) cause;
                if (cause instanceof UncheckedIOException) return ((UncheckedIOException) cause).getCause();
                cause = cause.getCause();
            }
            return new IOException("Failed to read streamed response", e);
        }
    }

}
