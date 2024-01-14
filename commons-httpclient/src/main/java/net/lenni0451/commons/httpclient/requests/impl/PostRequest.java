package net.lenni0451.commons.httpclient.requests.impl;

import net.lenni0451.commons.httpclient.requests.HttpContentRequest;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.MalformedURLException;
import java.net.URL;

@ParametersAreNonnullByDefault
public class PostRequest extends HttpContentRequest {

    private static final String METHOD = "POST";

    public PostRequest(final String url) throws MalformedURLException {
        super(METHOD, url);
    }

    public PostRequest(final URL url) {
        super(METHOD, url);
    }

}
