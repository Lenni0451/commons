package net.lenni0451.commons.color;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ColorUtilsTest {

    private static final Color EMPTY = new Color(0, 0, 0, 0);

    @Test
    void getRainbowColor() throws InterruptedException {
        Color c1 = ColorUtils.getRainbowColor(0, 1);
        Thread.sleep(100);
        Color c2 = ColorUtils.getRainbowColor(0, 1);
        assertNotEquals(c1, c2);
    }

    @Test
    void setRed() {
        assertEquals(255, ColorUtils.setRed(EMPTY, 1000).getRed());
        assertEquals(0, ColorUtils.setRed(EMPTY, -1000).getRed());
    }

    @Test
    void setGreen() {
    }

    @Test
    void setBlue() {
    }

    @Test
    void setAlpha() {
    }

    @Test
    void multiply() {
    }

    @Test
    void interpolate() {
    }

}
