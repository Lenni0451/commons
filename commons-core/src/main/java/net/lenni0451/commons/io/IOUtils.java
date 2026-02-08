package net.lenni0451.commons.io;

import lombok.experimental.UtilityClass;

import javax.annotation.WillNotClose;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

@UtilityClass
public class IOUtils {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

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
     * @param is The input stream to read from
     * @return The bytes read from the input stream
     * @throws IOException If an I/O error occurs
     */
    public static byte[] readAll(@WillNotClose final InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        transfer(is, out);
        return out.toByteArray();
    }

    /**
     * Transfer all bytes from the input stream to the output stream.<br>
     * Both streams will not be closed.
     *
     * @param is The input stream to read from
     * @param os The output stream to write to
     * @throws IOException If an I/O error occurs
     */
    public static void transfer(@WillNotClose final InputStream is, @WillNotClose final OutputStream os) throws IOException {
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        int len;
        while ((len = is.read(buf)) >= 0) {
            os.write(buf, 0, len);
        }
    }

}
