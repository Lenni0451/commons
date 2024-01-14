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

    public URL getURL() {
        return this.url;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return StatusCodes.STATUS_CODES.getOrDefault(this.statusCode, "Unknown");
    }

    public byte[] getBody() {
        return this.body;
    }

    public String getBodyAsString() {
        return this.getBodyAsString(Charset.defaultCharset());
    }

    public String getBodyAsString(final Charset charset) {
        return new String(this.body, charset);
    }

    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    public List<String> getHeader(final String name) {
        return this.headers.getOrDefault(name.toLowerCase(Locale.ROOT), Collections.emptyList());
    }

    public Optional<String> getFirstHeader(final String name) {
        List<String> values = this.headers.get(name.toLowerCase(Locale.ROOT));
        if (values == null || values.isEmpty()) return Optional.empty();
        return Optional.of(values.get(0));
    }

    public Optional<String> getLastHeader(final String name) {
        List<String> values = this.headers.get(name.toLowerCase(Locale.ROOT));
        if (values == null || values.isEmpty()) return Optional.empty();
        return Optional.of(values.get(values.size() - 1));
    }

}
