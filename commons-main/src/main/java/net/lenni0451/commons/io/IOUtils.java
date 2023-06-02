package net.lenni0451.commons.io;

import javax.annotation.WillNotClose;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

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
