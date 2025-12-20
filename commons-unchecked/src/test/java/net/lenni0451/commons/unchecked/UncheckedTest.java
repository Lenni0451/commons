package net.lenni0451.commons.unchecked;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UncheckedTest {

    @Test
    void cast() {
        assertDoesNotThrow(() -> Unchecked.<String>cast(null));
        assertDoesNotThrow(() -> Unchecked.<String>cast("test"));
        assertThrows(ClassCastException.class, () -> Unchecked.<Integer>cast("test"));
    }

}
