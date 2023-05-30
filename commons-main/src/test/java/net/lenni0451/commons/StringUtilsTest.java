package net.lenni0451.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void uppercaseFirst() {
        assertEquals("Test", StringUtils.uppercaseFirst("TEST"));
    }

    @Test
    void repeat() {
        assertEquals("TestTestTest", StringUtils.repeat("Test", 3));
    }

    @Test
    void cut() {
        assertEquals("Test", StringUtils.cut("Test", 10));
        assertEquals("TestTestTe", StringUtils.cut("TestTestTestTest", 10));
    }

    @Test
    void count() {
        assertEquals(3, StringUtils.count("This is a Test string containing the word Test 3 times to Test this method", "Test"));
    }

    @Test
    void flip() {
        assertEquals("tseT", StringUtils.flip("Test"));
    }

}
