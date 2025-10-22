package net.lenni0451.commons.httpclient.content;

import net.lenni0451.commons.httpclient.content.impl.*;
import net.lenni0451.commons.httpclient.model.ContentType;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.io.*;
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


    private final ContentType contentType;
    private int bufferSize = 1024;
    private boolean computed = false;
    private byte[] byteCache;

    public HttpContent(final ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * Deprecated, use {@link #getType()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public final ContentType getContentType() {
        return this.getType();
    }

    /**
     * @return The type of this content
     */
    public final ContentType getType() {
        return this.contentType;
    }

    /**
     * @return The recommended buffer size for streaming the content
     */
    public final int getBufferSize() {
        return this.bufferSize;
    }

    /**
     * Set the recommended buffer size for streaming the content.<br>
     * This is only a recommendation and may be ignored by the implementation.
     *
     * @param bufferSize The buffer size
     * @return This instance for chaining
     */
    public final HttpContent setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    /**
     * Transfer the content to the given output stream.<br>
     * The content stream will be closed after the transfer is complete.
     *
     * @param outputStream The output stream to transfer to
     * @throws IOException If an I/O error occurs
     */
    public final void transferTo(@WillNotClose final OutputStream outputStream) throws IOException {
        try (InputStream is = this.getAsStream()) {
            byte[] buffer = new byte[this.bufferSize];
            int read;
            while ((read = is.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, read);
            }
        }
    }

    /**
     * Get the content as an input stream.<br>
     * You are responsible for closing the input stream after use.
     *
     * @return The content as an input stream
     * @throws IOException If an I/O error occurs
     */
    public final InputStream getAsStream() throws IOException {
        if (this.byteCache != null) {
            return new ByteArrayInputStream(this.byteCache);
        } else if (!this.computed || this.canBeStreamedMultipleTimes()) {
            this.computed = true;
            return this.modify(this.compute());
        } else {
            throw new IOException("This content cannot be streamed multiple times");
        }
    }

    /**
     * @return The content as bytes
     * @throws IOException If an I/O error occurs
     */
    @Nonnull
    public final byte[] getAsBytes() throws IOException {
        if (this.byteCache == null) {
            int initSize = this.getLength() > 0 ? this.getLength() : this.bufferSize;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(initSize)) {
                this.transferTo(baos);
                this.byteCache = baos.toByteArray();
            }
        }
        return this.byteCache;
    }

    /**
     * @return The content as a UTF-8 string
     * @throws IOException If an I/O error occurs
     */
    @Nonnull
    public final String getAsString() throws IOException {
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
    public final String getAsString(final Charset charset) throws IOException {
        return new String(this.getAsBytes(), charset);
    }

    /**
     * Clear the cached byte array if present.<br>
     * This will force the content to be recomputed on the next access.<br>
     * This should only be used if {@link #canBeStreamedMultipleTimes()} returns {@code true}.
     */
    protected final void clearCache() {
        this.byteCache = null;
        this.computed = false;
    }

    /**
     * Deprecated, use {@link #getLength()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public final int getContentLength() {
        return this.getLength();
    }

    /**
     * Get if the content can be streamed multiple times.<br>
     * The return value is allowed to change between multiple calls depending on the implementation.
     *
     * @return If the content can be streamed multiple times
     */
    public abstract boolean canBeStreamedMultipleTimes();

    /**
     * Get the content length in bytes.<br>
     * If the content length is unknown, return {@code -1}.<br>
     * Streaming the content may require valid content length depending on the implementation.
     *
     * @return The content length
     */
    public abstract int getLength();

    /**
     * Compute the content and return it as an input stream.<br>
     * This method can be called multiple times if {@link #canBeStreamedMultipleTimes()} returns {@code true}.
     *
     * @return The content
     * @throws IOException If an I/O error occurs
     */
    @Nonnull
    protected abstract InputStream compute() throws IOException;

    /**
     * Modify the given input stream before returning it to the user.<br>
     * This can be used to wrap the input stream with another stream (e.g. for decompression).<br>
     * The default implementation just returns the given input stream.
     *
     * @param inputStream The input stream to modify
     * @return The modified input stream
     * @throws IOException If an I/O error occurs
     */
    protected InputStream modify(final InputStream inputStream) throws IOException {
        return inputStream;
    }

}
