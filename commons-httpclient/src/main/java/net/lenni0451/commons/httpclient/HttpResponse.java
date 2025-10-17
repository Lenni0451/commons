package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.content.impl.ByteArrayContent;
import net.lenni0451.commons.httpclient.content.impl.InputStreamContent;
import net.lenni0451.commons.httpclient.model.ContentType;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
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
                this.getFirstHeader(HttpHeaders.CONTENT_TYPE).map(ContentType::new).orElse(ContentTypes.APPLICATION_OCTET_STREAM),
                content
        );
    }

    public HttpResponse(final URL url, final int statusCode, final InputStream inputStream, final Map<String, List<String>> headers) {
        super(headers);
        this.url = url;
        this.statusCode = statusCode;
        this.content = new InputStreamContent(
                this.getFirstHeader(HttpHeaders.CONTENT_TYPE).map(ContentType::new).orElse(ContentTypes.APPLICATION_OCTET_STREAM),
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

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public InputStream getInputStream() throws IOException {
        return this.content.getAsStream();
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public String getContentAsString() throws IOException {
        return this.content.getAsString();
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public String getContentAsString(final Charset charset) throws IOException {
        return this.content.getAsString(charset);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public Optional<ContentType> getContentType() {
        return Optional.of(this.content.getContentType());
    }

}
