package com.base.abs;

import org.json.JSONObject;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public interface ILog {


    // 走的 LogUtil

    void v(String tag, String msg);
    void d(String tag, String msg);
    void i(String tag, String msg);
    void w(String tag, String msg);
    void e(String tag, String msg);


    // 以下走的 ALog

    void v(Object obj);
    void d(Object obj);
    void i(Object obj);
    void w(Object obj);
    void e(Object obj);

    void verbose(String tag, String msg);
    void debug(String tag, String msg);
    void info(String tag, String msg);
    void warn(String tag, String msg);
    void error(String tag, String msg);


    void json(JSONObject json);

    void textSingle(String msg);
    void textSingle(String className, String methodName, String text);


    void printStackTrace();
    void exception(Throwable e);
    void exception(String className, String methodName, Throwable e);
    void sendWebHook(Thread thread, Throwable e, String positionInfo);
}
