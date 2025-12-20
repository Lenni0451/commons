package net.lenni0451.commons.unchecked;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SneakyTest {

    @Test
    void fake() {
        assertDoesNotThrow(() -> Sneaky.fake(Exception.class));
    }

    @Test
    void sneakyThrow() {
        assertThrows(Exception.class, () -> new Runnable() {
            @Override
            public void run() {
                Sneaky.sneakyThrow(new Exception());
            }
        }.run());
    }

}
