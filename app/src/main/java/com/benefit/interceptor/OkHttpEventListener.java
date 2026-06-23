package com.benefit.interceptor;


import android.net.Uri;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.benefit.novelverse.utils.RequestTimeTrackUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time: 2023/10/27
 * Author: lhc
 * Desc:
 */
public class OkHttpEventListener extends EventListener {


    private static final String TAG = "HttpEvent";

    public static boolean showEventLog = false;

    /**
     * 请求开始
     */

    private long mCallStartTime;
    private long mDnsStartTime;
    private long mConnectStartTime;
    private long mSecureConnectStartTime;
    private long mConnectionStartTime;
    private long mRequestHeadersStartTime;
    private long mRequestBodyStartTime;
    private long mResponseHeadersStartTime;
    private long mResponseBodyStartTime;

    /**
     * 请求开始
     */
    @Override
    public void callStart(Call call) {
        super.callStart(call);
        mCallStartTime = System.nanoTime();
        logCallStart(call);
    }

    /**
     * 请求正常结束
     */
    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        logMethod(call, "callEnd()", mCallStartTime);
    }

    /**
     * 请求异常结束
     */
    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        logError(call, "callFailed()", mCallStartTime, ioe);
    }

    /**
     * dns解析开始
     * DNS解析是请求DNS（Domain Name System）服务器。将域名解析成ip的过程。
     */
    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        mDnsStartTime = System.nanoTime();
    }

    /**
     * dns解析结束
     */
    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        logDnsParse(call,  inetAddressList!=null?inetAddressList.toString():"", mDnsStartTime);
    }

    /**
     * 连接开始
     */
    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        mConnectStartTime = System.nanoTime();
    }

    /**
     * 连接结束
     */
    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        logMethod(call, "connectEnd()", mConnectStartTime);
    }

    /**
     * 连接失败
     */
    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        logError(call, "connectFailed()", mConnectStartTime, ioe);
    }

    /**
     * TLS安全连接开始
     */
    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        mSecureConnectStartTime = System.nanoTime();
    }

    /**
     * TLS安全连接结束
     */
    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        logTsl(call, mSecureConnectStartTime);
    }

    /**
     * 连接绑定
     */
    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        mConnectionStartTime = System.nanoTime();
    }

    /**
     * 连接释放
     */
    @Override
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
        logMethod(call, "connectionReleased()", mConnectionStartTime);
    }

    /**
     * 请求Header开始
     */
    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        mRequestHeadersStartTime = System.nanoTime();
    }

    /**
     * 请求Header结束
     */
    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        logMethod(call, "requestHeadersEnd()", mRequestHeadersStartTime);
    }

    /**
     * 请求Body开始
     */
    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        mRequestBodyStartTime = System.nanoTime();
    }

    /**
     * 请求Body结束
     */
    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        logMethod(call, "requestBodyEnd()", mRequestBodyStartTime);
    }

    /**
     * 响应Header开始
     */
    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        mResponseHeadersStartTime = System.nanoTime();
    }

    /**
     * 响应Header结束
     */
    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        logMethod(call, "responseHeadersEnd()", mResponseHeadersStartTime);
    }

    /**
     * 响应Body开始
     */
    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        mResponseBodyStartTime = System.nanoTime();
    }

    /**
     * 响应Body结束
     */
    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        logMethod(call, "responseBodyEnd()", mResponseBodyStartTime);
    }



    /**
     * 打印公共方法
     */
    private void logError(Call call, String method, long startTime, Exception e) {
        if(!showEventLog){
            return;
        }
        try {
            String path = getPath(call);
            long time = System.nanoTime() - startTime;
            LogUtil.v(TAG, path + "-->" + method + ", spend：" + (time / 1000000d) + "ms, message: " + e.getMessage());
        } catch (Throwable ex){
            ALog.exception(ex);
        }
    }

    private void logMethod(Call call, String method, long startTime) {
        if(!showEventLog){
            return;
        }
        try {
            String path = getPath(call);
            long time = System.nanoTime() - startTime;
            LogUtil.v(TAG, path + "-->" + method + ", spend：" + (time / 1000000d) + "ms.");
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    private void logDnsParse(Call call, String ip, long startTime) {
        try {
            String path = getPath(call);
            long time = System.nanoTime() - startTime;
            String url = call.request().url().toString();
            LogUtil.v(TAG, path + "-->dnsEnd(), dns parse spend " + (time / 1000000d) + "ms.");
            RequestTimeTrackUtil.saveDnsParseItem(url, time/1000000L, ip, 0, true);
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    private void logTsl(Call call, long startTime) {
        try {
            String path = getPath(call);
            long time = System.nanoTime() - startTime;
            LogUtil.v(TAG, path + "-->secureConnectEnd(), tsl connect spend " + (time / 1000000d) + "ms.");
            RequestTimeTrackUtil.saveDnsParseItem(call.request().url().toString(), 0, "", time/1000000L, false);
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    private void logCallStart(Call call) {
        if(!showEventLog){
            return;
        }
        try {
//            ALog.keyValue("OkHttpEventListener", "CallStart()", new String[]{TAG},
//                    call.request().url().toString());
            LogUtil.v(TAG, "CallStart: " + call.request().url().toString());
        } catch (Throwable e){
            ALog.exception(e);
        }
    }


    private String getPath(Call call){
        String url = call.request().url().toString();
        try {
            return Uri.parse(url).getPath();
        } catch (Throwable e){
            LogUtil.v(e);
            return url;
        }
    }
}
