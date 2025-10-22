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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class HttpResponse extends HeaderStore<HttpResponse> {

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
     *
     * @return The decoded content
     * @see #getDecodedContent(DecoderProvider)
     */
    public HttpContent getDecodedContent() {
        return this.getDecodedContent(encoding -> {
            switch (encoding.toLowerCase(Locale.ROOT)) {
                case "identity":
                    return inputStream -> inputStream;
                case "gzip":
                case "x-gzip": //Deprecated, earlier name for gzip
                    return GZIPInputStream::new;
                case "deflate":
                    return InflaterInputStream::new;
                default: // Unsupported encoding, return original content
                    return null;
            }
        });
    }

    /**
     * Get the decoded content of the response.<br>
     * Closing the returned content will also close the original content.<br>
     * If the content is not encoded, or uses an unsupported encoding, the original content will be returned.<br>
     * The {@link HttpHeaders#CONTENT_ENCODING} header is renamed to {@code Original-Content-Encoding} if the content has been decoded.
     *
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
    @ApiStatus.ScheduledForRemoval
    public InputStream getInputStream() {
        return this.content.getAsStream();
    }

    /**
     * Use {@link #getContent()} and {@link HttpContent#getAsString()} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval
    public String getContentAsString() {
        return this.content.getAsString();
    }

    /**
     * Use {@link #getContent()} and {@link HttpContent#getAsString(Charset)} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval
    public String getContentAsString(final Charset charset) {
        return this.content.getAsString(charset);
    }

    /**
     * Use {@link #getContent()} and {@link HttpContent#getType()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
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
