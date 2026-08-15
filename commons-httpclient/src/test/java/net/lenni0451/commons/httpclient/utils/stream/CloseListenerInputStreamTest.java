package net.lenni0451.commons.httpclient.utils.stream;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloseListenerInputStreamTest {

    @Test
    void listenerRunsWhenWrappedCloseThrows() {
        boolean[] listenerRan = {false};
        InputStream throwing = new InputStream() {
            @Override
            public int read() {
                return -1;
            }

            @Override
            public void close() throws IOException {
                throw new IOException("close failed");
            }
        };
        CloseListenerInputStream stream = new CloseListenerInputStream(throwing, () -> listenerRan[0] = true);

        //Closing must still surface the wrapped stream's error, but the listener must run so backend resources are released
        assertThrows(IOException.class, stream::close);
        assertTrue(listenerRan[0]);
    }

}
