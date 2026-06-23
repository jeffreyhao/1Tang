package com.base.net.webhook;

import android.text.TextUtils;

import com.base.api.Logger;
import com.base.api.UserApi;
import com.base.net.ApiBase;
import com.base.util.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by haojiangfeng on 2025/5/28.
 */
public class WebHookMonitorUtil {



    public static void sendTest(String message, String text){
        try {

            String content = buildJson(message + "\n" + text);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WebHookUtil.sendText("https://oapi.dingtalk.com/robot/send?access_token=" + WebHookUtil.getMonitorHookToken(WebHookUtil.TYPE_TEST), content);
                }
            }).start();
        } catch (Throwable e){
            Logger.e(e);
        }
    }

    public static void sendMonitor(int type, String message, String position, String... params) {
        try {

            String info = collectMessage(message, position, params);
            String content = buildJson(info);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WebHookUtil.sendText("https://oapi.dingtalk.com/robot/send?access_token=" + WebHookUtil.getMonitorHookToken(type), content);
                }
            }).start();


        } catch (Throwable e){
            Logger.e(e);
        }
    }


    private static String collectMessage(String message, String position, String... params) {
        StringBuilder sb = new StringBuilder();
        if(ApiBase.DEBUG) {
            sb.append("客户端实时监控（Debug包）\n");
        } else {
            sb.append("客户端实时监控\n");
        }
        sb.append("  【异常】 ").append(message).append("\n");
        if(params != null && params.length > 0) {
            sb.append("  【内容】 ");
            int index = 0;
            for(String param : params) {
                if(index > 0) {
                    sb.append("、");
                }
                sb.append("[").append(param).append("]");
                index++;
            }
            sb.append("\n");
        }
        sb.append("  【位置】 ").append(position).append("\n");
        sb.append("  【用户】 ").append(UserApi.getUserId()).append("\n");
        sb.append("  【版本】 ").append(ApiBase.FLAVOR).append(" V").append(AppUtil.getVersionName()).append("-").append(AppUtil.getVersionCode()).append("\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String time = sdf.format(new Date());
        sb.append("  【时间】 device: ").append(time).append("  ").append(AppUtil.getTimeZone()).append("  ").append(AppUtil.getCountryCode()).append("\n");
        return sb.toString();
    }


    private static String buildJson(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        JSONObject contentJson = new JSONObject();
        contentJson.put("content", content.replace("\"", "\\\""));
        jsonObject.put("text", contentJson);
        return jsonObject.toString();
    }








}
