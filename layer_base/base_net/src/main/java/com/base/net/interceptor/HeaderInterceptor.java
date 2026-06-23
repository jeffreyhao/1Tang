package com.base.net.interceptor;


import com.base.api.UserApi;
import com.base.net.ApiBase;
import com.base.util.content.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
* @author lhc
* @date 2022/5/9 9:12
* @desc 请求头 拦截器
*/
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = request.url().host();
        if ((ApiBase.BASE_URL.contains(host)
                || ApiBase.SEARCH_URL.contains(host)
                || ApiBase.UPLOAD_TIME_URL.contains(host))
                    && !StringUtils.isTrimEmpty(UserApi.getAccessToken())) {
            request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + UserApi.getAccessToken())
                    .build();
        }
        return chain.proceed(request);
    }
}
