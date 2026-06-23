package com.baidu.baselibrary.global.statusbar.impl;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.baidu.baselibrary.util.clz.ClassUtil;
import com.baidu.baselibrary.util.sys.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * MIUI黑字帮助类
 */

public class MIUIHelper{

    /**
     * 设置状态栏字体图标为深色，需要MIUI6以上
     *
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setStatusBarLightMode(Window window, boolean isFontColorDark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                if(layoutParams == null){
                    return false;
                }
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isFontColorDark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    AndroidMHelper.setStatusBarLightMode(window, isFontColorDark);
                }
                result = true;
            } catch (Throwable e) {
                LogUtil.e(e);
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUI6以上
     *
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setTXTStatusBarLightMode(Window window, boolean isFontColorDark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                if(layoutParams == null){
                    return false;
                }
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isFontColorDark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    AndroidMHelper.setTXTStatusBarLightMode(window, isFontColorDark);
                }
                result = true;
            } catch (Throwable e) {
                LogUtil.e(e);
            }
        }
        return result;
    }

    /**
     * 动态改变windowManager里的状态栏字体颜色
     * @param wm
     * @param params
     * @param view
     * @param isFontColorDark
     */
    public static boolean setStatusBarLightMode(WindowManager wm, WindowManager.LayoutParams params, View view, boolean isFontColorDark) {
        try {
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            if (isFontColorDark) {
                ClassUtil.setField(params,"extraFlags",darkModeFlag);//状态栏透明且黑色字体
            } else {
                ClassUtil.setField(params,"extraFlags",0);//清除黑色字体
            }
            wm.updateViewLayout(view,params);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isFontColorDark) {
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
}
