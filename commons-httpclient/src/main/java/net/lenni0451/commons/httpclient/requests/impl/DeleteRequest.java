package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;

public class DeleteRequest extends HttpRequest {

    private static final String METHOD = "DELETE";

    public DeleteRequest(@Nonnull final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public DeleteRequest(@Nonnull final URL url) {
        super(METHOD, url);
    }

}
