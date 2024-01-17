package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpContentRequest;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;

public class PostRequest extends HttpContentRequest {

    private static final String METHOD = "POST";

    public PostRequest(@Nonnull final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public PostRequest(@Nonnull final URL url) {
        super(METHOD, url);
    }

}
