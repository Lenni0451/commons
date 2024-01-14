package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.MalformedURLException;
import java.net.URL;

@ParametersAreNonnullByDefault
public class GetRequest extends HttpRequest {

    private static final String METHOD = "GET";

    public GetRequest(final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public GetRequest(final URL url) {
        super(METHOD, url);
    }

}
