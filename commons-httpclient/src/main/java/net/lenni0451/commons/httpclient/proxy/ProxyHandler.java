package net.lenni0451.commons.httpclient.proxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class ProxyHandler {

    private ProxyType proxyType;
    private String host;
    private int port;
    private String username;
    private String password;

    public ProxyHandler() {
    }

    public ProxyHandler(final ProxyType proxyType, final String host, final int port) {
        this(proxyType, host, port, null, null);
    }

    public ProxyHandler(final ProxyType proxyType, final String host, final int port, @Nullable final String username, @Nullable final String password) {
        this.proxyType = proxyType;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Set the proxy to use.
     *
     * @param type The type of the proxy
     * @param host The host of the proxy
     * @param port The port of the proxy
     */
    public void setProxy(final ProxyType type, final String host, final int port) {
        this.proxyType = type;
        this.host = host;
        this.port = port;
    }

    /**
     * Unset the proxy.
     */
    public void unsetProxy() {
        this.proxyType = null;
        this.host = null;
        this.port = 0;
    }

    /**
     * @return If the proxy is set
     */
    public boolean isProxySet() {
        return this.proxyType != null && this.host != null && this.port != 0;
    }

    /**
     * @return The type of the proxy
     */
    @Nullable
    public ProxyType getProxyType() {
        return this.proxyType;
    }

    /**
     * Set the type of the proxy.
     *
     * @param type The type of the proxy
     */
    public void setProxyType(@Nonnull final ProxyType type) {
        this.proxyType = type;
    }

    /**
     * @return The proxy host
     */
    @Nullable
    public String getHost() {
        return this.host;
    }

    /**
     * Set the proxy host.
     *
     * @param host The proxy host
     */
    public void setHost(@Nonnull final String host) {
        this.host = host;
    }

    /**
     * @return The proxy port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Set the proxy port.
     *
     * @param port The proxy port
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * @return If the authentication is set
     */
    public boolean isAuthenticationSet() {
        return this.username != null && this.password != null;
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
     * @throws IllegalStateException If the proxy is not set
     */
    public SingleProxySelector getProxySelector() {
        if (!this.isProxySet()) throw new IllegalStateException("Proxy is not set");
        return new SingleProxySelector(this.toJavaProxy(), this.username, this.password);
    }

    /**
     * @return The SingleProxyAuthenticator for this proxy
     * @throws IllegalStateException If the proxy or the username/password is not set
     */
    public SingleProxyAuthenticator getProxyAuthenticator() {
        if (!this.isProxySet()) throw new IllegalStateException("Proxy is not set");
        if (!this.isAuthenticationSet()) throw new IllegalStateException("Username or password is not set");
        return new SingleProxyAuthenticator(this.username, this.password);
    }

    /**
     * Create a {@link Proxy} object from this proxy.<br>
     * The {@link Proxy} might not support all proxy types.
     *
     * @return The created {@link Proxy} object
     */
    public Proxy toJavaProxy() {
        switch (this.proxyType) {
            case HTTP:
                return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.host, this.port));
            case SOCKS4:
                try {
                    Class<?> clazz = Class.forName("sun.net.SocksProxy");
                    Method createMethod = clazz.getDeclaredMethod("create", SocketAddress.class, int.class);
                    return (Proxy) createMethod.invoke(null, new InetSocketAddress(this.host, this.port), 4);
                } catch (Throwable t) {
                    throw new UnsupportedOperationException("SOCKS4 proxy type is not supported", t);
                }
            case SOCKS5:
                return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(this.host, this.port));
            default:
                throw new IllegalStateException("Unknown proxy type: " + this.proxyType.name());
        }
    }

}
