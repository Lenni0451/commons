package net.lenni0451.commons.network;

import lombok.experimental.UtilityClass;
import net.lenni0451.commons.network.trust.AllHostnameVerifier;
import net.lenni0451.commons.network.trust.AllTrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class InetUtils {

    /**
     * Open a HttpsURLConnection which does not check any certificates.
     *
     * @param url The url to open the connection to
     * @return The trusting connection
     * @throws IOException              If an I/O error occurs
     * @throws NoSuchAlgorithmException If the TLS algorithm is not available
     * @throws KeyManagementException   If the TLS algorithm is not available
     */
    public static HttpsURLConnection openTrustingConnection(final URL url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{new AllTrustManager()}, null);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(context.getSocketFactory());
        connection.setHostnameVerifier(new AllHostnameVerifier());
        return connection;
    }

    /**
     * Ignore all certificate errors in {@link HttpsURLConnection}s.
     *
     * @throws NoSuchAlgorithmException If the TLS algorithm is not available
     * @throws KeyManagementException   If the TLS algorithm is not available
     */
    public static void ignoreCertificateErrors() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{new AllTrustManager()}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new AllHostnameVerifier());
    }

}
