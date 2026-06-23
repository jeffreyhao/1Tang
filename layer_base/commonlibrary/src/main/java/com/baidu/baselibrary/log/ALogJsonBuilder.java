package com.baidu.baselibrary.log;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by haojiangfeng on 2024/6/11.
 */
public class ALogJsonBuilder {


    Map<String, Object> map = new LinkedHashMap<>();

    private ALogJsonBuilder(){

    }

    public static ALogJsonBuilder get(){
        return new ALogJsonBuilder();
    }


    public ALogJsonBuilder put(String key, Object value){
        map.put(key, value);
        return this;
    }

    public ALogJsonBuilder putCodeMsg (int code, String msg){
        put(CODE, code);
        put(MSG, msg);
        return this;
    }

    public ALogJsonBuilder putContent (String content){
        put(CONTENT, content);
        return this;
    }

    public JSONObject build(){
        JSONObject jsonObject = new JSONObject();

        for(Map.Entry<String, Object> entry : map.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                jsonObject.put(key, JSONObject.wrap(value));
            } catch (Throwable e){
                ALog.exception("ALogJsonBuilder", "build", e);
            }
        }

        return jsonObject;
    }

    //////////////////////////////////////////////////

    public static String CODE           = "code";
    public static String MSG            = "msg";
    public static String CONTENT        = "content";

    public static JSONObject codeMsg (int code, String msg){
        return ALogJsonBuilder.get()
                .putCodeMsg(code, msg)
                .build();
    }

    public static JSONObject click (String name, int code, String msg){
        return ALogJsonBuilder.get()
                .put("name", name)
                .putCodeMsg(code, msg)
                .build();
    }
}
