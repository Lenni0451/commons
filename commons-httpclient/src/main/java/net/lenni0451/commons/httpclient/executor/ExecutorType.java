package net.lenni0451.commons.httpclient.executor;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.HttpClient;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.stream.Stream;

public enum ExecutorType {

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    AUTO(
            "java.net.HttpURLConnection",
            "net.lenni0451.commons.httpclient.executor.URLConnectionExecutor"
    ),
    /**
     * Use the default URLConnection executor.
     *
     * @see net.lenni0451.commons.httpclient.executor.URLConnectionExecutor
     */
    URL_CONNECTION(
            "java.net.HttpURLConnection",
            "net.lenni0451.commons.httpclient.executor.URLConnectionExecutor"
    ),
    /**
     * Use the new HttpClient executor which was added in Java 11.<br>
     * This implementation is faster than the URLConnection executor and supports HTTP/2.
     *
     * @see net.lenni0451.commons.httpclient.executor.HttpClientExecutor
     */
    HTTP_CLIENT(
            "java.net.http.HttpClient",
            "net.lenni0451.commons.httpclient.executor.HttpClientExecutor"
    ),
    /**
     * Use the reactor-netty executor.<br>
     * reactor-netty needs to be added as a dependency to your project.
     *
     * @see net.lenni0451.commons.httpclient.executor.extra.ReactorNettyExecutor
     */
    REACTOR_NETTY(
            "reactor.netty.http.client.HttpClient",
            "net.lenni0451.commons.httpclient.executor.extra.ReactorNettyExecutor"
    ),
    ;

    public static final ExecutorType DEFAULT = Stream.of(ExecutorType.values()).filter(type -> !type.equals(AUTO)).filter(ExecutorType::isAvailable).findFirst()
            .orElseThrow(() -> new IllegalStateException("No available RequestExecutor found! Please add a supported HTTP client dependency to your project."));


    @Nullable
    private final Constructor<?> constructor;

    ExecutorType(final String dependency, final String executor) {
        this.constructor = this.getConstructor(dependency, executor);
    }

    public final boolean isAvailable() {
        return this.constructor != null;
    }

    @SneakyThrows
    public final RequestExecutor makeExecutor(final HttpClient client) {
        return (RequestExecutor) this.constructor.newInstance(client);
    }

    @Nullable
    private Constructor<?> getConstructor(final String dependency, final String executor) {
        try {
            Class.forName(dependency); //Check if dependency is available
            Class<?> executorClass = Class.forName(executor);
            return executorClass.getDeclaredConstructor(HttpClient.class);
        } catch (Throwable ignored) {
        }
        return null;
    }

}
