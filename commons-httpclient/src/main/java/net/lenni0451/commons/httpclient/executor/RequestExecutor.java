package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import java.io.IOException;

public abstract class RequestExecutor {

    public static RequestExecutor create(final HttpClient client) {
        return new URLConnectionExecutor(client);
    }


    protected final HttpClient client;

    protected RequestExecutor(final HttpClient client) {
        this.client = client;
    }

    public abstract HttpResponse execute(@Nonnull final HttpRequest request) throws IOException;

}
