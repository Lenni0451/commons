package net.lenni0451.commons.debugging;

import lombok.experimental.UtilityClass;

/**
 * Print debug messages to the console without spamming the same message multiple times.
 */
@UtilityClass
public class CachedSysout {

    private static String lastMessage;

    public static void println(final boolean b) {
        println(String.valueOf(b));
    }

    public static void println(final byte b) {
        println(String.valueOf(b));
    }

    public static void println(final char c) {
        println(String.valueOf(c));
    }

    public static void println(final short s) {
        println(String.valueOf(s));
    }

    public static void println(final int i) {
        println(String.valueOf(i));
    }

    public static void println(final long l) {
        println(String.valueOf(l));
    }

    public static void println(final float f) {
        println(String.valueOf(f));
    }

    public static void println(final double d) {
        println(String.valueOf(d));
    }

    public static void println(final Object o) {
        println(String.valueOf(o));
    }

    public static void println(final String message) {
        if (message.equals(lastMessage)) return;
        lastMessage = message;
        System.out.println(message);
    }

}
