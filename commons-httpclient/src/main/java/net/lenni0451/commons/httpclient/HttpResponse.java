package net.lenni0451.commons.httpclient;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.content.impl.ByteArrayContent;
import net.lenni0451.commons.httpclient.content.impl.InputStreamContent;
import net.lenni0451.commons.httpclient.model.ContentType;
import org.jetbrains.annotations.ApiStatus;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * Use {@link #getContent()} and {@link HttpContent#getContentType()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public Optional<ContentType> getContentType() {
        return this.getFirstHeader("Content-Type").map(ContentType::parse);
    }

}
