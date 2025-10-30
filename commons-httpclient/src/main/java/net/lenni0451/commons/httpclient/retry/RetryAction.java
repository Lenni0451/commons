package net.lenni0451.commons.httpclient.retry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetryAction {

    public static final RetryAction NO_RETRY = new RetryAction(false, 0);

    public static RetryAction waitFor(final long millis) {
        return waitFor(millis, TimeUnit.MILLISECONDS);
    }

    public static RetryAction waitFor(final long time, final TimeUnit timeUnit) {
        return new RetryAction(true, timeUnit.toMillis(time));
    }


    private final boolean shouldRetry;
    private final long waitMillis;

    public void sleep() throws InterruptedException {
        Thread.sleep(this.waitMillis);
    }

}
