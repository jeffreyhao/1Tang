package com.base.net.request;

import com.base.api.Logger;
import com.base.net.ApiBase;
import com.base.net.dns.ApiDns;
import com.base.net.interceptor.CommonParamsInterceptor;
import com.base.net.interceptor.CurlInterceptor;
import com.base.net.interceptor.DecryptionInterceptor;
import com.base.net.interceptor.HeaderInterceptor;
import com.base.net.interceptor.TokenInterceptor;
import com.base.util.collection.ListUtil;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
* @author lhc
* @date 2022/5/9 9:29
* @desc
*/

public class RetrofitUtils {
    private OkHttpClient client;
    private Retrofit.Builder retrofit = null;
    private List<Interceptor> mInterceptors = new ArrayList<>();
    private EventListener mEventListener;
    private RequestConfig mRequestConfig;

    private RetrofitUtils() {

    }

    private static class RetrofitHolder {
        private static final RetrofitUtils INSTANCE = new RetrofitUtils();
    }

    public static RetrofitUtils getInstance() {
        return RetrofitHolder.INSTANCE;
    }

    public void init(RequestConfig config){
        mRequestConfig = config;
    }

    public RequestConfig getConfig(){
        if(mRequestConfig == null){
            mRequestConfig = RequestConfig.newDefault();
        }
        return mRequestConfig;
    }

    public OkHttpClient getClient() {
        if(client==null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // DEBUG，打开明文
            if(ApiBase.DEBUG) {
                try {
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                            TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init((KeyStore) null);
                    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                        throw new IllegalStateException("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers));
                    }
                    X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, new TrustManager[] { trustManager }, null);

                    builder.sslSocketFactory(sslContext.getSocketFactory(),trustManager);
                    builder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                } catch (Throwable e){
                    Logger.exception(e);
                }
            }

            // 本期放开证书校验
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            RequestConfig config = getConfig();
            builder.dns(new ApiDns())
                    .connectionPool(new ConnectionPool(config.maxIdleConnections,config.keepAliveDuration, config.connectPoolUnit))
                    .connectTimeout(config.connectTimeOutValue, config.connectTimeOutUnit)
                    .readTimeout(config.readTimeOutValue, config.readTimeOutUnit)
                    .writeTimeout(config.writeTimeOutValue, config.writeTimeOutUnit)
                    .retryOnConnectionFailure(true);

            if(ListUtil.isNotEmpty(mInterceptors)){
                for(Interceptor interceptor: mInterceptors){
                    builder.addInterceptor(interceptor);
                }
            }

            if(mEventListener!=null){
                builder.eventListener(mEventListener);
            }
            client = builder.build();
        }
        return client;
    }

    public void addInterceptor(Interceptor interceptor){
        mInterceptors.add(interceptor);
    }

    public void setEventListener(EventListener listener) {
        this.mEventListener = listener;
    }

    private Retrofit.Builder getRetrofitBuilder(String url) {
        String baseUrl = url.endsWith("/")?url:url+"/";
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        return retrofit;
    }

    public <T> T getApiService(Class<T> cls) {
        Retrofit retrofit = getRetrofitBuilder(ApiBase.BASE_URL)
                .client(getClient()).build();
        return retrofit.create(cls);
    }

}
