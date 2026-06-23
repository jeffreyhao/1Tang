package com.example.crypto;

import android.content.Context;

import androidx.annotation.Keep;

import com.base.api.Logger;
import com.base.util.hybrid.WebCall;
import com.base.util.hybrid.WebCallListener;
import com.base.util.safe.ShaUtil;


@Keep
public class XyzKQ99 {

    private static boolean inited;

    static {
        try {
            System.loadLibrary("kq_meta_hybrid"); // 注意这里要匹配生成的 .so 名称 libkq.so
            inited = true;
        } catch (UnsatisfiedLinkError e) {
            Logger.sendWebHook(Thread.currentThread(), e, "XyzKQ99.static()");
            Logger.exception(e);
            inited = false;
        }
    }

    public static boolean init(){
        Logger.textSingle("XyzKQ99", "init", "XyzKQ99.inited=" + inited);
        return inited;
    }

    public static byte[] doSth(Context context, byte[] encrypted, String packageName, String sha256){
        return process(context, encrypted, packageName, sha256, getHybridListener());
    }

    public static boolean c_xcyh(Context context, String packageName) {
        return xcyh(context, packageName, ShaUtil.getAppSignaturesHash(), getHybridListener());
    }

    public static boolean ttrc() {
        return ttrc(getHybridListener());
    }


    // 原生方法：传入上下文和加密数据，返回解密后数据
    private static native byte[] process(Context context, byte[] encrypted, String packageName, String sha256, WebCallListener listener);

    private static native boolean xcyh(Context context, String packageName, String sha, WebCallListener listener);

    private static native boolean ttrc(WebCallListener listener);

    public static native byte[] rrccres(byte[] encryptedData);

    private static WebCallListener getHybridListener(){
        try {
            return WebCall.sHybrid;
        } catch (Throwable e){
            Logger.exception(e);
            return null;
        }
    }
}