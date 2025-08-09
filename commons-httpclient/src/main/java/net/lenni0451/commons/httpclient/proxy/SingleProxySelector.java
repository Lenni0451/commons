package net.lenni0451.commons.httpclient.proxy;

import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.net.*;
import java.util.Collections;
import java.util.List;

public class SingleProxySelector extends ProxySelector {

    private static final MethodHandle GET_AUTHENTICATOR;

    static {
        MethodHandle getAuthenticator;
        try {
            getAuthenticator = MethodHandles.lookup().findStatic(Authenticator.class, "getDefault", MethodType.methodType(Authenticator.class));
        } catch (Throwable t) {
            try {
                Field f = Authenticator.class.getDeclaredField("theAuthenticator");
                f.setAccessible(true);
                getAuthenticator = MethodHandles.lookup().unreflectGetter(f);
            } catch (Throwable t2) {
                getAuthenticator = MethodHandles.constant(Authenticator.class, null);
            }
        }
        GET_AUTHENTICATOR = getAuthenticator;
    }


    private final Proxy proxy;
    private final String username;
    private final String password;
    private final ProxySelector defaultProxySelector;
    private final Authenticator defaultAuthenticator;

    @SneakyThrows
    public SingleProxySelector(final Proxy proxy, final String username, final String password) {
        this.proxy = proxy;
        this.username = username;
        this.password = password;

        this.defaultProxySelector = ProxySelector.getDefault();
        this.defaultAuthenticator = (Authenticator) GET_AUTHENTICATOR.invokeExact();
    }

    /**
     * Set this proxy selector as default.<br>
     * This also sets the authenticator if username and password are set.
     *
     * @param setProxySelector If the default proxy selector should be set to this instance
     */
    public void set(final boolean setProxySelector) {
        if (setProxySelector) ProxySelector.setDefault(this);
        if (this.username != null && this.password != null) {
            Authenticator.setDefault(new SingleProxyAuthenticator(this.username, this.password));
        }
    }

    /**
     * Reset the default proxy selector and authenticator.
     *
     * @param resetProxySelector If the default proxy selector should be reset to the original one
     */
    public void reset(final boolean resetProxySelector) {
        if (resetProxySelector) ProxySelector.setDefault(this.defaultProxySelector);
        if (this.username != null && this.password != null) {
            Authenticator.setDefault(this.defaultAuthenticator);
        }
    }

    @Override
    public List<Proxy> select(URI uri) {
        return Collections.singletonList(this.proxy);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        //Do nothing
    }

}
