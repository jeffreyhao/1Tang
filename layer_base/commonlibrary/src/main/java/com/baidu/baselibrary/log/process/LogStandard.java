package com.baidu.baselibrary.log.process;

import android.text.TextUtils;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.LogConfig;
import com.baidu.baselibrary.log.annotate.LogFilePrefix;
import com.baidu.baselibrary.log.annotate.LogLevel;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.log.annotate.LogType;
import com.baidu.baselibrary.util.date.DateUtil;
import com.base.util.content.StringUtils;
import com.baidu.baselibrary.util.date.TimeUtil;

import org.json.JSONObject;


/**
 *  标准化日志
 *
 *      格式： < 服务器时间 | 客户端时间 | 日志级别 | 内容类型 | 当前线程 >  (类名.方法名) [一级标签][二级标签][三级标签]       {日志内容}
 *      例子： <2024-05-06 17:43:26:344|0506174326|E|TEXT|main> (DeepLinkingManager.parseClipBoard) [DEEPLINK][Manager][]     {日志内容}
 *
 *      客户端时间：0506174326。代表 05月06日，17:43:26
 */
class LogStandard {



    /**
     * @param sConfig sConfig
     * @param dirName dirName
     * @param tags tags
     * @param object string or json
     */
    static void print2File(LogConfig sConfig, @LogFilePrefix String filePrefix, String dirName, @LogLevel int level, @LogType int logType, String className, String methodName, String[] tags, Object object) {
        String dir = LogFileProcessor.getLogDir(sConfig);
        String logDir = (dirName == null ? dir : dir.replace(sConfig.mDirName, dirName));
        String date = DateUtil.getFixedDateWithBeijingTime();  // 北京时区日期
        String fullPath = logDir + LogFileProcessor.getLogFileName(filePrefix, date);

        // 日志内容
        String content = getStandardContent(level, logType, className, methodName, tags, object);

        // 写入文件
        LogFileProcessor.createOrInputFile(sConfig, content, fullPath);
    }


    /**
     * @param level level
     * @param className className
     * @param methodName methodName
     * @param tags tags
     * @param object  object
     *
     * @return  <2024-05-06 17:43:26:344|0506174326|E|TEXT|main>(DeepLinkingManager.parseClipBoard) [][][]
     *          {content}
     */
    static String getStandardContent(@LogLevel int level, @LogType int logType, String className, String methodName, String[] tags, Object object){
        StringBuilder sb = new StringBuilder();
        appendCommonParams(sb, level, logType);
        appendClassInfo(sb, className, methodName);
        appendTags(sb, tags);
        sb.append("\n");
        appendContent(sb, tags, object);
        sb.append("\n");
        return sb.toString();
    }

    static void appendContent(StringBuilder sb, String[] tags, Object object){
        if(object == null){
            sb.append("{}");
        } else if(object instanceof Throwable){
            appendThrowable(sb, (Throwable) object);
        } else if(object instanceof JSONObject){
            appendJSONObject(sb, (JSONObject) object);
        } else if(object instanceof String) {
            appendString(sb, (String) object, containsTag(tags, LogTag.SINGLE_LINE));
        } else if(object instanceof StackTraceElement[]){
            appendStackTrace(sb, (StackTraceElement[]) object);
        } else {
            appendOther(sb, object);
        }
    }


    /**
     * 格式：
     * {
     *     "content": "append other content"
     * }
     */
    static void appendOther(StringBuilder sb, Object object){
        String content = LogFormatter.formatObject(object);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", content);
            sb.append(jsonObject.toString(4));
        } catch (Throwable e){
            e.printStackTrace();
            ALog.exception("LogStandard", "appendOther", e);
        }
    }

    /**
     * 格式：
     * {
     * java.lang.Throwable
     * 	    at com.benefit.novelverse.view.MainActivity.onResume()
     * 	    at android.app.Instrumentation.callActivityOnResume()
     * }
     */
    static void appendThrowable(StringBuilder sb, Throwable throwable){
        try {
            sb.append("{\n");
            String body = LogFormatter.formatObject(throwable);
            sb.append(body);
            sb.append("}");
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    static void appendStackTrace(StringBuilder sb, StackTraceElement[] stackTrace){
        try {
            sb.append("{\n");
            String body = LogFormatter.stackTraceToString(stackTrace);
            sb.append(body);
            sb.append("}");
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 格式：
     * {
     *     "name": "Json",
     *     "url": "",
     *     "age": 8,
     *     "male": true
     * }
     */
    static void appendJSONObject(StringBuilder sb, JSONObject jsonObject){
        try {
            sb.append(jsonObject.toString(4));
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 格式：
     * {
     *      这是一行文本信息
     * }
     */
    static void appendString(StringBuilder sb, String text, boolean isSingleLine){
        try {
            if(TextUtils.isEmpty(text)){
                sb.append("{}");
            } else {
                sb.append("{\n");
                if(isSingleLine){
                    sb.append("     ");
                }
                sb.append(text);
                if(!text.endsWith("\n")){
                    sb.append("\n");
                }
                sb.append("}");
            }
        } catch (Throwable e){
            e.printStackTrace();
        }
    }


    /**
     * <20240506 17:43:26:344|0506174326|E|TEXT|main>
     *
     * @param sb StringBuilder
     * @param level  LogLevel
     * @param logType logType
     */
    static void appendCommonParams(StringBuilder sb, @LogLevel int level, @LogType int logType){
        String deviceTime = DateUtil.getDateWithBeijingTime(DateUtil.yyyyMMddHHmmssSSS, System.currentTimeMillis());
        String serverTime = DateUtil.getDateWithBeijingTime(DateUtil.Md_Hms, TimeUtil.getServerTimeOrZero());
        sb.append("<");
        sb.append(deviceTime);
        sb.append(LogConst.Sep.PIP_SEP);
        sb.append("S-");
        sb.append(serverTime);
        sb.append(LogConst.Sep.PIP_SEP);
        sb.append(LogConst.T[level - ALog.V]);
        sb.append(LogConst.Sep.PIP_SEP);
        sb.append(LogConst.typeString(logType));
        sb.append(LogConst.Sep.PIP_SEP);
        sb.append(Thread.currentThread().getName());
        sb.append(">");
    }

    /**
     * @param tags [一级标签][二级标签][三级标签]
     */
    static void appendTags(StringBuilder sb, String[] tags) {
        if(sb == null){
            return;
        }
        if(tags == null || tags.length == 0){
            sb.append("  [][][]");
        } else {
            sb.append("  ");
            int tagsLength = tags.length;
            for(int i = 0; i < 3; i++){
                if(i < tagsLength){
                    sb.append("[");
                    String tag = StringUtils.isEmptyNull(tags[i]) ? "" : tags[i];
                    sb.append(tag);
                    sb.append("]");
                } else {
                    sb.append("[]");
                }
            }
        }
    }

    /**
     *  (类名.方法名)
     *  <br>
     *  没有内容的表示：(.)
     */
    private static void appendClassInfo(StringBuilder sb, String className, String methodName) {
        sb.append(" (");
        sb.append(TextUtils.isEmpty(className) ? "" : className);
        sb.append(LogConst.Sep.DOT_SEP);
        if(TextUtils.isEmpty(methodName)){
            sb.append("");
        } else if(methodName.endsWith("()")){
            sb.append(methodName.replace("()", ""));
        } else {
            sb.append(methodName);
        }
        sb.append(") ");
    }


    private static boolean containsTag(String[] tags, String tag){
        if(tags == null || tags.length == 0 || TextUtils.isEmpty(tag)){
            return false;
        }
        for(String eachTag : tags){
            if(eachTag != null && eachTag.equals(tag)){
                return true;
            }
        }
        return false;
    }

}
