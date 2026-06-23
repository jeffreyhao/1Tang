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
public class WebHookFeedbackUtil {

    public static void sendFeedback(Map<String, Object> params, Object response) {
        try {

            String info = collectMessage(params, response);
            String content = buildJson(info);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WebHookUtil.sendText("https://oapi.dingtalk.com/robot/send?access_token=" + WebHookUtil.getFeedbackHookToken(), content);
                }
            }).start();


        } catch (Throwable e){
            Logger.e(e);
        }
    }


    /**
     * @param params 请求参数
            put("type_id",typeId)
            put("contacts",contacts)
            put("from_type",fromType)

            if(!bookId.isNullOrEmpty()) {
                put("book_id",bookId)
            }
            if(!chapterId.isNullOrEmpty()) {
                put("chapter_id",chapterId)
            }
            if(!bookId.isNullOrEmpty()&&!chapterId.isNullOrEmpty()&&chapterIndex>0) {
                put("chapter_index",chapterIndex)
            }
    * @param response 响应参数
        data = {LinkedTreeMap@19121}  size = 10
             "id" -> {Double@19144} 11898.0
             "account_id" -> "2486958"
             "type_id" -> "11"
             "type_name" -> "Questões de assinatura\n"
             "content" -> "test"
             "image_urls" -> {ArrayList@19154}  size = 0
     */
    private static String collectMessage(Map<String, Object> params, Object response) {
        Object contentValue = params.get("content");
        Object bookIdValue = params.get("book_id");
        Object chapterIdValue = params.get("chapter_id");
        Object chapterIndexValue = params.get("chapter_index");

        String content = contentValue == null ? "" : contentValue.toString();
        String bookId = bookIdValue == null ? "" : bookIdValue.toString();
        String chapterId = chapterIdValue == null ? "null" : chapterIdValue.toString();
        String chapterIndex = chapterIndexValue == null ? "null" : chapterIndexValue.toString();

        String problemId = "";
        if(response instanceof Map){
            try {
                Map<String, Object> map = (Map<String, Object>) response;
                double id = Double.parseDouble(map.get("id").toString());
                problemId = String.valueOf((int) id);
            } catch (Throwable e){
                e.printStackTrace();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Real-time feedback ").append(response != null ? "success （User Id: " : "fail （User ID: ").append(UserApi.getUserId()).append("）（")
                .append(TextUtils.isEmpty(problemId) ? "" : "Problem ID: " + problemId + "）（")
                .append(ApiBase.FLAVOR).append(" V").append(AppUtil.getVersionName()).append("-").append(AppUtil.getVersionCode())
                .append("）          \n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String time = sdf.format(new Date());
        sb.append("  【Time】 device: ").append(time).append("  ").append(AppUtil.getTimeZone()).append("  ").append(AppUtil.getCountryCode()).append("     \n");
        if(!TextUtils.isEmpty(bookId)){
            sb.append("  【Book】 bookId:").append(bookId).append(", chapterId:").append(chapterId).append(", chapterIndex:").append(chapterIndex).append("  \n");
        }
        sb.append(content).append("\n");
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
