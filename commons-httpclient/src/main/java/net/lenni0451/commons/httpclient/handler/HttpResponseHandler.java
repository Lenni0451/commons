package net.lenni0451.commons.httpclient.handler;

import net.lenni0451.commons.httpclient.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface HttpResponseHandler<R> {

    /**
     * @return A handler that returns the response
     */
    static HttpResponseHandler<HttpResponse> identity() {
        return response -> response;
    }


    /**
     * Handle the response and return the result.
     *
     * @param response The response to handle
     * @return The result
     * @throws IOException If an I/O error occurs
     */
    R handle(final HttpResponse response) throws IOException;

}
