package com.base.net.request;

import com.base.net.ApiBase;
import com.base.net.dns.ApiDns;
import com.base.net.interceptor.CommonParamsInterceptor;
import com.base.net.interceptor.HeaderInterceptor;
import com.base.net.interceptor.RequestRetryInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
* @author lhc
* @date 2022/5/9 9:29
* @desc
*/

public class RetrofitUtils2 {
    private OkHttpClient client;
    private Retrofit.Builder retrofit = null;

    private RetrofitUtils2() {

    }

    private static class RetrofitHolder {
        private static final RetrofitUtils2 INSTANCE = new RetrofitUtils2();
    }

    public static RetrofitUtils2 getInstance() {
        return RetrofitHolder.INSTANCE;
    }

    private OkHttpClient getClient() {
        if(client==null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new CommonParamsInterceptor())
                    .addInterceptor(new HeaderInterceptor())
                    .addInterceptor(new RequestRetryInterceptor.Builder().build())
                    .dns(new ApiDns())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
//            if(AppConfig.isDebug) {
//                builder.addInterceptor(new LoggingInterceptor());
//            }
            client = builder.build();
        }
        return client;
    }

    private Retrofit.Builder getRetrofitBuilder(String url) {
        if(retrofit==null){
            String baseUrl = url.endsWith("/")?url:url+"/";
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()));
        }
        return retrofit;
    }

    public <T> T getApiService(Class<T> cls) {
        Retrofit retrofit = getRetrofitBuilder(ApiBase.BASE_URL)
                .client(getClient()).build();
        return retrofit.create(cls);
    }

}
