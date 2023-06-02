package net.lenni0451.commons.time;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeFormatter {

    public static String format(final String format, final long time) {
        return format(format, time, false);
    }

    public static String format(final String format, final long time, final boolean utc) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (utc) sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(time);
    }

    public static String formatMMSS(final long time) {
        return format("mm:ss", time, true);
    }

    public static String formatHHMMSS(final long time) {
        return format("HH:mm:ss", time, true);
    }

}
