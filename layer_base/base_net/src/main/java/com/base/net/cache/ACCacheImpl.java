package com.base.net.cache;

import com.base.api.GlobalContext;

public class ACCacheImpl implements ICache {

    @Override
    public void saveCache(String key, String value, int cacheTime) {
        ACache.get(GlobalContext.getContext()).put(key, value, cacheTime);
    }

    @Override
    public String getCache(String key) {
        return ACache.get(GlobalContext.getContext()).getAsString(key);
    }

    @Override
    public void clearCache(String key) {
        if (GlobalContext.getContext() != null) {
            ACache.get(GlobalContext.getContext()).put(key, "");
        }
    }
}
