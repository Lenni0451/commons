package net.lenni0451.commons.httpclient;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.constants.StatusCodes;
import net.lenni0451.commons.httpclient.model.ContentType;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpResponse extends HeaderStore<HttpResponse> {

    private final URL url;
    private final int statusCode;
    private byte[] content;
    private InputStream inputStream;

    public HttpResponse(final URL url, final int statusCode, final byte[] content, final Map<String, List<String>> headers) {
        super(headers);
        this.url = url;
        this.statusCode = statusCode;
        this.content = content;
    }

    public HttpResponse(final URL url, final int statusCode, final InputStream inputStream, final Map<String, List<String>> headers) {
        super(headers);
        this.url = url;
        this.statusCode = statusCode;
        this.inputStream = inputStream;
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
     * @return The response body as a stream
     */
    public InputStream getInputStream() {
        if (this.inputStream == null) {
            return new ByteArrayInputStream(this.content);
        } else {
            return this.inputStream;
        }
    }

    /**
     * @return The response body
     * @throws IOException If streaming is enabled and the content could not be read
     */
    @SneakyThrows
    public byte[] getContent() {
        if (this.content == null) {
            //If the content is null, the response is streamed
            //Since the user wants the entire content, we have to read the stream into memory
            this.content = HttpRequestUtils.readFromStream(this.inputStream);
            //Close and null the stream to free resources
            this.inputStream.close();
            this.inputStream = null;
        }
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
    public String getContentAsString(final Charset charset) {
        return new String(this.getContent(), charset);
    }

    /**
     * @return The content type of the response
     */
    public Optional<ContentType> getContentType() {
        return this.getFirstHeader("Content-Type").map(ContentType::parse);
    }

}
