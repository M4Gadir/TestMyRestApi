package com.magad.testmyrestapi.api;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "https://newsapi.org/v2/";
    public static Retrofit retrofit;

    public static Retrofit getApiClient(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient().build())
                    .addConverterFactory( GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
    public static OkHttpClient.Builder getUnsafeOkHttpClient(){
        try {
            // membuat a trust manager that does not validate certifite chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {


                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }


            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSl");
            sslContext.init(null, trustAllCerts, new SecureRandom());


            // create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFacory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFacory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        }catch (Exception e) {
            throw  new RuntimeException(e);

        }
    }
}
