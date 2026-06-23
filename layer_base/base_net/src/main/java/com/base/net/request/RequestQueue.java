package com.base.net.request;

import android.text.TextUtils;

import com.base.net.cache.CacheKey;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Time: 2023/12/19
 * Author: lhc
 * Desc:
 */
public class RequestQueue {
    private static RequestQueue mRequestQueue;

    public static RequestQueue getInstance(){
        if(null == mRequestQueue){
            synchronized (RequestQueue.class){
                if(null == mRequestQueue){
                    mRequestQueue = new RequestQueue();
                }
            }
        }
        return mRequestQueue;
    }

    private static int INTERVAL_DEFAULT     = 10*1000;


    private ConcurrentHashMap<String, Long> mRequestMap;

    private Map<String, Integer> mIntervalMap;


    private RequestQueue(){
        mRequestMap = new ConcurrentHashMap<>();
        mIntervalMap = new HashMap<>();
    }

    public void setIntervalLimit(String url, int interval){
        if(!TextUtils.isEmpty(url)){
            mIntervalMap.put(url, interval);
        }
    }

    public void setIntervalLimit(Map<String, Integer> map){
        if(map != null && map.size() > 0){
            mIntervalMap.putAll(map);
        }
    }

    public void clearIntervalMap(){
        mIntervalMap.clear();
    }

    /**
     * { BookChapterFetcher } 专用
     * @param requestUrl 请求链接
     * @param paramMap 请求参数
     */
    public boolean addRequest(String requestUrl, LinkedHashMap<String,Object> paramMap){
        try{
            String tag = CacheKey.buildCacheKey(requestUrl, paramMap);
            if(mRequestMap.containsKey(tag)){
                Long requestTime = mRequestMap.get(tag);
                if(requestTime != null && System.currentTimeMillis() - requestTime < limitInterval(requestUrl)){
                    return false;
                }
            }
            mRequestMap.put(tag, System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
        return true;
    }


    /**
     * 请求队列使用
     *
     * @param requestUrl 请求链接
     * @param paramMap 请求参数
     * @param pageHash 所在页面hashcode
     */
    public boolean addRequest(String requestUrl, LinkedHashMap<String,Object> paramMap, String pageHash){
        try{
            String tag = CacheKey.buildQueueKey(requestUrl, paramMap, pageHash);
            if(mRequestMap.containsKey(tag)){
                Long requestTime = mRequestMap.get(tag);
                if(requestTime != null &&System.currentTimeMillis() - requestTime < limitInterval(requestUrl)){
                    return false;
                }
            }
            mRequestMap.put(tag, System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * { BookChapterFetcher } 专用
     * @param requestUrl 请求链接
     * @param paramMap 请求参数
     */
    public void removeRequest(String requestUrl, LinkedHashMap<String,Object> paramMap){
        if(mRequestMap != null){
            mRequestMap.remove(CacheKey.buildCacheKey(requestUrl, paramMap));
        }
    }

    /**
     * 请求队列使用
     *
     * @param requestUrl 请求链接
     * @param paramMap 请求参数
     * @param pageHash 请求所在页面hash
     */
    public void removeRequest(String requestUrl, LinkedHashMap<String,Object> paramMap, String pageHash){
        if(mRequestMap != null){
            mRequestMap.remove(CacheKey.buildQueueKey(requestUrl, paramMap, pageHash));
        }
    }


    public void clear() {
        if(mRequestMap != null){
            mRequestMap.clear();
        }
    }


    private int limitInterval(String url){
        Integer interval = mIntervalMap.getOrDefault(url, INTERVAL_DEFAULT);
        return (interval == null) ? INTERVAL_DEFAULT : interval;
    }


}
