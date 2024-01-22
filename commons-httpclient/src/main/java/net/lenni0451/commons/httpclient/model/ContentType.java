package net.lenni0451.commons.httpclient.model;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class ContentType {

    /**
     * Parse a content type string.
     *
     * @param contentType The content type string
     * @return The parsed content type
     */
    public static ContentType parse(final String contentType) {
        if (!contentType.contains(";")) return new ContentType(contentType.toLowerCase(Locale.ROOT), null, null);
        String[] parts = contentType.split(";");
        String type = parts[0].toLowerCase(Locale.ROOT);
        Charset charset = null;
        String boundary = null;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.startsWith("charset=")) {
                try {
                    charset = Charset.forName(part.substring(8));
                } catch (UnsupportedCharsetException ignored) {
                }
            } else if (part.startsWith("boundary=")) {
                boundary = part.substring(9);
            }
        }
        return new ContentType(type, charset, boundary);
    }


    private final String mimeType;
    private final Charset charset;
    private final String boundary;

    public ContentType(final String mimeType) {
        this(mimeType, null, null);
    }

    public ContentType(final String mimeType, @Nullable final Charset charset) {
        this(mimeType, charset, null);
    }

    public ContentType(final String mimeType, @Nullable final String boundary) {
        this(mimeType, null, boundary);
    }

    public ContentType(final String mimeType, @Nullable final Charset charset, @Nullable final String boundary) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.boundary = boundary;
    }

    /**
     * @return The mime type of the content type
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * @return The charset of the content type
     */
    public Optional<Charset> getCharset() {
        return Optional.ofNullable(this.charset);
    }

    /**
     * @return The boundary of the content type
     */
    public Optional<String> getBoundary() {
        return Optional.ofNullable(this.boundary);
    }

    @Override
    public String toString() {
        return this.mimeType + (this.charset != null ? "; charset=" + this.charset.name() : "") + (this.boundary != null ? "; boundary=" + this.boundary : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentType that = (ContentType) o;
        return Objects.equals(this.mimeType, that.mimeType) && Objects.equals(this.charset, that.charset) && Objects.equals(this.boundary, that.boundary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mimeType, this.charset, this.boundary);
    }

}
