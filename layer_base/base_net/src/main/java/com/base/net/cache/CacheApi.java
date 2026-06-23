package com.base.net.cache;

import android.text.TextUtils;

import com.base.api.GlobalContext;
import com.base.api.Logger;
import com.base.util.collection.ListUtil;
import com.base.util.content.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niepan on 31/12/25.
 */
public class CacheApi {



    public static String getCachedString(String key) {
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                return json;
            }
        } catch (Exception e) {
            Logger.exception("CacheApi", "getCachedStringList", e);
        }
        return null;
    }

    public static void saveToCachedStringList(String key, String itemString) {
        try{
            if(GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if(TextUtils.isEmpty(json)) {
                    List<String> initList = new ArrayList<>();
                    initList.add(itemString);
                    ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                } else {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(list != null) {
                        list.add(itemString);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    } else {
                        List<String> initList = new ArrayList<>();
                        initList.add(itemString);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                    }
                }
            }
        } catch (Exception e){
            Logger.exception("CacheApi", "saveToCachedStringList", e);
        }
    }

    public static List<String> getCachedStringList(String key) {
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if (!TextUtils.isEmpty(json)) {
                    return GsonUtil.parseJsonToList(json);
                }
            }
        } catch (Exception e) {
            Logger.exception("CacheApi", "getCachedStringList", e);
        }
        return null;
    }

    public static void removeFromCachedStringList(String key, String itemString) {
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if (!TextUtils.isEmpty(json)) {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(ListUtil.isNotEmpty(list)) {
                        list.remove(itemString);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }
                }
            }
        } catch (Exception e) {
            Logger.exception("CacheApi", "removeFromCachedStringList", e);
        }
    }

    public static void removeFromCachedStringList(List<String> itemList, String key) {
        if(ListUtil.isEmpty(itemList)) {
            return;
        }
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if (!TextUtils.isEmpty(json)) {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(ListUtil.isNotEmpty(list)) {
                        for(String content : itemList) {
                            list.remove(content);
                        }
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }
                }
            }
        } catch (Exception e) {
            Logger.exception("CacheUtil", "removeFromCachedStringList", e);
        }
    }
}
