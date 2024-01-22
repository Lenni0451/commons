package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import java.io.IOException;

public abstract class RequestExecutor {

    @Nonnull
    protected final HttpClient client;

    public RequestExecutor(@Nonnull final HttpClient client) {
        this.client = client;
    }

    @Nonnull
    public abstract HttpResponse execute(@Nonnull final HttpRequest request) throws IOException, InterruptedException;

}
