package net.lenni0451.commons.math;

public class MathUtils {

    private static final int DATA_BASE_UNIT = 1024;
    private static final String[] DATA_UNITS = new String[]{"KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    /**
     * Floor a float to an int.
     *
     * @param f The float to floor
     * @return The floored int
     */
    public static int floorInt(final float f) {
        return (int) Math.floor(f);
    }

    /**
     * Floor a double to an int.
     *
     * @param d The double to floor
     * @return The floored int
     */
    public static int floorInt(final double d) {
        return (int) Math.floor(d);
    }

    /**
     * Floor a float to a long.
     *
     * @param f The float to floor
     * @return The floored long
     */
    public static long floorLong(final float f) {
        return (long) Math.floor(f);
    }

    /**
     * Floor a double to a long.
     *
     * @param d The double to floor
     * @return The floored long
     */
    public static long floorLong(final double d) {
        return (long) Math.floor(d);
    }

    /**
     * Ceil a float to an int.
     *
     * @param f The float to ceil
     * @return The ceiled int
     */
    public static int ceilInt(final float f) {
        return (int) Math.ceil(f);
    }

    /**
     * Ceil a double to an int.
     *
     * @param d The double to ceil
     * @return The ceiled int
     */
    public static int ceilInt(final double d) {
        return (int) Math.ceil(d);
    }

    /**
     * Ceil a float to a long.
     *
     * @param f The float to ceil
     * @return The ceiled long
     */
    public static long ceilLong(final float f) {
        return (long) Math.ceil(f);
    }

    /**
     * Ceil a double to a long.
     *
     * @param d The double to ceil
     * @return The ceiled long
     */
    public static long ceilLong(final double d) {
        return (long) Math.ceil(d);
    }

    /**
     * Clamp an int between a min and max value.
     *
     * @param i   The int to clamp
     * @param min The min value
     * @param max The max value
     * @return The clamped int
     */
    public static int clamp(final int i, final int min, final int max) {
        return Math.min(max, Math.max(min, i));
    }

    /**
     * Clamp a long between a min and max value.
     *
     * @param l   The long to clamp
     * @param min The min value
     * @param max The max value
     * @return The clamped long
     */
    public static long clamp(final long l, final long min, final long max) {
        return Math.min(max, Math.max(min, l));
    }

    /**
     * Clamp a float between a min and max value.
     *
     * @param f   The float to clamp
     * @param min The min value
     * @param max The max value
     * @return The clamped float
     */
    public static float clamp(final float f, final float min, final float max) {
        return Math.min(max, Math.max(min, f));
    }

    /**
     * Clamp a double between a min and max value.
     *
     * @param d   The double to clamp
     * @param min The min value
     * @param max The max value
     * @return The clamped double
     */
    public static double clamp(final double d, final double min, final double max) {
        return Math.min(max, Math.max(min, d));
    }

    /**
     * Check if a float is a decimal number.
     *
     * @param f The float to check
     * @return If the float is a decimal number
     */
    public static boolean isDecimal(final float f) {
        return f % 1 != 0;
    }

    /**
     * Check if a double is a decimal number.
     *
     * @param d The double to check
     * @return If the double is a decimal number
     */
    public static boolean isDecimal(final double d) {
        return d % 1 != 0;
    }

    /**
     * Map an int from one range to another.
     *
     * @param in     The int to map
     * @param inMin  The min value of the input range
     * @param inMax  The max value of the input range
     * @param outMin The min value of the output range
     * @param outMax The max value of the output range
     * @return The mapped int
     */
    public static int map(final int in, final int inMin, final int inMax, final int outMin, final int outMax) {
        return (in - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * Map a long from one range to another.
     *
     * @param in     The long to map
     * @param inMin  The min value of the input range
     * @param inMax  The max value of the input range
     * @param outMin The min value of the output range
     * @param outMax The max value of the output range
     * @return The mapped long
     */
    public static long map(final long in, final long inMin, final long inMax, final long outMin, final long outMax) {
        return (in - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * Map a float from one range to another.
     *
     * @param in     The float to map
     * @param inMin  The min value of the input range
     * @param inMax  The max value of the input range
     * @param outMin The min value of the output range
     * @param outMax The max value of the output range
     * @return The mapped float
     */
    public static float map(final float in, final float inMin, final float inMax, final float outMin, final float outMax) {
        return (in - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * Map a double from one range to another.
     *
     * @param in     The double to map
     * @param inMin  The min value of the input range
     * @param inMax  The max value of the input range
     * @param outMin The min value of the output range
     * @param outMax The max value of the output range
     * @return The mapped double
     */
    public static double map(final double in, final double inMin, final double inMax, final double outMin, final double outMax) {
        return (in - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * Convert a byte count to a human-readable string.
     *
     * @param bytes The byte count
     * @return The human-readable string
     */
    public static String formatBytes(long bytes) {
        boolean neg = bytes < 0;
        bytes = Math.abs(bytes);
        if (bytes < DATA_BASE_UNIT) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(DATA_BASE_UNIT));
        return new StringBuilder()
                .append(neg ? "-" : "")
                .append(String.format("%.1f ", bytes / Math.pow(DATA_BASE_UNIT, exp)))
                .append(DATA_UNITS[exp - 1])
                .toString();
    }

    /**
     * Round a float to a certain precision.
     *
     * @param value     The float to round
     * @param precision The precision
     * @return The rounded float
     */
    public static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

    /**
     * Round a double to a certain precision.
     *
     * @param value     The double to round
     * @param precision The precision
     * @return The rounded double
     */
    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}
