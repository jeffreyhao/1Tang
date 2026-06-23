package com.benefit.interceptor;

import android.net.Uri;
import android.text.TextUtils;

import com.baidu.baselibrary.log.ALog;
import com.base.net.ApiBase;
import com.base.net.dns.DnsUtil;
import com.base.util.collection.ListUtil;
import com.base.watcher.Watcher;
import com.base.watcher.WatcherEvent;
import com.benefit.novelverse.utils.RequestTimeTrackUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

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
        long t1 = System.currentTimeMillis();
        Response response = doRequest(chain, request, t1);
        if(response!=null){
            if(shouldSwitchUrl(response.code())) {
                String url = request.url().toString();
                switchHost(url);
                ALog.textSingle("RequestRetryInterceptor", "intercept", "switchUrl: " + response.code());
            }
            long t2 = System.currentTimeMillis();
            RequestTimeTrackUtil.saveRequestItem(request.url().toString(), t2-t1, String.valueOf(response.code()), "",false);
        }
        if(response==null) {
            response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(1000) // PayCode.REMOTE_NO_RESPONSE
                    .message("response null")
                    .body(ResponseBody.create("request fail", MediaType.parse("application/json"))).build();
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request, long startTime) {
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            e.printStackTrace();

            if (e instanceof UnknownHostException
                    || e.getClass().getSimpleName().contains("UnknownHost")
                    || e.getClass().getSimpleName().contains("SSL")
                    || (e.getMessage()!=null && e.getMessage().contains("503"))
            ) {
                // （走代理测试，还会报这个：java.io.IOException: Unexpected response code for CONNECT: 503。设置代理后okhttp会跳过dns解析直接使用代理地址）
                // （UnknownHostException==>Unable to resolve host "appserver.plutofacts.com": No address associated with hostname 这个上报的日志有没有切换成功的）
                // 切换dns
                String url = request.url().toString();

                ALog.textSingle("[UnknownHostException] url:" + url);
                switchHost(url);
            }

            String msg = TextUtils.isEmpty(e.getMessage())? getStackTrace(e) : e.getClass().getSimpleName() + "==>" + e.getMessage();
            RequestTimeTrackUtil.saveRequestItem(request.url().toString(), System.currentTimeMillis()-startTime, "-1", msg, false);
        }
        return null;
    }

    private void switchHost(String url){
        if(ListUtil.isNotEmpty(ApiBase.backupDomain)) {
            Uri uri = Uri.parse(url);
            String host = "";
            if(uri!=null) {
                host = uri.getHost();
            }
            if(DnsUtil.isAppDomain(host)) {
                String backupDomain = DnsUtil.getNextDomain(host);
                ALog.textSingle("RequestRetryInterceptor", "switchHost", "->backupDomain: " + backupDomain);
                Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_SWITCH_DNS, backupDomain);
            }
        }
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        String msg = "";
        if(buffer!=null){
            msg = buffer.toString();
        }
        return e.getClass().getSimpleName() + "==>" + msg;
    }

    public static class Builder {

        private int mRetryCount = 3;
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

    private boolean shouldSwitchUrl(int code) {
        return code==503;
    }

}
