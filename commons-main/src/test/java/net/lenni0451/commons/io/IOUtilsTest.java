package net.lenni0451.commons.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class IOUtilsTest {

    @Test
    void readAll() throws IOException {
        byte[] bytes = IOUtils.readAll(new ByteArrayInputStream(new byte[]{0, 1, 2, 3, 4, 5}));
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5}, bytes);
    }

}
