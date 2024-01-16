package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.model.HttpHeader;

import java.util.*;
import java.util.stream.Collectors;

public abstract class HeaderStore<T extends HeaderStore<T>> {

    private final Map<String, List<String>> headers = new HashMap<>();

    public HeaderStore() {
    }

    public HeaderStore(final Map<String, List<String>> headers) {
        headers.forEach((k, v) -> this.headers.put(k.toLowerCase(Locale.ROOT), new ArrayList<>(v)));
    }

    /**
     * @return The headers
     */
    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(
                this.headers
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> new ArrayList<>(e.getValue())
                        ))
        );
    }

    /**
     * Get a header.
     *
     * @param name The name of the header
     * @return The header or null if not set
     */
    public List<String> getHeader(final String name) {
        return this.headers.get(name.toLowerCase());
    }

    /**
     * Get the first header with the given name.
     *
     * @param name The name of the header
     * @return The response header
     */
    public Optional<String> getFirstHeader(final String name) {
        List<String> values = this.headers.get(name.toLowerCase(Locale.ROOT));
        if (values == null || values.isEmpty()) return Optional.empty();
        return Optional.of(values.get(0));
    }

    /**
     * Get the last header with the given name.
     *
     * @param name The name of the header
     * @return The response header
     */
    public Optional<String> getLastHeader(final String name) {
        List<String> values = this.headers.get(name.toLowerCase(Locale.ROOT));
        if (values == null || values.isEmpty()) return Optional.empty();
        return Optional.of(values.get(values.size() - 1));
    }

    /**
     * Append a header. If the header already exists it will be appended to the list.
     *
     * @param name  The name of the header
     * @param value The value of the header
     * @return This instance for chaining
     */
    public T appendHeader(final String name, final String value) {
        this.headers.computeIfAbsent(name.toLowerCase(Locale.ROOT), n -> new ArrayList<>()).add(value);
        return (T) this;
    }

    /**
     * Append a header. If the header already exists it will be appended to the list.
     *
     * @param headers The headers to add
     * @return This instance for chaining
     */
    public T appendHeader(final HttpHeader... headers) {
        for (HttpHeader header : headers) this.appendHeader(header.getName(), header.getValue());
        return (T) this;
    }

    /**
     * Append a header. If the header already exists it will be appended to the list.
     *
     * @param headers The headers to add
     * @return This instance for chaining
     */
    public T appendHeader(final Collection<HttpHeader> headers) {
        for (HttpHeader header : headers) this.appendHeader(header.getName(), header.getValue());
        return (T) this;
    }

    /**
     * Set a header. If the header already exists it will be overwritten.
     *
     * @param name  The name of the header
     * @param value The value of the header
     * @return This instance for chaining
     */
    public T setHeader(final String name, final String value) {
        List<String> values = new ArrayList<>();
        values.add(value);
        this.headers.put(name.toLowerCase(), values);
        return (T) this;
    }

    /**
     * Set a header. If the header already exists it will be overwritten.
     *
     * @param headers The headers to set
     * @return This instance for chaining
     */
    public T setHeader(final HttpHeader... headers) {
        for (HttpHeader h : headers) {
            this.setHeader(h.getName(), h.getValue());
        }
        return (T) this;
    }

    /**
     * Set a header. If the header already exists it will be overwritten.
     *
     * @param headers The headers to set
     * @return This instance for chaining
     */
    public T setHeader(final Collection<HttpHeader> headers) {
        for (HttpHeader h : headers) {
            this.setHeader(h.getName(), h.getValue());
        }
        return (T) this;
    }

    /**
     * Remove a header.
     *
     * @param name The name of the header
     * @return This instance for chaining
     */
    public T removeHeader(final String name) {
        this.headers.remove(name.toLowerCase());
        return (T) this;
    }

    /**
     * Clear all headers.
     *
     * @return This instance for chaining
     */
    public T clearHeaders() {
        this.headers.clear();
        return (T) this;
    }

    /**
     * Check if a header is set.
     *
     * @param name The name of the header
     * @return Whether the header is set
     */
    public boolean hasHeader(final String name) {
        return this.headers.containsKey(name.toLowerCase());
    }

    /**
     * Check if a header is set.
     *
     * @param name  The name of the header
     * @param value The value of the header
     * @return Whether the header is set
     */
    public boolean hasHeader(final String name, final String value) {
        return this.headers.get(name.toLowerCase()).contains(value);
    }

    /**
     * Check if a header is set.
     *
     * @param header The header to check
     * @return Whether the header is set
     */
    public boolean hasHeader(final HttpHeader header) {
        return this.hasHeader(header.getName(), header.getValue());
    }

}
