package com.base.api;

import com.base.abs.IPreference;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public class PreferenceApi {


    public static IPreference iPreference;



    public static void put(String key, String value){
        if(iPreference != null){
            iPreference.put(key, value);
        }
    }


    public static String get(String key, String defValue){
        if(iPreference != null){
            return iPreference.get(key, defValue);
        }
        return "";
    }




}
