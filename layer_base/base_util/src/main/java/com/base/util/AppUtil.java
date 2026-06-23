package com.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import com.base.api.GlobalContext;
import com.base.api.PreferenceApi;

import java.util.Locale;
import java.util.TimeZone;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

public class AppUtil {

    private static final String PRODUCT_ID_NOVELHIVE = "13";
    public static String splashClassName = "";

    /**
     * 与App.sHandler是同一个
     */
    public static Handler sHandler;


    public static void setHandler(Handler handler){
        sHandler = handler;
    }

    public static void setAppLanguage(String language) {
        if(TextUtils.isEmpty(language)) return;
        PreferenceApi.put("language_set",language);
    }

    public static String getVersionName() {
        //获取包管理器
        PackageManager pm = GlobalContext.getContext().getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(GlobalContext.getContext().getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getPackageName() {
        try {
            PackageManager packageManager = GlobalContext.getContext().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packInfo = packageManager.getPackageInfo(GlobalContext.getContext().getPackageName(), PackageManager.GET_SIGNATURES);
            return packInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "novelhive";
    }


    public static String getLanguage() {
        String language = PreferenceApi.get("language_set","");
        if(TextUtils.isEmpty(language)) {
            language = getDeviceLanguage();
        }
        if (TextUtils.isEmpty(language)) {
            return "en";
        }
        return language;
    }

    public static boolean isRTLMode() {
        String[] rtlLanguages = {"ar", "fa", "fa", "ur", "he", "syr", "ku", "ps"};
        String language = getLanguage();
        boolean result = false;
        for(String languageCode: rtlLanguages) {
            if(TextUtils.equals(languageCode, language)) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isRTLMode(String language) {
        String[] rtlLanguages = {"ar", "fa", "fa", "ur", "he", "syr", "ku", "ps"};
        boolean result = false;
        for(String languageCode: rtlLanguages) {
            if(TextUtils.equals(languageCode, language)) {
                result = true;
            }
        }
        return result;
    }

    public static String getProcessName(Context context) {
        if (context == null) return "hive";
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return TextUtils.isEmpty(processInfo.processName)?"hive":processInfo.processName;
            }
        }
        return "hive";
    }

    /**
     * 获取版本号
     * @return 版本号
     */
    public static int getVersionCode() {

        //获取包管理器
        PackageManager pm = GlobalContext.getContext().getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(GlobalContext.getContext().getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

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

    public static String getLanguageAndCountryCode() {
        return getDeviceLanguage() + "-" + getCountryCode();
    }

    /**
     * @return 获取时区（地区时区，dst地区有夏令时影响）
     */
    public static String getTimeZone(){
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone==null?"":timeZone.getID();
    }

    /**
     * @return 获取时区（固定偏移量的时区）
     */
    public static String getFixedTimeZone(){
        TimeZone tz = TimeZone.getDefault();
        int offsetMillis = tz.getOffset(System.currentTimeMillis());

        // 将其转换为固定时区
        String fixedId = String.format(Locale.ENGLISH, "GMT%+d:%02d", offsetMillis / 3600000, Math.abs((offsetMillis / 60000) % 60));

        // 得到固定偏移时区，不再受 DST 影响
        TimeZone fixedTimeZone = TimeZone.getTimeZone(fixedId);
        return fixedTimeZone == null ? "" : fixedTimeZone.getID();
    }

    public static void restartApp(Context context) {
        try {
            if(!TextUtils.isEmpty(splashClassName)) {
                Class<?> aClass = Class.forName(splashClassName);
                Intent intent = new Intent(context, aClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断对应的context是否是Activity 并且Activity是否已经finishing状态
     * 用于解决badToken问题
     * @param context
     * @return
     */
    public static boolean isActivityAvailable(Context context){
        if (context instanceof Activity){
            return  !((Activity) context).isFinishing();
        }else{
            while (context instanceof ContextWrapper){
                if (context instanceof Activity){
                    return !((Activity) context).isFinishing();
                }else{
                    context = ((ContextWrapper) context).getBaseContext();
                }
            }
        }
        return false;
    }

    public static String getABIInfo() {
        try {
            // 检测当前设备 ABI
            String[] abiS;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                abiS = Build.SUPPORTED_ABIS;
            } else {
                abiS = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
            }
            return String.join(", ", abiS);
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static boolean post(Runnable runnable){
        if(sHandler == null || runnable == null){
            return false;
        }
        return sHandler.post(runnable);
    }

    public static boolean postDelayed(Runnable runnable, long delay){
        if(sHandler == null || runnable == null){
            return false;
        }
        return sHandler.postDelayed(runnable, delay);
    }
}
