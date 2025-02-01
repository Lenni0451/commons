package net.lenni0451.commons.httpclient.content;

import net.lenni0451.commons.httpclient.model.ContentType;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a streamed http content which is not fully loaded into memory.<br>
 * It requires special handling in the executor to support this type of content.<br>
 * It is fully backwards compatible if the executor does not support this feature by reading the entire stream into memory.<br>
 * All built-in executors support this type of content.
 */
public class StreamedHttpContent extends HttpContent {

    /**
     * Wrap the given http content into a streamed content.<br>
     * This will read the entire content into memory.
     *
     * @param other The content to wrap
     * @return The wrapped content
     * @throws IOException If an error occurs reading the content
     * @see HttpRequestUtils#readFromStream(InputStream)
     */
    public static StreamedHttpContent wrap(final HttpContent other) throws IOException {
        byte[] content = other.getAsBytes();
        return new StreamedHttpContent(other.getContentType(), new ByteArrayInputStream(content), content.length);
    }


    private final InputStream inputStream;
    private final int contentLength;
    private int bufferSize = 1024;

    public StreamedHttpContent(final ContentType contentType, final InputStream inputStream, final int contentLength) {
        super(contentType);
        this.inputStream = inputStream;
        this.contentLength = contentLength;
    }

    /**
     * @return The input stream
     */
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * @return The buffer size for reading the stream
     */
    public int getBufferSize() {
        return this.bufferSize;
    }

    /**
     * Set the buffer size for reading the stream.<br>
     * This option may be ignored depending on the executor implementation.
     *
     * @param bufferSize The buffer size
     * @return This instance for chaining
     */
    public StreamedHttpContent setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    @Override
    public int getContentLength() {
        return this.contentLength;
    }

    @Nonnull
    @Override
    protected byte[] compute() throws IOException {
        //Calling this method 100% defeats the purpose of a streamed content
        return HttpRequestUtils.readFromStream(this.inputStream);
    }

}
