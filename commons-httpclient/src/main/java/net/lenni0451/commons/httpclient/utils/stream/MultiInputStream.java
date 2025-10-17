package net.lenni0451.commons.httpclient.utils.stream;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

@RequiredArgsConstructor
public class MultiInputStream extends InputStream {

    private final Queue<InputStream> streams;

    @Override
    public int read() throws IOException {
        if (this.streams.isEmpty()) return -1;
        int result = this.streams.peek().read();
        if (result == -1) {
            this.streams.poll().close();
            return this.read();
        }
        return result;
    }

    @Override
    public int read(@Nonnull byte[] b, int off, int len) throws IOException {
        if (this.streams.isEmpty()) return -1;
        int bytesRead = this.streams.peek().read(b, off, len);
        if (bytesRead == -1) {
            this.streams.poll().close();
            return this.read(b, off, len);
        }
        return bytesRead;
    }

    @Override
    public void close() throws IOException {
        while (!this.streams.isEmpty()) {
            this.streams.poll().close();
        }
    }

}
