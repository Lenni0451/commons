package net.lenni0451.commons.color;

import java.awt.*;

import static net.lenni0451.commons.MathUtils.clamp;

public class ColorUtils {

    /**
     * Get a color calculated by the current time.<br>
     * Calling this method subsequently will generate an RGB rainbow color effect.
     *
     * @param offset  The offset added to the current time
     * @param divider The divider for the current time
     * @return The calculated color
     */
    public static Color getRainbowColor(final int offset, final float divider) {
        long l = System.currentTimeMillis() + offset;
        l %= (int) (1000 * divider);
        return Color.getHSBColor(l / divider / 1000F, 1F, 1F);
    }

    /**
     * Set the red value of a color.<br>
     * This will generate a new color object unless the red value is already the same.
     *
     * @param color The color to modify
     * @param r     The new red value
     * @return The modified color
     */
    public static Color setRed(final Color color, final int r) {
        if (color.getRed() == r) return color;
        return new Color(clamp(r, 0, 255), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Set the green value of a color.<br>
     * This will generate a new color object unless the green value is already the same.
     *
     * @param color The color to modify
     * @param g     The new green value
     * @return The modified color
     */
    public static Color setGreen(final Color color, final int g) {
        if (color.getGreen() == g) return color;
        return new Color(color.getRed(), clamp(g, 0, 255), color.getBlue(), color.getAlpha());
    }

    /**
     * Set the blue value of a color.<br>
     * This will generate a new color object unless the blue value is already the same.
     *
     * @param color The color to modify
     * @param b     The new blue value
     * @return The modified color
     */
    public static Color setBlue(final Color color, final int b) {
        if (color.getBlue() == b) return color;
        return new Color(color.getRed(), color.getGreen(), clamp(b, 0, 255), color.getAlpha());
    }

    /**
     * Set the alpha value of a color.<br>
     * This will generate a new color object unless the alpha value is already the same.
     *
     * @param color The color to modify
     * @param a     The new alpha value
     * @return The modified color
     */
    public static Color setAlpha(final Color color, final int a) {
        if (color.getAlpha() == a) return color;
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), clamp(a, 0, 255));
    }

    /**
     * Multiply a color with a factor.
     *
     * @param color  The color to multiply
     * @param factor The factor to multiply with
     * @return The multiplied color
     */
    public static Color multiply(final Color color, final float factor) {
        return new Color(
                clamp((int) (color.getRed() * factor), 0, 255),
                clamp((int) (color.getGreen() * factor), 0, 255),
                clamp((int) (color.getBlue() * factor), 0, 255),
                color.getAlpha()
        );
    }

    /**
     * Interpolate between two colors with a progress.<br>
     * The progress is a value between 0 and 1.
     *
     * @param color1   The first color
     * @param color2   The second color
     * @param progress The progress between the two colors
     * @return The interpolated color
     */
    public static Color interpolate(final Color color1, final Color color2, final float progress) {
        return new Color(
                clamp((int) (color1.getRed() + (color2.getRed() - color1.getRed()) * progress), 0, 255),
                clamp((int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * progress), 0, 255),
                clamp((int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * progress), 0, 255),
                clamp((int) (color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * progress), 0, 255)
        );
    }

}
