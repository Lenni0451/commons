package net.lenni0451.commons.httpclient.content;

import net.lenni0451.commons.httpclient.content.impl.ByteArrayContent;
import net.lenni0451.commons.httpclient.content.impl.FileContent;
import net.lenni0451.commons.httpclient.content.impl.FormContent;
import net.lenni0451.commons.httpclient.content.impl.StringContent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

@ParametersAreNonnullByDefault
public interface HttpContent {

    /**
     * Create a new content from the given bytes.
     *
     * @param content The bytes to send
     * @return The created content
     */
    static HttpContent bytes(final byte[] content) {
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
    static HttpContent bytes(final byte[] content, final int offset, final int length) {
        return new ByteArrayContent(content, offset, length);
    }

    /**
     * Create a new content from the given string.
     *
     * @param content The string to send
     * @return The created content
     */
    static HttpContent string(final String content) {
        return new StringContent(content);
    }

    /**
     * Create a new content from the given string.
     *
     * @param content The string to send
     * @param charset The charset to use
     * @return The created content
     */
    static HttpContent string(final String content, final Charset charset) {
        return new StringContent(content, charset);
    }

    /**
     * Create a new content from the given file.
     *
     * @param file The file to send
     * @return The created content
     */
    static HttpContent file(final File file) {
        return new FileContent(file);
    }

    /**
     * Create a new content from the given form data.
     *
     * @param key   The key
     * @param value The value
     * @return The created content
     */
    static HttpContent form(final String key, final String value) {
        return new FormContent().put(key, value);
    }

    /**
     * Create a new content from the given form data.
     *
     * @param form The form data
     * @return The created content
     */
    static HttpContent form(final Map<String, String> form) {
        return new FormContent(form);
    }


    /**
     * @return The default content type
     */
    String getDefaultContentType();

    /**
     * @return The content length
     */
    int getContentLength();

    /**
     * Write the content to the given output stream.
     *
     * @param outputStream The output stream to write to
     * @throws IOException If an I/O error occurs
     */
    void writeContent(final OutputStream outputStream) throws IOException;

}
