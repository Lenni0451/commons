package net.lenni0451.commons.io.stream;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class DelegatingInputStream extends InputStream {

    @Delegate
    private final InputStream inputStream;

    @Override
    public int read() throws IOException {
        return this.inputStream.read();
    }

}
