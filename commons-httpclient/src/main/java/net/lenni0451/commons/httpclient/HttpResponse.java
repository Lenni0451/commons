package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.StatusCodes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class HttpResponse extends HeaderStore<HttpResponse> {

    private final URL url;
    private final int statusCode;
    private final byte[] body;

    public HttpResponse(final URL url, final int statusCode, final byte[] body, final Map<String, List<String>> headers) {
        this.url = url;
        this.statusCode = statusCode;
        this.body = body;
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

}
