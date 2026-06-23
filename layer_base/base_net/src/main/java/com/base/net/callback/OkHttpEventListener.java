package com.base.net.callback;


import android.net.Uri;

import com.base.api.Logger;
import com.base.api.TrackApi;
import com.base.net.monitor.HttpMonitor;

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



public class OkHttpEventListener extends EventListener {


    private static final String TAG = "HttpEvent";

    public static boolean showEventLog = false;


    /**
     * 请求开始
     */
    @Override
    public void callStart(Call call) {
        super.callStart(call);
        HttpMonitor.callStart(call);
        logCallStart(call);
    }

    /**
     * 请求正常结束
     */
    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        HttpMonitor.callEnd(call);
        HttpMonitor.Bean event = HttpMonitor.remove(call);
        logMethod(call, "callEnd()", event.mCallDuration);
    }

    /**
     * 请求异常结束
     */
    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        HttpMonitor.callFailed(call, ioe);
        HttpMonitor.Bean event = HttpMonitor.remove(call);
        logError(call, "callFailed()", event.mCallDuration, ioe);
    }

    /**
     * dns解析开始
     * DNS解析是请求DNS（Domain Name System）服务器。将域名解析成ip的过程。
     */
    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        HttpMonitor.dnsStart(call, domainName);
    }

    /**
     * dns解析结束
     */
    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        HttpMonitor.dnsEnd(call, domainName, inetAddressList);
        logDnsParse(call,  inetAddressList!=null?inetAddressList.toString():"", HttpMonitor.get(call).mDnsDuration);
    }

    /**
     * 连接开始
     */
    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        HttpMonitor.connectStart(call, inetSocketAddress, proxy);
    }

    /**
     * 连接结束
     */
    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        HttpMonitor.connectEnd(call, inetSocketAddress, proxy, protocol);
        logMethod(call, "connectEnd()", HttpMonitor.get(call).mConnectDuration);
    }

    /**
     * 连接失败
     */
    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        HttpMonitor.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        logError(call, "connectFailed()", HttpMonitor.get(call).mConnectDuration, ioe);
    }

    /**
     * TLS安全连接开始
     */
    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        HttpMonitor.secureConnectStart(call);
    }

    /**
     * TLS安全连接结束
     */
    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        HttpMonitor.secureConnectEnd(call, handshake);
        logTsl(call, HttpMonitor.get(call).mSecureConnectDuration);
    }

    /**
     * 连接绑定
     */
    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        HttpMonitor.connectionAcquired(call, connection);
    }

    /**
     * 连接释放
     */
    @Override
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
        HttpMonitor.connectionReleased(call, connection);
        logMethod(call, "connectionReleased()", HttpMonitor.get(call).mConnectionDuration);
    }

    /**
     * 请求Header开始
     */
    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        HttpMonitor.requestHeadersStart(call);
    }

    /**
     * 请求Header结束
     */
    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        HttpMonitor.requestHeadersEnd(call, request);
        logMethod(call, "requestHeadersEnd()", HttpMonitor.get(call).mRequestHeadersDuration);
    }

    /**
     * 请求Body开始
     */
    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        HttpMonitor.requestBodyStart(call);
    }

    /**
     * 请求Body结束
     */
    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        HttpMonitor.requestBodyEnd(call, byteCount);
        logMethod(call, "requestBodyEnd()", HttpMonitor.get(call).mRequestBodyDuration);
    }

    /**
     * 响应Header开始
     */
    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        HttpMonitor.responseHeadersStart(call);
    }

    /**
     * 响应Header结束
     */
    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        HttpMonitor.responseHeadersEnd(call, response);
        logMethod(call, "responseHeadersEnd()", HttpMonitor.get(call).mResponseHeadersDuration);
    }

    /**
     * 响应Body开始
     */
    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        HttpMonitor.responseBodyStart(call);
    }

    /**
     * 响应Body结束
     */
    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        HttpMonitor.responseBodyEnd(call, byteCount);
        logMethod(call, "responseBodyEnd()", HttpMonitor.get(call).mResponseBodyDuration);
    }



    /**
     * 打印公共方法
     */
    private void logError(Call call, String method, long duration, Exception e) {
        if(!showEventLog){
            return;
        }
        try {
            String path = getPath(call);
            Logger.v(TAG, "[" + call.toString() + "] " + path + "-->" + method + ", spend：" + duration + "ms, message: " + e.getMessage());
        } catch (Throwable ex){
            Logger.exception("OkHttpEventListener", method + ".logError", e);
        }
    }

    private void logMethod(Call call, String method, long duration) {
        if(!showEventLog){
            return;
        }
        try {
            String path = getPath(call);
            Logger.v(TAG, "[" + call.toString() + "] " + path + "-->" + method + ", spend：" + duration + "ms.");
        } catch (Throwable e){
            Logger.exception("OkHttpEventListener", method + ".logMethod", e);
        }
    }

    private void logDnsParse(Call call, String ip, long duration) {
        try {
            String path = getPath(call);
            String url = call.request().url().toString();
            Logger.v(TAG, "[" + call.toString() + "] " + path + "-->dnsEnd(), dns parse spend " + duration + "ms.");
            TrackApi.tackDnsParse(url, duration, ip, 0, true);
        } catch (Throwable e){
            Logger.exception("OkHttpEventListener", "logDnsParse", e);
        }
    }

    private void logTsl(Call call, long duration) {
        try {
            String path = getPath(call);
            Logger.v(TAG, "[" + call.toString() + "] " + path + "-->secureConnectEnd(), tsl connect spend " + duration + "ms.");
            TrackApi.tackDnsParse(call.request().url().toString(), 0, "", duration, false);
        } catch (Throwable e){
            Logger.exception("OkHttpEventListener", "logTsl", e);
        }
    }

    private void logCallStart(Call call) {
        if(!showEventLog){
            return;
        }
        try {
            String path = getPath(call);
            Logger.d(TAG, "[" + call.toString() + "] " + "-->CallStart(), " + path);
        } catch (Throwable e){
            Logger.exception("OkHttpEventListener", "logCallStart", e);
        }
    }


    private String getPath(Call call){
        String url = call.request().url().toString();
        try {
            return Uri.parse(url).getPath();
        } catch (Throwable e){
            Logger.v(e);
            return url;
        }
    }
}
