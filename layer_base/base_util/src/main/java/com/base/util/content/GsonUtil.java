package com.base.util.content;

import com.base.api.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @file GsonUtil.java
 * @brief Gson工具类
 */
public class GsonUtil {
    /**
     * 将JSON转为实体
     * <br>
     * ⚠️注意：该方法不一定全对，请谨慎使用，一定要经过自测。
     * <br>
     * 比如 {"1825":0,"1U1293":0}，调用GsonUtil.json2Bean(json, Map.class)解析出来，是这样的map，value变成了float：
     * <br>1825 -> {Double@19320} 0.0，
     * <br>1U1293 -> {Double@19322} 0.0
     * <br>
     * @param json json字符串
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {

        Gson gson = new Gson();
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            Logger.exception(e);
            return null;
        }
    }

    public static <T> T json2Bean(String json, Type type) {
        Gson gson = new Gson();
        try{
            return gson.fromJson(json, type);
        } catch (Exception e){
            Logger.exception(e);
            return null;
        }
    }

    /**
     * @param obj
     * @return
     * @brief 将一个对象装为Json格式的字符串
     */
    public static String bean2json(Object obj) {
        try{
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            Logger.exception(e);
        }
        return "";
    }

    /**
     * 把json字符串解析成集合
     * params: new TypeToken<List<AppInfo>>(){}.getType(),
     *
     * @param json
     * type new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static <T> List<T> parseJsonToList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
    }
}
