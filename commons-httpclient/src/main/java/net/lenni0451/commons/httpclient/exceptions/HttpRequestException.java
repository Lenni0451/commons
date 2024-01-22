package net.lenni0451.commons.httpclient.exceptions;

import net.lenni0451.commons.httpclient.HttpResponse;

import java.io.IOException;

public class HttpRequestException extends IOException {

    private final HttpResponse response;

    public HttpRequestException(final HttpResponse response) {
        this(response, "Request failed: " + response.getStatusCode() + " " + response.getStatusMessage());
    }

    public HttpRequestException(final HttpResponse response, final String message) {
        super(message);
        this.response = response;
    }

    /**
     * @return The response of the request
     */
    public HttpResponse getResponse() {
        return this.response;
    }

}
