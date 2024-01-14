package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpContentRequest;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.MalformedURLException;
import java.net.URL;

@ParametersAreNonnullByDefault
public class PutRequest extends HttpContentRequest {

    private static final String METHOD = "PUT";

    public PutRequest(final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public PutRequest(final URL url) {
        super(METHOD, url);
    }

}
