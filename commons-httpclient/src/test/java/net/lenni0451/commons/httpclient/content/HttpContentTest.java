package net.lenni0451.commons.httpclient.content;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpContentTest {

    @Test
    void getAsStream() {
        HttpContent content = HttpContent.bytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        InputStream stream = assertDoesNotThrow(content::getAsStream);
        for (int i = 0; i < 10; i++) {
            int read = assertDoesNotThrow(() -> stream.read());
            assertEquals(i, read);
        }
    }

    @Test
    void getAsBytes() {
        HttpContent content = HttpContent.bytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        byte[] bytes = assertDoesNotThrow(content::getAsBytes);
        for (int i = 0; i < 10; i++) {
            assertEquals(i, bytes[i]);
        }
    }

    @Test
    void getAsString() {
        HttpContent content = HttpContent.string("Hello, World!");
        String str = assertDoesNotThrow(() -> content.getAsString());
        assertEquals("Hello, World!", str);
    }

    @Test
    void testGetAsString() {
        HttpContent content = HttpContent.string("Hello, World!");
        String str = assertDoesNotThrow(() -> content.getAsString(StandardCharsets.UTF_8));
        assertEquals("Hello, World!", str);
    }

}
