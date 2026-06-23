package com.base.net.cache;

public interface ICache {

    void saveCache(String key, String value, int cacheTime);

    String getCache(String key);

    void clearCache(String key);
}
