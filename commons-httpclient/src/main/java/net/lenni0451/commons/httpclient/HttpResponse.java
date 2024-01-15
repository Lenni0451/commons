package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.StatusCodes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

@ParametersAreNonnullByDefault
public class HttpResponse {

    private final URL url;
    private final int statusCode;
    private final byte[] body;
    private final Map<String, List<String>> headers;

    public HttpResponse(final URL url, final int statusCode, final byte[] body, final Map<String, List<String>> headers) {
        this.url = url;
        this.statusCode = statusCode;
        this.body = body;
        this.headers = new HashMap<>();
        headers.forEach((key, value) -> this.headers.put(key.toLowerCase(Locale.ROOT), value));
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
    public byte[] getBody() {
        return this.body;
    }

    /**
     * @return The response body as a string
     */
    public String getBodyAsString() {
        return this.getBodyAsString(Charset.defaultCharset());
    }

    /**
     * Get the response body as a string with the given charset.
     *
     * @param charset The charset to use
     * @return The response body as a string
     */
    public String getBodyAsString(final Charset charset) {
        return new String(this.body, charset);
    }

    /**
     * @return The response headers
     */
    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    /**
     * Get the response headers with the given name.
     *
     * @param name The name of the header
     * @return The response headers
     */
    public List<String> getHeader(final String name) {
        return this.headers.getOrDefault(name.toLowerCase(Locale.ROOT), Collections.emptyList());
    }

    /**
     * Get the first response header with the given name.
     *
     * @param name The name of the header
     * @return The response header
     */
    public Optional<String> getFirstHeader(final String name) {
        List<String> values = this.headers.get(name.toLowerCase(Locale.ROOT));
        if (values == null || values.isEmpty()) return Optional.empty();
        return Optional.of(values.get(0));
    }

    /**
     * Get the last response header with the given name.
     *
     * @param name The name of the header
     * @return The response header
     */
    public Optional<String> getLastHeader(final String name) {
        List<String> values = this.headers.get(name.toLowerCase(Locale.ROOT));
        if (values == null || values.isEmpty()) return Optional.empty();
        return Optional.of(values.get(values.size() - 1));
    }

}
