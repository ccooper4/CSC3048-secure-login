package qub.network;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Standard Spring Snippet to disable SSL Cert checks.
 * Based on http://forum.spring.io/forum/spring-projects/web-services/52629-disabling-hostname-verification
 */
public final class DisableSSLCertCheck {

    //region Constructor

    /**
     * Prevent instantiation of utility class.
     */
    private DisableSSLCertCheck() {

    }

    //endregion

    //region NullX509TrustManager

    /**
     * Provides a Trust manager that does not perform nay checks.
     */
    private static class NullX509TrustManager implements X509TrustManager {

        /**
         * Checks if the client is trusted.
         * @param chain The cert chain.
         * @param authType The auth type.
         * @throws CertificateException A certificate exception.
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //Do Nothing.
        }

        /**
         * Checks if the server cert is trusted.
         * @param chain The cert chain.
         * @param authType The auth type.
         * @throws CertificateException A certificate exception.
         */
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //Do Nothing.
        }


        /**
         * Gets the accept issuers.
         * @return The accepted issuers.
         */
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

    //endregion

    //region NullHostnameVerifier

    /**
     * Host name verifier that does not perform nay checks.
     */
    private static class NullHostnameVerifier implements HostnameVerifier {

        /**
         * Verify the SSL Connection details.
         * @param hostname The hostname.
         * @param session The SSL Session.
         * @return A boolean indicating if this session is valid.
         */
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    //endregion

    //region Methods

    /**
     * Disable trust checks for SSL connections.
     */
    public static void disableChecks() throws NoSuchAlgorithmException, KeyManagementException {

        try {

            new URL("https://0.0.0.0/").getContent();

        } catch (IOException e) {

        }

        try {

            SSLContext sslc;

            sslc = SSLContext.getInstance("TLS");

            TrustManager[] trustManagerArray = {new NullX509TrustManager()};
            sslc.init(null, trustManagerArray, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion
}
