package net.lenni0451.commons.strings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void uppercaseFirst() {
        assertEquals("Test", StringUtils.uppercaseFirst("TEST"));
    }

    @Test
    void repeatChar() {
        assertEquals("TTT", StringUtils.repeat('T', 3));
    }

    @Test
    void repeatString() {
        assertEquals("TestTestTest", StringUtils.repeat("Test", 3));
    }

    @Test
    void cut() {
        assertEquals("Test", StringUtils.cut("Test", 10));
        assertEquals("TestTestTe", StringUtils.cut("TestTestTestTest", 10));
    }

    @Test
    void sizeCut() {
        assertEquals("Test______", StringUtils.cut("Test", '_', 10));
        assertEquals("TestTestTe", StringUtils.cut("TestTestTestTest", '_', 10));
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
