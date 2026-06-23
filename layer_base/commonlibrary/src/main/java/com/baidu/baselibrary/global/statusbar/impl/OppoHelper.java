package com.baidu.baselibrary.global.statusbar.impl;

import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * OPPO的color os系统的状态栏帮助类
 */
public class OppoHelper {

    public static final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 16;

    public static boolean setStatusBarLightMode(Window window, boolean isFontColorDark) {
        try {
            Class.forName("com.color.os.ColorBuild");
        } catch (ClassNotFoundException e) {
            return false;
        }
        if (isFontColorDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT);
            }
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndroidMHelper.setStatusBarLightMode(window, isFontColorDark);
        }
        return true;
    }

    public static boolean setTXTStatusBarLightMode(Window window, boolean isFontColorDark) {
        try {
            Class.forName("com.color.os.ColorBuild");
        } catch (ClassNotFoundException e) {
            return false;
        }
        if (isFontColorDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndroidMHelper.setTXTStatusBarLightMode(window, isFontColorDark);
        }
        return true;
    }

    public static boolean setStatusBarLightMode(View view, boolean isFontColorDark) {
        try {
            Class.forName("com.color.os.ColorBuild");
        } catch (ClassNotFoundException e) {
            return false;
        }
        if (isFontColorDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT);
            }
        } else {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndroidMHelper.setStatusBarLightMode(view, isFontColorDark);
        }
        return true;
    }
}
