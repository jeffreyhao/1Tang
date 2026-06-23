package com.base.callback;

import java.util.Map;

public interface NetworkCallback {

    /**
     * 请求数据成功回调
     *
     * @param apiPath   请求的api
     * @param paramMap  请求的参数map
     * @param content   response内容
     * @param response  返回的response
     * @param isCache   是否来自缓存
     */
    void onSuccess(String apiPath, Map<String,Object> paramMap, String content, Object response, boolean isCache);

    /**
     * 请求数据失败回调
     *
     * @param apiPath   请求的api
     * @param paramMap  请求的参数map
     * @param code      错误码
     * @param message   异常信息
     * @param isCache   是否来自缓存
     */
    void onFail(String apiPath, Map<String,Object> paramMap, int code, String message, boolean isCache);

    /**
     * 请求取消回调（目前在队列中有重复会取消）
     *
     * @param apiPath   请求的api
     * @param paramMap  请求的参数map
     */
    void onCancel(String apiPath, Map<String,Object> paramMap);

}
