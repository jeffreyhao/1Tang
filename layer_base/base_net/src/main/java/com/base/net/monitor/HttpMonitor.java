package com.base.net.monitor;

import android.net.Uri;
import android.util.LruCache;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;


public class HttpMonitor  {

    /**
     * 正在请求接口的数量监听器
     */
    public interface OnRequestingCountChangListener{

        /**
         * 数量变化
         *
         * @param isAdd 是否在增加
         * @param path  正在处理的Call接口url
         * @param count 当前的请求数量
         */
        void onRequestCountChanged(boolean isAdd, String path, int count);
    }


    public static class Bean {

        public String path;

        private long mCallStartTime;
        public int mCallDuration;

        private long mDnsStartTime;
        public int mDnsDuration;

        private long mConnectStartTime;
        public int mConnectDuration;

        private long mSecureConnectStartTime;
        public int mSecureConnectDuration;

        private long mConnectionStartTime;
        public int mConnectionDuration;

        private long mRequestHeadersStartTime;
        public int mRequestHeadersDuration;

        private long mRequestBodyStartTime;
        public int mRequestBodyDuration;

        private long mResponseHeadersStartTime;
        public int mResponseHeadersDuration;

        private long mResponseBodyStartTime;
        public int mResponseBodyDuration;

    }

    private final static LruCache<String, Bean> eventPool = new LruCache<>(30);

    private final static Set<OnRequestingCountChangListener> mRequestingCountChangListeners = new HashSet<>();


    /**
     * 请求开始
     */
    public static void callStart(Call call) {
        get(call).mCallStartTime = System.nanoTime();
    }

    /**
     * 请求正常结束
     */
    public static void callEnd(Call call) {
        get(call).mCallDuration = spend(get(call).mCallStartTime);
    }

    /**
     * 请求异常结束
     */
    public static void callFailed(Call call, IOException ioe) {
        get(call).mCallDuration = spend(get(call).mCallStartTime);
    }

    /**
     * dns解析开始
     * DNS解析是请求DNS（Domain Name System）服务器。将域名解析成ip的过程。
     */
    public static void dnsStart(Call call, String domainName) {
        get(call).mDnsStartTime = System.nanoTime();
    }

    /**
     * dns解析结束
     */
    public static void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        get(call).mDnsDuration = spend(get(call).mDnsStartTime);
    }

    /**
     * 连接开始
     */
    public static void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        get(call).mConnectStartTime = System.nanoTime();
    }

    /**
     * 连接结束
     */
    public static void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        get(call).mConnectDuration = spend(get(call).mConnectStartTime);
    }

    /**
     * 连接失败
     */
    public static void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        get(call).mConnectDuration = spend(get(call).mConnectStartTime);
    }

    /**
     * TLS安全连接开始
     */
    public static void secureConnectStart(Call call) {
        get(call).mSecureConnectStartTime = System.nanoTime();
    }

    /**
     * TLS安全连接结束
     */
    public static void secureConnectEnd(Call call, Handshake handshake) {
        get(call).mSecureConnectDuration = spend(get(call).mSecureConnectStartTime);
    }

    /**
     * 连接绑定
     */
    public static void connectionAcquired(Call call, Connection connection) {
        get(call).mConnectionStartTime = System.nanoTime();
    }

    /**
     * 连接释放
     */
    public static void connectionReleased(Call call, Connection connection) {
        get(call).mConnectionDuration = spend(get(call).mConnectionStartTime);
    }

    /**
     * 请求Header开始
     */
    public static void requestHeadersStart(Call call) {
        get(call).mRequestHeadersStartTime = System.nanoTime();
    }

    /**
     * 请求Header结束
     */
    public static void requestHeadersEnd(Call call, Request request) {
        get(call).mRequestHeadersDuration = spend(get(call).mRequestHeadersStartTime);
    }

    /**
     * 请求Body开始
     */
    public static void requestBodyStart(Call call) {
        get(call).mRequestBodyStartTime = System.nanoTime();
    }

    /**
     * 请求Body结束
     */
    public static void requestBodyEnd(Call call, long byteCount) {
        get(call).mRequestBodyDuration = spend(get(call).mRequestBodyStartTime);
    }

    /**
     * 响应Header开始
     */
    public static void responseHeadersStart(Call call) {
        get(call).mResponseHeadersStartTime = System.nanoTime();
    }

    /**
     * 响应Header结束
     */
    public static void responseHeadersEnd(Call call, Response response) {
        get(call).mResponseHeadersDuration = spend(get(call).mResponseHeadersStartTime);
    }

    /**
     * 响应Body开始
     */
    public static void responseBodyStart(Call call) {
        get(call).mResponseBodyStartTime = System.nanoTime();
    }

    /**
     * 响应Body结束
     */
    public static void responseBodyEnd(Call call, long byteCount) {
        get(call).mResponseBodyDuration = spend(get(call).mResponseBodyStartTime);
    }


    public static Bean get(Call call){
        String hash = call.toString();
        Bean bean = eventPool.get(hash);
        if(bean == null){
            bean = new Bean();
            bean.path = getPath(call);
            eventPool.put(hash, bean);
            notifyRequestCountChange(true, bean.path, eventPool.size());
        }
        return bean;
    }

    public static Bean remove(Call call){
        String hash = call.toString();
        Bean bean = eventPool.remove(hash);
        if(bean != null){
            notifyRequestCountChange(false, bean.path, eventPool.size());
        }
        return bean;
    }


    public static void registerRequestingCountChangListener(OnRequestingCountChangListener listener) {
        synchronized (HttpMonitor.class) {
            if (listener != null) {
                mRequestingCountChangListeners.add(listener);
            }
        }
    }

    public static void unregisterRequestingCountChangListener(OnRequestingCountChangListener listener) {
        synchronized (HttpMonitor.class) {
            mRequestingCountChangListeners.remove(listener);
        }
    }

    private static void notifyRequestCountChange(boolean isAdd, String path, int count){
        synchronized (HttpMonitor.class) {
            for (OnRequestingCountChangListener observer : mRequestingCountChangListeners) {
                observer.onRequestCountChanged(isAdd, path, count);
            }
        }
    }

    private static int spend(long startTime){
        long time = System.nanoTime() - startTime;
        int spend = (int) (time / 1000000d);
        return spend;
    }

    private static String getPath(Call call){
        String url = call.request().url().toString();
        try {
            return Uri.parse(url).getPath();
        } catch (Throwable e){
            return url;
        }
    }

}
