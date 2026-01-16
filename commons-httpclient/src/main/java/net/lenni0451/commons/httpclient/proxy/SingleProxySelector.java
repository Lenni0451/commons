package net.lenni0451.commons.httpclient.proxy;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class SingleProxySelector extends ProxySelector {

    private final Proxy proxy;

    public SingleProxySelector(final Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
            return Collections.singletonList(this.proxy);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        //Do nothing
    }

}
