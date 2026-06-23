package com.base.net.interceptor;

import android.text.TextUtils;

import com.base.api.Logger;
import com.base.api.UserApi;
import com.base.net.ApiBase;
import com.base.net.callback.ApiService2;
import com.base.net.bean.HttpResult;
import com.base.net.request.RetrofitUtils2;
import com.base.util.content.GsonUtil;

import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.Synchronized;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
* @author lhc
* @date 2022/5/9 9:12
* @desc token过期 拦截器
*/
public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response proceed = chain.proceed(request);
        if(isTokenExpired(proceed)) {
            refreshToken();
            return chain.proceed(request);
        }
        return proceed;
    }

    private boolean isTokenExpired(Response response) {
        try{
            ResponseBody responseBody = response.body();
            if(responseBody!=null) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();
                if(buffer!=null) {
                    String bodyContent = buffer.clone().readString(StandardCharsets.UTF_8);
                    if(!TextUtils.isEmpty(bodyContent)&&!bodyContent.startsWith("{")){
                        return false;
                    }
                    HttpResult result = GsonUtil.json2Bean(bodyContent, HttpResult.class);
                    if(result!=null&&result.getCode()==10003) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Logger.exception("TokenInterceptor", "isTokenExpired", e);
            return false;
        }
        return false;
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            Logger.exception("TokenInterceptor", "isPlaintext", e);
            return false;
        }
    }

    @Synchronized
    private void refreshToken() {
        try {
            if(TextUtils.isEmpty(UserApi.getAccessToken())) return;
            Map<String, Object> params = new HashMap<>();
            params.put("old_token", UserApi.getAccessToken());
            retrofit2.Response<Object> response = RetrofitUtils2.getInstance().getApiService(ApiService2.class).refreshToken(ApiBase.BASE_URL + "app/api/refresh_token", params).execute();
            if(response.body()!=null) {
                String bodyContent = GsonUtil.bean2json(response.body());
                JSONObject jsonObject = new JSONObject(bodyContent);
                Logger.d("refreshToken===>" + jsonObject);
                double code = (double) jsonObject.get("code");
                if(code==0) {
                    JSONObject body = (JSONObject) jsonObject.get("body");
                    String token = (String) body.get("token");
                    if(!TextUtils.isEmpty(token)) {
                        UserApi.saveToken(token);
                    }
                }
            }
        } catch (Exception e) {
            Logger.exception("TokenInterceptor", "refreshToken", e);
        }
    }
}
