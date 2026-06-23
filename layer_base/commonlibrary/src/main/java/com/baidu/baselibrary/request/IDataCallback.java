package com.baidu.baselibrary.request;

import android.text.TextUtils;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.ALogJsonBuilder;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.util.date.TimeUtil;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.HttpResult;
import com.base.net.callback.ResponseListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
* @author lhc
* @date 2022/5/9 9:20
* @desc String 转成泛型具体类
*/

public abstract class IDataCallback<T> implements ResponseListener {

    protected boolean mIsCache = false;

    @Override
    public void onSuccess(String apiPath, Map<String,Object> paramMap, String content, Response<ResponseBody> response, boolean isCache) {
        logConsole(apiPath, paramMap, content, response, isCache);
        mIsCache = isCache;
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(content);
            int code = jsonObject.optInt("code", ApiErrorCode.LOCAL_PARSE_NO_CODE);
            String message = jsonObject.optString("message", "");
            if(code == HttpResult.SUCCESS){
                Object body = jsonObject.opt("body");
                if(body != null) {
                    try {
                        Type type = getClassType(this);
                        T data = gson.fromJson(body.toString(), type);
                        onSuccess(new HttpResult(code, message), data, isCache);
                    } catch (Throwable e){
                        ALog.exception("IDataCallback", "onSuccess1", e);
                        logError(apiPath, paramMap, content, response == null ? "null": response.toString(), code, message);
                        onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_PARSE_BODY_ERROR, "parse error"), isCache);
                    }
                } else {
                    logError(apiPath, paramMap, content, response == null ? "null": response.toString(), -1, "body is empty.");
                    onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, "body is empty"), isCache);
                }
            } else {
                logError(apiPath, paramMap, content, response == null ? "null": response.toString(), code, message);
                onFail(apiPath, paramMap, new ApiException(code, message), isCache);
            }
            TimeUtil.updateTs(apiPath, jsonObject);
        } catch (Throwable e) {
            ALog.exception("IDataCallback", "onSuccess2", e);
            String errMsg = TextUtils.isEmpty(e.getMessage())
                    ? ((response == null || TextUtils.isEmpty(response.message())) ? "response is empty" : response.message())
                    : e.getMessage();
            onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, errMsg), isCache);
        }
    }

    @Override
    public void onFail(String apiPath, Map<String,Object> paramMap, ApiException e, boolean isCache) {
        onFail(e, isCache);
    }

    @Override
    public void onCancel(String apiPath, Map<String, Object> paramMap) {
        logCancel(apiPath, paramMap);
    }

    private void logCancel(String apiPath, Map<String,Object> paramMap){
        JSONObject jsonObject = ALogJsonBuilder.get()
                .put("success", false)
                .putCodeMsg(-1, "onCancel()")
                .putContent("canAddRequest=false")
                .put("apiPath", apiPath)
                .build();
        ALog.jsonObject(ALog.W, "IDataCallback", "url-logCancel", new String[]{LogTag.REQUEST, LogTag.RequestResult}, jsonObject);
    }

    private void logError(String apiPath, Map<String,Object> paramMap, String content, String response, int code, String message){
        JSONObject jsonObject = ALogJsonBuilder.get()
                .put("success", false)
                .putCodeMsg(code, message)
                .put("apiPath", apiPath)
                .putContent(content)
                .put("response", response)
                .build();
        ALog.jsonObject(ALog.W, "IDataCallback", "url-logError", new String[]{LogTag.REQUEST, LogTag.RequestResult}, jsonObject);
    }

    private Type getClassType(Object obj) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] args = ((ParameterizedType) genType).getActualTypeArguments();
        return args[0];
    }


    public abstract void onSuccess(HttpResult result, T t, boolean isCache);

    public abstract void onFail(ApiException e, boolean isCache);


    private void logConsole(String apiPath, Map<String,Object> paramMap, String content, Response<ResponseBody> response, boolean isCache){
        if(GlobalBuildConfig.DEBUG){
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("IDataCallback.onSuccess(isCache:");
                sb.append(isCache);
                sb.append(")\n【Api】\n");
                sb.append(apiPath);
                sb.append("\n【params】\n");
                sb.append(paramMap);
                sb.append("\n【Body】\n");
                RequestBody requestBody = response.raw().request().body();
                if (requestBody instanceof FormBody) {
                    FormBody body = (FormBody) requestBody;
                    for (int i = 0; i < body.size(); i++) {
                        String name = body.encodedName(i);
                        String value = body.encodedValue(i);
                        if(i != 0){
                            sb.append("&");
                        }
                        sb.append(name).append("=").append(value);
                    }
                }
                sb.append("\n【response】\n");
                sb.append(content);
                sb.append("\n------------------");
                LogUtil.d("url-response", sb.toString());
            } catch (Throwable e){
                e.printStackTrace();
            }
        }
    }
}