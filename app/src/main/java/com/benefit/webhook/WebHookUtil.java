package com.benefit.webhook;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by haojiangfeng on 2025/5/28.
 */
public class WebHookUtil {

    private static final String TAG = "WebHookUtil";


    public static void sendText(String webhookUrl, String json){
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // 发送请求
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "DingTalk response code: " + responseCode);

            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error sending to DingTalk", e);
        }
    }

}
