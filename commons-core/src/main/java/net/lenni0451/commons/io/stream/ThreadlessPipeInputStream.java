package net.lenni0451.commons.io.stream;

import net.lenni0451.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A thread-less pipe that connects an input stream to an output stream wrapper, allowing the transformed data to be read back as an input stream.<br>
 * Useful for applying stream transformations (GZIP, Cipher, etc.) without the overhead of PipedInputStream/PipedOutputStream and separate threads.
 */
public class ThreadlessPipeInputStream extends InputStream {

    private final InputStream source;
    private final InternalOutputStream sink;
    private final OutputStream pipe;
    private final byte[] scratchBuffer;

    private boolean sourceExhausted = false;
    private boolean closed = false;

    /**
     * @param source  The source input stream
     * @param wrapper A function that wraps our internal sink with your transformer (e.g. GZIPOutputStream)
     */
    public ThreadlessPipeInputStream(final InputStream source, final OutputStreamWrapper wrapper) throws IOException {
        this(source, wrapper, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    /**
     * @param source     The source input stream
     * @param wrapper    A function that wraps our internal sink with your transformer (e.g. GZIPOutputStream)
     * @param bufferSize The size of the internal buffer used to transfer data from the source to the sink
     */
    public ThreadlessPipeInputStream(final InputStream source, final OutputStreamWrapper wrapper, final int bufferSize) throws IOException {
        this.source = source;
        this.sink = new InternalOutputStream(bufferSize);
        this.pipe = wrapper.wrap(this.sink);
        this.scratchBuffer = new byte[bufferSize];
    }

    @Override
    public int read() throws IOException {
        if (this.closed) throw new IOException("Stream closed");

        while (this.sink.available() == 0) {
            if (this.sourceExhausted) return -1;
            this.fillBuffer();
        }
        return this.sink.readByte();
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) throw new IOException("Stream closed");
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) return 0;

        while (this.sink.available() == 0) {
            if (this.sourceExhausted) return -1;
            this.fillBuffer();
        }
        return this.sink.readArray(b, off, len);
    }

    @Override
    public int available() throws IOException {
        if (this.closed) throw new IOException("Stream closed");
        return this.sink.available();
    }

    @Override
    public void close() throws IOException {
        if (this.closed) return;
        this.closed = true;
        try {
            this.pipe.close();
        } finally {
            this.source.close();
        }
    }

    private void fillBuffer() throws IOException {
        if (this.sourceExhausted) return;

        int read = this.source.read(this.scratchBuffer);
        if (read < 0) {
            this.sourceExhausted = true;
            //Closing the pipe is crucial to flush trailers (e.g. GZIP footer)
            this.pipe.close();
        } else {
            this.pipe.write(this.scratchBuffer, 0, read);
            //Some streams buffer internally, flush to ensure data reaches our sink
            this.pipe.flush();
        }
    }


    private static class InternalOutputStream extends OutputStream {
        private byte[] buffer;
        private int head;
        private int tail;
        private int count;

        public InternalOutputStream(final int initialCapacity) {
            this.buffer = new byte[initialCapacity];
        }

        public int available() {
            return this.count;
        }

        public int readByte() {
            if (this.count == 0) throw new IllegalStateException("Buffer empty");
            int val = this.buffer[this.head] & 0xFF;
            this.head = (this.head + 1) % this.buffer.length;
            this.count--;
            return val;
        }

        public int readArray(final byte[] target, final int off, final int len) {
            if (this.count == 0) return 0;
            int toRead = Math.min(len, this.count);
            int firstChunk = Math.min(toRead, this.buffer.length - this.head);
            System.arraycopy(this.buffer, this.head, target, off, firstChunk);
            if (firstChunk < toRead) {
                System.arraycopy(this.buffer, 0, target, off + firstChunk, toRead - firstChunk);
            }
            this.head = (this.head + toRead) % this.buffer.length;
            this.count -= toRead;
            return toRead;
        }

        @Override
        public void write(final int b) {
            this.ensureCapacity(1);
            this.buffer[this.tail] = (byte) b;
            this.tail = (this.tail + 1) % this.buffer.length;
            this.count++;
        }

        @Override
        public void write(final byte[] b, final int off, final int len) {
            this.ensureCapacity(len);
            int firstChunk = Math.min(len, this.buffer.length - this.tail);
            System.arraycopy(b, off, this.buffer, this.tail, firstChunk);
            if (firstChunk < len) {
                System.arraycopy(b, off + firstChunk, this.buffer, 0, len - firstChunk);
            }
            this.tail = (this.tail + len) % this.buffer.length;
            this.count += len;
        }

        private void ensureCapacity(final int required) {
            if (this.buffer.length - this.count >= required) return;

            int newCapacity = this.buffer.length;
            while (newCapacity - this.count < required) {
                newCapacity *= 2;
            }
            byte[] newBuffer = new byte[newCapacity];
            int firstChunk = Math.min(this.count, this.buffer.length - this.head);
            System.arraycopy(this.buffer, this.head, newBuffer, 0, firstChunk);
            if (firstChunk < this.count) {
                System.arraycopy(this.buffer, 0, newBuffer, firstChunk, this.count - firstChunk);
            }
            this.buffer = newBuffer;
            this.head = 0;
            this.tail = this.count;
        }
    }

    @FunctionalInterface
    public interface OutputStreamWrapper {
        OutputStream wrap(final OutputStream os) throws IOException;
    }

}
