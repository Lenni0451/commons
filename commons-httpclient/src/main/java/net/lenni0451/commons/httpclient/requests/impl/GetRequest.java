package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.constants.RequestMethods;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;

public class GetRequest extends HttpRequest {

    public GetRequest(final String url) throws MalformedURLException {
        super(RequestMethods.GET, url);
    }

    public GetRequest(final URL url) {
        super(RequestMethods.GET, url);
    }

}
