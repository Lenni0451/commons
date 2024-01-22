package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.constants.RequestMethods;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;

public class DeleteRequest extends HttpRequest {

    public DeleteRequest(final String url) throws MalformedURLException {
        super(RequestMethods.DELETE, url);
    }

    public DeleteRequest(final URL url) {
        super(RequestMethods.DELETE, url);
    }

}
