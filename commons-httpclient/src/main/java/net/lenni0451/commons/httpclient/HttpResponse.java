package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.model.ContentType;

import javax.annotation.Nonnull;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpResponse extends HeaderStore<HttpResponse> {

    private final URL url;
    private final int statusCode;
    private final byte[] content;

    public HttpResponse(@Nonnull final URL url, final int statusCode, @Nonnull final byte[] content, @Nonnull final Map<String, List<String>> headers) {
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
     * @return The response body
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * @return The response body as a string
     */
    public String getContentAsString() {
        return this.getContentAsString(this.getContentType().flatMap(ContentType::getCharset).orElse(StandardCharsets.UTF_8));
    }

    /**
     * Get the response body as a string with the given charset.
     *
     * @param charset The charset to use
     * @return The response body as a string
     */
    public String getContentAsString(@Nonnull final Charset charset) {
        return new String(this.content, charset);
    }

    /**
     * @return The content type of the response
     */
    public Optional<ContentType> getContentType() {
        return this.getFirstHeader("Content-Type").map(ContentType::parse);
    }

}
