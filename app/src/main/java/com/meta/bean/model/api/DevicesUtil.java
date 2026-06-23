package com.meta.bean.model.api;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import com.blankj.utilcode.util.PhoneUtils;
import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.tencent.common.util.Abase;
import com.baidu.baselibrary.Widget;

/**
 * 设备信息Util
 */
public class DevicesUtil {
    private DevicesUtil() {
    }

    /**
     * 获取设备Id
     *
     * @return 设备Id
     */
    public static String getDeviceId() {
        String deviceId = com.blankj.utilcode.util.DeviceUtils.getAndroidID();
        if (TextUtils.isEmpty(deviceId)) {
            return "0";
        }
        return deviceId;
    }

    /**
     * 获取AndroidId
     *
     * @return androidId
     */
    public static String getAndroidId() {
        String androidId = com.blankj.utilcode.util.DeviceUtils.getAndroidID();
        if (TextUtils.isEmpty(androidId)) {
            return "0";
        }
        return androidId;
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

    /**
     * 获取uuid
     *
     * @return uuid
     */
    public static String getUUID() {
        return DeviceIdentifier.getGUID(Widget.getContext());
    }

    /**
     * 获取设备系统系统版本
     */
    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
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
        return (Abase.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}