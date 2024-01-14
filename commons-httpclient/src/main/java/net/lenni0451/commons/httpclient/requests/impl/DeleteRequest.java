package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.MalformedURLException;
import java.net.URL;

@ParametersAreNonnullByDefault
public class DeleteRequest extends HttpRequest {

    private static final String METHOD = "DELETE";

    public DeleteRequest(final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public DeleteRequest(final URL url) {
        super(METHOD, url);
    }

}
