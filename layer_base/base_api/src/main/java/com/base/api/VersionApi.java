package com.base.api;

import com.base.abs.IVersion;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public class VersionApi {

    public static IVersion iVersion;

    public static String getVersionName(){
        if(iVersion != null){
            return iVersion.getVersionName();
        }
        return "";
    }

}
