package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.constants.RequestMethods;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;

public class HeadRequest extends HttpRequest {

    public HeadRequest(final String url) throws MalformedURLException {
        super(RequestMethods.HEAD, url);
    }

    public HeadRequest(final URL url) {
        super(RequestMethods.HEAD, url);
    }

}
