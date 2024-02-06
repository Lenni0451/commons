package net.lenni0451.commons.color;

import net.lenni0451.commons.math.MathUtils;

import java.util.Objects;

public class Color {

    public static final Color TRANSPARENT = Color.fromRGBA(0, 0, 0, 0);
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
        return fromRGB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xBBGGRR
     *
     * @param bgr The int
     * @return The created color
     */
    public static Color fromBGR(final int bgr) {
        return fromRGB(bgr & 255, (bgr >> 8) & 255, (bgr >> 16) & 255);
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
        return fromRGBA((rgba >> 24) & 255, (rgba >> 16) & 255, (rgba >> 8) & 255, rgba & 255);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xAARRGGBB
     *
     * @param argb The int
     * @return The created color
     */
    public static Color fromARGB(final int argb) {
        return fromRGBA((argb >> 16) & 255, (argb >> 8) & 255, argb & 255, (argb >> 24) & 255);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xBBGGRRAA
     *
     * @param bgra The int
     * @return The created color
     */
    public static Color fromBGRA(final int bgra) {
        return fromRGBA((bgra >> 8) & 255, (bgra >> 16) & 255, (bgra >> 24) & 255, bgra & 255);
    }

    /**
     * Create a color from an int.<br>
     * The int must be in the format: 0xAABBGGRR
     *
     * @param abgr The int
     * @return The created color
     */
    public static Color fromABGR(final int abgr) {
        return fromRGBA(abgr & 255, (abgr >> 8) & 255, (abgr >> 16) & 255, (abgr >> 24) & 255);
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
        verify(r, g, b, a);
        return directRGBA(r, g, b, a);
    }

    /**
     * Directly create a color from red, green, blue and alpha values.<br>
     * This method does not perform bounds checking.
     *
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The created color
     */
    public static Color directRGBA(final int r, final int g, final int b, final int a) {
        return new Color(
                (byte) (r & 255),
                (byte) (g & 255),
                (byte) (b & 255),
                (byte) (a & 255)
        );
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
    public static Color fromRGBF(final float[] rgb) {
        return fromRGBF(rgb[0], rgb[1], rgb[2]);
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
    public static Color fromBGRF(final float[] bgr) {
        return fromRGBF(bgr[2], bgr[1], bgr[0]);
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
    public static Color fromRGBF(final float r, final float g, final float b) {
        return fromRGBAF(r, g, b, 1);
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
    public static Color fromRGBAF(final float[] rgba) {
        return fromRGBAF(rgba[0], rgba[1], rgba[2], rgba[3]);
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
    public static Color fromARGBF(final float[] argb) {
        return fromRGBAF(argb[1], argb[2], argb[3], argb[0]);
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
    public static Color fromBGRAF(final float[] bgra) {
        return fromRGBAF(bgra[2], bgra[1], bgra[0], bgra[3]);
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
    public static Color fromABGRF(final float[] abgr) {
        return fromRGBAF(abgr[3], abgr[2], abgr[1], abgr[0]);
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
    public static Color fromRGBAF(final float r, final float g, final float b, final float a) {
        verify(r, g, b, a);
        return directRGBA(
                (int) (r * 255) & 255,
                (int) (g * 255) & 255,
                (int) (b * 255) & 255,
                (int) (a * 255) & 255
        );
    }

    /**
     * Create a color from a float array.<br>
     * The array must have a length of 3.<br>
     * The array must contain the values in the order: h, s, b<br>
     * The values must be between 0 and 1.
     *
     * @param hsb The float array
     * @return The created color
     */
    public static Color fromHSB(final float[] hsb) {
        return fromHSB(hsb[0], hsb[1], hsb[2]);
    }

    /**
     * Create a color from HSB values.<br>
     * The values must be between 0 and 1.
     *
     * @param hue        The hue value
     * @param saturation The saturation value
     * @param brightness The brightness value
     * @return The created color
     */
    public static Color fromHSB(final float hue, final float saturation, final float brightness) {
        if (saturation == 0) return fromRGBF(brightness, brightness, brightness);
        float r;
        float g;
        float b;
        float h = (float) ((hue - Math.floor(hue)) * 6);
        float f = h - (float) Math.floor(h);
        float p = brightness * (1 - saturation);
        float q = brightness * (1 - saturation * f);
        float t = brightness * (1 - saturation * (1 - f));
        switch ((int) h) {
            case 0:
                r = brightness;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = brightness;
                b = p;
                break;
            case 2:
                r = p;
                g = brightness;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = brightness;
                break;
            case 4:
                r = t;
                g = p;
                b = brightness;
                break;
            case 5:
                r = brightness;
                g = p;
                b = q;
                break;
            default:
                throw new IllegalStateException("Unable to convert HSB to RGB. Input:" + hue + ", " + saturation + ", " + brightness);
        }
        return fromRGBF(r, g, b);
    }


    /**
     * Get a color calculated by the current time.<br>
     * Calling this method subsequently will generate an RGB rainbow color effect.<br>
     * The offset depends on the used divider. With a divider of {@code 7.5F} the color will be the same with the offset of {@code 0} and {@code 7500}.<br>
     * The higher the divider the slower and smoother the color will change.
     *
     * @param offset  The offset added to the current time
     * @param divider The divider for the current time
     * @return The calculated color
     * @see ColorUtils#getRainbowColor(int, float)
     */
    public static Color getRainbowColor(final int offset, final float divider) {
        long l = System.currentTimeMillis() + offset;
        l %= (int) (1000 * divider);
        return Color.fromHSB(l / divider / 1000F, 1F, 1F);
    }

    /**
     * Interpolate between two colors with a progress.<br>
     * The progress is a value between 0 and 1.
     *
     * @param progress The progress between the two colors
     * @param color1   The first color
     * @param color2   The second color
     * @return The interpolated color
     * @see ColorUtils#interpolate(float, java.awt.Color, java.awt.Color)
     */
    public static Color interpolate(final float progress, final Color color1, final Color color2) {
        return directRGBA(
                MathUtils.clamp((int) (color1.getRed() + (color2.getRed() - color1.getRed()) * progress), 0, 255),
                MathUtils.clamp((int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * progress), 0, 255),
                MathUtils.clamp((int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * progress), 0, 255),
                MathUtils.clamp((int) (color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * progress), 0, 255)
        );
    }

    /**
     * Interpolate between multiple colors with a progress.<br>
     * The progress is a value between 0 and 1.<br>
     * The colors and steps must have the same length.
     *
     * @param progress The progress between the colors
     * @param colors   The colors to interpolate between
     * @param steps    The steps between the colors
     * @return The interpolated color
     * @see ColorUtils#interpolate(float, java.awt.Color[], float[])
     */
    public static Color interpolate(final float progress, final Color[] colors, final float[] steps) {
        if (colors.length != steps.length) throw new IllegalArgumentException("Colors and steps must have the same length");
        if (colors.length == 0) throw new IllegalArgumentException("Colors and steps must have a length greater than 0");

        if (colors.length == 1) return colors[0];
        if (progress <= steps[0]) return colors[0];
        if (progress >= steps[steps.length - 1]) return colors[colors.length - 1];
        for (int i = 0; i < steps.length; i++) {
            if (progress < steps[i]) {
                float stepProgress = (progress - steps[i - 1]) / (steps[i] - steps[i - 1]);
                return interpolate(stepProgress, colors[i - 1], colors[i]);
            }
        }
        return colors[colors.length - 1];
    }

    /**
     * Interpolate between multiple colors with a progress.<br>
     * The progress is a value between 0 and 1.<br>
     * The colors must have a length greater than 0.
     *
     * @param progress The progress between the colors
     * @param colors   The colors to interpolate between
     * @return The interpolated color
     * @see ColorUtils#interpolate(float, java.awt.Color...)
     */
    public static Color interpolate(final float progress, final Color... colors) {
        if (colors.length == 0) throw new IllegalArgumentException("Colors must have a length greater than 0");
        if (colors.length == 1) return colors[0];
        if (colors.length == 2) return interpolate(progress, colors[0], colors[1]);

        float step = 1F / (colors.length - 1);
        float[] steps = new float[colors.length];
        for (int i = 0; i < colors.length; i++) steps[i] = step * i;
        return interpolate(progress, colors, steps);
    }


    private final byte r;
    private final byte g;
    private final byte b;
    private final byte a;

    private Color(final byte r, final byte g, final byte b, final byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
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
        verify(r, "Red");
        return new Color((byte) (r & 255), this.g, this.b, this.a);
    }

    /**
     * Get a new color with a different red value.
     *
     * @param r The new red value between 0 and 1
     * @return The new color
     */
    public Color withRedF(final float r) {
        verify(r, "Red");
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
        verify(g, "Green");
        return new Color(this.r, (byte) (g & 255), this.b, this.a);
    }

    /**
     * Get a new color with a different green value.
     *
     * @param g The new green value between 0 and 1
     * @return The new color
     */
    public Color withGreenF(final float g) {
        verify(g, "Green");
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
        verify(b, "Blue");
        return new Color(this.r, this.g, (byte) (b & 255), this.a);
    }

    /**
     * Get a new color with a different blue value.
     *
     * @param b The new blue value between 0 and 1
     * @return The new color
     */
    public Color withBlueF(final float b) {
        verify(b, "Blue");
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
        verify(a, "Alpha");
        return new Color(this.r, this.g, this.b, (byte) (a & 255));
    }

    /**
     * Get a new color with a different alpha value.
     *
     * @param a The new alpha value between 0 and 1
     * @return The new color
     */
    public Color withAlphaF(final float a) {
        verify(a, "Alpha");
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
     * @return The color as a float array with the values in the order: h, s, b
     */
    public float[] toHSB() {
        float r = this.getRedF();
        float g = this.getGreenF();
        float b = this.getBlueF();
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;
        float h;
        float s;
        float v = max;
        if (max == 0) {
            h = 0;
            s = 0;
        } else {
            s = delta / max;
            if (r == max) h = (g - b) / delta;
            else if (g == max) h = 2 + (b - r) / delta;
            else h = 4 + (r - g) / delta;
        }
        h /= 6;
        if (h < 0) h++;
        return new float[]{h, s, v};
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
        if (r == 0 && g == 0 && b == 0) return directRGBA(gray, gray, gray, a);
        if (r > 0 && r < gray) r = gray;
        if (g > 0 && g < gray) g = gray;
        if (b > 0 && b < gray) b = gray;

        return directRGBA(
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
        return directRGBA(
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
        return directRGBA(255 - this.getRed(), 255 - this.getGreen(), 255 - this.getBlue(), this.getAlpha());
    }

    /**
     * Multiply this color with a factor.<br>
     * The alpha value will not be changed.
     *
     * @param multiplier The factor to multiply with
     * @return The multiplied color
     */
    public Color multiply(final float multiplier) {
        return directRGBA(
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
        return directRGBA(
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
        return directRGBA(
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
        return directRGBA(
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
    public int distance(final Color color) {
        return Math.abs(this.getRed() - color.getRed())
                + Math.abs(this.getGreen() - color.getGreen())
                + Math.abs(this.getBlue() - color.getBlue());
    }


    private static void verify(final int i, final String channel) {
        if (i < 0 || i > 255) throw new IllegalArgumentException(channel + " value must be between 0 and 255");
    }

    private static void verify(final int r, final int g, final int b, final int a) {
        if (r < 0 || r > 255) throw new IllegalArgumentException("Red value must be between 0 and 255");
        if (g < 0 || g > 255) throw new IllegalArgumentException("Green value must be between 0 and 255");
        if (b < 0 || b > 255) throw new IllegalArgumentException("Blue value must be between 0 and 255");
        if (a < 0 || a > 255) throw new IllegalArgumentException("Alpha value must be between 0 and 255");
    }

    private static void verify(final float f, final String channel) {
        if (f < 0 || f > 1) throw new IllegalArgumentException(channel + " value must be between 0 and 1");
    }

    private static void verify(final float r, final float g, final float b, final float a) {
        if (r < 0 || r > 1) throw new IllegalArgumentException("Red value must be between 0 and 1");
        if (g < 0 || g > 1) throw new IllegalArgumentException("Green value must be between 0 and 1");
        if (b < 0 || b > 1) throw new IllegalArgumentException("Blue value must be between 0 and 1");
        if (a < 0 || a > 1) throw new IllegalArgumentException("Alpha value must be between 0 and 1");
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + this.r +
                ", g=" + this.g +
                ", b=" + this.b +
                ", a=" + this.a +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return this.r == color.r && this.g == color.g && this.b == color.b && this.a == color.a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.r, this.g, this.b, this.a);
    }

}
