package com.base.net.cache;

import com.base.abs.ICacheTime;

/**
 * Created by haojiangfeng on 2024/11/6.
 */
public class CacheConfig {

    private static final int MAX_SIZE 		= 1000 * 1000 * 100; // 50 mb
    private static final int MAX_COUNT 		= Integer.MAX_VALUE; // 不限制存放数据的数量


    public static int maxSize       = MAX_SIZE;
    public static int maxCount      = MAX_COUNT;

    public static ICacheTime iCacheTime;

    public static boolean showCacheLog = false;


    public static int getCacheTime(String url) {
        return iCacheTime.getCacheTime(url);
    }


}
