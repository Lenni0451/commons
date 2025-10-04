package net.lenni0451.commons.io.stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequentialInputStreamTest {

    @Test
    void test() throws Throwable {
        try (SequentialInputStream sis = new SequentialInputStream()) {
            Throwable[] readerException = new Throwable[1];
            boolean[] running = new boolean[1];
            Thread reader = new Thread(() -> {
                try {
                    running[0] = true;
                    byte[] buffer = new byte[1024];
                    int count = sis.read(buffer);
                    assertEquals(11, count);
                    assertEquals("Hello World", new String(buffer, 0, count));
                } catch (Throwable t) {
                    readerException[0] = t;
                }
                running[0] = false;
            }, "SequentialInputStreamTest-Reader");
            reader.setDaemon(true);
            reader.start();
            while (!running[0]) Thread.yield(); //Wait for the reader to start
            Thread.sleep(500); //Wait a bit to ensure the reader is really waiting
            sis.append("Hello World".getBytes());
            while (running[0]) Thread.yield(); //Wait for the reader to finish
            if (readerException[0] != null) throw readerException[0]; //Rethrow any exception from the reader
        }
    }


}
