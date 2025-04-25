package net.lenni0451.commons.io.stream;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SequentialInputStream extends InputStream {

    private final Object lock = new Object();
    private byte[] available = new byte[0];
    private int pos = 0;
    private int limit = 0;
    private boolean closed = false;
    private Throwable error = null;

    public boolean isClosed() {
        synchronized (this.lock) {
            return this.closed;
        }
    }

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
                this.available = Arrays.copyOf(this.available, this.limit + bytes.length);
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

    @Override
    public int read() throws IOException {
        synchronized (this.lock) {
            this.waitForData();
            if (this.pos >= this.limit) return -1; //Closed
            return this.available[this.pos++] & 0xFF;
        }
    }

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

    @Override
    public void close() {
        synchronized (this.lock) {
            this.closed = true;
            this.lock.notifyAll();
        }
    }

    public void close(final Throwable error) {
        synchronized (this.lock) {
            this.error = error;
            this.closed = true;
            this.lock.notifyAll();
        }
    }

}
