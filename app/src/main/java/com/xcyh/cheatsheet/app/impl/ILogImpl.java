package com.xcyh.cheatsheet.app.impl;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.abs.ILog;
import com.benefit.webhook.WebHookCrashUtil;

import org.json.JSONObject;

/**
 * Created by haojiangfeng on 2024/10/30.
 */
public class ILogImpl implements ILog {


    public static ILogImpl get(){
        return new ILogImpl();
    }



    @Override
    public void v(Object obj) {
        ALog.v(obj);
    }

    @Override
    public void d(Object obj) {
        ALog.d(obj);
    }

    @Override
    public void i(Object obj) {
        ALog.i(obj);
    }

    @Override
    public void w(Object obj) {
        ALog.w(obj);
    }

    @Override
    public void e(Object obj) {
        ALog.e(obj);
    }

    @Override
    public void v(String tag, String msg) {
        LogUtil.v(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        LogUtil.d(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        LogUtil.i(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        LogUtil.w(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        LogUtil.e(tag, msg);
    }

    @Override
    public void verbose(String tag, String msg) {
        ALog.vTag(tag, msg);
    }

    @Override
    public void debug(String tag, String msg) {
        ALog.dTag(tag, msg);
    }

    @Override
    public void info(String tag, String msg) {
        ALog.iTag(tag, msg);
    }

    @Override
    public void warn(String tag, String msg) {
        ALog.textSingle(ALog.W, "ILogImpl", "warn", "[" + tag + "] " + msg);
    }

    @Override
    public void error(String tag, String msg) {
        ALog.textSingle(ALog.E, "ILogImpl", "error", "[" + tag + "] " + msg);
    }



    @Override
    public void json(JSONObject json) {
        ALog.jsonObject(json);
    }

    @Override
    public void textSingle(String msg) {
        ALog.textSingle(msg);
    }

    @Override
    public void textSingle(String className, String methodName, String text) {
        ALog.textSingle(className, methodName, text);
    }

    @Override
    public void printStackTrace() {

    }

    @Override
    public void exception(Throwable e) {
        ALog.exception(e);
    }

    @Override
    public void exception(String className, String methodName, Throwable e) {
        ALog.exception(className, methodName, e);
    }

    @Override
    public void sendWebHook(Thread thread, Throwable e, String positionInfo) {
        WebHookCrashUtil.sendCrash(thread, e, positionInfo);
    }
}
