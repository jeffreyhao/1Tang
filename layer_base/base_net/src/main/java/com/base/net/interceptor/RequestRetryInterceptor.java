package com.base.net.interceptor;

import com.base.api.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
* @author lhc
* @date 2022/5/9 9:28
* @desc 网络请求失败重试拦截器
*/
public class RequestRetryInterceptor implements Interceptor {

    private final int mMaxRetryCount;
    private final long mRetryInterval;

    public RequestRetryInterceptor(int maxRetryCount, long retryInterval) {
        mMaxRetryCount = maxRetryCount;
        mRetryInterval = retryInterval;
    }

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        Response response = doRequest(chain, request);
        int retryNum = 1;
        while(((response==null)||!response.isSuccessful())&&retryNum<mMaxRetryCount){
            try {
                Thread.sleep(mRetryInterval*retryNum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retryNum++;
            response = doRequest(chain, request);

        }
        if(response==null) {
            response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(1000)
                    .message("response null")
                    .body(ResponseBody.create("request fail", MediaType.parse("application/json"))).build();
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            Logger.exception("RequestRetryInterceptor", "doRequest", e);
        }
        return null;
    }

    public static class Builder {

        private int mRetryCount = 2;
        private long mRetryInterval = 50;

        public Builder buildRetryCount(int retryCount) {
            this.mRetryCount = retryCount;
            return this;
        }

        public Builder buildRetryInterval(long retryInterval) {
            this.mRetryInterval = retryInterval;
            return this;
        }

        public RequestRetryInterceptor build() {
            return new RequestRetryInterceptor(mRetryCount, mRetryInterval);
        }
    }
}
