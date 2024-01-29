package net.lenni0451.commons.color;

import net.lenni0451.commons.math.MathUtils;

public class Color {

    public static final Color BLACK = Color.fromRGB(0, 0, 0);
    public static final Color LIGHT_GRAY = Color.fromRGB(192, 192, 192);
    public static final Color GRAY = Color.fromRGB(128, 128, 128);
    public static final Color DARK_GRAY = Color.fromRGB(64, 64, 64);
    public static final Color WHITE = Color.fromRGB(255, 255, 255);

    public static final Color RED = Color.fromRGB(255, 0, 0);
    public static final Color GREEN = Color.fromRGB(0, 255, 0);
    public static final Color BLUE = Color.fromRGB(0, 0, 255);
    public static final Color ORANGE = Color.fromRGB(255, 200, 0);
    public static final Color YELLOW = Color.fromRGB(255, 255, 0);
    public static final Color CYAN = Color.fromRGB(0, 255, 255);
    public static final Color PINK = Color.fromRGB(255, 175, 175);
    public static final Color MAGENTA = Color.fromRGB(255, 0, 255);

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xRRGGBB
     *
     * @param rgb The int
     * @return The created color
     */
    public static Color fromRGB(final int rgb) {
        return fromRGB(rgb >> 16, rgb >> 8, rgb);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xBBGGRR
     *
     * @param bgr The int
     * @return The created color
     */
    public static Color fromBGR(final int bgr) {
        return fromRGB(bgr, bgr >> 8, bgr >> 16);
    }

    /**
     * Create a color from red, green and blue values.<br>
     * The values must be between 0 and 255.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @return The created color
     */
    public static Color fromRGB(final int r, final int g, final int b) {
        return fromRGBA(r, g, b, 255);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xRRGGBBAA
     *
     * @param rgba The int
     * @return The created color
     */
    public static Color fromRGBA(final int rgba) {
        return fromRGBA(rgba >> 24, rgba >> 16, rgba >> 8, rgba);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xAARRGGBB
     *
     * @param argb The int
     * @return The created color
     */
    public static Color fromARGB(final int argb) {
        return fromRGBA(argb >> 16, argb >> 8, argb, argb >> 24);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xBBGGRRAA
     *
     * @param bgra The int
     * @return The created color
     */
    public static Color fromBGRA(final int bgra) {
        return fromRGBA(bgra >> 8, bgra >> 16, bgra >> 24, bgra);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xAABBGGRR
     *
     * @param abgr The int
     * @return The created color
     */
    public static Color fromABGR(final int abgr) {
        return fromRGBA(abgr, abgr >> 8, abgr >> 16, abgr >> 24);
    }

    /**
     * Create a color from red, green, blue and alpha values.<br>
     * The values must be between 0 and 255.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The created color
     */
    public static Color fromRGBA(final int r, final int g, final int b, final int a) {
        return new Color(r, g, b, a);
    }

    /**
     * Create a color from a float array.<br>
     * The array must have a length of 3.<br>
     * The array must contain the values in the order: r, g, b<br>
     * The values must be between 0 and 1.
     *
     * @param rgb The float array
     * @return The created color
     */
    public static Color fromRGB(final float[] rgb) {
        return fromRGB(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Create a color from a float array.<br>
     * The array must have a length of 3.<br>
     * The array must contain the values in the order: b, g, r<br>
     * The values must be between 0 and 1.
     *
     * @param bgr The float array
     * @return The created color
     */
    public static Color fromBGR(final float[] bgr) {
        return fromRGB(bgr[2], bgr[1], bgr[0]);
    }

    /**
     * Create a color from red, green and blue values.<br>
     * The values must be between 0 and 1.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @return The created color
     */
    public static Color fromRGB(final float r, final float g, final float b) {
        return fromRGBA(r, g, b, 1);
    }

    /**
     * Create a color from a float array.<br>
     * The array must have a length of 4.<br>
     * The array must contain the values in the order: r, g, b, a<br>
     * The values must be between 0 and 1.
     *
     * @param rgba The float array
     * @return The created color
     */
    public static Color fromRGBA(final float[] rgba) {
        return fromRGBA(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    /**
     * Create a color from a float array.<br>
     * The array must have a length of 4.<br>
     * The array must contain the values in the order: a, r, g, b<br>
     * The values must be between 0 and 1.
     *
     * @param argb The float array
     * @return The created color
     */
    public static Color fromARGB(final float[] argb) {
        return fromRGBA(argb[1], argb[2], argb[3], argb[0]);
    }

    /**
     * Create a color from a float array.<br>
     * The array must have a length of 4.<br>
     * The array must contain the values in the order: b, g, r, a<br>
     * The values must be between 0 and 1.
     *
     * @param bgra The float array
     * @return The created color
     */
    public static Color fromBGRA(final float[] bgra) {
        return fromRGBA(bgra[2], bgra[1], bgra[0], bgra[3]);
    }

    /**
     * Create a color from a float array.<br>
     * The array must have a length of 4.<br>
     * The array must contain the values in the order: a, b, g, r<br>
     * The values must be between 0 and 1.
     *
     * @param abgr The float array
     * @return The created color
     */
    public static Color fromABGR(final float[] abgr) {
        return fromRGBA(abgr[3], abgr[2], abgr[1], abgr[0]);
    }

    /**
     * Create a color from red, green, blue and alpha values.<br>
     * The values must be between 0 and 1.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The created color
     */
    public static Color fromRGBA(final float r, final float g, final float b, final float a) {
        return new Color(
                (int) (r * 255),
                (int) (g * 255),
                (int) (b * 255),
                (int) (a * 255)
        );
    }


    private final byte r;
    private final byte g;
    private final byte b;
    private final byte a;

    private Color(final int r, final int g, final int b, final int a) {
        this.r = (byte) (r & 255);
        this.g = (byte) (g & 255);
        this.b = (byte) (b & 255);
        this.a = (byte) (a & 255);
    }

    /**
     * @return The red value between 0 and 255
     */
    public int getRed() {
        return this.r & 255;
    }

    /**
     * @return The red value between 0 and 1
     */
    public float getRedF() {
        return (float) (this.r & 255) / 255;
    }

    /**
     * Get a new color with a different red value.
     *
     * @param r The new red value between 0 and 255
     * @return The new color
     */
    public Color withRed(final int r) {
        if (r == this.r) return this;
        return new Color(r, this.g, this.b, this.a);
    }

    /**
     * Get a new color with a different red value.
     *
     * @param r The new red value between 0 and 1
     * @return The new color
     */
    public Color withRedF(final float r) {
        return this.withRed((int) (r * 255));
    }

    /**
     * @return The green value between 0 and 255
     */
    public int getGreen() {
        return this.g & 255;
    }

    /**
     * @return The green value between 0 and 1
     */
    public float getGreenF() {
        return (float) (this.g & 255) / 255;
    }

    /**
     * Get a new color with a different green value.
     *
     * @param g The new green value between 0 and 255
     * @return The new color
     */
    public Color withGreen(final int g) {
        if (g == this.g) return this;
        return new Color(this.r, g, this.b, this.a);
    }

    /**
     * Get a new color with a different green value.
     *
     * @param g The new green value between 0 and 1
     * @return The new color
     */
    public Color withGreenF(final float g) {
        return this.withGreen((int) (g * 255));
    }

    /**
     * @return The blue value between 0 and 255
     */
    public int getBlue() {
        return this.b & 255;
    }

    /**
     * @return The blue value between 0 and 1
     */
    public float getBlueF() {
        return (float) (this.b & 255) / 255;
    }

    /**
     * Get a new color with a different blue value.
     *
     * @param b The new blue value between 0 and 255
     * @return The new color
     */
    public Color withBlue(final int b) {
        if (b == this.b) return this;
        return new Color(this.r, this.g, b, this.a);
    }

    /**
     * Get a new color with a different blue value.
     *
     * @param b The new blue value between 0 and 1
     * @return The new color
     */
    public Color withBlueF(final float b) {
        return this.withBlue((int) (b * 255));
    }

    /**
     * @return The alpha value between 0 and 255
     */
    public int getAlpha() {
        return this.a & 255;
    }

    /**
     * @return The alpha value between 0 and 1
     */
    public float getAlphaF() {
        return (float) (this.a & 255) / 255;
    }

    /**
     * Get a new color with a different alpha value.
     *
     * @param a The new alpha value between 0 and 255
     * @return The new color
     */
    public Color withAlpha(final int a) {
        if (a == this.a) return this;
        return new Color(this.r, this.g, this.b, a);
    }

    /**
     * Get a new color with a different alpha value.
     *
     * @param a The new alpha value between 0 and 1
     * @return The new color
     */
    public Color withAlphaF(final float a) {
        return this.withAlpha((int) (a * 255));
    }

    /**
     * @return The color as an int in the format: 0xRRGGBB
     */
    public int toRGB() {
        return (this.r & 255) << 16 | (this.g & 255) << 8 | (this.b & 255);
    }

    /**
     * @return The color as an int in the format: 0xBBGGRR
     */
    public int toBGR() {
        return (this.b & 255) << 16 | (this.g & 255) << 8 | (this.r & 255);
    }

    /**
     * @return The color as an int in the format: 0xRRGGBBAA
     */
    public int toRGBA() {
        return (this.r & 255) << 24 | (this.g & 255) << 16 | (this.b & 255) << 8 | (this.a & 255);
    }

    /**
     * @return The color as an int in the format: 0xAARRGGBB
     */
    public int toARGB() {
        return (this.a & 255) << 24 | (this.r & 255) << 16 | (this.g & 255) << 8 | (this.b & 255);
    }

    /**
     * @return The color as an int in the format: 0xBBGGRRAA
     */
    public int toBGRA() {
        return (this.b & 255) << 24 | (this.g & 255) << 16 | (this.r & 255) << 8 | (this.a & 255);
    }

    /**
     * @return The color as an int in the format: 0xAABBGGRR
     */
    public int toABGR() {
        return (this.a & 255) << 24 | (this.b & 255) << 16 | (this.g & 255) << 8 | (this.r & 255);
    }

    /**
     * @return The color as a float array with the values in the order: r, g, b
     */
    public float[] toRGBF() {
        return new float[]{this.getRedF(), this.getGreenF(), this.getBlueF()};
    }

    /**
     * @return The color as a float array with the values in the order: b, g, r
     */
    public float[] toBGRF() {
        return new float[]{this.getBlueF(), this.getGreenF(), this.getRedF()};
    }

    /**
     * @return The color as a float array with the values in the order: r, g, b, a
     */
    public float[] toRGBAF() {
        return new float[]{this.getRedF(), this.getGreenF(), this.getBlueF(), this.getAlphaF()};
    }

    /**
     * @return The color as a float array with the values in the order: a, r, g, b
     */
    public float[] toARGBF() {
        return new float[]{this.getAlphaF(), this.getRedF(), this.getGreenF(), this.getBlueF()};
    }

    /**
     * @return The color as a float array with the values in the order: b, g, r, a
     */
    public float[] toBGRAF() {
        return new float[]{this.getBlueF(), this.getGreenF(), this.getRedF(), this.getAlphaF()};
    }

    /**
     * @return The color as a float array with the values in the order: a, b, g, r
     */
    public float[] toABGRF() {
        return new float[]{this.getAlphaF(), this.getBlueF(), this.getGreenF(), this.getRedF()};
    }

    /**
     * @return This color brightened by 0.7
     */
    public Color brighter() {
        return this.brighter(0.7F);
    }

    /**
     * Brighten this color by the given factor.
     *
     * @param factor The factor to brighten this color by
     * @return The new color
     */
    public Color brighter(final float factor) {
        int r = this.getRed();
        int g = this.getGreen();
        int b = this.getBlue();
        int a = this.getAlpha();

        int gray = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) return new Color(gray, gray, gray, a);
        if (r > 0 && r < gray) r = gray;
        if (g > 0 && g < gray) g = gray;
        if (b > 0 && b < gray) b = gray;

        return new Color(
                MathUtils.clamp((int) (r / factor), 0, 255),
                MathUtils.clamp((int) (g / factor), 0, 255),
                MathUtils.clamp((int) (b / factor), 0, 255),
                a
        );
    }

    /**
     * @return This color darkened by 0.7
     */
    public Color darker() {
        return this.darker(0.7F);
    }

    /**
     * Darken this color by the given factor.
     *
     * @param factor The factor to darken this color by
     * @return The new color
     */
    public Color darker(final float factor) {
        return new Color(
                MathUtils.clamp((int) (this.getRed() * factor), 0, 255),
                MathUtils.clamp((int) (this.getGreen() * factor), 0, 255),
                MathUtils.clamp((int) (this.getBlue() * factor), 0, 255),
                this.getAlpha()
        );
    }

    /**
     * Invert this color.<br>
     * The alpha value will not be changed.
     *
     * @return The inverted color
     */
    public Color invert() {
        return new Color(255 - this.getRed(), 255 - this.getGreen(), 255 - this.getBlue(), this.getAlpha());
    }

    /**
     * Multiply this color with a factor.<br>
     * The alpha value will not be changed.
     *
     * @param multiplier The factor to multiply with
     * @return The multiplied color
     */
    public Color multiply(final float multiplier) {
        return new Color(
                MathUtils.clamp((int) (this.getRed() * multiplier), 0, 255),
                MathUtils.clamp((int) (this.getGreen() * multiplier), 0, 255),
                MathUtils.clamp((int) (this.getBlue() * multiplier), 0, 255),
                this.getAlpha()
        );
    }

    /**
     * Multiply this colors alpha value with a factor.<br>
     * The other values will not be changed.
     *
     * @param multiplier The factor to multiply with
     * @return The multiplied color
     */
    public Color multiplyAlpha(final float multiplier) {
        return new Color(
                this.getRed(),
                this.getGreen(),
                this.getBlue(),
                MathUtils.clamp((int) (this.getAlpha() * multiplier), 0, 255)
        );
    }

    /**
     * Multiply this color with a factor.
     *
     * @param multiplier The factor to multiply with
     * @return The multiplied color
     */
    public Color multiplyAll(final float multiplier) {
        return new Color(
                MathUtils.clamp((int) (this.getRed() * multiplier), 0, 255),
                MathUtils.clamp((int) (this.getGreen() * multiplier), 0, 255),
                MathUtils.clamp((int) (this.getBlue() * multiplier), 0, 255),
                MathUtils.clamp((int) (this.getAlpha() * multiplier), 0, 255)
        );
    }

    /**
     * Multiply this color with another color.<br>
     * The values of this color will be multiplied with the float values of the other color.
     *
     * @param color The color to multiply with
     * @return The multiplied color
     */
    public Color multiply(final Color color) {
        return new Color(
                MathUtils.clamp((int) (this.getRed() * color.getRedF()), 0, 255),
                MathUtils.clamp((int) (this.getGreen() * color.getGreenF()), 0, 255),
                MathUtils.clamp((int) (this.getBlue() * color.getBlueF()), 0, 255),
                MathUtils.clamp((int) (this.getAlpha() * color.getAlphaF()), 0, 255)
        );
    }

    /**
     * Calculate the distance between this color and another color.
     *
     * @param color The other color
     * @return The distance between the colors
     */
    public double distance(final Color color) {
        return Math.abs(this.getRed() - color.getRed())
                + Math.abs(this.getGreen() - color.getGreen())
                + Math.abs(this.getBlue() - color.getBlue());
    }

}
