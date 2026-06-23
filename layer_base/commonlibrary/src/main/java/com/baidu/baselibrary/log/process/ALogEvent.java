package com.baidu.baselibrary.log.process;

import com.baidu.baselibrary.util.sys.EventUtil;

import java.util.List;

/**
 * Created by haojiangfeng on 2024/6/19.
 */
public class ALogEvent {

    public static final int TYPE_CONTENT        = 1;
    public static final int TYPE_EXCEPTION      = 2;
    public static final int TYPE_ERROR          = 3;
    public static final int TYPE_OUT_OF_DATE    = 4;


    public int type;
    public String className;
    public String methodName;
    public String message;
    public Object object;



    public ALogEvent(int type, String className, String methodName, String message){
        this(type, className, methodName, message, null);
    }

    public ALogEvent(int type, String className, String methodName, String message, Object object){
        this.type = type;
        this.className = className == null ? "" : className;
        this.methodName = methodName == null ? "" : methodName;
        this.message = message == null ? "" : message;
        this.object = object;
    }


    public static void postContent(String className, String methodName, String content) {
        EventUtil.post(new ALogEvent(TYPE_CONTENT, className, methodName, content));
    }

    public static void postException(String className, String methodName, Exception e) {
        String body = LogFormatter.formatObject(e);
        EventUtil.post(new ALogEvent(TYPE_EXCEPTION, className, methodName, body));
    }

    public static void postError(String className, String methodName, String error) {
        EventUtil.post(new ALogEvent(TYPE_ERROR, className, methodName, error));
    }

    public static void postOutOfDateLogs(String className, String methodName, List<String> outOfDateLogs) {
        EventUtil.post(new ALogEvent(TYPE_OUT_OF_DATE, className, methodName, "", outOfDateLogs));
    }
}
