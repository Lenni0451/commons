package net.lenni0451.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LazyTest {

    @Test
    void test() {
        int[] i = {0};
        Lazy<int[]> lazy = new Lazy<>(() -> {
            i[0] = 1;
            return i;
        });
        assertEquals(0, i[0]);
        assertEquals(1, lazy.get()[0]);
        assertEquals(1, i[0]);
    }

}
