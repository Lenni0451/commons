package net.lenni0451.commons.httpclient.handler;

import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.exceptions.HttpRequestException;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ThrowingResponseHandler implements HttpResponseHandler<HttpResponse> {

    @Override
    public HttpResponse handle(@Nonnull HttpResponse response) throws IOException {
        if (response.getStatusCode() >= 300) throw new HttpRequestException(response);
        return response;
    }

}
