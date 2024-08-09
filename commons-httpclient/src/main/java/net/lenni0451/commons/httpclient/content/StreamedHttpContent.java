package net.lenni0451.commons.httpclient.content;

import net.lenni0451.commons.httpclient.model.ContentType;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a streamed http content which is not fully loaded into memory.<br>
 * It requires special handling in the executor to support this type of content.<br>
 * It is fully backwards compatible if the executor does not support this feature by reading the entire stream into memory.<br>
 * All built-in executors support this type of content.
 */
public class StreamedHttpContent extends HttpContent {

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
        return HttpRequestUtils.readFromStream(this.inputStream);
    }

}
