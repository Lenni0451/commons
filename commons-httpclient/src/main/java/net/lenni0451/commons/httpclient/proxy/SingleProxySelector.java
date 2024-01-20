package net.lenni0451.commons.httpclient.proxy;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.List;

public class SingleProxySelector extends ProxySelector {

    private final Proxy proxy;
    private final String username;
    private final String password;
    private final ProxySelector defaultProxySelector;
    private final Authenticator defaultAuthenticator;

    public SingleProxySelector(final Proxy proxy, final String username, final String password) {
        this.proxy = proxy;
        this.username = username;
        this.password = password;

        this.defaultProxySelector = ProxySelector.getDefault();
        this.defaultAuthenticator = null;
    }

    /**
     * Set this proxy selector as default.
     */
    public void set() {
        ProxySelector.setDefault(this);
        if (this.username != null && this.password != null) {
            Authenticator.setDefault(new SingleProxyAuthenticator(this.username, this.password));
        }
    }

    /**
     * Reset the default proxy selector.
     */
    public void reset() {
        ProxySelector.setDefault(this.defaultProxySelector);
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
