package net.lenni0451.commons.httpclient.utils;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class URLCoder {

    /**
     * Encode a string to be used in a URL.<br>
     * This method uses {@link StandardCharsets#UTF_8} as charset.
     *
     * @param s The string to encode
     * @return The encoded string
     */
    @SneakyThrows
    public static String encode(@Nonnull final String s) {
        return encode(s, StandardCharsets.UTF_8);
    }

    /**
     * Encode a string to be used in a URL.
     *
     * @param s       The string to encode
     * @param charset The charset to use
     * @return The encoded string
     */
    @SneakyThrows
    public static String encode(@Nonnull final String s, @Nonnull final Charset charset) {
        return URLEncoder.encode(s, charset.name());
    }

    /**
     * Decode a string from a URL.<br>
     * This method uses {@link StandardCharsets#UTF_8} as charset.
     *
     * @param s The string to decode
     * @return The decoded string
     */
    @SneakyThrows
    public static String decode(@Nonnull final String s) {
        return decode(s, StandardCharsets.UTF_8);
    }

    /**
     * Decode a string from a URL.
     *
     * @param s       The string to decode
     * @param charset The charset to use
     * @return The decoded string
     */
    @SneakyThrows
    public static String decode(@Nonnull final String s, @Nonnull final Charset charset) {
        return URLDecoder.decode(s, charset.name());
    }

}
