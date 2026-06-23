package com.xcyh.cheatsheet.app.impl;

import com.base.abs.IVersion;
import com.base.util.AppUtil;

/**
 * Created by haojiangfeng on 2024/10/30.
 */
public class IVersionImpl implements IVersion {


    public static IVersionImpl get(){
        return new IVersionImpl();
    }


    @Override
    public String getVersionName() {
        return AppUtil.getVersionName();
    }
}
