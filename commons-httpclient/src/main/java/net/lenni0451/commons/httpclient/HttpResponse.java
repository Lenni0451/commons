package net.lenni0451.commons.httpclient;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.content.DelegatingHttpContent;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.content.impl.ByteArrayContent;
import net.lenni0451.commons.httpclient.content.impl.InputStreamContent;
import net.lenni0451.commons.httpclient.model.ContentType;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class HttpResponse extends HeaderStore<HttpResponse> {

    private static final Map<String, InputStreamMapper> DEFAULT_DECODERS = new HashMap<>();

    static {
        DEFAULT_DECODERS.put("identity", inputStream -> inputStream);
        DEFAULT_DECODERS.put("gzip", GZIPInputStream::new);
        DEFAULT_DECODERS.put("x-gzip", GZIPInputStream::new); //Deprecated, earlier name for gzip
        DEFAULT_DECODERS.put("deflate", InflaterInputStream::new);
        DEFAULT_DECODERS.put("br", getExistingDecoder(
                //brotli4j - com.aayushatharva.brotli4j:brotli4j
                "com.aayushatharva.brotli4j.decoder.BrotliInputStream",
                //brotli - org.brotli:dec
                "org.brotli.dec.BrotliInputStream"
        ));
        DEFAULT_DECODERS.put("zstd", getExistingDecoder(
                //aircompressor - io.airlift:aircompressor
                "io.airlift.compress.zstd.ZstdInputStream",
                //aircompressor - io.airlift:aircompressor-v3
                "io.airlift.compress.v3.zstd.ZstdInputStream",
                //zsdt-jni - com.github.luben:zstd-jni
                "com.github.luben.zstd.ZstdInputStream"
        ));
    }

    @Nullable
    private static InputStreamMapper getExistingDecoder(final String... decoderClasses) {
        for (String decoderClass : decoderClasses) {
            try {
                Class<? extends InputStream> clazz = Class.forName(decoderClass).asSubclass(InputStream.class);
                Constructor<? extends InputStream> constructor = clazz.getDeclaredConstructor(InputStream.class);
                return inputStream -> {
                    try {
                        return constructor.newInstance(inputStream);
                    } catch (Throwable t) {
                        throw new IOException("Failed to create decoder input stream of type " + decoderClass, t);
                    }
                };
            } catch (Throwable ignored) {
            }
        }
        return null;
    }


    private final URL url;
    private final int statusCode;
    private final HttpContent content;

    public HttpResponse(final URL url, final int statusCode, final byte[] content, final Map<String, List<String>> headers) {
        super(headers);
        this.url = url;
        this.statusCode = statusCode;
        this.content = new ByteArrayContent(
                this.getFirstHeader(HttpHeaders.CONTENT_TYPE).map(ContentType::parse).orElse(ContentTypes.APPLICATION_OCTET_STREAM),
                content
        );
    }

    public HttpResponse(final URL url, final int statusCode, final InputStream inputStream, final Map<String, List<String>> headers) {
        super(headers);
        this.url = url;
        this.statusCode = statusCode;
        this.content = new InputStreamContent(
                this.getFirstHeader(HttpHeaders.CONTENT_TYPE).map(ContentType::parse).orElse(ContentTypes.APPLICATION_OCTET_STREAM),
                inputStream,
                this.getFirstHeader(HttpHeaders.CONTENT_LENGTH).map(s -> {
                    try {
                        return Integer.valueOf(s);
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                }).orElse(-1)
        );
    }

    public HttpResponse(final URL url, final int statusCode, final HttpContent content, final Map<String, List<String>> headers) {
        super(headers);
        this.url = url;
        this.statusCode = statusCode;
        this.content = content;
    }

    /**
     * @return The request url
     */
    public URL getURL() {
        return this.url;
    }

    /**
     * @return The status code of the response
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * @return The message of the status code
     */
    public String getStatusMessage() {
        return StatusCodes.STATUS_CODES.getOrDefault(this.statusCode, "Unknown");
    }

    /**
     * @return The content of the response
     */
    public HttpContent getContent() {
        return this.content;
    }

    /**
     * Get the decoded content of the response.<br>
     * Supported encodings are:
     * <ul>
     *     <li>identify (no encoding)</li>
     *     <li>gzip (also x-gzip)</li>
     *     <li>deflate</li>
     * </ul>
     * Library support for the following encodings is also included, if the respective library is available on the classpath:
     * <ul>
     *     <li>brotli
     *         <ul>
     *             <li>com.aayushatharva.brotli4j:brotli4j</li>
     *             <li>org.brotli:dec</li>
     *         </ul>
     *     </li>
     *     <li>zstd
     *         <ul>
     *             <li>io.airlift:aircompressor</li>
     *             <li>io.airlift:aircompressor-v3</li>
     *             <li>com.github.luben:zstd-jni</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @return The decoded content
     * @see #getDecodedContent(DecoderProvider)
     */
    public HttpContent getDecodedContent() {
        return this.getDecodedContent(encoding -> DEFAULT_DECODERS.get(encoding.toLowerCase(Locale.ROOT)));
    }

    /**
     * Get the decoded content of the response.<br>
     * Closing the returned content will also close the original content.<br>
     * If the content is not encoded, or uses an unsupported encoding, the original content will be returned.<br>
     * The {@link HttpHeaders#CONTENT_ENCODING} header is renamed to {@code Original-Content-Encoding} if the content has been decoded.
     *
     * @param decoderProvider The provider for content decoders
     * @return The decoded content
     */
    public HttpContent getDecodedContent(final DecoderProvider decoderProvider) {
        String contentEncoding = this.getFirstHeader(HttpHeaders.CONTENT_ENCODING).orElse(null);
        if (contentEncoding == null) return this.content;
        String[] encodings = contentEncoding.split(",\\s*");
        List<InputStreamMapper> decoders = new ArrayList<>(encodings.length);
        for (String encoding : encodings) {
            InputStreamMapper decoder = decoderProvider.get(encoding);
            if (decoder == null) {
                // Unsupported encoding, return original content
                return this.content;
            }
            decoders.add(decoder);
        }
        this.removeHeader(HttpHeaders.CONTENT_ENCODING);
        this.setHeader("Original-" + HttpHeaders.CONTENT_ENCODING, contentEncoding);
        return new DelegatingHttpContent(this.content) {
            @Override
            protected InputStream modify(InputStream inputStream) throws IOException {
                inputStream = super.modify(inputStream);
                for (int i = decoders.size() - 1; i >= 0; i--) {
                    inputStream = decoders.get(i).map(inputStream);
                }
                return inputStream;
            }
        };
    }

    /**
     * Use {@link #getContent()} and {@link HttpContent#getAsStream()} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval //17.04.2026
    public InputStream getInputStream() {
        return this.content.getAsStream();
    }

    /**
     * Use {@link #getContent()} and {@link HttpContent#getAsString()} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval //17.04.2026
    public String getContentAsString() {
        return this.content.getAsString();
    }

    /**
     * Use {@link #getContent()} and {@link HttpContent#getAsString(Charset)} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval //17.04.2026
    public String getContentAsString(final Charset charset) {
        return this.content.getAsString(charset);
    }

    /**
     * Use {@link #getContent()} and {@link HttpContent#getType()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval //17.04.2026
    public Optional<ContentType> getContentType() {
        return this.getFirstHeader("Content-Type").map(ContentType::parse);
    }


    /**
     * A function that maps an input stream to another input stream, e.g., for decompression.
     */
    @FunctionalInterface
    public interface InputStreamMapper {
        InputStream map(final InputStream inputStream) throws IOException;
    }

    /**
     * A provider for decoders based on the encoding name.
     */
    @FunctionalInterface
    public interface DecoderProvider {
        /**
         * Get a decoder for the given encoding name.<br>
         * All names will be converted to lower case before lookup.
         *
         * @param encoding The encoding name
         * @return The decoder, or null if the encoding is not supported
         */
        @Nullable
        InputStreamMapper get(final String encoding);
    }

}
