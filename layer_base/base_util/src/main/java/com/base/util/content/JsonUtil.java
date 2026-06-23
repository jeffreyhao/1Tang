package com.base.util.content;

import android.os.Bundle;

import com.base.api.Logger;

import org.json.JSONObject;

import java.util.Set;

public class JsonUtil {


    public static String parse(Bundle bundle){
        if(bundle == null) {
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        try{
            Set<String> keySet = bundle.keySet();
            if(keySet !=null && !keySet.isEmpty()){
                for(String key: keySet) {
                    Object obj = bundle.get(key);
                    jsonObject.put(key, obj);
                }
            }
        } catch (Exception e){
            Logger.exception(e);
        }
        return jsonObject.toString();
    }


}
