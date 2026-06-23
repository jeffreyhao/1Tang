package com.base.net.callback;

import com.base.net.bean.ApiException;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by haojiangfeng on 2024/11/5.
 */
public interface ResponseListener {

    /**
     * 请求数据成功回调
     *
     * @param apiPath   请求的api
     * @param paramMap  请求的参数map
     * @param content   response内容
     * @param response  返回的response
     * @param isCache   是否来自缓存
     */
    void onSuccess(String apiPath, Map<String,Object> paramMap, String content, Response<ResponseBody> response, boolean isCache);

    /**
     * 请求数据失败回调
     *
     * @param apiPath   请求的api
     * @param paramMap  请求的参数map
     * @param e         异常信息
     * @param isCache   是否来自缓存
     */
    void onFail(String apiPath, Map<String,Object> paramMap, ApiException e, boolean isCache);

    /**
     * 请求取消回调（目前在队列中有重复会取消）
     *
     * @param apiPath   请求的api
     * @param paramMap  请求的参数map
     */
    void onCancel(String apiPath, Map<String,Object> paramMap);
}
