package com.benefit.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.base.global.PreferencesUtil;
import com.base.util.AppUtil;
import com.base.util.collection.ListUtil;
import com.github.bean.app.LanguageItemBean;

import java.util.List;
import java.util.Locale;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

public class LanguageUtil {
    public static void changeAppLanguage(Context context) {
        if(null == context) return;
        String language = PreferencesUtil.get("language_set","");
        if(TextUtils.isEmpty(language)) {
            language = AppUtil.getDeviceLanguage();
        }
        if (!TextUtils.isEmpty(language)) {
            // 本地语言设置
            Locale myLocale = new Locale(language);

            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            //做版本兼容性判断
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                conf.setLocale(myLocale);
            } else {
                conf.locale = myLocale;
                //点进去看方法详情
            }
            res.updateConfiguration(conf, dm);
        }

    }

    public static String getLanguage() {
        String language = PreferencesUtil.get("language_set","");
        if(TextUtils.isEmpty(language)) {
            language = AppUtil.getDeviceLanguage();
        }
        if (!TextUtils.isEmpty(language)) {
            List<LanguageItemBean> languageList = CacheUtil.getLanguageList();
            if(ListUtil.isNotEmpty(languageList)) {
                boolean containLanguage = false;
                for(LanguageItemBean item: languageList) {
                    if(TextUtils.equals(item.getLanguage(),language)) {
                        containLanguage = true;
                        break;
                    }
                }
                if(!containLanguage) {
                    language = "en";
                }
            }
        }else{
            language = "en";
        }
        return language;
    }

    public static String getLanguageName() {
        String language = PreferencesUtil.get("language_set","");
        if(TextUtils.isEmpty(language)) {
            language = AppUtil.getDeviceLanguage();
        }
        List<LanguageItemBean> languageList = CacheUtil.getLanguageList();
        if(ListUtil.isNotEmpty(languageList)) {
            for(LanguageItemBean item: languageList) {
                if(TextUtils.equals(item.getLanguage(),language)) {
                    return item.getDes_local();
                }
            }
        }
        return "English";
    }






    public static boolean contains(List<? extends LanguageItemBean> list, String language){
        if(ListUtil.isEmpty(list) || TextUtils.isEmpty(language)){
            return false;
        }
        for(LanguageItemBean bean : list){
            if(bean.getLanguage().equals(language)){
                return true;
            }
        }
        return false;
    }

    public static void setAppLanguage(String language) {
        if(TextUtils.isEmpty(language)) return;
        PreferencesUtil.put("language_set",language);
    }

    public static String getLanguageCode() {
        return Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry().toUpperCase(Locale.US);
    }

    public static String getCountryCode() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleListCompat listCompat= ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
            locale= listCompat.get(0);
        }else{
            locale=Locale.getDefault();
        }
        return locale.getCountry().toUpperCase(Locale.US);
    }

    public static String getDeviceLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleListCompat listCompat= ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
            if(listCompat.size()>0) {
                locale = listCompat.get(0);
            }else{
                locale = Locale.getDefault();
            }
        }else{
            locale = Locale.getDefault();
        }
        return locale.getLanguage();
    }




}
