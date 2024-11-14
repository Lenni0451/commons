package net.lenni0451.commons.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IOUtilsTest {

    @Test
    void readResource() throws IOException {
        String className = IOUtils.class.getName().replace('.', '/') + ".class";
        Optional<byte[]> classBytes = IOUtils.readResource("/" + className);
        Optional<byte[]> classLoaderBytes = IOUtils.readResource(IOUtils.class.getClassLoader(), className);
        Optional<byte[]> unknownBytes = IOUtils.readResource("unknown");

        assertTrue(classBytes.isPresent());
        assertTrue(classLoaderBytes.isPresent());
        assertFalse(unknownBytes.isPresent());
    }

    @Test
    void readAll() throws IOException {
        byte[] bytes = IOUtils.readAll(new ByteArrayInputStream(new byte[]{0, 1, 2, 3, 4, 5}));
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5}, bytes);
    }

}
