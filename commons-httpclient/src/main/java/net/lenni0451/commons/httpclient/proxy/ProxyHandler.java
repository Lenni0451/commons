package net.lenni0451.commons.httpclient.proxy;

import sun.net.SocksProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyHandler {

    private Proxy proxy;
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
        switch (type) {
            case HTTP:
                this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                break;
            case SOCKS4:
                this.proxy = SocksProxy.create(new InetSocketAddress(host, port), 4);
                break;
            case SOCKS5:
                this.proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
                break;
            default:
                throw new IllegalArgumentException("Unknown proxy type: " + type.name());
        }
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
     * Set the proxy to use.
     *
     * @param proxy The proxy to use
     */
    public void setProxy(@Nonnull final Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Set the proxy to use.
     *
     * @param type The type of the proxy
     * @param host The host of the proxy
     * @param port The port of the proxy
     */
    public void setProxy(@Nonnull final ProxyType type, @Nonnull final String host, final int port) {
        switch (type) {
            case HTTP:
                this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                break;
            case SOCKS4:
                this.proxy = SocksProxy.create(new InetSocketAddress(host, port), 4);
                break;
            case SOCKS5:
                this.proxy = SocksProxy.create(new InetSocketAddress(host, port), 5);
                break;
            default:
                throw new IllegalArgumentException("Unknown proxy type: " + type.name());
        }
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


    public enum ProxyType {
        HTTP,
        SOCKS4,
        SOCKS5
    }

}
