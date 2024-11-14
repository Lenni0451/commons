package net.lenni0451.commons.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeFormatterTest {

    @Test
    void formatMMSS() {
        assertEquals("02:03", TimeFormatter.formatMMSS(123_000));
    }

    @Test
    void formatHHMMSS() {
        assertEquals("01:02:03", TimeFormatter.formatHHMMSS(3_723_000));
    }

}
