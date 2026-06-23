package com.base.net.util;

import android.text.TextUtils;

import com.base.api.Logger;
import com.base.api.UserApi;
import com.base.util.content.GsonUtil;

import org.json.JSONObject;

import java.util.Map;

public class RequestLog {


    public static void logRequest(String path, Map<String, Object> paramMap, int index, boolean canRequest){
        if(canRequest){
            Logger.textSingle("BaseRequest", "canAddRequest", "Request(" + index + ") -> path: " + path + ", param: " + GsonUtil.bean2json(paramMap));
            if(TextUtils.isEmpty(UserApi.getUserId())) {
                Logger.textSingle("BaseRequest", "canAddRequest", "->token is empty: " + TextUtils.isEmpty(UserApi.getAccessToken()));
            }
        } else {
            Logger.textSingle("BaseRequest", "canAddRequest", "Request(" + index + ") canAddRequest: false ->path: " + path);
        }
    }

    public static JSONObject buildJSON(Map<String, Object> map){
        JSONObject jsonObject = new JSONObject();

        for(Map.Entry<String, Object> entry : map.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                jsonObject.put(key, JSONObject.wrap(value));
            } catch (Throwable e){
                Logger.exception(e);
            }
        }

        return jsonObject;
    }

    public void text(String className, String methodName, String text){
        Logger.textSingle(className, methodName, text);

    }

    public void json(JSONObject jsonObject){
        Logger.json(jsonObject);

    }


}
