package com.benefit.webhook;

import android.os.Build;

import com.base.api.UserApi;
import com.base.global.GlobalBuildConfig;
import com.base.util.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by haojiangfeng on 2025/5/28.
 */
public class WebHookCrashUtil {

    private static final String TAG = "WebHookCrashUtil";

    public static void sendCrash(Thread thread, Throwable ex, String position) {
        try {
            String info = collectCrashInfo(thread, ex, position);
            String title = buildTitle();
            String content = buildJson(title, info.replace("\"", "\\\""));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WebHookUtil.sendText(getCrashHookUrl(), content);
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

    private static String buildTitle(){
        StringBuilder sb = new StringBuilder();
        sb.append(GlobalBuildConfig.DEBUG ? "Debug包崩溃通知" : "线上崩溃通知");
        sb.append(" （");
        sb.append(AppUtil.getPackageName());
        sb.append(" V");
        sb.append(AppUtil.getVersionName());
        sb.append("）（");
        sb.append(UserApi.getUserId());
        sb.append("）\n");
        return sb.toString();
    }

    private static String collectCrashInfo(Thread thread, Throwable ex, String position) {
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);  // 记录时间
        String time = sdf.format(new Date());
        sb.append("        * 设备: ").append(Build.BRAND).append(" ").append(Build.MODEL).append(" Android").append(Build.VERSION.RELEASE).append("  |  ");
        sb.append(time).append(" ").append(AppUtil.getTimeZone()).append(" ").append(AppUtil.getCountryCode()).append("   \n");
        if(GlobalBuildConfig.DEBUG){
            sb.append("        * 位置: ").append(position).append(" | ").append(thread.getName()).append("\n");
        }
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

        // 截取前3000字符
        if (result.length() > 3000) {
            return result.substring(0, 3000);
        } else {
            return result;
        }
    }


    private static String getCrashHookUrl(){
        if(GlobalBuildConfig.DEBUG){
            return "https://oapi.dingtalk.com/robot/send?access_token=5d6c94e3be0baad8ae2f52e0c56196d79b4b7cab1a807cb3f6d59e8e358887de";
        }
        String flavor = GlobalBuildConfig.FLAVOR == null ? "" : GlobalBuildConfig.FLAVOR.toLowerCase();
        switch (flavor){
            case "noveldaily":      return "https://oapi.dingtalk.com/robot/send?access_token=209c7b9f7a761e6ce12b02514117402a1f54ebe080e2936f04146abd13860d97";
            case "novelclub":       return "https://oapi.dingtalk.com/robot/send?access_token=7c8eb238d7e88e1e5152571072abee68baf6fa0cdcb8c1e1278b330fa67d01fb";
            case "novelhome":       return "https://oapi.dingtalk.com/robot/send?access_token=7ed6b549d46ae8432919b95da714ddb3a26903083a1c29aba38efc57edb843e0";
            case "novelpal":        return "https://oapi.dingtalk.com/robot/send?access_token=87fa1712177142073ff6033b63e99c8f4f7a9faff7e92f482ed2ba105dd021f3";
            case "peaknovel":       return "https://oapi.dingtalk.com/robot/send?access_token=9696cd305088b3c8317b19ae87133eef3385449ea9201a57481ad5b027c687ed";
            case "novelworm":       return "https://oapi.dingtalk.com/robot/send?access_token=76c06ca23f93f79a379e5dc0ae63f52aaccd6f8f2908c30b624748e8ea766f50";
            case "novelverse":      return "https://oapi.dingtalk.com/robot/send?access_token=eb920586b7dec80d47ec705ddd4d20d1f34c72385f3288c3957efbb9dfda7b3a";

            case "romanfic":        return "https://oapi.dingtalk.com/robot/send?access_token=b3fc0a334b14d4622c3d559f80fc813a2a81a009bde515c4172832e5d427c5e4";
            case "novelnook":       return "https://oapi.dingtalk.com/robot/send?access_token=16fb16d57317a999d01296d3c7bd12e05e1c1629aaaae852cdb622f1ea469979";
            case "novelplant":      return "https://oapi.dingtalk.com/robot/send?access_token=44a31aeec5ddb8fab89e820ef751037dae5f6f5c70606bc7fe05ad1296fec3be";
            case "novelhive":       return "https://oapi.dingtalk.com/robot/send?access_token=911b9db38fe8f6556f0dcb55fedd975897919464d14c09f4197f9712dbdab116";

            case "alphafiction":    return "https://oapi.dingtalk.com/robot/send?access_token=fd7c18afeae5a27694efc893a2fb4f33628dd0ad3a8c0b84a77578f29c01a5af";
            case "lycanfiction":    return "https://oapi.dingtalk.com/robot/send?access_token=942092f913bc78d4585170f069e40a3561104beef5847fbe7c186fe7789c63e8";
            case "lycannovel":      return "https://oapi.dingtalk.com/robot/send?access_token=01fb75a102d40792b94139a93e5991da4efe42b718e161257a61aa4b2e91adf8";
            case "wolffiction":     return "https://oapi.dingtalk.com/robot/send?access_token=85ff1c6c79408e53ffda8408ff5d8edc688f700028daeba5b908e099cd455bdb";
        }
        return "";
    }


}
