package net.lenni0451.commons.io;

import javax.annotation.WillNotClose;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class IOUtils {

    /**
     * Read a resource from the classpath as bytes.<br>
     * The resource will be searched in the classpath of this class.
     *
     * @param path The path to the resource
     * @return The bytes of the resource or empty if the resource does not exist
     * @throws IOException If an I/O error occurs
     */
    public static Optional<byte[]> readResource(final String path) throws IOException {
        return readResource(IOUtils.class, path);
    }

    /**
     * Read a resource from the classpath as bytes.<br>
     * The resource will be searched in the classpath of the given class.
     *
     * @param clazz The class to search the resource in
     * @param path  The path to the resource
     * @return The bytes of the resource or empty if the resource does not exist
     * @throws IOException If an I/O error occurs
     */
    public static Optional<byte[]> readResource(final Class<?> clazz, final String path) throws IOException {
        try (InputStream is = clazz.getResourceAsStream(path)) {
            if (is == null) return Optional.empty();
            return Optional.of(readAll(is));
        }
    }

    /**
     * Read a resource from the classpath as bytes.<br>
     * The resource will be searched in the classpath of the given class loader.
     *
     * @param loader The class loader to search the resource in
     * @param path   The path to the resource
     * @return The bytes of the resource or empty if the resource does not exist
     * @throws IOException If an I/O error occurs
     */
    public static Optional<byte[]> readResource(final ClassLoader loader, final String path) throws IOException {
        try (InputStream is = loader.getResourceAsStream(path)) {
            if (is == null) return Optional.empty();
            return Optional.of(readAll(is));
        }
    }

    /**
     * Read all bytes from a stream.<br>
     * The stream will not be closed.
     *
     * @param is The InputStream to read from
     * @return The bytes read from the InputStream
     * @throws IOException If an I/O error occurs
     */
    @WillNotClose
    public static byte[] readAll(final InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) != -1) out.write(buf, 0, len);
        return out.toByteArray();
    }

}
