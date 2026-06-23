package com.base.net.webhook;

import android.os.Build;
import android.text.TextUtils;

import com.base.api.UserApi;
import com.base.net.ApiBase;
import com.base.util.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by haojiangfeng on 2025/5/28.
 */
public class WebHookCrashUtil {


    public static void sendCrash(boolean isCrash, Thread thread, Throwable ex, String position) {
        try {
            String info = collectCrashInfo(thread, ex, position);
            String title = buildTitle(isCrash);
            String content = buildJson(title, info.replace("\"", "\\\""));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WebHookUtil.sendText("https://oapi.dingtalk.com/robot/send?access_token=" + WebHookUtil.getCrashHookToken(ex), content);
                }
            }).start();
        } catch (Throwable e){
            e.printStackTrace();
        }
    }


    private static String buildJson(String title, String info) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        JSONObject contentJson = new JSONObject();
        contentJson.put("content", title + info);
        jsonObject.put("text", contentJson);
        return jsonObject.toString();
    }

    private static String buildTitle(boolean isCrash){
        StringBuilder sb = new StringBuilder();
        if(isCrash) {
            if(ApiBase.DEBUG) {
                sb.append("Debug崩溃通知");
            } else {
                sb.append("线上崩溃通知");
            }
        } else {
            if(ApiBase.DEBUG) {
                sb.append("Debug重要异常捕获");
            } else {
                sb.append("线上重要异常捕获");
            }
        }
        sb.append(" （");
        sb.append(AppUtil.getPackageName());
        sb.append(" V");
        sb.append(AppUtil.getVersionName()).append("-").append(AppUtil.getVersionCode());
        sb.append("）（");
        String userId = UserApi.getUserId();
        sb.append(TextUtils.isEmpty(userId) ? "no user id" : "Uid: " + userId);
        sb.append("）\n");
        return sb.toString();
    }

    private static String collectCrashInfo(Thread thread, Throwable ex, String position) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);  // 记录时间
        String time = sdf.format(new Date());
        sb.append("【设备】").append(Build.BRAND).append(" ").append(Build.MODEL).append(" Android").append(Build.VERSION.RELEASE).append("\n");
        sb.append("【时间】").append(time).append(" ").append(AppUtil.getTimeZone()).append(" ").append(AppUtil.getCountryCode()).append("\n");
        sb.append("【位置】").append(position).append(" | ").append(thread.getName()).append("\n");
        sb.append("【崩溃】").append("\n");
        sb.append(throwable2String(ex));
        return sb.toString();
    }


    public static String throwable2String(final Throwable e) {
        if(e == null){
            return "null";
        }
        Throwable t = e;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.flush();
        String result = sw.toString();

        // 截取前3600字符
        if (result.length() > 3600) {
            return result.substring(0, 3600);
        } else {
            return result;
        }
    }





}
