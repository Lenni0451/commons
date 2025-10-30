package net.lenni0451.commons.httpclient.retry;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.retry.handlers.RetryAfterRetryHandler;

import javax.annotation.Nonnull;

@Getter
@Setter
@Accessors(chain = true)
public class RetryConfig {

    /**
     * The maximum number of retries for connection errors.<br>
     * If the connection times out on read, it will also count as a connection error.<br>
     * Default is 0 (no retries).
     */
    private int maxConnectRetries;
    /**
     * The maximum number of retries indicated by the server (e.g. via the {@link HttpHeaders#RETRY_AFTER} header).<br>
     * Default is 0 (no retries).
     */
    private int maxResponseRetries;
    /**
     * The handler to determine if a request should be retried based on the response.<br>
     * Default is {@link RetryAfterRetryHandler} (use the {@link HttpHeaders#RETRY_AFTER} header).
     */
    @Nonnull
    private RetryHandler retryHandler;

    public RetryConfig() {
        this(0, 0);
    }

    public RetryConfig(final int maxConnectRetries, final int maxResponseRetries) {
        this(maxConnectRetries, maxResponseRetries, new RetryAfterRetryHandler());
    }

    public RetryConfig(final int maxConnectRetries, final int maxResponseRetries, @Nonnull final RetryHandler retryHandler) {
        this.maxConnectRetries = maxConnectRetries;
        this.maxResponseRetries = maxResponseRetries;
        this.retryHandler = retryHandler;
    }

}
