package net.lenni0451.commons.httpclient.content;

import lombok.Getter;
import net.lenni0451.commons.httpclient.content.impl.*;
import net.lenni0451.commons.httpclient.model.ContentType;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class HttpContent {

    /**
     * Create a new content from the given bytes.
     *
     * @param content The bytes to send
     * @return The created content
     */
    public static HttpContent bytes(final byte[] content) {
        return new ByteArrayContent(content);
    }

    /**
     * Create a new content from the given bytes.
     *
     * @param content The bytes to send
     * @param offset  The offset to start reading from
     * @param length  The length of the bytes to read
     * @return The created content
     */
    public static HttpContent bytes(final byte[] content, final int offset, final int length) {
        return new ByteArrayContent(content, offset, length);
    }

    /**
     * Create a new content from the given string.
     *
     * @param content The string to send
     * @return The created content
     */
    public static HttpContent string(final String content) {
        return new StringContent(content);
    }

    /**
     * Create a new content from the given string.
     *
     * @param content The string to send
     * @param charset The charset to use
     * @return The created content
     */
    public static HttpContent string(final String content, final Charset charset) {
        return new StringContent(content, charset);
    }

    /**
     * Create a new content from the given file.
     *
     * @param file The file to send
     * @return The created content
     */
    public static HttpContent file(final File file) {
        return new FileContent(file);
    }

    /**
     * Create a new content from the given form data.
     *
     * @param key   The key
     * @param value The value
     * @return The created content
     */
    public static HttpContent form(final String key, final String value) {
        return new URLEncodedFormContent().put(key, value);
    }

    /**
     * Create a new content from the given form data.
     *
     * @param form The form data
     * @return The created content
     */
    public static HttpContent form(final Map<String, String> form) {
        return new URLEncodedFormContent(form);
    }

    /**
     * Create a new multipart form content.
     *
     * @return The created content
     */
    public static MultiPartFormContent multiPartForm() {
        return new MultiPartFormContent();
    }

    /**
     * Create a new input stream content from the given input stream.
     *
     * @param contentType   The content type
     * @param inputStream   The input stream
     * @param contentLength The content length
     * @return The created content
     */
    public static InputStreamContent inputStream(final ContentType contentType, final InputStream inputStream, final int contentLength) {
        return new InputStreamContent(contentType, inputStream, contentLength);
    }


    @Getter
    private final ContentType contentType;
    @Getter
    private int bufferSize = 1024;
    private boolean computed = false;
    protected byte[] byteCache;

    public HttpContent(final ContentType contentType) {
        this.contentType = contentType;
    }

    public HttpContent setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    /**
     * Get the content as an input stream.<br>
     * You are responsible for closing the input stream after use.
     *
     * @return The content as an input stream
     * @throws IOException If an I/O error occurs
     */
    public InputStream getAsStream() throws IOException {
        if (this.byteCache != null) {
            return new ByteArrayInputStream(this.byteCache);
        } else if (!this.computed || this.canBeStreamedMultipleTimes()) {
            this.computed = true;
            return this.compute();
        } else {
            throw new IOException("This content cannot be streamed multiple times");
        }
    }

    /**
     * @return The content as bytes
     * @throws IOException If an I/O error occurs
     */
    @Nonnull
    public byte[] getAsBytes() throws IOException {
        if (this.byteCache == null) {
            try (InputStream is = this.getAsStream()) {
                this.byteCache = HttpRequestUtils.readFromStream(is);
            }
        }
        return this.byteCache;
    }

    /**
     * @return The content as a UTF-8 string
     * @throws IOException If an I/O error occurs
     */
    @Nonnull
    public String getAsString() throws IOException {
        return this.getAsString(StandardCharsets.UTF_8);
    }

    /**
     * Get the content as a string with the given charset.
     *
     * @param charset The charset to use
     * @return The content as a string
     * @throws IOException If an I/O error occurs
     */
    @Nonnull
    public String getAsString(final Charset charset) throws IOException {
        return new String(this.getAsBytes(), charset);
    }

    /**
     * Get if the content can be streamed multiple times.<br>
     * The return value is allowed to change between multiple calls depending on the implementation.
     *
     * @return If the content can be streamed multiple times
     */
    public abstract boolean canBeStreamedMultipleTimes();

    /**
     * @return The content length
     */
    public abstract int getContentLength();

    /**
     * @return The content
     * @throws IOException If an I/O error occurs
     */
    @Nonnull
    protected abstract InputStream compute() throws IOException;

}
