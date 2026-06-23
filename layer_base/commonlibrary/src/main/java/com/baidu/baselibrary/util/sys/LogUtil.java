package com.baidu.baselibrary.util.sys;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 用于输出日志。<br/>
 * 可以输入日志级别V/I/D/W/E，每个级别都有三种种重载方法：<br/>
 * 两个String参数的方法可以自定义TAG和message，一个String参数的方法可以自动获取当前类名作为TAG。
 * 另外还加入了传入 {@link Throwable} 参数的方法，便于直接输出异常信息。 <br/>
 * 输出日志的message部分都会包含输出日志所在位置的方法名和行号，方便查找。
 */
public class LogUtil {

    //日志状态，开发设置为true，上线设置为false
    private static boolean    DEBUG              = true;
    private static int        MAX_LEN            = 4000;


    private static String classname;
    private static ArrayList<String> methods;


    static {
        classname = LogUtil.class.getName();
        methods = new ArrayList<String>();
        Method[] ms = LogUtil.class.getDeclaredMethods();
        for (Method m : ms) {
            methods.add(m.getName());
        }
    }


    public static void setDebug(boolean isDebug) {
        DEBUG = isDebug;
    }



    /**
     * 输出Vorbose级别的日志。
     *
     * @param msg 日志内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void v(String msg) {
        if (DEBUG) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            print(Log.VERBOSE, content[0], content[1]);
        }
    }

    /**
     * 输出Vorbose级别的日志。
     *
     * @param tag TAG.
     * @param msg 日志内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void v(String tag, String msg) {
        if (DEBUG) {
            print(Log.VERBOSE, tag, getMsgWithLineNumber(msg));
        }
    }

    /**
     * 输出Vorbose级别的异常日志。
     *
     * @param t 异常对象。
     * @since 2014-7-10
     */
    public static void v(Throwable t) {
        v(t.getMessage());
    }

    /**
     * 输出Debug级别的日志，自动获取类名作为TAG。
     *
     * @param msg 输出内容，带有方法名和行号。
     * @since 2014-7-10
     */

    public static void d(String msg) {
        if (DEBUG) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            print(Log.DEBUG, content[0], content[1]);
        }
    }

    /**
     * 输出Debug级别的日志。
     *
     * @param tag TAG.
     * @param msg 输出内容，带有方法名和行号。
     * @since 2014-7-10
     */
    public static void d(String tag, String msg) {
        if (DEBUG) {
            print(Log.DEBUG, tag, getMsgWithLineNumber(msg));
        }
    }

    /**
     * 输出Debug级别的异常日志。
     *
     * @param t 异常对象。
     * @since 2014-7-10
     */
    public static void d(Throwable t) {
        d(t.getMessage());
    }


    /**
     * 输出Info级别的日志。
     *
     * @param msg 日志内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void i(String msg) {
        if (DEBUG) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            Log.println(Log.INFO, content[0], content[1]);
        }
    }

    /**
     * 输出Info级别的日志。
     *
     * @param tag TAG.
     * @param msg 日志内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void i(String tag, String msg) {
        if (DEBUG) {
            print(Log.INFO, tag, getMsgWithLineNumber(msg));
        }
    }

    /**
     * 输出Info级别的异常日志。
     *
     * @param t 异常对象。
     * @since 2014-7-10
     */
    public static void i(Throwable t) {
        i(t.getMessage());
    }

    /**
     * 输出Warn级别的日志。
     *
     * @param msg 日志内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void w(String msg) {
        if (DEBUG) {
            String[] content = getMsgAndTagWithLineNumber(msg);
            print(Log.WARN, content[0], content[1]);
        }
    }

    /**
     * 输出Warn级别的日志。
     *
     * @param tag TAG.
     * @param msg 日志内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void w(String tag, String msg) {
        if (DEBUG) {
            print(Log.WARN, tag, getMsgWithLineNumber(msg));
        }
    }

    /**
     * 输出Warn级别的异常日志。
     *
     * @param t 异常对象。
     * @since 2014-7-10
     */
    public static void w(Throwable t) {
        w(t.getMessage());
    }

    /**
     * 输出Error级别的日志。
     *
     * @param tag TAG.
     * @param msg 输出内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void e(String tag, String msg) {
        if (DEBUG) {
            print(Log.ERROR, tag, getMsgWithLineNumber(msg));
        }
    }

    /**
     * 输出Error级别的日志。
     *
     * @param msg 输出内容，带有方法名和行号。
     * @since 2014年3月4日
     */
    public static void e(String msg) {
        if (DEBUG) {
//            ALog.e(msg);
            String[] content = getMsgAndTagWithLineNumber(msg);
            print(Log.ERROR, content[0], content[1]);
        }
    }

    /**
     * 输出Error级别的异常日志。
     *
     * @param t 异常对象。
     * @since 2014-7-10
     */
    public static void e(Throwable t) {
        if (DEBUG) {
            t.printStackTrace();
        }
    }





    /**
     * 获取日志信息的TAG、带行号的内容。
     *
     * @param msg 日志内容。
     * @return TAG、带行号的内容组成的字符串数组。
     * @since 2014年3月4日
     */
    private static String[] getMsgAndTagWithLineNumber(String msg) {
        try {
            for (StackTraceElement st : (new Throwable()).getStackTrace()) {
                if (classname.equals(st.getClassName()) || methods.contains(st.getMethodName())) {
                    continue;
                } else {
                    int b = st.getClassName().lastIndexOf(".") + 1;
                    String tag = st.getClassName().substring(b);
                    String message = "[" + st.getMethodName() + "():" + st.getLineNumber() + "]->  " + msg;
                    String[] content = new String[]{tag, message};
                    return content;
                }

            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return new String[]{"MoboGenie", msg};
    }

    /**
     * 获取类名
     *
     * @return
     */
    private static String getClassName() {
        try {
            for (StackTraceElement st : (new Throwable()).getStackTrace()) {
                if (classname.equals(st.getClassName()) || methods.contains(st.getMethodName())) {
                    continue;
                } else {
                    int b = st.getClassName().lastIndexOf(".") + 1;
                    String tag = st.getClassName().substring(b);
                    String content = tag;
                    return content;
                }
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return null;
    }

    /**
     * 获取带行号的日志信息内容。
     *
     * @param msg 日志内容。
     * @return 带行号的日志信息内容。
     * @since 2014年3月4日
     */
    private static String getMsgWithLineNumber(String msg) {
        try {
            for (StackTraceElement st : (new Throwable()).getStackTrace()) {
                if (classname.equals(st.getClassName()) || methods.contains(st.getMethodName())) {
                    continue;
                } else {
                    int b = st.getClassName().lastIndexOf(".") + 1;
                    String tag = st.getClassName().substring(b);
                    String message = "[" + tag + "->" + st.getMethodName() + "():" + st.getLineNumber() + "]->  " + msg;
                    return message;
                }
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return msg;
    }

    private static void print(int priority, String tag, String msg){
        if(TextUtils.isEmpty(msg) || msg.length() < MAX_LEN){
            Log.println(priority, tag, msg);
        } else {
            int len = msg.length();
            int count = len / MAX_LEN;
            if(len % MAX_LEN != 0){
                count ++;
            }
            for(int i = 0; i < count; i++){
                if(i == count - 1){
                    String message = msg.substring(i * MAX_LEN, len);
                    Log.println(priority, tag, message);
                } else {
                    String message = msg.substring(i * MAX_LEN, (i+1)*MAX_LEN);
                    Log.println(priority, tag, message);
                }
            }
        }
    }


}
