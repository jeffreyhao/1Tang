package com.base.net.cache;

import com.base.api.Logger;
import com.base.net.ApiBase;
import com.base.util.thread.ExecutorsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

public class CacheControlManager {

    private static final CacheControlManager mInstance = new CacheControlManager();

    public static CacheControlManager getInstance() {
        return mInstance;
    }



    private ICache mCache;
    private final List<String> mList = new ArrayList<>();

    private CacheControlManager(){
        mCache = new ACCacheImpl();
    }

    public String getCache(String url, LinkedHashMap<String, Object> queryMap) {
        String cacheKey = CacheKey.buildCacheKey(url, queryMap);
        String cache = (mCache != null) ? mCache.getCache(cacheKey) : null;

        if (ApiBase.DEBUG && CacheConfig.showCacheLog) {
            Logger.debug("cache",
                    "getCache() " + url
                            + "\n       param:" + queryMap
                            + "\n       cacheKey:" + cacheKey
                            + "\n       cache:" + (cache != null ? cache.hashCode() : "null")
            );
        }

        return cache;
    }

    public void cacheResponse(String url, LinkedHashMap<String, Object> paramMap, String bodyString) {
        try {
            JSONObject objBody = new JSONObject(bodyString != null ? bodyString : "");
            int code = objBody.optInt("code");
            if (code == 0) {
                setCache(url, paramMap, bodyString);
            }
        } catch (JSONException e) {
            Logger.exception("CacheControlManager", "cacheResponse", e);
        }
    }

    public void clearApiCacheData() {
        for (String item : mList) {
            if (mCache != null) {
                mCache.clearCache(item);
            }
        }
    }

    private synchronized void setCache(final String url, final LinkedHashMap<String, Object> queryMap, final String value) {
        final String saveValue = (value != null && !value.isEmpty()) ? value : "";

        ExecutorsUtils.getInstance().getAppExecutors().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String cacheKey = CacheKey.buildCacheKey(url, queryMap);
                int cacheTime = CacheConfig.getCacheTime(url);

                if (ApiBase.DEBUG && CacheConfig.showCacheLog) {
                    Logger.debug("cache",
                            "setCache() " + url
                                    + "\n       param:" + queryMap
                                    + "\n       cacheKey:" + cacheKey
                                    + "\n       cache.hash:" + saveValue.hashCode()
                                    + "\n       cacheTime:" + cacheTime
                    );
                }

                if (mCache != null) {
                    mCache.saveCache(cacheKey, saveValue, cacheTime);
                }
                mList.add(cacheKey);
            }
        });
    }
}
