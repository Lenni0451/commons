package net.lenni0451.commons.httpclient.retry.handlers;

import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.retry.RetryAction;
import net.lenni0451.commons.httpclient.retry.RetryHandler;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class RetryAfterRetryHandler implements RetryHandler {

    @Override
    public RetryAction shouldRetry(HttpResponse response) {
        Optional<String> retryAfter = response.getFirstHeader(HttpHeaders.RETRY_AFTER);
        if (retryAfter.isPresent()) {
            Long delay = this.parseSecondsOrHttpDate(retryAfter.get());
            if (delay != null && delay > 0) {
                return RetryAction.waitFor(delay);
            }
        }
        return RetryAction.NO_RETRY;
    }

    @Nullable
    private Long parseSecondsOrHttpDate(final String value) {
        try {
            Instant date = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(value));
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
