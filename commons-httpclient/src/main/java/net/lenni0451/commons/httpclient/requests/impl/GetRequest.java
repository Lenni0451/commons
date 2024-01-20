package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.constants.RequestMethods;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRequest extends HttpRequest {

    public GetRequest(@Nonnull final String url) throws MalformedURLException {
        super(RequestMethods.GET, url);
    }

    public GetRequest(@Nonnull final URL url) {
        super(RequestMethods.GET, url);
    }

}
