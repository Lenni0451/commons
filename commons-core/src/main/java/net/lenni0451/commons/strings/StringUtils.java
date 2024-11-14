package net.lenni0451.commons.strings;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    /**
     * Uppercase the first letter of the string and lowercase the rest.
     *
     * @param s The string to uppercase
     * @return The uppercase string
     */
    public static String uppercaseFirst(final String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    /**
     * Repeat the char a given amount of times.
     *
     * @param c     The char to repeat
     * @param count The amount of times to repeat
     * @return The repeated string
     */
    public static String repeat(final char c, final int count) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < count; i++) out.append(c);
        return out.toString();
    }

    /**
     * Repeat the string a given amount of times.
     *
     * @param s     The string to repeat
     * @param count The amount of times to repeat
     * @return The repeated string
     */
    public static String repeat(final String s, final int count) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < count; i++) out.append(s);
        return out.toString();
    }

    /**
     * Cut the string to the given length.<br>
     * If the string is shorter it will be returned as it is.
     *
     * @param s      The string to cut
     * @param length The length to cut to
     * @return The cut string
     */
    public static String cut(final String s, final int length) {
        if (s.length() < length) return s;
        return s.substring(0, length);
    }

    /**
     * Cut the string to the given length.<br>
     * The filler will be appended if the string is shorter.
     *
     * @param s      The string to cut
     * @param filler The filler to add if the string is shorter
     * @param length The length to cut to
     * @return The cut string
     */
    public static String cut(String s, final char filler, final int length) {
        if (s.length() < length) return s + repeat(filler, length - s.length());
        return s.substring(0, length);
    }

    /**
     * Count the amount of occurrences of a string in another string.
     *
     * @param s       The string to search in
     * @param toCount The string to count
     * @return The amount of occurrences
     */
    public static int count(final String s, final String toCount) {
        int count = s.length() - s.replace(toCount, "").length();
        return count / toCount.length();
    }

    /**
     * Flip the given string.
     *
     * @param s The string to flip
     * @return The flipped string
     */
    public static String flip(final String s) {
        char[] chars = s.toCharArray();
        char[] out = new char[chars.length];
        for (int i = 0; i < chars.length; i++) out[i] = chars[chars.length - i - 1];
        return new String(out);
    }

}
