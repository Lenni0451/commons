package net.lenni0451.commons;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUtilsTest {

    @RepeatedTest(10)
    void randomInt() {
        int i = RandomUtils.randomInt(10, 20);
        assertTrue(i >= 10);
        assertTrue(i <= 20);
    }

    @RepeatedTest(10)
    void randomLong() {
        long l = RandomUtils.randomLong(10, 20);
        assertTrue(l >= 10);
        assertTrue(l <= 20);
    }

    @RepeatedTest(10)
    void randomDouble() {
        double d = RandomUtils.randomDouble(10, 20);
        assertTrue(d >= 10);
        assertTrue(d <= 20);
    }

    @Test
    void randomBytes() {
        byte[] bytes = RandomUtils.randomBytes(10);
        assertEquals(10, bytes.length);
    }

}
