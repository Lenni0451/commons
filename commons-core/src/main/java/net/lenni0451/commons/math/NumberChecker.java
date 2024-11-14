package net.lenni0451.commons.math;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberChecker {

    /**
     * Check if a string is a valid byte.
     *
     * @param s The string to check
     * @return If the string is a valid byte
     */
    public static boolean isByte(final String s) {
        try {
            Byte.parseByte(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid short.
     *
     * @param s The string to check
     * @return If the string is a valid short
     */
    public static boolean isShort(final String s) {
        try {
            Short.parseShort(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid integer.
     *
     * @param s The string to check
     * @return If the string is a valid integer
     */
    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid long.
     *
     * @param s The string to check
     * @return If the string is a valid long
     */
    public static boolean isLong(final String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid float.
     *
     * @param s The string to check
     * @return If the string is a valid float
     */
    public static boolean isFloat(final String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid double.
     *
     * @param s The string to check
     * @return If the string is a valid double
     */
    public static boolean isDouble(final String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
