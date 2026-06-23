package com.baidu.baselibrary.request;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.HttpResult;
import com.base.net.callback.ResponseListener;
import com.google.gson.Gson;

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
* @desc 未通过HttpResult转换
*/

public abstract class IObjectCallback<T> implements ResponseListener {

    protected boolean mIsCache = false;

    @Override
    public void onSuccess(String apiPath, Map<String,Object> paramMap, String content, Response<ResponseBody> response, boolean isCache) {
        logConsole(apiPath, paramMap, content, response, isCache);
        mIsCache = isCache;
        Gson gson = new Gson();
        Type type = getClassType(this);
        try {
            T t = gson.fromJson(content, type);
            if (t != null) {
                onSuccess(null, t, isCache);
            }else{
                onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, "parse bean fail"), isCache);
            }
        } catch (Exception e) {
            onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, e.getMessage()), isCache);
            e.printStackTrace();
            ALog.textSingle("IObjectCallback-->exception: " + e.getMessage());
        }
    }

    @Override
    public void onFail(String apiPath, Map<String, Object> paramMap, ApiException e, boolean isCache) {
        onFail(e, isCache);
    }

    @Override
    public void onCancel(String apiPath, Map<String, Object> paramMap) {
        // none
    }

    private Type getClassType(Object obj) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] args = ((ParameterizedType) genType).getActualTypeArguments();
        return args[0];
    }


    public abstract void onSuccess(HttpResult result, T t, boolean isCache);

    protected void onFail(ApiException e, boolean isCache) {

    }


    private void logConsole(String apiPath, Map<String,Object> paramMap, String content, Response<ResponseBody> response, boolean isCache){
        if(GlobalBuildConfig.DEBUG){
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("onSuccess(isCache:");
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
                sb.append("\n  ---  ");
                LogUtil.d("Response", sb.toString());
            } catch (Throwable e){
                e.printStackTrace();
            }
        }
    }
}