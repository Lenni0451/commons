package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import java.io.IOException;

public abstract class RequestExecutor {

    protected final HttpClient client;

    public RequestExecutor(final HttpClient client) {
        this.client = client;
    }

    public abstract HttpResponse execute(final HttpRequest request) throws IOException, InterruptedException;

}
