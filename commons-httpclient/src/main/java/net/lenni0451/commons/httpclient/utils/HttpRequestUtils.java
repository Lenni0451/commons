package net.lenni0451.commons.httpclient.utils;

import lombok.experimental.UtilityClass;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;

import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.BiConsumer;

@UtilityClass
public class HttpRequestUtils {

    /**
     * Merge multiple headers into one map.
     *
     * @param maps The headers to merge
     * @return The merged headers
     */
    @SafeVarargs
    public static Map<String, List<String>> mergeHeaders(final Map<String, List<String>>... maps) {
        Map<String, List<String>> headers = new HashMap<>();
        for (Map<String, List<String>> map : maps) {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getValue().isEmpty()) continue; //Skip empty headers
                headers.put(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue());
            }
        }
        return headers;
    }

    /**
     * Get the cookie headers for a URL.
     *
     * @param cookieManager The cookie manager to use
     * @param url           The URL to get the cookies for
     * @return The cookie headers
     * @throws IOException If an I/O error occurs
     */
    public static Map<String, List<String>> getCookieHeaders(@Nullable final CookieManager cookieManager, final URL url) throws IOException {
        try {
            if (cookieManager == null) return Collections.emptyMap();
            return cookieManager.get(url.toURI(), Collections.emptyMap());
        } catch (URISyntaxException e) {
            throw new IOException("Failed to parse URL as URI", e);
        }
    }

    /**
     * Update the cookies for a URL.
     *
     * @param cookieManager The cookie manager to use
     * @param url           The URL to update the cookies for
     * @param headers       The headers to update the cookies from
     * @throws IOException If an I/O error occurs
     */
    public static void updateCookies(@Nullable final CookieManager cookieManager, final URL url, final Map<String, List<String>> headers) throws IOException {
        if (cookieManager == null) return;
        try {
            cookieManager.put(url.toURI(), headers);
        } catch (URISyntaxException e) {
            throw new IOException("Failed to parse URL as URI", e);
        }
    }

    /**
     * Set the headers for a connection.<br>
     * Cookies will be merged into one header separated by {@code ;}.
     *
     * @param connection The connection to set the headers for
     * @param headers    The headers to set
     */
    public static void setHeaders(final HttpURLConnection connection, final Map<String, List<String>> headers) {
        setHeaders(headers, connection::setRequestProperty, connection::addRequestProperty);
    }

    /**
     * Set the headers for a dynamic connection type.<br>
     * Cookies will be merged into one header separated by {@code ;}.
     *
     * @param headers     The headers to set
     * @param setConsumer The consumer to set the header
     * @param addConsumer The consumer to add the header
     */
    public static void setHeaders(final Map<String, List<String>> headers, final BiConsumer<String, String> setConsumer, final BiConsumer<String, String> addConsumer) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (HttpHeaders.COOKIE.equalsIgnoreCase(entry.getKey())) {
                setConsumer.accept(entry.getKey(), String.join("; ", entry.getValue()));
            } else {
                boolean first = true;
                for (String val : entry.getValue()) {
                    if (first) {
                        first = false;
                        //Use the first value to clear all previous values
                        setConsumer.accept(entry.getKey(), val);
                    } else {
                        addConsumer.accept(entry.getKey(), val);
                    }
                }
            }
        }
    }

    /**
     * Read the body of a connection.
     *
     * @param connection The connection to read the body from
     * @return The body of the connection
     * @throws IOException If an I/O error occurs
     */
    public static byte[] readBody(final HttpURLConnection connection) throws IOException {
        return readFromStream(getInputStream(connection));
    }

    /**
     * Get the input stream of a connection.
     *
     * @param connection The connection to get the input stream from
     * @return The input stream of the connection
     * @throws IOException If an I/O error occurs
     */
    public static InputStream getInputStream(final HttpURLConnection connection) throws IOException {
        InputStream is;
        if (connection.getResponseCode() >= 400) is = connection.getErrorStream();
        else is = connection.getInputStream();
        if (is == null) is = new ByteArrayInputStream(new byte[0]);
        return is;
    }

    /**
     * Read the body of a connection.
     *
     * @param is The input stream to read the body from
     * @return The body of the connection
     * @throws IOException If an I/O error occurs
     */
    public static byte[] readFromStream(@WillNotClose final InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) != -1) baos.write(buf, 0, len);
        return baos.toByteArray();
    }

    /**
     * Parse a HTTP date.
     *
     * @param httpDate The HTTP date to parse
     * @return The parsed date
     * @throws DateTimeParseException If the date could not be parsed
     */
    public static Instant parseHttpDate(final String httpDate) throws DateTimeParseException {
        return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(httpDate));
    }

    /**
     * Parse an HTTP date or seconds string as milliseconds until the date.
     *
     * @param value The value to parse
     * @return The parsed value in milliseconds
     */
    @Nullable
    public static Long parseSecondsOrHttpDate(final String value) {
        try {
            Instant date = HttpRequestUtils.parseHttpDate(value);
            return date.toEpochMilli() - Instant.now().toEpochMilli();
        } catch (DateTimeParseException ignored) {
        }
        try {
            int seconds = Integer.parseInt(value);
            return (long) seconds * 1000;
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

}
