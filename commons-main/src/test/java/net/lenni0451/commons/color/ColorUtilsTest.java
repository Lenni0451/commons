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
        assertEquals(255, ColorUtils.setGreen(EMPTY, 1000).getGreen());
        assertEquals(0, ColorUtils.setGreen(EMPTY, -1000).getGreen());
    }

    @Test
    void setBlue() {
        assertEquals(255, ColorUtils.setBlue(EMPTY, 1000).getBlue());
        assertEquals(0, ColorUtils.setBlue(EMPTY, -1000).getBlue());
    }

    @Test
    void setAlpha() {
        assertEquals(255, ColorUtils.setAlpha(EMPTY, 1000).getAlpha());
        assertEquals(0, ColorUtils.setAlpha(EMPTY, -1000).getAlpha());
    }

    @Test
    void multiply() {
        Color color = new Color(255, 255, 255, 255);

        Color multiply0 = ColorUtils.multiply(color, 0);
        assertEquals(0, multiply0.getRed());
        assertEquals(0, multiply0.getGreen());
        assertEquals(0, multiply0.getBlue());
        assertEquals(255, multiply0.getAlpha());

        Color multiply1 = ColorUtils.multiply(color, 1);
        assertEquals(255, multiply1.getRed());
        assertEquals(255, multiply1.getGreen());
        assertEquals(255, multiply1.getBlue());
        assertEquals(255, multiply1.getAlpha());
    }

    @Test
    void interpolate() {
        Color color1 = new Color(255, 255, 255, 255);
        Color color2 = new Color(0, 0, 0, 0);

        Color interpolate0 = ColorUtils.interpolate(color1, color2, 0);
        assertEquals(255, interpolate0.getRed());
        assertEquals(255, interpolate0.getGreen());
        assertEquals(255, interpolate0.getBlue());
        assertEquals(255, interpolate0.getAlpha());

        Color interpolate1 = ColorUtils.interpolate(color1, color2, 1);
        assertEquals(0, interpolate1.getRed());
        assertEquals(0, interpolate1.getGreen());
        assertEquals(0, interpolate1.getBlue());
        assertEquals(0, interpolate1.getAlpha());
    }

    @Test
    void interpolateMultiple() {
        Color[] colors = new Color[]{new Color(255, 0, 0, 0), new Color(0, 255, 0, 0), new Color(0, 0, 255, 0)};

        Color interpolate0 = ColorUtils.interpolate(0, colors);
        assertEquals(255, interpolate0.getRed());
        assertEquals(0, interpolate0.getGreen());
        assertEquals(0, interpolate0.getBlue());
        assertEquals(0, interpolate0.getAlpha());

        Color interpolate1 = ColorUtils.interpolate(0.5F, colors);
        assertEquals(0, interpolate1.getRed());
        assertEquals(255, interpolate1.getGreen());
        assertEquals(0, interpolate1.getBlue());
        assertEquals(0, interpolate1.getAlpha());

        Color interpolate2 = ColorUtils.interpolate(1, colors);
        assertEquals(0, interpolate2.getRed());
        assertEquals(0, interpolate2.getGreen());
        assertEquals(255, interpolate2.getBlue());
        assertEquals(0, interpolate2.getAlpha());
    }

    @Test
    void interpolateMultipleSteps() {
        Color[] colors = new Color[]{new Color(255, 0, 0, 0), new Color(0, 255, 0, 0), new Color(0, 0, 255, 0)};
        float[] steps = new float[]{0, 0.2F, 0.8F};

        Color interpolate0 = ColorUtils.interpolate(0, colors, steps);
        assertEquals(255, interpolate0.getRed());
        assertEquals(0, interpolate0.getGreen());
        assertEquals(0, interpolate0.getBlue());
        assertEquals(0, interpolate0.getAlpha());

        Color interpolate1 = ColorUtils.interpolate(0.2F, colors, steps);
        assertEquals(0, interpolate1.getRed());
        assertEquals(255, interpolate1.getGreen());
        assertEquals(0, interpolate1.getBlue());
        assertEquals(0, interpolate1.getAlpha());

        Color interpolate2 = ColorUtils.interpolate(0.8F, colors, steps);
        assertEquals(0, interpolate2.getRed());
        assertEquals(0, interpolate2.getGreen());
        assertEquals(255, interpolate2.getBlue());
        assertEquals(0, interpolate2.getAlpha());

        Color interpolate3 = ColorUtils.interpolate(1, colors, steps);
        assertEquals(0, interpolate3.getRed());
        assertEquals(0, interpolate3.getGreen());
        assertEquals(255, interpolate3.getBlue());
        assertEquals(0, interpolate3.getAlpha());
    }

    @Test
    void distance() {
        Color color1 = new Color(255, 255, 255, 255);
        Color color2 = new Color(0, 0, 0, 0);

        assertEquals(255 * 3, ColorUtils.distance(color1, color2));
    }

}
