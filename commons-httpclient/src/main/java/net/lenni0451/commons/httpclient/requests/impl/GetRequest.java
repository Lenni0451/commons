package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRequest extends HttpRequest {

    private static final String METHOD = "GET";

    public GetRequest(@Nonnull final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public GetRequest(@Nonnull final URL url) {
        super(METHOD, url);
    }

}
