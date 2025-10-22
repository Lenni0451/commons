package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.executor.ExecutorType;

import java.util.Arrays;

public class HttpClientSource {

    public static final String DATA_SOURCE = "net.lenni0451.commons.httpclient.HttpClientSource#make";

    public static HttpClient[] make() {
        return Arrays.stream(ExecutorType.values())
                .filter(executorType -> executorType != ExecutorType.AUTO)
                .filter(ExecutorType::isAvailable)
                .map(HttpClient::new)
                .toArray(HttpClient[]::new);
    }

}
