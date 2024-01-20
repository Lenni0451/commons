package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.constants.RequestMethods;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;

public class PostRequest extends HttpContentRequest {

    public PostRequest(@Nonnull final String url) throws MalformedURLException {
        super(RequestMethods.POST, url);
    }

    public PostRequest(@Nonnull final URL url) {
        super(RequestMethods.POST, url);
    }

}
