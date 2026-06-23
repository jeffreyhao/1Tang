package com.benefit.interceptor;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.baselibrary.util.click.NoDoubleClickUtil;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.HttpResult;
import com.base.util.content.GsonUtil;
import com.blankj.utilcode.util.ActivityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
* @author lhc
* @date 2023/10/26 9:12
* @desc
*/

public class AccountDeletedInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response proceed = chain.proceed(request);
        if(isAccountDeleted(proceed)) {
            startAccountDeletedDialog();
        }
        return proceed;
    }

    private void startAccountDeletedDialog() {
    }

    private boolean isAccountDeleted(Response response) {
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
                    if(result!=null&&result.getCode() == ApiErrorCode.ACCOUNT_DELETED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
