package net.lenni0451.commons.io;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * An {@link URLStreamHandler} which opens an {@link URLConnection} to a byte array.
 */
public class ByteArrayURLStreamHandler extends URLStreamHandler {

    /**
     * Make a url which has an {@link URLConnection} to the given byte array.
     *
     * @param file The file name of the url
     * @param data The byte array to open the connection to
     * @return The url
     */
    @SneakyThrows
    public static URL makeURL(final String file, final byte[] data) {
        return new URL("x-buffer", null, -1, file, new ByteArrayURLStreamHandler(data));
    }


    private final byte[] data;

    public ByteArrayURLStreamHandler(final byte[] data) {
        this.data = data;
    }

    @Override
    protected URLConnection openConnection(URL u) {
        return new URLConnection(u) {
            @Override
            public void connect() {
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(ByteArrayURLStreamHandler.this.data);
            }
        };
    }

}
