package net.lenni0451.commons.time;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@SuppressWarnings("unused")
public class TimeFormatter {

    /**
     * Format a time in milliseconds to a string.<br>
     * See {@link SimpleDateFormat} for the format string.
     *
     * @param format The format string
     * @param time   The time in milliseconds
     * @return The formatted time
     */
    public static String format(final String format, final long time) {
        return format(format, time, false);
    }

    /**
     * Format a time in milliseconds to a (UTC) string.<br>
     * See {@link SimpleDateFormat} for the format string.
     *
     * @param format The format string
     * @param time   The time in milliseconds
     * @param utc    If the time should be formatted in UTC
     * @return The formatted time
     */
    public static String format(final String format, final long time, final boolean utc) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (utc) sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(time);
    }

    /**
     * Format a time in milliseconds to a string in the format "mm:ss".<br>
     * e.g. {@code 123000 -> 02:03}
     *
     * @param time The time in milliseconds
     * @return The formatted time
     */
    public static String formatMMSS(final long time) {
        return format("mm:ss", time, true);
    }

    /**
     * Format a time in milliseconds to a string in the format "HH:mm:ss".<br>
     * e.g. {@code 3723000 -> 01:02:03}
     *
     * @param time The time in milliseconds
     * @return The formatted time
     */
    public static String formatHHMMSS(final long time) {
        return format("HH:mm:ss", time, true);
    }

}
