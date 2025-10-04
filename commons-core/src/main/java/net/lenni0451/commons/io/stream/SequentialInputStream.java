package net.lenni0451.commons.io.stream;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * An {@link InputStream} that allows appending data to the stream.<br>
 * The stream will block if no data is available until new data is appended or the stream is closed.<br>
 * To ensure thread safety, all methods are synchronized internally.
 */
public class SequentialInputStream extends InputStream {

    private static final int DEFAULT_INITIAL_CAPACITY = 0;
    private static final float DEFAULT_GROWTH_FACTOR = 1;

    private final Object lock = new Object();
    private final float growthFactor;
    private byte[] available;
    private int pos;
    private int limit;
    private boolean closed;
    private Throwable error;

    /**
     * Create a new SequentialInputStream with default initial capacity and growth factor.
     */
    public SequentialInputStream() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_GROWTH_FACTOR);
    }

    /**
     * Create a new SequentialInputStream with the given initial capacity and default growth factor.
     *
     * @param initialCapacity The initial capacity of the internal buffer (must be >= 0)
     */
    public SequentialInputStream(final int initialCapacity) {
        this(initialCapacity, DEFAULT_GROWTH_FACTOR);
    }

    /**
     * Create a new SequentialInputStream with the given growth factor and default initial capacity.
     *
     * @param growthFactor The growth factor of the internal buffer when it needs to grow (must be >= 1)
     */
    public SequentialInputStream(final float growthFactor) {
        this(DEFAULT_INITIAL_CAPACITY, growthFactor);
    }

    /**
     * Create a new SequentialInputStream with the given initial capacity and growth factor.
     *
     * @param initialCapacity The initial capacity of the internal buffer (must be >= 0)
     * @param growthFactor    The growth factor of the internal buffer when it needs to grow (must be >= 1)
     */
    public SequentialInputStream(final int initialCapacity, final float growthFactor) {
        if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0");
        if (growthFactor < 1) throw new IllegalArgumentException("growthFactor must be >= 1");
        this.growthFactor = growthFactor;
        this.available = new byte[initialCapacity];
        this.pos = 0;
        this.limit = 0;
        this.closed = false;
        this.error = null;
    }

    /**
     * @return If the stream is closed
     */
    public boolean isClosed() {
        synchronized (this.lock) {
            return this.closed;
        }
    }

    /**
     * Append data to the stream.<br>
     * If the internal buffer is full, it will be resized according to the growth factor.
     *
     * @param bytes The data to append
     * @throws NullPointerException  If bytes is null
     * @throws IllegalStateException If the stream is closed
     */
    public void append(final byte[] bytes) {
        if (bytes == null) throw new NullPointerException("bytes");
        synchronized (this.lock) {
            if (this.closed) throw new IllegalStateException("Stream is closed");
            if (this.pos > 0) {
                System.arraycopy(this.available, this.pos, this.available, 0, this.limit - this.pos);
                this.limit -= this.pos;
                this.pos = 0;
            }
            if (this.limit + bytes.length > this.available.length) {
                this.available = Arrays.copyOf(this.available, (int) ((this.limit + bytes.length) * this.growthFactor));
            }
            System.arraycopy(bytes, 0, this.available, this.limit, bytes.length);
            this.limit += bytes.length;
            this.lock.notifyAll();
        }
    }

    private void waitForData() throws IOException {
        synchronized (this.lock) {
            while (this.pos >= this.limit && !this.closed) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Thread interrupted", e);
                }
            }
            if (this.error != null) {
                if (this.error instanceof IOException) {
                    throw (IOException) this.error;
                } else {
                    throw new IOException("Error while reading stream", this.error);
                }
            }
        }
    }

    /**
     * Read a single byte from the stream.<br>
     * This method will block if no data is available until new data is appended or the stream is closed.
     *
     * @return The read byte or -1 if the stream is closed and no more data is available
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        synchronized (this.lock) {
            this.waitForData();
            if (this.pos >= this.limit) return -1; //Closed
            return this.available[this.pos++] & 0xFF;
        }
    }

    /**
     * Read bytes from the stream into the given buffer.<br>
     * This method will block if no data is available until new data is appended or the stream is closed.
     *
     * @param b   The buffer into which the data is read
     * @param off The start offset in array <code>b</code> at which the data is written
     * @param len The maximum number of bytes to read
     * @return The total number of bytes read into the buffer, or -1 if there is no more data because the end of the stream has been reached
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int read(@Nonnull byte[] b, int off, int len) throws IOException {
        synchronized (this.lock) {
            this.waitForData();
            if (this.pos >= this.limit) return -1; //Closed
            int bytesToRead = Math.min(len, this.limit - this.pos);
            System.arraycopy(this.available, this.pos, b, off, bytesToRead);
            this.pos += bytesToRead;
            return bytesToRead;
        }
    }

    /**
     * Close the stream.<br>
     * Any blocked read operations will be unblocked and return -1 if no more data is available.
     */
    @Override
    public void close() {
        synchronized (this.lock) {
            this.closed = true;
            this.lock.notifyAll();
        }
    }

    /**
     * Close the stream with an error.<br>
     * Any blocked read operations will be unblocked and throw an IOException with the given error as cause.
     *
     * @param error The error that caused the stream to close
     */
    public void close(final Throwable error) {
        synchronized (this.lock) {
            this.error = error;
            this.closed = true;
            this.lock.notifyAll();
        }
    }

}
