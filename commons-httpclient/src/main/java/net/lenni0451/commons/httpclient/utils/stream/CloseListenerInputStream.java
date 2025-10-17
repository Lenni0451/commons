package net.lenni0451.commons.httpclient.utils.stream;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

public class CloseListenerInputStream extends InputStream {

    private final InputStream inputStream;
    private final CloseListener closeListener;

    public CloseListenerInputStream(final InputStream inputStream, final CloseListener closeListener) {
        this.inputStream = inputStream;
        this.closeListener = closeListener;
    }

    @Override
    public int read() throws IOException {
        return this.inputStream.read();
    }

    @Override
    public int read(@Nonnull byte[] b) throws IOException {
        return this.inputStream.read(b);
    }

    @Override
    public int read(@Nonnull byte[] b, int off, int len) throws IOException {
        return this.inputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return this.inputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return this.inputStream.available();
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
        this.closeListener.close();
    }


    @FunctionalInterface
    public interface CloseListener {
        void close() throws IOException;
    }

}
