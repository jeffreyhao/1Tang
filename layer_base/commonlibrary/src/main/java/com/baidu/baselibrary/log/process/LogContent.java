package com.baidu.baselibrary.log.process;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.LogConfig;
import com.base.util.AppUtil;
import com.baidu.baselibrary.util.date.DateUtil;
import com.baidu.baselibrary.util.date.TimeUtil;
import com.baidu.baselibrary.util.UserManager;

import java.util.Formatter;


/**
 *  旧的日志格式
 */
class LogContent {

    static class TagHead {

        String tag;
        String[] consoleHead;
        String fileHead;

        public TagHead(String tag, String[] consoleHead, String fileHead) {
            this.tag = tag;
            this.consoleHead = consoleHead;
            this.fileHead = fileHead;
        }
    }

    private static final String NOTHING         = "log nothing";
    private static final String ARGS            = "args";


    static TagHead processTagAndHead(LogConfig sConfig, String tag) {
        if (!sConfig.mTagIsSpace && !sConfig.mLogHeadSwitch) {
            tag = sConfig.mGlobalTag;
        } else {
            final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            final int stackIndex = 3 + sConfig.mStackOffset;
            if (stackIndex >= stackTrace.length) {
                StackTraceElement targetElement = stackTrace[3];
                final String fileName = LogFileProcessor.getFileName(targetElement);
                if (sConfig.mTagIsSpace && LogFileProcessor.isSpace(tag)) {
                    int index = fileName.indexOf('.');// Use proguard may not find '.'.
                    tag = index == -1 ? fileName : fileName.substring(0, index);
                }
                return new TagHead(tag, null, ": ");
            }
            StackTraceElement targetElement = stackTrace[stackIndex];
            final String fileName = LogFileProcessor.getFileName(targetElement);
            if (sConfig.mTagIsSpace && LogFileProcessor.isSpace(tag)) {
                int index = fileName.indexOf('.');// Use proguard may not find '.'.
                tag = index == -1 ? fileName : fileName.substring(0, index);
            }
            if (sConfig.mLogHeadSwitch) {
                String tName = Thread.currentThread().getName();
                final String head = new Formatter()
                        .format("%s, %s.%s(%s:%d)",
                                tName,
                                targetElement.getClassName(),
                                targetElement.getMethodName(),
                                fileName,
                                targetElement.getLineNumber())
                        .toString();
                final String fileHead = " [" + head + "]: ";
                if (sConfig.mStackDeep <= 1) {
                    return new TagHead(tag, new String[]{head}, fileHead);
                } else {
                    final String[] consoleHead =
                            new String[Math.min(
                                    sConfig.mStackDeep,
                                    stackTrace.length - stackIndex
                            )];
                    consoleHead[0] = head;
                    int spaceLen = tName.length() + 2;
                    String space = new Formatter().format("%" + spaceLen + "s", "").toString();
                    for (int i = 1, len = consoleHead.length; i < len; ++i) {
                        targetElement = stackTrace[i + stackIndex];
                        consoleHead[i] = new Formatter()
                                .format("%s%s.%s(%s:%d)",
                                        space,
                                        targetElement.getClassName(),
                                        targetElement.getMethodName(),
                                        LogFileProcessor.getFileName(targetElement),
                                        targetElement.getLineNumber())
                                .toString();
                    }
                    return new TagHead(tag, consoleHead, fileHead);
                }
            }
        }
        return new TagHead(tag, null, ": ");
    }


    static String processBody(final int type, final Object... contents) {
        try{
            String body = LogConst.NULL;
            if (contents != null) {
                if (contents.length == 1) {
                    body = LogFormatter.formatObject(type, contents[0]);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0, len = contents.length; i < len; ++i) {
                        Object content = contents[i];
                        sb.append(ARGS)
                                .append("[")
                                .append(i)
                                .append("]")
                                .append(" = ")
                                .append(LogFormatter.formatObject(content))
                                .append(LogConst.Sep.LINE_SEP);
                    }
                    body = sb.toString();
                }
            }
            return body==null||body.length() == 0 ? NOTHING : body;
        }catch (Exception e){
            e.printStackTrace();
            return NOTHING;
        }
    }

    static String getFileHead(LogConfig sConfig){
        String appName = "";
        String versionName = "";
        int versionCode = 0;
        try {
            PackageInfo pi = sConfig.sAppContext
                    .getPackageManager()
                    .getPackageInfo(sConfig.sAppContext.getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
                appName = sConfig.sAppContext.getResources().getString(pi.applicationInfo.labelRes);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String serverTime = DateUtil.getDateWithBeijingTime(DateUtil.formatYMDHMS, TimeUtil.getServerTimeOrPhoneTime());  // 北京时区时间
        String beijingTime = DateUtil.getDateWithBeijingTime(DateUtil.formatYMDHMS, System.currentTimeMillis());  // 北京时区时间
        String deviceYmdHms = DateUtil.getDateYMDHMS();
        String timeZone = AppUtil.getTimeZone();
        String timeZoneGMT = DateUtil.getTimeZoneGMT();
        String countryCode = AppUtil.getCountryCode();
        String deviceLanguage = AppUtil.getDeviceLanguage();
        String uid = UserManager.getUserId();
        final String head =
                "************* Log Head ****************" +
                        "\nServer Time          : " + serverTime +
                        "\nBeijing Time         : " + beijingTime +
                        "\nDevice Time          : " + deviceYmdHms +
                        "\nDevice timeZone      : " + timeZone +
                        "\nDevice timeZoneGMT   : " + timeZoneGMT +
                        "\nDevice countryCode   : " + countryCode +
                        "\nDevice Language      : " + deviceLanguage +
                        "\nDevice Manufacturer  : " + Build.MANUFACTURER +
                        "\nDevice Model         : " + Build.MODEL +
                        "\nAndroid Version      : " + Build.VERSION.RELEASE +
                        "\nAndroid SDK          : " + Build.VERSION.SDK_INT +
                        "\nApp Name             : " + appName +
                        "\nApp VersionName      : " + versionName +
                        "\nApp VersionCode      : " + versionCode +
                        "\nUid                  : " + uid +
                        "\n************* Log Head ****************\n\n";
        return head;
    }


    static String getLogContent(final int type, final String tag, final String msg){
        String fixedTime = DateUtil.getDateWithBeijingTime(DateUtil.formatHMS, DateUtil.getFixedTimeStamp());  // 北京时区时间
        StringBuilder sb = new StringBuilder();
        sb.append(fixedTime)
                .append(LogConst.T[type - ALog.V])
                .append("/")
                .append(tag)
                .append(msg)
                .append(LogConst.Sep.LINE_SEP);
        return sb.toString();
    }

    static String tag(String[] tags){
        if(tags == null || tags.length == 0){
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        for(String tag: tags){
            sb.append("[");
            sb.append(tag);
            sb.append("]");
        }
        return sb.toString();
    }

}
