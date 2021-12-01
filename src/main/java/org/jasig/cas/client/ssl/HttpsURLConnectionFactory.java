

package org.jasig.cas.client.ssl;

import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

/**
 * 若CAS中使用 https访问，且不可信的ssl时可使用
 *
 * @since 1.2.0
 */
public final class HttpsURLConnectionFactory implements HttpURLConnectionFactory {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsURLConnectionFactory.class);
    private HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    private Properties sslConfiguration = new Properties();
    private static HttpsTrustManager httpsTrustManager = new HttpsTrustManager();

    public HttpsURLConnectionFactory() {
    }

    public HttpsURLConnectionFactory(HostnameVerifier verifier, Properties config) {
        this.setHostnameVerifier(verifier);
        this.setSSLConfiguration(config);
    }

    public final void setSSLConfiguration(Properties config) {
        this.sslConfiguration = config;
    }

    public final void setHostnameVerifier(HostnameVerifier verifier) {
        this.hostnameVerifier = verifier;
    }

    @Override
    public HttpURLConnection buildHttpURLConnection(URLConnection url) {
        return this.configureHttpsConnectionIfNeeded(url);
    }

    private HttpURLConnection configureHttpsConnectionIfNeeded(URLConnection conn) {
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
            final SSLContext sslContext = createSSLContext();
            httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            conn = httpsConnection;
//            SSLSocketFactory socketFactory = this.createSSLSocketFactory();
//            if (socketFactory != null) {
//                httpsConnection.setSSLSocketFactory(socketFactory);
//            }
            if (this.hostnameVerifier != null) {
                httpsConnection.setHostnameVerifier(this.hostnameVerifier);
            }
        }

        return (HttpURLConnection) conn;
    }

    private SSLContext createSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new HttpsTrustManager[]{httpsTrustManager}, null);
            return sslContext;
        } catch (Exception e) {
            throw new IllegalStateException("Create SSLContext error", e);
        }
    }

    private static class HttpsTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            //ignore
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            //ignore
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private SSLSocketFactory createSSLSocketFactory() {
        FileInputStream keyStoreIS = null;

        try {
            SSLContext sslContext = SSLContext.getInstance(this.sslConfiguration.getProperty("protocol", "SSL"));
            if (this.sslConfiguration.getProperty("keyStoreType") != null) {
                KeyStore keyStore = KeyStore.getInstance(this.sslConfiguration.getProperty("keyStoreType"));
                if (this.sslConfiguration.getProperty("keyStorePath") != null) {
                    keyStoreIS = new FileInputStream(this.sslConfiguration.getProperty("keyStorePath"));
                    if (this.sslConfiguration.getProperty("keyStorePass") != null) {
                        keyStore.load(keyStoreIS, this.sslConfiguration.getProperty("keyStorePass").toCharArray());
                        LOGGER.debug("Keystore has {} keys", keyStore.size());
                        KeyManagerFactory keyManager = KeyManagerFactory.getInstance(this.sslConfiguration.getProperty("keyManagerType", "SunX509"));
                        keyManager.init(keyStore, this.sslConfiguration.getProperty("certificatePassword").toCharArray());
                        sslContext.init(keyManager.getKeyManagers(), (TrustManager[]) null, (SecureRandom) null);
                        SSLSocketFactory var5 = sslContext.getSocketFactory();
                        return var5;
                    }
                }
            }
        } catch (Exception var9) {
            LOGGER.error(var9.getMessage(), var9);
        } finally {
            CommonUtils.closeQuietly(keyStoreIS);
        }

        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            HttpsURLConnectionFactory that = (HttpsURLConnectionFactory) o;
            if (!this.hostnameVerifier.equals(that.hostnameVerifier)) {
                return false;
            } else {
                return this.sslConfiguration.equals(that.sslConfiguration);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.hostnameVerifier.hashCode();
        result = 31 * result + this.sslConfiguration.hashCode();
        return result;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (this.hostnameVerifier == HttpsURLConnection.getDefaultHostnameVerifier()) {
            out.writeObject((Object) null);
        } else {
            out.writeObject(this.hostnameVerifier);
        }

        out.writeObject(this.sslConfiguration);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object internalHostNameVerifier = in.readObject();
        if (internalHostNameVerifier == null) {
            this.hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        } else {
            this.hostnameVerifier = (HostnameVerifier) internalHostNameVerifier;
        }

        this.sslConfiguration = (Properties) in.readObject();
    }
}
