package net.lenni0451.commons.color;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorConverterTest {

    @Test
    void convert() {
        assertEquals(0xCCBBAA, ColorConverter.convert(0xAABBCC, ColorConverter.BGR, ColorConverter.RGB));
    }

    @Test
    void getRed() {
        assertEquals(0xAA, ColorConverter.RGB.getRed(0xAABBCC));
    }

    @Test
    void setRed() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setRed(0x00BBCC, 0xAA));
    }

    @Test
    void getGreen() {
        assertEquals(0xBB, ColorConverter.RGB.getGreen(0xAABBCC));
    }

    @Test
    void setGreen() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setGreen(0xAA00CC, 0xBB));
    }

    @Test
    void getBlue() {
        assertEquals(0xCC, ColorConverter.RGB.getBlue(0xAABBCC));
    }

    @Test
    void setBlue() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setBlue(0xAABB00, 0xCC));
    }

    @Test
    void getAlpha() {
        assertEquals(0xFF, ColorConverter.RGB.getAlpha(0xAABBCC));
        assertEquals(0xDD, ColorConverter.ARGB.getAlpha(0xDD000000));
    }

    @Test
    void setAlpha() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setAlpha(0xAABBCC, 0xFF));
        assertEquals(0xDDAABBCC, ColorConverter.ARGB.setAlpha(0xAABBCC, 0xDD));
    }

    @Test
    void to() {
        assertEquals(0xDDAABBCC, ColorConverter.ARGB.to(0xAA, 0xBB, 0xCC, 0xDD));
    }

    @Test
    void toFloats() {
        assertArrayEquals(new float[]{1, 1, 1, 1}, ColorConverter.ARGB.toFloats(0xFF, 0xFF, 0xFF, 0xFF));
    }

    @Test
    void fromInt() {
        Color color = ColorConverter.ARGB.from(0xDDAABBCC);
        assertEquals(0xAA, color.getRed());
        assertEquals(0xBB, color.getGreen());
        assertEquals(0xCC, color.getBlue());
        assertEquals(0xDD, color.getAlpha());
    }

    @Test
    void fromFloats() {
        Color color = ColorConverter.ARGB.from(new float[]{1, 1, 1, 1});
        assertEquals(0xFF, color.getRed());
        assertEquals(0xFF, color.getGreen());
        assertEquals(0xFF, color.getBlue());
        assertEquals(0xFF, color.getAlpha());
    }

}
