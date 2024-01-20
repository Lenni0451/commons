package net.lenni0451.commons.httpclient.utils;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class URLWrapper {

    /**
     * Create a new empty URLWrapper.
     *
     * @return The URLWrapper
     */
    public static URLWrapper empty() {
        return new URLWrapper();
    }

    /**
     * Create a new URLWrapper from a {@link String}.
     *
     * @param url The URL to wrap
     * @return The URLWrapper
     * @throws MalformedURLException If the URL is invalid
     */
    public static URLWrapper of(@Nonnull final String url) throws MalformedURLException {
        return new URLWrapper(url);
    }

    /**
     * Create a new URLWrapper from an {@link URL}.
     *
     * @param url The URL to wrap
     * @return The URLWrapper
     */
    public static URLWrapper of(final URL url) {
        return new URLWrapper(url);
    }

    /**
     * Create a new URLWrapper from an {@link URI}.
     *
     * @param uri The URI to wrap
     * @return The URLWrapper
     */
    public static URLWrapper of(final URI uri) {
        return new URLWrapper(uri);
    }


    private String protocol;
    private String host;
    private int port = -1;
    private String path;
    private String query;
    private String userInfo;
    private String reference;

    public URLWrapper() {
    }

    public URLWrapper(@Nonnull final String url) throws MalformedURLException {
        this(new URL(url));
    }

    public URLWrapper(@Nonnull final URL url) {
        this.protocol = url.getProtocol();
        this.host = url.getHost();
        this.port = url.getPort();
        this.path = url.getPath();
        this.query = url.getQuery();
        this.userInfo = url.getUserInfo();
        this.reference = url.getRef();
    }

    public URLWrapper(@Nonnull final URI uri) {
        this.protocol = uri.getScheme();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
        this.query = uri.getQuery();
        this.userInfo = uri.getUserInfo();
        this.reference = uri.getFragment();
    }

    /**
     * @return The protocol of the URL. e.g. {@code https}
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Set the protocol of the URL.<br>
     * e.g. {@code https}
     *
     * @param protocol The new protocol
     * @return The URLWrapper
     */
    public URLWrapper setProtocol(@Nonnull final String protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * @return The host of the URL. e.g. {@code www.example.com}
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Set the host of the URL.<br>
     * e.g. {@code www.example.com}
     *
     * @param host The new host
     * @return The URLWrapper
     */
    public URLWrapper setHost(@Nonnull final String host) {
        this.host = host;
        return this;
    }

    /**
     * @return The port of the URL. e.g. {@code 443}
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Set the port of the URL.<br>
     * e.g. {@code 443}
     *
     * @param port The new port
     * @return The URLWrapper
     */
    public URLWrapper setPort(final int port) {
        this.port = port;
        return this;
    }

    /**
     * @return The file of the URL. e.g. {@code /search}
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Set the file of the URL.<br>
     * e.g. {@code /search}
     *
     * @param path The new file
     * @return The URLWrapper
     */
    public URLWrapper setPath(@Nonnull final String path) {
        this.path = path;
        return this;
    }

    /**
     * @return The query of the URL. e.g. {@code q=hello}
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * Set the query of the URL.<br>
     * e.g. {@code q=hello}
     *
     * @param query The new query
     * @return The URLWrapper
     */
    public URLWrapper setQuery(@Nonnull final String query) {
        this.query = query;
        return this;
    }

    /**
     * Get a wrapper for the query parameters.
     *
     * @return The query wrapper
     */
    public QueryWrapper wrapQuery() {
        return new QueryWrapper();
    }

    /**
     * @return The user info of the URL. e.g. {@code user:password}
     */
    public String getUserInfo() {
        return this.userInfo;
    }

    /**
     * Set the user info of the URL.<br>
     * e.g. {@code user:password}
     *
     * @param userInfo The new user info
     * @return The URLWrapper
     */
    public URLWrapper setUserInfo(@Nonnull final String userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    /**
     * @return The reference of the URL. e.g. {@code reference}
     */
    public String getReference() {
        return this.reference;
    }

    /**
     * Set the reference of the URL.<br>
     * e.g. {@code reference}
     *
     * @param reference The new reference
     * @return The URLWrapper
     */
    public URLWrapper setReference(@Nonnull final String reference) {
        this.reference = reference;
        return this;
    }

    /**
     * @return The wrapped URL
     * @throws MalformedURLException If the URL is invalid
     */
    public URL toURL() throws MalformedURLException {
        return new URL(this.toString());
    }

    /**
     * @return The wrapped URI
     */
    public URI toURI() {
        return URI.create(this.toString());
    }

    @Override
    public String toString() {
        String url = this.protocol + "://";
        if (this.userInfo != null) url += this.userInfo + "@";
        url += this.host;
        if (this.port >= 0) url += ":" + this.port;
        if (this.path != null) url += this.path;
        if (this.query != null) url += "?" + this.query;
        if (this.reference != null) url += "#" + this.reference;
        return url;
    }

    public class QueryWrapper {
        private final Map<String, String> queries = new HashMap<>();

        private QueryWrapper() {
            String query = URLWrapper.this.getQuery();
            if (query != null) {
                for (String queryPart : query.split("&")) {
                    String[] split = queryPart.split("=", 2);
                    if (split.length == 2) {
                        this.queries.put(URLCoder.decode(split[0]), URLCoder.decode(split[1]));
                    } else {
                        this.queries.put(URLCoder.decode(split[0]), "");
                    }
                }
            }
        }

        /**
         * @return A map of all query parameters
         */
        public Map<String, String> getQueries() {
            return Collections.unmodifiableMap(this.queries);
        }

        /**
         * Get a query parameter by its key.
         *
         * @param key The key of the query parameter
         * @return The value of the query parameter or null if it does not exist
         */
        public Optional<String> getQuery(@Nonnull final String key) {
            return Optional.ofNullable(this.queries.get(key));
        }

        /**
         * Set a query parameter.
         *
         * @param key   The key of the query parameter
         * @param value The value of the query parameter
         * @return The URLWrapper
         */
        public QueryWrapper setQuery(@Nonnull final String key, @Nonnull final String value) {
            this.queries.put(key, value);
            return this;
        }

        /**
         * Add multiple query parameters.
         *
         * @param queries The query parameters to add
         * @return The URLWrapper
         */
        public QueryWrapper addQueries(@Nonnull final Map<String, String> queries) {
            this.queries.putAll(queries);
            return this;
        }

        /**
         * Remove a query parameter.
         *
         * @param key The key of the query parameter
         * @return The URLWrapper
         */
        public QueryWrapper removeQuery(final String key) {
            this.queries.remove(key);
            return this;
        }

        /**
         * Check if a query parameter exists.
         *
         * @param key The key of the query parameter
         * @return True if the query parameter exists
         */
        public boolean hasQuery(final String key) {
            return this.queries.containsKey(key);
        }

        /**
         * Apply the changes to the URL.
         *
         * @return The URLWrapper
         */
        public URLWrapper apply() {
            StringBuilder query = new StringBuilder();
            for (Map.Entry<String, String> entry : this.queries.entrySet()) {
                query.append(URLCoder.encode(entry.getKey())).append("=").append(URLCoder.encode(entry.getValue())).append("&");
            }
            if (query.length() > 0) query.deleteCharAt(query.length() - 1);
            URLWrapper.this.setQuery(query.toString());
            return URLWrapper.this;
        }

        /**
         * Discard the changes to the URL.
         *
         * @return The URLWrapper
         */
        public URLWrapper discard() {
            return URLWrapper.this;
        }
    }

}
