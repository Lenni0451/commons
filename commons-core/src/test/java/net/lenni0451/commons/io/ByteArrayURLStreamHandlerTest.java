package net.lenni0451.commons.io;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ByteArrayURLStreamHandlerTest {

    @Test
    void openByteArrayConnection() {
        byte[] data = "Test".getBytes();
        URLConnection con = assertDoesNotThrow(() -> ByteArrayURLStreamHandler.makeURL("test", data).openConnection());
        InputStream is = assertDoesNotThrow(con::getInputStream);
        byte[] read = assertDoesNotThrow(() -> IOUtils.readAll(is));
        assertArrayEquals(data, read);
    }

}
