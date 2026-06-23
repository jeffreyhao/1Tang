package com.benefit.novelverse.utils;

import com.base.global.PreferencesUtil;
import com.benefit.utils.CacheUtil;

/**
 * Time: 2024/4/24
 * Author: lhc
 * Desc: 检查是否需要请求动态数据  bookstore and ranking
 */
public class RequestCheckUtil {
    public static boolean canRequestDynamicData(String key) {
        long requestTime = CacheUtil.getLastRequestTime(key);
        long intervalTime = PreferencesUtil.get(Constant.SP_CONSTANT.REFRESH_DATA_INTERVAL_TIME, 0);
        if(intervalTime==0) {
            PreferencesUtil.put(Constant.SP_CONSTANT.REFRESH_DATA_INTERVAL_TIME, Constant.getRandomRequestIntervalTime());
        }
        return requestTime>0&&requestTime+intervalTime<System.currentTimeMillis();

    }
}
