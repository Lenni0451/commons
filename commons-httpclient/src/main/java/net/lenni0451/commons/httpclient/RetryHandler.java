package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.constants.HttpHeaders;

public class RetryHandler {

    private int maxConnectRetries = 0;
    private int maxHeaderRetries = 0;

    public RetryHandler() {
    }

    public RetryHandler(final int maxConnectRetries, final int maxHeaderRetries) {
        this.maxConnectRetries = maxConnectRetries;
        this.maxHeaderRetries = maxHeaderRetries;
    }

    /**
     * Get the maximum amount of connect retries.<br>
     * A connect attempt is counted when the connection times out.
     *
     * @return The maximum amount of connect retries
     */
    public int getMaxConnectRetries() {
        return this.maxConnectRetries;
    }

    /**
     * Set the maximum amount of connect retries.
     *
     * @param maxConnectRetries The maximum amount of connect retries
     * @return This instance for chaining
     */
    public RetryHandler setMaxConnectRetries(final int maxConnectRetries) {
        if (maxConnectRetries < 0) throw new IllegalArgumentException("maxConnectRetries must be >= 0");
        this.maxConnectRetries = maxConnectRetries;
        return this;
    }

    /**
     * Get the maximum amount of header retries.<br>
     * A header retry is counted when the {@link HttpHeaders#RETRY_AFTER} header is present.
     *
     * @return The maximum amount of header retries
     */
    public int getMaxHeaderRetries() {
        return this.maxHeaderRetries;
    }

    /**
     * Set the maximum amount of header retries.
     *
     * @param maxHeaderRetries The maximum amount of header retries
     * @return This instance for chaining
     */
    public RetryHandler setMaxHeaderRetries(final int maxHeaderRetries) {
        if (maxHeaderRetries < 0) throw new IllegalArgumentException("maxHeaderRetries must be >= 0");
        this.maxHeaderRetries = maxHeaderRetries;
        return this;
    }

}
