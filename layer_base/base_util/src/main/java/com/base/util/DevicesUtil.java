package com.base.util;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.base.api.GlobalContext;
import com.base.api.PreferenceApi;
import com.base.util.ui.UIUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 设备信息Util
 */
public class DevicesUtil {

    private static boolean mInited;

    private static int mScreenWidth;                //屏幕横向分辨率
    private static int mScreenHeight;                //屏幕竖向分辨率
    public static final String ANDROID_ID  = "android_id";

    private DevicesUtil() {
    }


    synchronized public static void init() {
        try {
            if (mInited && mScreenWidth != 0 && mScreenHeight != 0) return;
//            mPackageName = context.getPackageName();
//            if (TextUtils.isEmpty(mPackageName)) mPackageName = "";
//            initBrand();
//            initSerialNum(context);
//            initTelepone(context);
//            getAPPName(context);
//            initRomId();
//            setNetType(getNetTypeImmediately(context));
//            initURL(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mInited = true;
    }



    /**
     * 获取设备Id
     *
     * @return 设备Id
     */
    public static String getDeviceId() {
        String androidId = PreferenceApi.get(ANDROID_ID, "");
        if(!TextUtils.isEmpty(androidId)) {
            return androidId;
        }
        String deviceId = com.blankj.utilcode.util.DeviceUtils.getAndroidID();
        if (TextUtils.isEmpty(deviceId)) {
            return "0";
        }
        PreferenceApi.put(ANDROID_ID, deviceId);
        return deviceId;
    }

    /**
     * 获取AndroidId
     *
     * @return androidId
     */
    public static String getAndroidId() {
        String androidId = PreferenceApi.get(ANDROID_ID, "");
        if(!TextUtils.isEmpty(androidId)) {
            return androidId;
        }
        String deviceId = com.blankj.utilcode.util.DeviceUtils.getAndroidID();
        if (TextUtils.isEmpty(deviceId)) {
            return "0";
        }
        PreferenceApi.put(ANDROID_ID, deviceId);
        return deviceId;
    }


    /**
     * 获取serialNo
     *
     * @return serialNo
     */
    public static String getSerialNo() {
        return "0";
    }

    /**
     * 获取mac
     *
     * @return mac
     */
    public static String getMac() {
        String mac = com.blankj.utilcode.util.DeviceUtils.getMacAddress();
        if (!TextUtils.isEmpty(mac) && !"02:00:00:00:00:00".equals(mac) &&
                !"00:00:00:00:00:00".equals(mac)) {
            return mac;
        }
        return "0";
    }

    /**
     * 获取utid
     *
     * @return utid
     */
    public static String getUTID() {
//        String utid = com.blankj.utilcode.util.DeviceUtils.getAndroidID();
//        if (!TextUtils.isEmpty(utid)) {
//            return utid;
//        }
        return "0";
    }

//    /**
//     * 获取uuid
//     *
//     * @return uuid
//     */
//    public static String getUUID() {
//        return DeviceIdentifier.getGUID(GlobalContext.getContext());
//    }

    /**
     * 获取设备系统系统版本
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备厂商 如 Xiaomi
     *
     * @return 设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号 如MI2SC
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 判断是否平板设备
     * @return true:平板,false:手机
     */
    public static  boolean isTabletDevice() {
        return (UIUtil.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public static int getDisplayWidth() {
        if (mScreenWidth == 0) {
            initDisplayMetrics();
        }
        return mScreenWidth;
    }


    public static int getDisplayHeight() {
        if (mScreenHeight == 0) {
            initDisplayMetrics();
        }
        return mScreenHeight;
    }

    /**
     * 初始化屏幕参数
     */
    private static void initDisplayMetrics() {
        DisplayMetrics dm = GlobalContext.getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels < dm.heightPixels ? dm.widthPixels : dm.heightPixels;
        mScreenHeight = dm.widthPixels > dm.heightPixels ? dm.widthPixels : dm.heightPixels;
//        if (dm.ydpi > 4 * dm.densityDpi / 5 && dm.ydpi < 6 * dm.densityDpi / 5) {
//            //--有效的DPI
//            mScreenDensityDpiY = dm.ydpi;
//        } else {
//            mScreenDensityDpiY = dm.densityDpi;
//        }
//        mScreenInchF = (float) (Math.sqrt(Math.pow(mScreenWidth, 2) + Math.pow(mScreenHeight, 2)) / mScreenDensityDpiY);
//        mScreenInch = Math.round(mScreenInchF);
//        Logger.E("LOG", "initDisplayMetrics ScreenInch:" + mScreenInch + "," + mScreenInchF + " mScreenWidth:" + mScreenWidth + " mScreenHeight:" + mScreenHeight);
//        mScreenType = ScreenType.getScreenType(mScreenInch);
//        mLCDType = getLCDType();
//        initRealMetrics();
    }


    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 4096);
            line = input.readLine();
            input.close();
        } catch (Throwable ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    /**
     * 获取simOperator
     * @return
     */
    public static String getSimOperator() {
        if(GlobalContext.getContext()!=null) {
            TelephonyManager telephonyManager = (TelephonyManager) GlobalContext.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSimOperator();
        }
        return "";
    }

    public static void saveDNS(String dns) {
        PreferenceApi.put("dns", dns);
    }

    /**
     * 获取存储的dns
     * @return
     */
    public static String getDNS() {
        return PreferenceApi.get("dns","");
    }

    public static void saveNetCountry(String netCountry) {
        PreferenceApi.put("netCountry",netCountry);
    }

    public static String getNetCountry() {
        return PreferenceApi.get("netCountry","");
    }
}