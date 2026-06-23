package com.base.net.cache;

import android.text.TextUtils;

import com.base.api.LanguageApi;
import com.base.api.Logger;
import com.base.api.UserApi;
import com.base.api.VersionApi;
import com.base.net.ApiBase;
import com.base.util.safe.MD5Util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by haojiangfeng on 2024/11/18.
 */
public class CacheKey {


    /**
     * 构建请求本地缓存key
     *
     * @param url url
     * @param queryMap 参数
     */
    public static String buildCacheKey(String url, LinkedHashMap<String, Object> queryMap) {
        String keyString = buildKeyString(url, queryMap, null);
        String cacheKey = keyString.isEmpty() ? "" : MD5Util.md5(keyString);

        if(ApiBase.DEBUG && CacheConfig.showCacheLog){
            Logger.v("cache",
                    "buildCacheKey() " + url
                            + "\n       param:" + queryMap
                            + "\n       keyString:" + keyString
                            + "\n       cacheKey:" + cacheKey
            );
        }

        return cacheKey;
    }

    /**
     *  构建请求队列缓存key
     *
     *  ⚠️注：由于需要转换成String，再进行md5运算。为了保持String一致，这里需要传入LinkedHashMap保证顺序。
     *
     *  @param url url
     *  @param queryMap 参数
     */
    public static String buildQueueKey(String url, LinkedHashMap<String, Object> queryMap, String pageHash) {
        String keyString = buildKeyString(url, queryMap, pageHash);
        String cacheKey = keyString.isEmpty() ? "" : MD5Util.md5(keyString);

        if(ApiBase.DEBUG && CacheConfig.showCacheLog){
            Logger.v("cache",
                    "buildQueueKey() " + url
                            + "\n       param:" + queryMap
                            + "\n       keyString:" + keyString
                            + "\n       cacheKey:" + cacheKey
            );
        }

        return cacheKey;
    }


    private static String buildKeyString(String url, LinkedHashMap<String, Object> queryMap, String pageHash){
        StringBuilder sb = new StringBuilder();
        sb.append(url);

        // add params
        if (queryMap != null && !queryMap.isEmpty()) {
            Set<Map.Entry<String, Object>> entrySet = queryMap.entrySet();
            int index = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                if(index == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                index++;
            }
        }

        // add headers
        if(pageHash != null && !TextUtils.isEmpty(pageHash)){
            sb.append("&pageHash=").append(pageHash);
        }
        sb
                .append("&user_id=").append(UserApi.getUserId())
                .append("&app_version=").append(VersionApi.getVersionName())
                .append("&language=").append(LanguageApi.getLanguage());

        return sb.toString();
    }

}
