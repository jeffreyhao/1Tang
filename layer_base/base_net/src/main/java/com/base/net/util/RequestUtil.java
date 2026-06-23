package com.base.net.util;

import android.text.TextUtils;

import com.base.net.bean.RequestType;
import com.base.net.cache.CacheControlManager;
import com.base.net.callback.ApiService;
import com.base.net.request.RetrofitUtils;
import com.base.util.collection.ListUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by haojiangfeng on 2024/11/11.
 */
public class RequestUtil {


    public static void saveCache(String path, LinkedHashMap<String, Object> paramMap, String responseBodyContent){

        CacheControlManager.getInstance().cacheResponse(path, paramMap, responseBodyContent);
    }

    public static String getCache(String path, LinkedHashMap<String, Object> paramMap){
        String content = CacheControlManager.getInstance().getCache(path, paramMap);

        return content;
    }



    public static <T> Response<T> success(String url, @Nullable T body) {
        return Response.success(body,
                new okhttp3.Response.Builder()
                        .code(200)
                        .message("OK")
                        .protocol(Protocol.HTTP_1_1)
                        .request(new Request.Builder().url(TextUtils.isEmpty(url) ? "http://localhost/" : url).build())
                        .build());
    }


    public static  Response<ResponseBody> newResponse(String path, String responseBodyContent){
        ResponseBody newResponse = ResponseBody.create(responseBodyContent, MediaType.get("application/json"));
        return RequestUtil.success(path, newResponse);
    }

    @Nullable
    public static Observable<Response<ResponseBody>> newObservableFromCache(String path, LinkedHashMap<String, Object> paramMap) {
        String cacheResponse = RequestUtil.getCache(path, paramMap);
        if (TextUtils.isEmpty(cacheResponse)) {
            return null;
        }
        return Observable.create(e -> {
            Response<ResponseBody> response = RequestUtil.newResponse(path, cacheResponse);
            e.onNext(response);
            e.onComplete();
        });
    }


    @NonNull
    public static Observable<Response<ResponseBody>> newDefaultObservableFromNet(String path, Map<String, Object> paramMap) {
        if(paramMap == null || ListUtil.isEmpty(paramMap)){
            return RetrofitUtils.getInstance().getApiService(ApiService.class).post(path);
        } else {
            return RetrofitUtils.getInstance().getApiService(ApiService.class).post(path, paramMap);
        }
    }

    /**
     * @param requestType  请求类型：GET、POST
     * @param path         path
     * @param authToken    鉴权token
     * @param paramMap     参数
     */
    public static Observable<Response<ResponseBody>> newObservableFromNet(@RequestType String requestType, String path, String authToken, Map<String, Object> paramMap) {
        if(requestType.equals(RequestType.GET)){
            if(TextUtils.isEmpty(authToken)){
                return newGetObservableFromNet(path, paramMap);
            } else {
                return newGetObservableWithAuthFromNet(path, authToken, paramMap);
            }
        } else {
            return newDefaultObservableFromNet(path, paramMap);
        }
    }

    public static Observable<Response<ResponseBody>> newGetObservableFromNet(String path, Map<String, Object> paramMap) {
        if(paramMap == null || ListUtil.isEmpty(paramMap)){
            return RetrofitUtils.getInstance().getApiService(ApiService.class).get(path);
        } else {
            return RetrofitUtils.getInstance().getApiService(ApiService.class).get(path, paramMap);
        }
    }

    public static Observable<Response<ResponseBody>> newGetObservableWithAuthFromNet(String path, String authToken, Map<String, Object> paramMap) {
        if(paramMap == null || ListUtil.isEmpty(paramMap)){
            return RetrofitUtils.getInstance().getApiService(ApiService.class).get(path, authToken);
        } else {
            return RetrofitUtils.getInstance().getApiService(ApiService.class).get(path, authToken, paramMap);
        }
    }

}
