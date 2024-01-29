package net.lenni0451.commons.color;

import java.awt.Color;

/**
 * Convert colors between different formats.
 */
public class ColorConverter {

    /**
     * The RGB color format ({@code 0xRR_GG_BB}).
     */
    public static final ColorConverter RGB = new ColorConverter(16, 8, 0);
    /**
     * The ARGB color format ({@code 0xAA_RR_GG_BB}).
     */
    public static final ColorConverter ARGB = new ColorConverter(16, 8, 0, 24);
    /**
     * The RGBA color format ({@code 0xRR_GG_BB_AA}).
     */
    public static final ColorConverter RGBA = new ColorConverter(24, 16, 8, 0);
    /**
     * The BGR color format ({@code 0xBB_GG_RR}).
     */
    public static final ColorConverter BGR = new ColorConverter(0, 8, 16);
    /**
     * The ABGR color format ({@code 0xAA_BB_GG_RR}).
     */
    public static final ColorConverter ABGR = new ColorConverter(0, 8, 16, 24);
    /**
     * The BGRA color format ({@code 0xBB_GG_RR_AA}).
     */
    public static final ColorConverter BGRA = new ColorConverter(8, 16, 24, 0);

    /**
     * Convert a color from one format to another.
     *
     * @param color The color to convert
     * @param from  The format the color is in
     * @param to    The format to convert the color to
     * @return The converted color
     */
    public static int convert(final int color, final ColorConverter from, final ColorConverter to) {
        int r = from.getRed(color);
        int g = from.getGreen(color);
        int b = from.getBlue(color);
        int a = from.getAlpha(color);
        return to.to(r, g, b, a);
    }

    /**
     * Convert a color from one format to another.
     *
     * @param color The color to convert
     * @param from  The format the color is in
     * @param to    The format to convert the color to
     * @return The converted color
     */
    public static float[] convert(final float[] color, final ColorConverter from, final ColorConverter to) {
        float r = from.getRed(color);
        float g = from.getGreen(color);
        float b = from.getBlue(color);
        float a = from.getAlpha(color);
        return to.toFloats(r, g, b, a);
    }

    /**
     * Convert a float color representation to an int color representation.
     *
     * @param color The color to convert
     * @param from  The format the color is in
     * @param to    The format to convert the color to
     * @return The converted color
     */
    public static int convertToInt(final float[] color, final ColorConverter from, final ColorConverter to) {
        float r = from.getRed(color);
        float g = from.getGreen(color);
        float b = from.getBlue(color);
        float a = from.getAlpha(color);
        return to.to(r, g, b, a);
    }

    /**
     * Convert an int color representation to a float color representation.
     *
     * @param color The color to convert
     * @param from  The format the color is in
     * @param to    The format to convert the color to
     * @return The converted color
     */
    public static float[] convertToFloats(final int color, final ColorConverter from, final ColorConverter to) {
        int r = from.getRed(color);
        int g = from.getGreen(color);
        int b = from.getBlue(color);
        int a = from.getAlpha(color);
        return to.toFloats(r, g, b, a);
    }


    private final int rShift;
    private final int gShift;
    private final int bShift;
    private final int aShift;

    /**
     * Create a new color converter.<br>
     * The shifts <b>have</b> to be a multiple of 8.
     *
     * @param rShift The shift for the red value
     * @param gShift The shift for the green value
     * @param bShift The shift for the blue value
     */
    public ColorConverter(final int rShift, final int gShift, final int bShift) {
        this(rShift, gShift, bShift, -1);
    }

    /**
     * Create a new color converter.<br>
     * The shifts <b>have</b> to be a multiple of 8.
     *
     * @param rShift The shift for the red value
     * @param gShift The shift for the green value
     * @param bShift The shift for the blue value
     * @param aShift The shift for the alpha value
     */
    public ColorConverter(final int rShift, final int gShift, final int bShift, final int aShift) {
        this.rShift = rShift;
        this.gShift = gShift;
        this.bShift = bShift;
        this.aShift = aShift;
    }

    /**
     * Get the red value of a color.
     *
     * @param color The color
     * @return The red value
     */
    public int getRed(final int color) {
        return (color >> this.rShift) & 0xFF;
    }

    /**
     * Get the red value of a color.
     *
     * @param color The color
     * @return The red value
     */
    public float getRed(final float[] color) {
        return color[color.length - 1 - (this.rShift / 8)];
    }

    /**
     * Set the red value of a color.
     *
     * @param color The color to modify
     * @param r     The red value
     * @return The new color
     */
    public int setRed(int color, final int r) {
        color &= ~(0xFF << this.rShift);
        color |= (r & 0xFF) << this.rShift;
        return color;
    }

    /**
     * Set the red value of a color.
     *
     * @param color The color to modify
     * @param r     The red value
     * @return The new color
     */
    public float[] setRed(float[] color, final float r) {
        color[color.length - 1 - (this.rShift / 8)] = r;
        return color;
    }

    /**
     * Get the green value of a color.
     *
     * @param color The color
     * @return The green value
     */
    public int getGreen(final int color) {
        return (color >> this.gShift) & 0xFF;
    }

    /**
     * Get the green value of a color.
     *
     * @param color The color
     * @return The green value
     */
    public float getGreen(final float[] color) {
        return color[color.length - 1 - (this.gShift / 8)];
    }

    /**
     * Set the green value of a color.
     *
     * @param color The color to modify
     * @param g     The green value
     * @return The new color
     */
    public int setGreen(int color, final int g) {
        color &= ~(0xFF << this.gShift);
        color |= (g & 0xFF) << this.gShift;
        return color;
    }

    /**
     * Set the green value of a color.
     *
     * @param color The color to modify
     * @param g     The green value
     * @return The new color
     */
    public float[] setGreen(float[] color, final float g) {
        color[color.length - 1 - (this.gShift / 8)] = g;
        return color;
    }

    /**
     * Get the blue value of a color.
     *
     * @param color The color
     * @return The blue value
     */
    public int getBlue(final int color) {
        return (color >> this.bShift) & 0xFF;
    }

    /**
     * Get the blue value of a color.
     *
     * @param color The color
     * @return The blue value
     */
    public float getBlue(final float[] color) {
        return color[color.length - 1 - (this.bShift / 8)];
    }

    /**
     * Set the blue value of a color.
     *
     * @param color The color to modify
     * @param b     The blue value
     * @return The new color
     */
    public int setBlue(int color, final int b) {
        color &= ~(0xFF << this.bShift);
        color |= (b & 0xFF) << this.bShift;
        return color;
    }

    /**
     * Set the blue value of a color.
     *
     * @param color The color to modify
     * @param b     The blue value
     * @return The new color
     */
    public float[] setBlue(float[] color, final float b) {
        color[color.length - 1 - (this.bShift / 8)] = b;
        return color;
    }

    /**
     * Get the alpha value of a color.
     *
     * @param color The color
     * @return The alpha value
     */
    public int getAlpha(final int color) {
        if (this.aShift == -1) return 255;
        return (color >> this.aShift) & 0xFF;
    }

    /**
     * Get the alpha value of a color.
     *
     * @param color The color
     * @return The alpha value
     */
    public float getAlpha(final float[] color) {
        if (this.aShift == -1) return 1;
        return color[color.length - 1 - (this.aShift / 8)];
    }

    /**
     * Set the alpha value of a color.
     *
     * @param color The color to modify
     * @param a     The alpha value
     * @return The new color
     */
    public int setAlpha(int color, final int a) {
        if (this.aShift == -1) return color;
        color &= ~(0xFF << this.aShift);
        color |= (a & 0xFF) << this.aShift;
        return color;
    }

    /**
     * Set the alpha value of a color.
     *
     * @param color The color to modify
     * @param a     The alpha value
     * @return The new color
     */
    public float[] setAlpha(float[] color, final float a) {
        if (this.aShift == -1) return color;
        color[color.length - 1 - (this.aShift / 8)] = a;
        return color;
    }

    /**
     * Convert a color to its int representation.
     *
     * @param color The color
     * @return The int representation
     */
    public int to(final Color color) {
        return this.to(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Convert a color to its int representation.<br>
     * If the format has no alpha value, the alpha value will be ignored.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The int representation
     */
    public int to(final int r, final int g, final int b, final int a) {
        return r << this.rShift |
                g << this.gShift |
                b << this.bShift |
                (this.aShift == -1 ? 0 : a << this.aShift);
    }

    /**
     * Convert a color to its int representation.<br>
     * If the format has no alpha value, the alpha value will be ignored.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The int representation
     */
    public int to(final float r, final float g, final float b, final float a) {
        return (int) (r * 255) << this.rShift |
                (int) (g * 255) << this.gShift |
                (int) (b * 255) << this.bShift |
                (this.aShift == -1 ? 0 : (int) (a * 255) << this.aShift);
    }

    /**
     * Convert a color to its float representation.
     *
     * @param color The color
     * @return The float representation
     */
    public float[] toFloats(final Color color) {
        return this.toFloats(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Convert a color to its float representation.<br>
     * If the format has an alpha value, it will be set to 1.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @return The float representation
     */
    public float[] toFloats(final int r, final int g, final int b) {
        return this.toFloats(r, g, b, 255);
    }

    /**
     * Convert a color to its float representation.<br>
     * If the format has no alpha value, the alpha value will be ignored.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The float representation
     */
    public float[] toFloats(final int r, final int g, final int b, final int a) {
        float[] floats = new float[this.aShift == -1 ? 3 : 4];
        int arrayOffset = floats.length - 1;
        floats[arrayOffset - (this.rShift / 8)] = r / 255F;
        floats[arrayOffset - (this.gShift / 8)] = g / 255F;
        floats[arrayOffset - (this.bShift / 8)] = b / 255F;
        if (this.aShift != -1) floats[arrayOffset - (this.aShift / 8)] = a / 255F;
        return floats;
    }

    /**
     * Convert a color to its float representation.<br>
     * If the format has no alpha value, the alpha value will be ignored.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The float representation
     */
    public float[] toFloats(final float r, final float g, final float b, final float a) {
        float[] floats = new float[this.aShift == -1 ? 3 : 4];
        int arrayOffset = floats.length - 1;
        floats[arrayOffset - (this.rShift / 8)] = r;
        floats[arrayOffset - (this.gShift / 8)] = g;
        floats[arrayOffset - (this.bShift / 8)] = b;
        if (this.aShift != -1) floats[arrayOffset - (this.aShift / 8)] = a;
        return floats;
    }

    /**
     * Convert the int representation of a color to a color.
     *
     * @param color The int representation
     * @return The color
     */
    public Color from(final int color) {
        return new Color(color >> this.rShift & 0xFF,
                color >> this.gShift & 0xFF,
                color >> this.bShift & 0xFF,
                this.aShift == -1 ? 255 : color >> this.aShift & 0xFF
        );
    }

    /**
     * Convert the float representation of a color to a color.
     *
     * @param color The float representation
     * @return The color
     */
    public Color from(final float[] color) {
        if (color.length == 3 && this.aShift != -1) throw new IllegalArgumentException("Color format has no alpha value");
        if (color.length == 4 && this.aShift == -1) throw new IllegalArgumentException("Color format has an alpha value");
        if (color.length != 3 && color.length != 4) throw new IllegalArgumentException("Color format has an invalid length");

        int arrayOffset = color.length - 1;
        return new Color(color[arrayOffset - (this.rShift / 8)],
                color[arrayOffset - (this.gShift / 8)],
                color[arrayOffset - (this.bShift / 8)],
                this.aShift == -1 ? 1 : color[arrayOffset - (this.aShift / 8)]);
    }

    /**
     * Convert the float representation of a color to a color.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @return The color
     */
    public Color from(final float r, final float g, final float b) {
        return this.from(r, g, b, 1);
    }

    /**
     * Convert the float representation of a color to a color.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The color
     */
    public Color from(final float r, final float g, final float b, final float a) {
        return new Color(r, g, b, a);
    }

}
