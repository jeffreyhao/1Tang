package com.baidu.baselibrary.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.log.annotate.ClickId;
import com.baidu.baselibrary.log.annotate.LogFilePrefix;
import com.baidu.baselibrary.log.annotate.LogLevel;
import com.baidu.baselibrary.log.annotate.LogLifecycleTag;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.log.annotate.LogType;
import com.baidu.baselibrary.log.process.ALogProxy;
import com.baidu.baselibrary.util.sys.LogUtil;

import org.json.JSONObject;

import java.util.Map;


/**
 *  Alog api
 */
public final class ALog {


    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;


    private static LogConfig sConfig;


    private ALog() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }



    public static void init(Context context){
        ALog.getConfig(context)
                .setLogSwitch(true)         // 设置 log 总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(GlobalBuildConfig.LOG_DEBUG)    // 设置是否输出到控制台开关，默认开
                .setLog2FileSwitch(true)    // 打印 log 时是否存到文件的开关，默认关
                .setLogHeadSwitch(false)    // 设置 log 头信息开关，默认为开
                .setBorderSwitch(true)      // 输出日志是否带边框开关，默认开
                .setSingleTagSwitch(true)   // 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setConsoleFilter(ALog.V)   // log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(ALog.D)      // log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setGlobalTag("lhc")        // 设置 log 全局标签，默认为空。标签显示：如果传入的 tag 为空那就显示类名，否则显示 tag
                .setDir("")                 // 当自定义路径为空时，写入应用的 /cache/log/ 目录中
                .setFilePrefix("")           // 当文件前缀为空时，默认为 ""，即写入文件为 "yyyy-MM-dd.txt"
                .setStackDeep(2)            // log 栈深度，默认为 1
                .setStackOffset(0)          // 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setSaveDays(1);            // 设置日志可保留天数，默认为 -1 表示无限时长
    }


    public static LogConfig getConfig(Context context) {
        if (sConfig == null) {
            sConfig = new LogConfig(context);
        }
        return sConfig;
    }

    public static String getLogDir() {
        return ALogProxy.getLogDir(sConfig);
    }


    public static void v(Object... contents) {
        console(LogType.TEXT, V, sConfig.mGlobalTag, contents);
    }

    public static void vTag(String tag, Object... contents) {
        console(LogType.TEXT, V, tag, contents);
    }

    public static void d(Object... contents) {
        console(LogType.TEXT, D, sConfig.mGlobalTag, contents);
    }

    public static void dTag(String tag, Object... contents) {
        console(LogType.TEXT, D, tag, contents);
    }

    public static void i(Object... contents) {
        console(LogType.TEXT, I, sConfig.mGlobalTag, contents);
    }

    public static void iTag(String tag, Object... contents) {
        console(LogType.TEXT, I, tag, contents);
    }

    public static void w(Object... contents) {
        console(LogType.TEXT, W, sConfig.mGlobalTag, contents);
    }

    public static void e(Object... contents) {
        console(LogType.TEXT, E, sConfig.mGlobalTag, contents);
    }

    /**
     * 之前版本的log调用，现在只显示到控制台
     *
     * @param type      日志类型
     * @param level     日志级别
     * @param tag       日志tag
     * @param contents  日志内容
     */
    public static void console(@LogType int type, @LogLevel int level, String tag, Object... contents) {
        ALogProxy.console(sConfig, sConfig.mDirName, sConfig.mFilePrefix, type, level, tag, contents);
    }





    //////////////////////////////////////    以下为新 Api    //////////////////////////////////////


    /**
     *  click事件日志
     */
    public static void click(String className, String methodName,  ClickId clickId, String message) {
        standard(I, LogFilePrefix.STANDARD, LogType.JSON, className, methodName, new String[]{LogTag.ACTION, LogTag.Action_Click},
                ALogJsonBuilder.click(clickId.name(), clickId.toInt(), message));
    }


    /**
     * crash信息日志（来自uncaughtException()）
     */
    public static void crash(String className, String methodName, Throwable throwable) {
        standard(E, LogFilePrefix.CSH, LogType.EXCEPTION, className, methodName, new String[]{LogTag.CRASH}, throwable);
    }


    /**
     * 异常信息日志（来自try-catch）
     */
    public static void exception(Throwable throwable) {
        standard(E, LogFilePrefix.STANDARD, LogType.EXCEPTION, "", "", new String[]{LogTag.EXCEPTION}, throwable);
    }

    /**
     * 异常信息日志（来自try-catch）
     */
    public static void exception(String className, String methodName, Throwable throwable) {
        standard(E, LogFilePrefix.STANDARD, LogType.EXCEPTION, className, methodName, new String[]{LogTag.EXCEPTION}, throwable);
    }

    /**
     * 异常信息日志（来自try-catch）
     */
    public static void exception(@LogFilePrefix String filePrefix, String className, String methodName, Throwable throwable) {
        standard(E, filePrefix, LogType.EXCEPTION, className, methodName, new String[]{LogTag.EXCEPTION}, throwable);
    }

    /**
     * 单行文本日志
     */
    public static void textSingle(String text) {
        standard(I, LogFilePrefix.STANDARD, LogType.TEXT, "", "", new String[]{LogTag.SINGLE_LINE}, text);
    }

    /**
     * 单行文本日志
     */
    public static void textSingle(String className, String methodName, String text) {
        standard(I, LogFilePrefix.STANDARD, LogType.TEXT, className, methodName, new String[]{LogTag.SINGLE_LINE}, text);
    }

    /**
     * 单行文本日志
     */
    public static void textSingle(@LogLevel int level, String className, String methodName, String text) {
        standard(level, LogFilePrefix.STANDARD, LogType.TEXT, className, methodName, new String[]{LogTag.SINGLE_LINE}, text);
    }

    /**
     * 文本日志
     */
    public static void text(String text) {
        standard(I, LogFilePrefix.STANDARD, LogType.TEXT, "", "", new String[]{LogTag.TEXT}, text);
    }

    /**
     * 文本日志
     */
    public static void text(String className, String methodName, String text) {
        standard(I, LogFilePrefix.STANDARD, LogType.TEXT, className, methodName, new String[]{LogTag.TEXT}, text);
    }

    /**
     *  key-value日志，内容是将 key(content)-value 装进json
     */
    public static void keyValue(String className, String methodName, String[] tags, String value){
        keyValue(LogFilePrefix.STANDARD, className, methodName, tags, "", value);
    }

    /**
     *  key-value日志，内容是将key-value装进json
     */
    public static void keyValue(String className, String methodName, String[] tags, String key, String value){
        keyValue(LogFilePrefix.STANDARD, className, methodName, tags, key, value);
    }

    /**
     *  key-value日志，内容是将key-value装进json
     */
    public static void keyValue (@LogFilePrefix String filePrefix, String className, String methodName, String[] tags, String key, String value){
        JSONObject jsonObject = new JSONObject();
        if(!TextUtils.isEmpty(value)){
            try {
                jsonObject.put(TextUtils.isEmpty(key) ? "content" : key, value);
            } catch (Throwable e){
                LogUtil.e(e);
            }
        }
        standard(I, filePrefix, LogType.JSON, className, methodName, tags, jsonObject);
    }


    /**
     * map日志，转换为json
     */
    public static void map (String className, String methodName, String[] tags, Map<String, Object> map){
        map(LogFilePrefix.STANDARD, className, methodName, tags, map);
    }

    /**
     * map日志，转换为json
     */
    public static void map (@LogFilePrefix String filePrefix, String className, String methodName, String[] tags, Map<String, Object> map){
        JSONObject jsonObject = new JSONObject();
        if(map != null && map.size() > 0){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                try {
                    jsonObject.put(key, JSONObject.wrap(value));
                } catch (Throwable e){
                    LogUtil.e(e);
                }
            }
        }
        standard(I, filePrefix, LogType.JSON, className, methodName, tags, jsonObject);
    }

    /**
     * JSONObject日志
     *
     * @param json 该字符串json会装进JSONObject，如果失败则用文本日志
     */
    public static void jsonObject(String json) {
        if(TextUtils.isEmpty(json)){
            standard(I, LogFilePrefix.STANDARD, LogType.JSON, "", "", null, new JSONObject());
        } else {
            try {
                JSONObject jsonObject = new JSONObject(json);
                standard(I, LogFilePrefix.STANDARD, LogType.JSON, "", "", new String[]{LogTag.JSON}, jsonObject);
            } catch (Throwable e){
                text("", "", json);
            }
        }
    }

    /**
     * JSONObject日志
     */
    public static void jsonObject(JSONObject jsonObject) {
        standard(I, LogFilePrefix.STANDARD, LogType.JSON, "", "", null, jsonObject);
    }

    /**
     * JSONObject日志
     */
    public static void jsonObject(String className, String methodName, String[] tags, JSONObject jsonObject) {
        standard(I, LogFilePrefix.STANDARD, LogType.JSON, className, methodName, tags, jsonObject);
    }

    /**
     * JSONObject日志
     */
    public static void jsonObject(@LogFilePrefix String filePrefix, String className, String methodName, String[] tags, JSONObject jsonObject) {
        standard(I, filePrefix, LogType.JSON, className, methodName, tags, jsonObject);
    }

    public static void jsonObject(@LogLevel int level, String className, String methodName, String[] tags, JSONObject jsonObject) {
        standard(level, LogFilePrefix.STANDARD, LogType.JSON, className, methodName, tags, jsonObject);
    }

    /**
     * xml日志
     */
    public static void xml(String content) {
        standard(I, LogFilePrefix.STANDARD, LogType.XML, "", "", null, content);
    }

    /**
     * xml日志
     */
    public static void xml(@LogLevel int level, String content) {
        standard(level, LogFilePrefix.STANDARD, LogType.XML, "", "", null, content);
    }

    /**
     * 生命周期日志
     *
     * @param secondaryTag 二级标签（一级标签为：LIFECYCLE）
     */
    public static void lifeCycle(@LogLifecycleTag String secondaryTag, String className, String methodName){
        standard(I, LogFilePrefix.STANDARD, LogType.JSON, className, methodName, new String[]{LogTag.LIFECYCLE, secondaryTag}, new JSONObject());
    }

    /**
     * 空内容日志（只有时间等信息）
     */
    public static void none(String className, String methodName, String[] tags){
        standard(I, LogFilePrefix.STANDARD, LogType.TEXT, className, methodName, tags, new JSONObject());
    }

    /**
     * 打印堆栈信息
     */
    public static void printStackTrace(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        standard(E, LogFilePrefix.STANDARD, LogType.STACKTRACE, "ALog", "printStackTrace", new String[]{LogTag.STACKTRACE}, stackTrace);
    }

    /**
     * 标准化日志
     *
     * @param level       日志级别
     * @param filePrefix  文件前缀
     * @param className   所在类名
     * @param methodName  所在方法名
     * @param tags        标签： [一级标签][二级标签][三级标签]  {@link LogTag}
     * @param content     日志内容
     */
    public static void standard(
            @LogLevel int level,
            @LogFilePrefix String filePrefix,
            @LogType int logType,
            String className,
            String methodName,
            String[] tags,
            Object content){

        ALogProxy.standard(sConfig, level, filePrefix, logType, className, methodName, tags, content);
    }

}
