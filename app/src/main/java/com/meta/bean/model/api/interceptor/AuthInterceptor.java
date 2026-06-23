package com.meta.bean.model.api.interceptor;

import com.tencent.common.util.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 在请求中添加一个auth token 头
 */
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        if (AccountHelper.get().isLoggedIn() &&
//                !StringUtils.isTrimEmpty(AccountHelper.get().getAccessToken()) &&
//                !StringUtils.isTrimEmpty(AccountHelper.get().getAccessTokenType())) {
//            request = chain.request().newBuilder()
//                    .addHeader("Authorization", AccountHelper.get().getAccessTokenType() + " " + AccountHelper.get().getAccessToken())
//                    .build();
//        }
        return chain.proceed(request);
    }
}
