package net.lenni0451.commons.color;

import net.lenni0451.commons.math.MathUtils;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorConverterTest {

    @Test
    void convert() {
        assertEquals(0xCCBBAA, ColorConverter.convert(0xAABBCC, ColorConverter.BGR, ColorConverter.RGB));
        assertEquals(0xCCBBAA, ColorConverter.convertToInt(new float[]{0xAA / 255F, 0xBB / 255F, 0xCC / 255F}, ColorConverter.BGR, ColorConverter.RGB));
        assertArrayEquals(new float[]{0, 0.25F, 0.5F, 0.75F}, ColorConverter.convert(new float[]{0.75F, 0.5F, 0.25F, 0}, ColorConverter.BGRA, ColorConverter.ARGB));
        assertArrayEquals(new float[]{0, 0.25F, 0.5F, 0.75F}, MathUtils.round(ColorConverter.convertToFloats(ColorConverter.BGRA.to(0.25F, 0.5F, 0.75F, 0), ColorConverter.BGRA, ColorConverter.ARGB), 2));
    }

    @Test
    void getRed() {
        assertEquals(0xAA, ColorConverter.RGB.getRed(0xAABBCC));
        assertEquals(1, ColorConverter.RGB.getRed(new float[]{1, 0, 0}));
    }

    @Test
    void setRed() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setRed(0x00BBCC, 0xAA));
        assertArrayEquals(new float[]{1, 0, 0}, ColorConverter.RGB.setRed(new float[3], 1));
    }

    @Test
    void getGreen() {
        assertEquals(0xBB, ColorConverter.RGB.getGreen(0xAABBCC));
        assertEquals(1, ColorConverter.RGB.getGreen(new float[]{0, 1, 0}));
    }

    @Test
    void setGreen() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setGreen(0xAA00CC, 0xBB));
        assertArrayEquals(new float[]{0, 1, 0}, ColorConverter.RGB.setGreen(new float[3], 1));
    }

    @Test
    void getBlue() {
        assertEquals(0xCC, ColorConverter.RGB.getBlue(0xAABBCC));
        assertEquals(1, ColorConverter.RGB.getBlue(new float[]{0, 0, 1}));
    }

    @Test
    void setBlue() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setBlue(0xAABB00, 0xCC));
        assertArrayEquals(new float[]{0, 0, 1}, ColorConverter.RGB.setBlue(new float[3], 1));
    }

    @Test
    void getAlpha() {
        assertEquals(0xFF, ColorConverter.RGB.getAlpha(0xAABBCC));
        assertEquals(0xDD, ColorConverter.ARGB.getAlpha(0xDD000000));

        assertEquals(1, ColorConverter.RGB.getAlpha(new float[]{0, 0, 0}));
        assertEquals(0.5F, ColorConverter.ARGB.getAlpha(new float[]{0.5F, 0, 0, 0}));
    }

    @Test
    void setAlpha() {
        assertEquals(0xAABBCC, ColorConverter.RGB.setAlpha(0xAABBCC, 0xFF));
        assertEquals(0xDDAABBCC, ColorConverter.ARGB.setAlpha(0xAABBCC, 0xDD));

        assertArrayEquals(new float[]{0, 0, 0}, ColorConverter.RGB.setAlpha(new float[3], 1));
        assertArrayEquals(new float[]{0.5F, 0, 0, 0}, ColorConverter.ARGB.setAlpha(new float[4], 0.5F));
    }

    @Test
    void to() {
        assertEquals(0xDDAABBCC, ColorConverter.ARGB.to(0xAA, 0xBB, 0xCC, 0xDD));
        assertEquals(0xDDAABBCC, ColorConverter.ARGB.to(0xAA / 255F, 0xBB / 255F, 0xCC / 255F, 0xDD / 255F));
    }

    @Test
    void toFloats() {
        assertArrayEquals(new float[]{1, 1, 1, 1}, ColorConverter.ARGB.toFloats(0xFF, 0xFF, 0xFF, 0xFF));
        assertArrayEquals(new float[]{0.75F, 0, 0.25F, 0.5F}, ColorConverter.ARGB.toFloats(0, 0.25F, 0.5F, 0.75F));
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
    void fromFloats1() {
        Color color = ColorConverter.ARGB.from(new float[]{1, 1, 1, 1});
        assertEquals(0xFF, color.getRed());
        assertEquals(0xFF, color.getGreen());
        assertEquals(0xFF, color.getBlue());
        assertEquals(0xFF, color.getAlpha());
    }

    @Test
    void fromFloats2() {
        Color color = ColorConverter.ARGB.from(1, 1, 1);
        assertEquals(0xFF, color.getRed());
        assertEquals(0xFF, color.getGreen());
        assertEquals(0xFF, color.getBlue());
        assertEquals(0xFF, color.getAlpha());
    }

    @Test
    void fromFloats3() {
        Color color = ColorConverter.ARGB.from(1, 1, 1, 1);
        assertEquals(0xFF, color.getRed());
        assertEquals(0xFF, color.getGreen());
        assertEquals(0xFF, color.getBlue());
        assertEquals(0xFF, color.getAlpha());
    }

}
