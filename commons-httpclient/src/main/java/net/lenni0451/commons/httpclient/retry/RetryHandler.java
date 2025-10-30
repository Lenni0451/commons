package net.lenni0451.commons.httpclient.retry;

import net.lenni0451.commons.httpclient.HttpResponse;

@FunctionalInterface
public interface RetryHandler {

    RetryAction shouldRetry(final HttpResponse response);

}
