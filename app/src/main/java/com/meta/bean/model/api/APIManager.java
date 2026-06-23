package com.meta.bean.model.api;

import com.base.net.ApiBase;
import com.base.net.dns.ApiDns;
import com.base.net.interceptor.CommonParamsInterceptor;
import com.base.net.interceptor.HeaderInterceptor;
import com.benefit.novelverse.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API管理类
 */
public class APIManager {
    public static final String CLIENT_ID = "";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String ENDPOINT = BuildConfig.ENDPOINT;
    public static String ENDPOINT_API = BuildConfig.ENDPOINT_API;
    public static String WEB_ENDPOINT = BuildConfig.WEB_ENDPOINT;
    public static String WEB_ENDPOINT_WITHOUT_PREFIX = BuildConfig.WEB_ENDPOINT_PREFIX;

    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private APIService mAPIService;

    static {
        APIManager.get().createApi();
    }

    private APIManager() {

    }

    private static class APIManagerHolder {
        private static final APIManager INSTANCE = new APIManager();
    }

    public static APIManager get() {
        return APIManagerHolder.INSTANCE;
    }

    //创建Retrofit实例
    public void createApi() {
        if (mOkHttpClient == null) {
            mOkHttpClient = getOkHttpClient();
        }
        final Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiBase.BASE_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        mRetrofit = builder.build();

        mAPIService = mRetrofit.create(APIService.class);
    }

    private OkHttpClient getOkHttpClient() {
        final long DEFAULT_CONNECT_TIMEOUT = 20;
        final long DEFAULT_WRITE_TIMEOUT = 20;
        final long DEFAULT_READ_TIMEOUT = 20;

        final OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .addInterceptor(new CommonParamsInterceptor())
                .addInterceptor(new HeaderInterceptor())
                .dns(new ApiDns());

//        if (BuildConfig.DEBUG) {
//            okHttpBuilder.addInterceptor(new HttpLoggingInterceptor());
//        }

        okHttpBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        //设置cookie
        okHttpBuilder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
                //TODO:安全码服务端 session问题临时处理
                if (httpUrl.host().contains(ENDPOINT)) {
                    cookieStore.put(ENDPOINT, list);
                }
            }

            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
                if (httpUrl.host().contains(ENDPOINT)) {
                    List<Cookie> cookies = cookieStore.get(ENDPOINT);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
                return new ArrayList<>();
            }
        });
        return okHttpBuilder.build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * 获取API服务
     *
     * @return api服务
     */
    public APIService getApi() {
        if (mAPIService == null) {
            createApi();
        }
        return mAPIService;
    }

    public HashMap<String, List<Cookie>> getCookieStore() {
        return cookieStore;
    }
}
