package net.lenni0451.commons;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;

@Deprecated
@UtilityClass
@ApiStatus.ScheduledForRemoval
public class StringUtils {

    /**
     * Please use {@link net.lenni0451.commons.strings.StringUtils#uppercaseFirst(String)}.
     *
     * @param s The string to uppercase
     * @return The uppercase string
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String uppercaseFirst(final String s) {
        return net.lenni0451.commons.strings.StringUtils.uppercaseFirst(s);
    }

    /**
     * Please use {@link net.lenni0451.commons.strings.StringUtils#repeat(char, int)}.
     *
     * @param c     The char to repeat
     * @param count The amount of times to repeat
     * @return The repeated string
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String repeat(final char c, final int count) {
        return net.lenni0451.commons.strings.StringUtils.repeat(c, count);
    }

    /**
     * Please use {@link net.lenni0451.commons.strings.StringUtils#repeat(String, int)}.
     *
     * @param s     The string to repeat
     * @param count The amount of times to repeat
     * @return The repeated string
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String repeat(final String s, final int count) {
        return net.lenni0451.commons.strings.StringUtils.repeat(s, count);
    }

    /**
     * Please use {@link net.lenni0451.commons.strings.StringUtils#cut(String, int)}.
     *
     * @param s      The string to cut
     * @param length The length to cut to
     * @return The cut string
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String cut(final String s, final int length) {
        return net.lenni0451.commons.strings.StringUtils.cut(s, length);
    }

    /**
     * Please use {@link net.lenni0451.commons.strings.StringUtils#cut(String, char, int)}.
     *
     * @param s      The string to cut
     * @param filler The filler to add if the string is shorter
     * @param length The length to cut to
     * @return The cut string
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String cut(String s, final char filler, final int length) {
        return net.lenni0451.commons.strings.StringUtils.cut(s, filler, length);
    }

    /**
     * Please use {@link net.lenni0451.commons.strings.StringUtils#count(String, String)}.
     *
     * @param s       The string to search in
     * @param toCount The string to count
     * @return The amount of occurrences
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static int count(final String s, final String toCount) {
        return net.lenni0451.commons.strings.StringUtils.count(s, toCount);
    }

    /**
     * Please use {@link net.lenni0451.commons.strings.StringUtils#flip(String)}.
     *
     * @param s The string to flip
     * @return The flipped string
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String flip(final String s) {
        return net.lenni0451.commons.strings.StringUtils.flip(s);
    }

}
