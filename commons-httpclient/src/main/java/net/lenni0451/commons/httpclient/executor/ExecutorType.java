package net.lenni0451.commons.httpclient.executor;

import net.lenni0451.commons.httpclient.HttpClient;

import java.lang.reflect.Constructor;

public enum ExecutorType {

    /**
     * Automatically choose the best executor type for the current Java version.<br>
     * This will try to use the other types in reverse order and use the first one that is available.<br>
     * e.g. if {@link #HTTP_CLIENT} is available it will be used, otherwise {@link #URL_CONNECTION} will be used.
     */
    AUTO {
        @Override
        public RequestExecutor initExecutor(HttpClient client) {
            for (int i = values().length - 1; i >= 0; i--) {
                ExecutorType type = values()[i];
                if (AUTO.equals(type)) continue;
                if (type.isAvailable()) {
                    RequestExecutor executor = type.makeExecutor(client);
                    if (executor != null) return executor;
                }
            }
            throw new IllegalStateException("Failed to find a suitable executor. This should never happen. Please report this to the developer.");
        }
    },
    /**
     * Use the default URLConnection executor.<br>
     * This is the default executor for Java 10 and below.
     *
     * @see net.lenni0451.commons.httpclient.executor.URLConnectionExecutor
     */
    URL_CONNECTION {
        @Override
        public RequestExecutor initExecutor(HttpClient client) {
            return new URLConnectionExecutor(client);
        }
    },
    /**
     * Use the reactor-netty executor.<br>
     * reactor-netty needs to be added as a dependency to your project.
     *
     * @see net.lenni0451.commons.httpclient.executor.extra.ReactorNettyExecutor
     */
    REACTOR_NETTY {
        private Constructor<?> constructor;

        @Override
        protected void init() throws Throwable {
            Class.forName("reactor.netty.http.client.HttpClient");
            Class<?> executorClass = Class.forName("net.lenni0451.commons.httpclient.executor.extra.ReactorNettyExecutor");
            this.constructor = executorClass.getDeclaredConstructor(HttpClient.class);
        }

        @Override
        protected RequestExecutor initExecutor(HttpClient client) throws Throwable {
            return (RequestExecutor) this.constructor.newInstance(client);
        }
    },
    /**
     * Use the new HttpClient executor which was added in Java 11.<br>
     * This implementation is faster than the URLConnection executor and supports HTTP/2.
     *
     * @see net.lenni0451.commons.httpclient.executor.HttpClientExecutor
     */
    HTTP_CLIENT {
        private Constructor<?> constructor;

        @Override
        protected void init() throws Throwable {
            Class.forName("java.net.http.HttpClient");
            Class<?> executorClass = Class.forName("net.lenni0451.commons.httpclient.executor.HttpClientExecutor");
            this.constructor = executorClass.getDeclaredConstructor(HttpClient.class);
        }

        @Override
        protected RequestExecutor initExecutor(HttpClient client) throws Throwable {
            return (RequestExecutor) this.constructor.newInstance(client);
        }
    },
    ;


    private boolean available;

    ExecutorType() {
        try {
            this.init();
            this.available = true;
        } catch (Throwable ignored) {
            this.available = false;
        }
    }

    public final boolean isAvailable() {
        return this.available;
    }

    public final RequestExecutor makeExecutor(final HttpClient client) {
        try {
            return this.initExecutor(client);
        } catch (Throwable ignored) {
        }
        return null;
    }

    protected void init() throws Throwable {
    }

    protected abstract RequestExecutor initExecutor(final HttpClient client) throws Throwable;

}
