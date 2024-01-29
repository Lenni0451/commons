package net.lenni0451.commons.color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorTest {

    private static final int I_RGB = 0xAABBCC;
    private static final int I_BGR = 0xCCBBAA;
    private static final int I_RGBA = 0xAABBCCDD;
    private static final int I_ARGB = 0xDDAABBCC;
    private static final int I_BGRA = 0xCCBBAAFF;
    private static final int I_ABGR = 0xFFCCBBAA;

    private static final float[] F_RGB = new float[]{0, 1, 1};
    private static final float[] F_BGR = new float[]{1, 1, 0};
    private static final float[] F_RGBA = new float[]{0, 1, 0, 1};
    private static final float[] F_ARGB = new float[]{1, 0, 1, 0};
    private static final float[] F_BGRA = new float[]{0, 0, 1, 1};
    private static final float[] F_ABGR = new float[]{1, 1, 0, 0};

    @Test
    void from() {
        this.checkColor(Color.fromRGB(I_RGB), 0xAA, 0xBB, 0xCC, 255);
        this.checkColor(Color.fromBGR(I_BGR), 0xAA, 0xBB, 0xCC, 255);
        this.checkColor(Color.fromRGBA(I_RGBA), 0xAA, 0xBB, 0xCC, 0xDD);
        this.checkColor(Color.fromARGB(I_ARGB), 0xAA, 0xBB, 0xCC, 0xDD);
        this.checkColor(Color.fromBGRA(I_BGRA), 0xAA, 0xBB, 0xCC, 0xFF);
        this.checkColor(Color.fromABGR(I_ABGR), 0xAA, 0xBB, 0xCC, 0xFF);

        this.checkColor(Color.fromRGB(F_RGB), 0, 255, 255, 255);
        this.checkColor(Color.fromBGR(F_BGR), 0, 255, 255, 255);
        this.checkColor(Color.fromRGBA(F_RGBA), 0, 255, 0, 255);
        this.checkColor(Color.fromARGB(F_ARGB), 0, 255, 0, 255);
        this.checkColor(Color.fromBGRA(F_BGRA), 255, 0, 0, 255);
        this.checkColor(Color.fromABGR(F_ABGR), 0, 0, 255, 255);
    }

    @Test
    void with() {
        this.checkColor(Color.BLACK.withRed(1).withGreen(2).withBlue(3).withAlpha(4), 1, 2, 3, 4);
        this.checkColor(Color.BLACK.withRedF(1).withGreenF(0).withBlueF(1).withAlphaF(0), 255, 0, 255, 0);
    }

    @Test
    void to() {
        assertEquals(I_RGB, Color.fromRGB(I_RGB).toRGB());
        assertEquals(I_BGR, Color.fromBGR(I_BGR).toBGR());
        assertEquals(I_RGBA, Color.fromRGBA(I_RGBA).toRGBA());
        assertEquals(I_ARGB, Color.fromARGB(I_ARGB).toARGB());
        assertEquals(I_BGRA, Color.fromBGRA(I_BGRA).toBGRA());
        assertEquals(I_ABGR, Color.fromABGR(I_ABGR).toABGR());

        assertArrayEquals(F_RGB, Color.fromRGB(F_RGB).toRGBF());
        assertArrayEquals(F_BGR, Color.fromBGR(F_BGR).toBGRF());
        assertArrayEquals(F_RGBA, Color.fromRGBA(F_RGBA).toRGBAF());
        assertArrayEquals(F_ARGB, Color.fromARGB(F_ARGB).toARGBF());
        assertArrayEquals(F_BGRA, Color.fromBGRA(F_BGRA).toBGRAF());
        assertArrayEquals(F_ABGR, Color.fromABGR(F_ABGR).toABGRF());
    }

    @Test
    void brighter() {
        java.awt.Color awtRed = new java.awt.Color(255, 0, 0);
        java.awt.Color awtGreen = new java.awt.Color(0, 255, 0);
        java.awt.Color awtBlue = new java.awt.Color(0, 0, 255);

        assertEquals(awtRed.brighter().getRGB(), Color.RED.brighter().toARGB());
        assertEquals(awtGreen.brighter().getRGB(), Color.GREEN.brighter().toARGB());
        assertEquals(awtBlue.brighter().getRGB(), Color.BLUE.brighter().toARGB());
    }

    @Test
    void darker() {
        java.awt.Color awtRed = new java.awt.Color(255, 0, 0);
        java.awt.Color awtGreen = new java.awt.Color(0, 255, 0);
        java.awt.Color awtBlue = new java.awt.Color(0, 0, 255);

        assertEquals(awtRed.darker().getRGB(), Color.RED.darker().toARGB());
        assertEquals(awtGreen.darker().getRGB(), Color.GREEN.darker().toARGB());
        assertEquals(awtBlue.darker().getRGB(), Color.BLUE.darker().toARGB());
    }

    @Test
    void invert() {
        this.checkColor(Color.fromRGB(0, 255, 255).invert(), 255, 0, 0, 255);
    }

    @Test
    void multiply() {
        this.checkColor(Color.fromRGB(255, 255, 255).multiply(0.5F), 127, 127, 127, 255);
        this.checkColor(Color.fromRGB(255, 255, 255).multiplyAlpha(0.5F), 255, 255, 255, 127);
        this.checkColor(Color.fromRGB(255, 255, 255).multiplyAll(0.5F), 127, 127, 127, 127);
        this.checkColor(Color.fromRGB(255, 255, 255).multiply(Color.fromRGBA(0.5F, 0.5F, 0.5F, 0.5F)), 127, 127, 127, 127);
    }

    @Test
    void distance() {
        assertEquals(255, Color.fromRGB(255, 255, 0).distance(Color.fromRGB(255, 0, 0)));
        assertEquals(255 * 2, Color.fromRGB(255, 255, 255).distance(Color.fromRGB(255, 0, 0)));
        assertEquals(255 * 3, Color.fromRGB(255, 255, 255).distance(Color.fromRGB(0, 0, 0)));
    }

    private void checkColor(final Color color, final int r, final int g, final int b, final int a) {
        assertEquals(r, color.getRed());
        assertEquals(g, color.getGreen());
        assertEquals(b, color.getBlue());
        assertEquals(a, color.getAlpha());
    }

}
