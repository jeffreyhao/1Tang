package com.xcyh.cheatsheet.app.impl;

import com.base.abs.ICacheTime;

/**
 * Created by haojiangfeng on 2024/11/6.
 */
public class ICacheTimeImpl implements ICacheTime {

    public static ICacheTimeImpl get(){
        return new ICacheTimeImpl();
    }

    @Override
    public int getCacheTime(String url) {
        return ApiCacheTime.getCacheTime(url);
    }

}
