package net.lenni0451.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SneakyTest {

    @Test
    void sneakThrowable() {
        assertThrows(Throwable.class, () -> Sneaky.sneak(new Throwable()));
    }

    @Test
    void sneakRunnable() {
        assertThrows(Throwable.class, () -> Sneaky.sneak((Sneaky.SneakyRunnable) () -> {
            throw new Throwable();
        }));
    }

    @Test
    void sneakSupplier() {
        assertThrows(Throwable.class, () -> Sneaky.sneak((Sneaky.SneakyRunnable) () -> {
            throw new Throwable();
        }));
    }

}
