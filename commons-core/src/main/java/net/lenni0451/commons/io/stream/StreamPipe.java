package net.lenni0451.commons.io.stream;

import javax.annotation.Nonnull;
import javax.annotation.WillCloseWhenClosed;
import java.io.*;
import java.util.function.Function;

public class StreamPipe {

    public static InputStream pipe(@WillCloseWhenClosed final InputStream input, final Function<OutputStream, OutputStream> outputStreamWrapper, final int bufferSize) throws IOException {
        PipedInputStream pis = new PipedInputStream(bufferSize * 10);
        ExceptionInputStream eis = new ExceptionInputStream(pis);
        PipedOutputStream pos = new PipedOutputStream(pis);
        Thread t = new Thread(() -> {
            try (InputStream in = input; OutputStream out = outputStreamWrapper.apply(pos)) {
                byte[] buffer = new byte[bufferSize];
                int read;
                while ((read = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }
            } catch (IOException e) {
                eis.exception = e;
            }
        }, "StreamPipe-Thread");
        t.setDaemon(true);
        t.start();
        return eis;
    }


    private static class ExceptionInputStream extends DelegatingInputStream {
        private IOException exception;

        public ExceptionInputStream(final InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public int read() throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
            return super.read();
        }

        @Override
        public int read(@Nonnull byte[] b) throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
            return super.read(b);
        }

        @Override
        public int read(@Nonnull byte[] b, int off, int len) throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
            return super.read(b, off, len);
        }
    }

}
