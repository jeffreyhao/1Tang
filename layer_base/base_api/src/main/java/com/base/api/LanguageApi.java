package com.base.api;

import com.base.abs.ILanguage;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public class LanguageApi {

    public static ILanguage iLanguage;

    public static String getLanguage(){
        if(iLanguage != null){
            return iLanguage.getLanguage();
        }
        return "";
    }
}
