package net.lenni0451.commons.httpclient.proxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class ProxyHandler {

    private ProxyType proxyType;
    private SocketAddress address;
    private String username;
    private String password;

    public ProxyHandler() {
    }

    public ProxyHandler(final ProxyType proxyType, final String host, final int port) {
        this(proxyType, host, port, null, null);
    }

    public ProxyHandler(final ProxyType proxyType, final String host, final int port, @Nullable final String username, @Nullable final String password) {
        this(proxyType, new InetSocketAddress(host, port), username, password);
    }

    public ProxyHandler(final ProxyType proxyType, final SocketAddress address) {
        this(proxyType, address, null, null);
    }

    public ProxyHandler(final ProxyType proxyType, final SocketAddress address, @Nullable final String username, @Nullable final String password) {
        this.proxyType = proxyType;
        this.address = address;
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
        this.setProxy(type, new InetSocketAddress(host, port));
    }

    /**
     * Set the proxy to use.
     *
     * @param type The type of the proxy
     * @param address The address of the proxy
     */
    public void setProxy(final ProxyType type, final SocketAddress address) {
        this.proxyType = type;
        this.address = address;
    }

    /**
     * Unset the proxy.
     */
    public void unsetProxy() {
        this.proxyType = null;
        this.address = null;
    }

    /**
     * @return If the proxy is set
     */
    public boolean isProxySet() {
        return this.proxyType != null && this.address != null;
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
     * @return The proxy address
     */
    @Nullable
    public SocketAddress getAddress() {
        return this.address;
    }

    /**
     * Set the proxy address.
     *
     * @param address The proxy address
     */
    public void setAddress(@Nonnull final SocketAddress address) {
        this.address = address;
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
                return new Proxy(Proxy.Type.HTTP, address);
            case SOCKS4:
                try {
                    Class<?> clazz = Class.forName("sun.net.SocksProxy");
                    Method createMethod = clazz.getDeclaredMethod("create", SocketAddress.class, int.class);
                    return (Proxy) createMethod.invoke(null, address, 4);
                } catch (Throwable t) {
                    throw new UnsupportedOperationException("SOCKS4 proxy type is not supported", t);
                }
            case SOCKS5:
                return new Proxy(Proxy.Type.SOCKS, address);
            default:
                throw new IllegalStateException("Unknown proxy type: " + this.proxyType.name());
        }
    }

}
