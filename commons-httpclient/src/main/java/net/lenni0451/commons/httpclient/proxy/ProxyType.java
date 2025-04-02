package net.lenni0451.commons.httpclient.proxy;

import java.net.Proxy;

public enum ProxyType {

    /**
     * By default, basic authentication is deactivated for HTTP proxies because it is insecure.<br>
     * If you need it, you can enable it by setting the {@code jdk.http.auth.tunneling.disabledSchemes} system property to an empty string.
     */
    HTTP,
    SOCKS4,
    SOCKS5;

    /**
     * Get the {@link ProxyType} from a {@link Proxy.Type}.<br>
     * If the type is {@link Proxy.Type#DIRECT} an IllegalArgumentException will be thrown.<br>
     * If the type is {@link Proxy.Type#SOCKS} {@link ProxyType#SOCKS5} will be returned.
     *
     * @param type The type to convert
     * @return The converted type
     */
    public static ProxyType from(final Proxy.Type type) {
        switch (type) {
            case HTTP:
                return HTTP;
            case SOCKS:
                return SOCKS5;
            default:
                throw new IllegalArgumentException("Unknown proxy type: " + type.name());
        }
    }

}
