package net.lenni0451.commons.network.trust;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class AllHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }

}
