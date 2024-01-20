package net.lenni0451.commons.httpclient.executor;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.requests.HttpRequest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public abstract class RequestExecutor {

    private static final Constructor<?> HTTP_CLIENT_EXECUTOR_CONSTRUCTOR = ((Supplier<Constructor<?>>) () -> {
        try {
            Class.forName("java.net.http.HttpClient");
            return Class.forName("net.lenni0451.commons.httpclient.executor.HttpClientExecutor").getDeclaredConstructor(HttpClient.class);
        } catch (Throwable ignored) {
        }
        return null;
    }).get();

    @SneakyThrows
    public static RequestExecutor create(final HttpClient client) {
        if (HTTP_CLIENT_EXECUTOR_CONSTRUCTOR != null) {
            return (RequestExecutor) HTTP_CLIENT_EXECUTOR_CONSTRUCTOR.newInstance(client);
        } else {
            return new URLConnectionExecutor(client);
        }
    }


    protected final HttpClient client;

    protected RequestExecutor(final HttpClient client) {
        this.client = client;
    }

    public abstract HttpResponse execute(@Nonnull final HttpRequest request) throws IOException, InterruptedException;

}
