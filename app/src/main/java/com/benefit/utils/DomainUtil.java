package com.benefit.utils;

import android.text.TextUtils;

import com.base.api.GlobalContext;
import com.base.global.GlobalBuildConfig;
import com.base.net.ApiBase;
import com.base.net.cache.ACache;
import com.base.util.collection.ListUtil;
import com.base.util.content.GsonUtil;
import com.base.watcher.Watcher;
import com.base.watcher.WatcherEvent;
import com.github.bean.model.AppDomainsBean;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2024/6/3
 * Author: lhc
 * Desc:
 */
public class DomainUtil {
    public static void checkUpdateDomain(List<AppDomainsBean> domainList) {
        if(ListUtil.isNotEmpty(domainList)) {
            for(AppDomainsBean item: domainList) {
                if(item.getType()==1) {
                    if(!TextUtils.isEmpty(item.getDefaultDomain()) && !TextUtils.equals(ApiBase.BASE_URL,item.getDefaultDomain())) {
                        String defaultDomain = item.getDefaultDomain();
                        cacheDefaultDomain(defaultDomain);
                        alog("checkUpdateDomain", 1, defaultDomain);
                        Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_SWITCH_DNS, item.getDefaultDomain());
                    }
                    if(ListUtil.isNotEmpty(item.getBackUpDomain())) {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(item.getDefaultDomain());
                        for(String domain: item.getBackUpDomain()) {
                            if(!TextUtils.isEmpty(domain)) {
                                tempList.add(domain);
                            }
                        }
                        ApiBase.backupDomain = tempList;
                        cacheBackUpDomain(GsonUtil.bean2json(tempList));
                    }
                }else if(item.getType()==2) {
                    if(!TextUtils.isEmpty(item.getDefaultDomain()) && !TextUtils.equals(ApiBase.SEARCH_URL, item.getDefaultDomain())) {
                        String seDomain = item.getDefaultDomain();
                        ApiBase.SEARCH_URL = seDomain;
//                        Api.resetSeStUrl();
                        cacheSeDomain(seDomain);
                        alog("checkUpdateDomain", 2, seDomain);
                    }
                }else if(item.getType()==3) {
                    if(!TextUtils.isEmpty(item.getDefaultDomain()) && !TextUtils.equals(ApiBase.UPLOAD_TIME_URL, item.getDefaultDomain())) {
                        String stDomain = item.getDefaultDomain();
                        ApiBase.UPLOAD_TIME_URL = item.getDefaultDomain();
//                        Api.resetSeStUrl();
                        cacheStDomain(stDomain);
                        alog("checkUpdateDomain", 3, stDomain);
                    }
                }
            }
        }else{
            cacheDefaultDomain("");
            cacheSeDomain("");
            cacheStDomain("");
            cacheBackUpDomain("");
            if(ApiBase.backupDomain!=null) {
                ApiBase.backupDomain.clear();
            }
        }
    }

    public static void cacheBackUpDomain(String backUpDomain) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("backUpDomain", backUpDomain);
        }
    }

    public static List<String> getBackupDomain() {
        if(GlobalContext.getContext()!=null) {
            String backUpDomain = ACache.get(GlobalContext.getContext()).getAsString("backUpDomain");
            if(!TextUtils.isEmpty(backUpDomain)) {
                List<String> list = GsonUtil.json2Bean(backUpDomain, new TypeToken<List<String>>(){}.getType());
                if(ListUtil.isNotEmpty(list)) {
                    return list;
                }else{
                    return new ArrayList<>();
                }
            }
        }
        return new ArrayList<>();
    }

    private static void cacheDefaultDomain(String defaultDomain) {
        CacheUtil.saveCacheValue("defaultDomain", defaultDomain);
    }

    private static String getDefaultDomain() {
        return CacheUtil.getCacheValueByKey("defaultDomain");
    }

    private static void cacheSeDomain(String defaultDomain) {
        CacheUtil.saveCacheValue("seDomain", defaultDomain);
    }

    private static String getSeDomain() {
        return CacheUtil.getCacheValueByKey("seDomain");
    }

    private static void cacheStDomain(String defaultDomain) {
        CacheUtil.saveCacheValue("stDomain", defaultDomain);
    }

    private static String getStDomain() {
        return CacheUtil.getCacheValueByKey("stDomain");
    }

    public static String getBaseUrl() {
        String defaultDomain = getDefaultDomain();
        if(TextUtils.isEmpty(defaultDomain)) {
            return GlobalBuildConfig.BASE_URL;
        }
        if(!TextUtils.equals(defaultDomain,GlobalBuildConfig.BASE_URL)) {
            return defaultDomain;
        }
        return GlobalBuildConfig.BASE_URL;
    }

    public static String getSearchUrl() {
        String seDomain = getSeDomain();
        if(TextUtils.isEmpty(seDomain)) {
            return GlobalBuildConfig.SEARCH_URL;
        }
        if(!TextUtils.equals(seDomain,GlobalBuildConfig.SEARCH_URL)) {
            return seDomain;
        }
        return GlobalBuildConfig.SEARCH_URL;
    }

    public static String getUploadTimeUrl() {
        String stDomain = getStDomain();
        if(TextUtils.isEmpty(stDomain)) {
            return GlobalBuildConfig.UPLOAD_TIME_URL;
        }
        if(!TextUtils.equals(stDomain,GlobalBuildConfig.UPLOAD_TIME_URL)) {
            return stDomain;
        }
        return GlobalBuildConfig.UPLOAD_TIME_URL;
    }

    private static void alog(String methodName, int type, String domain){
    }

}
