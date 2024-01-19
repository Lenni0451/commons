package net.lenni0451.commons.httpclient.exceptions;

import net.lenni0451.commons.httpclient.HttpResponse;

import javax.annotation.Nonnull;

public class RetryExceededException extends HttpRequestException {

    public RetryExceededException(@Nonnull final HttpResponse response) {
        super(response, "Maximum retry count exceeded");
    }

    public RetryExceededException(@Nonnull final HttpResponse response, @Nonnull final String message) {
        super(response, message);
    }

}
