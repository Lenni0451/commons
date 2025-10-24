package net.lenni0451.commons.httpclient.utils;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
     * Deprecated, use {@link #ofURL(String)} or {@link #ofURI(String)} instead.<br>
     * The original behavior was {@link #ofURL(String)}.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval //22.04.2026
    public static URLWrapper of(final String url) throws MalformedURLException {
        return ofURL(url);
    }

    /**
     * Create a new URLWrapper from a URL string.
     *
     * @param url The URL to wrap
     * @return The URLWrapper
     * @throws MalformedURLException If the URL is invalid
     */
    public static URLWrapper ofURL(final String url) throws MalformedURLException {
        return new URLWrapper(new URL(url));
    }

    /**
     * Create a new URLWrapper from a URI string.
     *
     * @param uri The URI to wrap
     * @return The URLWrapper
     * @throws IllegalArgumentException If the URI is invalid
     */
    public static URLWrapper ofURI(final String uri) throws IllegalArgumentException {
        return new URLWrapper(URI.create(uri));
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
    private String fragment;

    public URLWrapper() {
    }

    /**
     * Deprecated, use {@link #ofURL(String)} or {@link #ofURI(String)} instead.<br>
     * The original behavior was {@link #ofURL(String)}.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval //22.04.2026
    public URLWrapper(final String url) throws MalformedURLException {
        this(new URL(url));
    }

    public URLWrapper(final URL url) {
        this.protocol = url.getProtocol();
        this.host = this.emptyToNull(url.getHost());
        this.port = url.getPort();
        this.path = this.emptyToNull(url.getPath());
        this.query = this.emptyToNull(url.getQuery());
        this.userInfo = this.emptyToNull(url.getUserInfo());
        this.fragment = this.emptyToNull(url.getRef());
    }

    public URLWrapper(final URI uri) {
        this.protocol = this.emptyToNull(uri.getScheme());
        this.host = this.emptyToNull(uri.getHost());
        this.port = uri.getPort();
        this.path = this.emptyToNull(uri.getPath());
        this.query = this.emptyToNull(uri.getQuery());
        this.userInfo = this.emptyToNull(uri.getUserInfo());
        this.fragment = this.emptyToNull(uri.getFragment());
    }

    /**
     * @return If the protocol is set
     */
    public boolean hasProtocol() {
        return this.protocol != null;
    }

    /**
     * @return The protocol of the URL (e.g. {@code https})
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * @param defaultProtocol The default protocol to return if none is set
     * @return The protocol of the URL (e.g. {@code https})
     */
    public String getProtocolOr(final String defaultProtocol) {
        return this.protocol == null ? defaultProtocol : this.protocol;
    }

    /**
     * Set the protocol of the URL (e.g. {@code https}).
     *
     * @param protocol The new protocol
     * @return The URLWrapper
     */
    public URLWrapper setProtocol(final String protocol) {
        this.protocol = this.emptyToNull(protocol);
        return this;
    }

    /**
     * @return If the host is set
     */
    public boolean hasHost() {
        return this.host != null;
    }

    /**
     * @return The host of the URL (e.g. {@code www.example.com})
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @param defaultHost The default host to return if none is set
     * @return The host of the URL (e.g. {@code www.example.com})
     */
    public String getHostOr(final String defaultHost) {
        return this.host == null ? defaultHost : this.host;
    }

    /**
     * Set the host of the URL (e.g. {@code www.example.com}).
     *
     * @param host The new host
     * @return The URLWrapper
     */
    public URLWrapper setHost(final String host) {
        this.host = this.emptyToNull(host);
        return this;
    }

    /**
     * @return If the port is set
     */
    public boolean hasPort() {
        return this.port >= 0;
    }

    /**
     * @return The port of the URL (e.g. {@code 443})
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @param defaultPort The default port to return if none is set
     * @return The port of the URL (e.g. {@code 443})
     */
    public int getPortOr(final int defaultPort) {
        return this.port < 0 ? defaultPort : this.port;
    }

    /**
     * Set the port of the URL (e.g. {@code 443}).
     *
     * @param port The new port
     * @return The URLWrapper
     */
    public URLWrapper setPort(final int port) {
        if (port > 65535) {
            throw new IllegalArgumentException("Port must not be greater than 65535");
        }
        this.port = port;
        return this;
    }

    /**
     * @return If the path is set
     */
    public boolean hasPath() {
        return this.path != null;
    }

    /**
     * @return The path of the URL (e.g. {@code /search})
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @param defaultPath The default path to return if none is set
     * @return The path of the URL (e.g. {@code /search})
     */
    public String getPathOr(final String defaultPath) {
        return this.path == null ? defaultPath : this.path;
    }

    /**
     * Set the path of the URL (e.g. {@code /search}).
     *
     * @param path The new path
     * @return The URLWrapper
     */
    public URLWrapper setPath(final String path) {
        this.path = this.emptyToNull(path);
        return this;
    }

    /**
     * @return If the query is set
     */
    public boolean hasQuery() {
        return this.query != null;
    }

    /**
     * @return The query of the URL (e.g. {@code q=hello})
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * @param defaultQuery The default query to return if none is set
     * @return The query of the URL (e.g. {@code q=hello})
     */
    public String getQueryOr(final String defaultQuery) {
        return this.query;
    }

    /**
     * Set the query of the URL (e.g. {@code q=hello}).
     *
     * @param query The new query
     * @return The URLWrapper
     */
    public URLWrapper setQuery(final String query) {
        this.query = this.emptyToNull(query);
        return this;
    }

    /**
     * Get a wrapper for the query parameters.
     *
     * @return The query wrapper
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval //18.04.2026
    public QueryWrapper wrapQuery() {
        return new QueryWrapper();
    }

    /**
     * Get a wrapper for the query parameters.
     *
     * @return The query parameters wrapper
     */
    public QueryParametersWrapper wrapQueryParameters() {
        return new QueryParametersWrapper();
    }

    /**
     * @return If the user info is set
     */
    public boolean hasUserInfo() {
        return this.userInfo != null;
    }

    /**
     * @return The user info of the URL (e.g. {@code user:password})
     */
    public String getUserInfo() {
        return this.userInfo;
    }

    /**
     * @param defaultUserInfo The default user info to return if none is set
     * @return The user info of the URL (e.g. {@code user:password})
     */
    public String getUserInfoOr(final String defaultUserInfo) {
        return this.userInfo == null ? defaultUserInfo : this.userInfo;
    }

    /**
     * Set the user info of the URL (e.g. {@code user:password}).
     *
     * @param userInfo The new user info
     * @return The URLWrapper
     */
    public URLWrapper setUserInfo(final String userInfo) {
        this.userInfo = this.emptyToNull(userInfo);
        return this;
    }

    /**
     * @return If the fragment is set
     */
    public boolean hasFragment() {
        return this.fragment != null;
    }

    /**
     * @return The fragment of the URL (e.g. {@code fragment})
     */
    public String getFragment() {
        return this.fragment;
    }

    /**
     * @param defaultFragment The default fragment to return if none is set
     * @return The fragment of the URL (e.g. {@code fragment})
     */
    public String getFragmentOr(final String defaultFragment) {
        return this.fragment == null ? defaultFragment : this.fragment;
    }

    /**
     * Set the fragment of the URL (e.g. {@code fragment}).
     *
     * @param fragment The new fragment
     * @return The URLWrapper
     */
    public URLWrapper setFragment(final String fragment) {
        this.fragment = this.emptyToNull(fragment);
        return this;
    }

    /**
     * Deprecated, use {@link #getFragment()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval //23.04.2026
    public String getReference() {
        return this.getFragment();
    }

    /**
     * Deprecated, use {@link #setFragment(String)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval //23.04.2026
    public URLWrapper setReference(final String reference) {
        return this.setFragment(reference);
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
        StringBuilder url = new StringBuilder();
        if (this.protocol != null) url.append(this.protocol).append("://");
        if (this.userInfo != null) url.append(this.userInfo).append("@");
        if (this.host != null) url.append(this.host);
        if (this.port >= 0) url.append(":").append(this.port);
        if (this.path != null) {
            if (!this.path.startsWith("/")) {
                url.append("/");
            }
            url.append(this.path);
        }
        if (this.query != null) url.append("?").append(this.query);
        if (this.fragment != null) url.append("#").append(this.fragment);
        return url.toString();
    }

    private String emptyToNull(final String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }


    @Deprecated
    @ApiStatus.ScheduledForRemoval //18.04.2026
    public class QueryWrapper {
        private final Map<String, String> queries = new HashMap<>();

        private QueryWrapper() {
            String query = URLWrapper.this.getQuery();
            if (query != null && !query.isEmpty()) {
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
        public Optional<String> getQuery(final String key) {
            return Optional.ofNullable(this.queries.get(key));
        }

        /**
         * Set a query parameter.
         *
         * @param key   The key of the query parameter
         * @param value The value of the query parameter
         * @return The URLWrapper
         */
        public QueryWrapper setQuery(final String key, final String value) {
            this.queries.put(key, value);
            return this;
        }

        /**
         * Add multiple query parameters.
         *
         * @param queries The query parameters to add
         * @return The URLWrapper
         */
        public QueryWrapper addQueries(final Map<String, String> queries) {
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

    public class QueryParametersWrapper {
        @Getter
        private final List<Parameter> parameters = new ArrayList<>();

        public QueryParametersWrapper() {
            String query = URLWrapper.this.getQuery();
            if (query != null && !query.isEmpty()) {
                for (String queryPart : query.split("&")) {
                    String[] split = queryPart.split("=", 2);
                    if (split.length == 2) {
                        this.parameters.add(new Parameter(URLCoder.decode(split[0]), URLCoder.decode(split[1])));
                    } else {
                        this.parameters.add(new Parameter(URLCoder.decode(split[0])));
                    }
                }
            }
        }

        /**
         * Get all values for a specific key.<br>
         * {@code null} values are filtered out.
         *
         * @param key The key of the parameter
         * @return A list of all values for the key
         */
        public List<String> getValues(final String key) {
            return this.parameters.stream()
                    .filter(param -> param.getKey().equals(key))
                    .map(Parameter::getValue)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        /**
         * Get the first value for a specific key.<br>
         * {@code null} values are filtered out.
         *
         * @param key The key of the parameter
         * @return An optional containing the first value for the key
         */
        public Optional<String> getFirstValue(final String key) {
            return this.parameters.stream()
                    .filter(param -> param.getKey().equals(key))
                    .map(Parameter::getValue)
                    .filter(Objects::nonNull)
                    .findFirst();
        }

        /**
         * Check if a parameter with the given key exists.
         *
         * @param key The key of the parameter
         * @return If a parameter with the key exists
         */
        public boolean hasParameter(final String key) {
            return this.parameters.stream().anyMatch(param -> param.getKey().equals(key));
        }

        /**
         * Add a parameter.
         *
         * @param key   The key of the parameter
         * @param value The value of the parameter
         * @return This instance for chaining
         */
        public QueryParametersWrapper addParameter(final String key, @Nullable final String value) {
            this.parameters.add(new Parameter(key, value));
            return this;
        }

        /**
         * Add a parameter.
         *
         * @param parameter The parameter to add
         * @return This instance for chaining
         */
        public QueryParametersWrapper addParameter(final Parameter parameter) {
            this.parameters.add(parameter);
            return this;
        }

        /**
         * Add multiple parameters.
         *
         * @param parameters The parameters to add
         * @return This instance for chaining
         */
        public QueryParametersWrapper addParameters(final Iterable<Parameter> parameters) {
            parameters.forEach(this.parameters::add);
            return this;
        }

        /**
         * Add multiple parameters.
         *
         * @param parameters The parameters to add
         * @return This instance for chaining
         */
        public QueryParametersWrapper addParameters(final Parameter[] parameters) {
            Collections.addAll(this.parameters, parameters);
            return this;
        }

        /**
         * Add multiple parameters.
         *
         * @param parameters The parameters to add
         * @return This instance for chaining
         */
        public QueryParametersWrapper addParameters(final Map<String, String> parameters) {
            parameters.forEach((key, value) -> this.parameters.add(new Parameter(key, value)));
            return this;
        }

        /**
         * Set a parameter. Existing parameters with the same key are removed.
         *
         * @param key   The key of the parameter
         * @param value The value of the parameter
         * @return This instance for chaining
         */
        public QueryParametersWrapper setParameter(final String key, @Nullable final String value) {
            this.parameters.removeIf(param -> param.getKey().equals(key));
            this.parameters.add(new Parameter(key, value));
            return this;
        }

        /**
         * Set multiple parameters. Existing parameters with the same keys are removed.
         *
         * @param parameters The parameters to set
         * @return This instance for chaining
         */
        public QueryParametersWrapper setParameters(final Map<String, String> parameters) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                this.setParameter(entry.getKey(), entry.getValue());
            }
            return this;
        }

        /**
         * Remove all parameters with the given key.
         *
         * @param key The key of the parameters to remove
         * @return This instance for chaining
         */
        public QueryParametersWrapper removeParameters(final String key) {
            this.parameters.removeIf(param -> param.getKey().equals(key));
            return this;
        }

        /**
         * Clear all parameters.
         *
         * @return This instance for chaining
         */
        public QueryParametersWrapper clearParameters() {
            this.parameters.clear();
            return this;
        }

        /**
         * Apply the changes to the URL.
         *
         * @return The URLWrapper
         */
        public URLWrapper apply() {
            StringBuilder query = new StringBuilder();
            for (Parameter parameter : this.parameters) {
                query.append(URLCoder.encode(parameter.getKey()));
                if (parameter.getValue() != null) {
                    query.append("=").append(URLCoder.encode(parameter.getValue()));
                }
                query.append("&");
            }
            if (query.length() > 0) query.deleteCharAt(query.length() - 1);
            URLWrapper.this.setQuery(query.toString());
            return URLWrapper.this;
        }
    }

    @Getter
    @Setter
    public static class Parameter {
        private String key;
        @Nullable
        private String value;

        public Parameter(final String key) {
            this.key = key;
        }

        public Parameter(final String key, @Nullable final String value) {
            this.key = key;
            this.value = value;
        }
    }

}
