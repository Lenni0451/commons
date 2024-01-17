package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpContentRequest;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;

public class PutRequest extends HttpContentRequest {

    private static final String METHOD = "PUT";

    public PutRequest(@Nonnull final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public PutRequest(@Nonnull final URL url) {
        super(METHOD, url);
    }

}
