package com.base.api;

import com.base.abs.ILog;

import org.json.JSONObject;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public class Logger {

    public static ILog iLog;




    ////////////////////////////////////    走的LogUtil    /////////////////////////////////////////


    public static void v(String tag, String msg) {
        if(iLog != null){
            iLog.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if(iLog != null){
            iLog.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if(iLog != null){
            iLog.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if(iLog != null){
            iLog.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if(iLog != null){
            iLog.e(tag, msg);
        }
    }


    ////////////////////////////////////    走的ALog    /////////////////////////////////////////




    public static void v(Object obj) {
        if(iLog != null){
            iLog.v(obj);
        }
    }

    public static void d(Object obj) {
        if(iLog != null){
            iLog.d(obj);
        }
    }

    public static void i(Object obj) {
        if(iLog != null){
            iLog.i(obj);
        }
    }

    public static void w(Object obj) {
        if(iLog != null){
            iLog.w(obj);
        }
    }

    public static void e(Object obj) {
        if(iLog != null){
            iLog.e(obj);
        }
    }


    public static void verbose(String tag, String msg) {
        if(iLog != null){
            iLog.verbose(tag, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if(iLog != null){
            iLog.debug(tag, msg);
        }
    }

    public static void info(String tag, String msg) {
        if(iLog != null){
            iLog.info(tag, msg);
        }
    }

    public static void warn(String tag, String msg) {
        if(iLog != null){
            iLog.warn(tag, msg);
        }
    }

    public static void error(String tag, String msg) {
        if(iLog != null){
            iLog.error(tag, msg);
        }
    }

    public static void textSingle(String msg){
        if(iLog != null){
            iLog.textSingle(msg);
        }
    }

    public static void textSingle(String className, String methodName, String text){
        if(iLog != null){
            iLog.textSingle(className, methodName, text);
        }
    }

    public static void json(JSONObject json){
        if(iLog != null){
            iLog.json(json);
        }
    }


    ////////////////////////////////////     异常日志     /////////////////////////////////////////

    public static void printStackTrace() {
        if(iLog != null){
            iLog.printStackTrace();
        }
    }


    public static void exception(Throwable e) {
        if(iLog != null){
            iLog.exception(e);
        }
    }

    public static void exception(String className, String methodName, Throwable text){
        if(iLog != null){
            iLog.exception(className, methodName, text);
        }
    }

    public static void sendWebHook(Thread thread, Throwable e, String positionInfo){
        if(iLog != null){
            iLog.sendWebHook(thread, e, positionInfo);
        }
    }

}
