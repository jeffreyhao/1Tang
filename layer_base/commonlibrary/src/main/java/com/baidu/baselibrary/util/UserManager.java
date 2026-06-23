package com.baidu.baselibrary.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.global.Clazz;
import com.base.global.PreferencesUtil;

public class UserManager {
    public static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCESS_TOKEN = "KEY_TOKEN";
    private static final String KEY_ACCESS_TOKEN_TYPE = "KEY_ACCESS_TOKEN_TYPE";
    private static final String KEY_ACCESS_LOGIN_ACCOUNT = "KEY_ACCESS_LOGIN_ACCOUNT";
    private static final String KEY_USER = "KEY_USER";
    private static final String KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE";
    private static final String KEY_IS_PAYING = "KEY_IS_PAYING";
    private static final String KEY_IS_NEW_USER = "KEY_IS_NEW_USER";
    public static final String KEY_CREATE_TIME = "key_create_time";
    public static final String KEY_NAME_INFO = "key_name_info";

    public static final String KEY_IS_FIRST_LAUNCH = "key_is_first_launch";

    /**
     * 仅在AppInit初始化。
     */
    public static boolean isFirstLaunch = true;


    public static String getUserId() {
        return PreferencesUtil.get(KEY_USER_ID, "");
    }

    public static String getAccessToken() {
        return PreferencesUtil.get(KEY_ACCESS_TOKEN, "");
    }

    public static void saveToken(String token) {
        PreferencesUtil.put(KEY_ACCESS_TOKEN, token);
    }

    public static void saveIsPaying(boolean isPaying) {
        PreferencesUtil.put(KEY_IS_PAYING, isPaying);
    }

    public static boolean isPaying() {
        return PreferencesUtil.get(KEY_IS_PAYING, false);
    }

    public static String getAccessTokenType() {
        return PreferencesUtil.get(KEY_ACCESS_TOKEN_TYPE, "");
    }

    public static boolean isLoggedIn() {
        return !TextUtils.isEmpty(getAccessToken()) && !TextUtils.isEmpty(getUserId());
    }

    public static void setPayPlatform(int payPlatform) {
        PreferencesUtil.put("payPlatform", payPlatform);
    }

    public static int getPayPlatform() {
        return PreferencesUtil.get("payPlatform",1);
    }

    /**
     * 缓存最近一次选择的支付方式
     * @param payPlatform
     */
    public static void setLastPayPlatform(int payPlatform, String subCode) {
        PreferencesUtil.put("lastPayPlatform", payPlatform + "-" + subCode);
    }

    /**
     * 缓存最近一次选择的支付方式
     * @param payPlatform
     */
    public static void setLastPayPlatform(String payPlatform) {
        if(!TextUtils.isEmpty(payPlatform)) {
            PreferencesUtil.put("lastPayPlatform", payPlatform);
        }
    }

    public static String getLastPayPlatform() {
        return PreferencesUtil.get("lastPayPlatform","");
    }

    public static void setPushToken(String token) {
        PreferencesUtil.put("pushToken",token);
    }

    public static String getPushToken() {
        return PreferencesUtil.get("pushToken","");
    }

    public static void clearPushToken() {
        PreferencesUtil.remove("pushToken");
    }

    public static int getLoginType() {
        return PreferencesUtil.get(KEY_LOGIN_TYPE, -1);
    }

    public static void setIsFirstSearch() {
        PreferencesUtil.put("is_first_search", false);
    }

    public static boolean isFirstSearch() {
        return PreferencesUtil.get("is_first_search", true);
    }

    public static void clearUserInfo() {
        PreferencesUtil.put(KEY_ACCESS_LOGIN_ACCOUNT, false);
        PreferencesUtil.put(KEY_ACCESS_TOKEN, "");
        PreferencesUtil.put(KEY_USER_ID, "");
        PreferencesUtil.put(KEY_USER, "");
        PreferencesUtil.put(KEY_ACCESS_TOKEN_TYPE, "");
        PreferencesUtil.put(KEY_LOGIN_TYPE, -1);
        PreferencesUtil.put(KEY_IS_PAYING, false);
    }

    public static void saveIsNewUser(boolean isNewUser) {
        PreferencesUtil.put(KEY_IS_NEW_USER, isNewUser);
    }

    public static boolean isNewUser() {
        return PreferencesUtil.get(KEY_IS_NEW_USER, true);
    }

    public static String getCreateTime() {
        return PreferencesUtil.get(KEY_CREATE_TIME, "");
    }

    public static void saveNameInfo(String firstName, String lastName) {
        PreferencesUtil.put(KEY_NAME_INFO, firstName + "," + lastName);
    }

    public static String getNameInfo() {
        return PreferencesUtil.get(KEY_NAME_INFO, "");
    }

    public static void savePremiumExpireTime(String expireTime) {
        String userId = getUserId();
        PreferencesUtil.put(userId + "_premiumExpireTime", expireTime);
    }

    public static String getPremiumExpireTime() {
        String userId = getUserId();
        return PreferencesUtil.get(userId + "_premiumExpireTime", "");
    }

    public static void tokenExpire(Context context) {
        try {
            Class<?> clz = TextUtils.isEmpty(getUserId()) ? Clazz.splashActivityClass : Clazz.loginActivityClass;
            Intent intent = new Intent(context, clz);
            intent.putExtra("tokenExpire",true);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initFirstLaunch() {
        isFirstLaunch = PreferencesUtil.get(KEY_IS_FIRST_LAUNCH, true);
        PreferencesUtil.put(KEY_IS_FIRST_LAUNCH, false);
        ALog.textSingle("NativeAd, isFirstLaunch=" + isFirstLaunch);
    }
}
