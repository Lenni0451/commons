package net.lenni0451.commons;

import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static final Random RND = new Random();

    /**
     * Generate a random string with the given length.<br>
     * The charset is: {@code a-z, A-Z, 0-9, _}
     *
     * @param length The length of the string
     * @return The generated string
     */
    public static String randomString(final int length) {
        return randomString(length, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_");
    }

    /**
     * Generate a random string with the given length and charset.
     *
     * @param length  The length of the string
     * @param charset The charset to use
     * @return The generated string
     */
    public static String randomString(final int length, final String charset) {
        char[] out = new char[length];
        for (int i = 0; i < length; i++) out[i] = charset.charAt(RND.nextInt(charset.length()));
        return new String(out);
    }

    /**
     * Randomize the case of the given string.
     *
     * @param s The string to randomize
     * @return The randomized string
     */
    public static String randomizeCase(final String s) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (RND.nextBoolean()) chars[i] = Character.toUpperCase(chars[i]);
            else chars[i] = Character.toLowerCase(chars[i]);
        }
        return new String(chars);
    }

    /**
     * Get a random element from the given array.
     *
     * @param array The array to get the element from
     * @param <T>   The type of the array
     * @return The random element
     */
    public static <T> T randomElement(final T[] array) {
        return array[RND.nextInt(array.length)];
    }

    /**
     * Get a random element from the given list.
     *
     * @param list The list to get the element from
     * @param <T>  The type of the list
     * @return The random element
     */
    public static <T> T randomElement(final List<T> list) {
        return list.get(RND.nextInt(list.size()));
    }

}
