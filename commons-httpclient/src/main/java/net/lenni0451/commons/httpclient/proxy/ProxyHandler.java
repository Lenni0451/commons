package net.lenni0451.commons.httpclient.proxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class ProxyHandler {

    private Proxy proxy;
    private ProxyType type;
    private String username;
    private String password;

    public ProxyHandler(@Nonnull final Proxy proxy) {
        this(proxy, null, null);
    }

    public ProxyHandler(@Nonnull final Proxy proxy, @Nullable final String username, @Nullable final String password) {
        this.proxy = proxy;
        this.username = username;
        this.password = password;
    }

    public ProxyHandler(@Nonnull final ProxyType type, @Nonnull final String host, final int port) {
        this(type, host, port, null, null);
    }

    public ProxyHandler(@Nonnull final ProxyType type, @Nonnull final String host, final int port, @Nullable final String username, @Nullable final String password) {
        this.proxy = this.make(type, host, port);
        this.type = type;
        this.username = username;
        this.password = password;
    }

    /**
     * @return The proxy
     */
    @Nullable
    public Proxy getProxy() {
        return this.proxy;
    }

    /**
     * Set the proxy to use.<br>
     * If the proxy is {@link Proxy.Type#DIRECT} the proxy will be set to {@code null}.
     *
     * @param proxy The proxy to use
     */
    public void setProxy(@Nonnull final Proxy proxy) {
        if (Proxy.Type.DIRECT.equals(proxy.type())) {
            this.proxy = null;
            this.type = null;
        } else {
            this.proxy = proxy;
            this.type = ProxyType.from(proxy.type());
        }
    }

    /**
     * Set the proxy to use.
     *
     * @param type The type of the proxy
     * @param host The host of the proxy
     * @param port The port of the proxy
     */
    public void setProxy(@Nonnull final ProxyType type, @Nonnull final String host, final int port) {
        this.proxy = this.make(type, host, port);
        this.type = type;
    }

    /**
     * @return The type of the proxy
     */
    @Nullable
    public ProxyType getProxyType() {
        return this.type;
    }

    /**
     * @return The username for the proxy
     */
    @Nullable
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the username for the proxy.
     *
     * @param username The username for the proxy
     */
    public void setUsername(@Nullable final String username) {
        this.username = username;
    }

    /**
     * @return The password for the proxy
     */
    @Nullable
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the password for the proxy.
     *
     * @param password The password for the proxy
     */
    public void setPassword(@Nullable final String password) {
        this.password = password;
    }

    /**
     * @return The SingleProxySelector for this proxy
     */
    public SingleProxySelector getProxySelector() {
        return new SingleProxySelector(this.proxy, this.username, this.password);
    }

    /**
     * @return The SingleProxyAuthenticator for this proxy
     */
    public SingleProxyAuthenticator getProxyAuthenticator() {
        return new SingleProxyAuthenticator(this.username, this.password);
    }

    private Proxy make(final ProxyType type, final String host, final int port) {
        switch (type) {
            case HTTP:
                return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            case SOCKS4:
                try {
                    Class<?> clazz = Class.forName("sun.net.SocksProxy");
                    Method createMethod = clazz.getDeclaredMethod("create", SocketAddress.class, int.class);
                    return (Proxy) createMethod.invoke(null, new InetSocketAddress(host, port), 4);
                } catch (Throwable t) {
                    throw new UnsupportedOperationException("SOCKS4 proxy type is not supported", t);
                }
            case SOCKS5:
                return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
            default:
                throw new IllegalArgumentException("Unknown proxy type: " + type.name());
        }
    }


    public enum ProxyType {
        HTTP,
        SOCKS4,
        SOCKS5;

        /**
         * Get the {@link ProxyType} from a {@link Proxy.Type}.
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

}
