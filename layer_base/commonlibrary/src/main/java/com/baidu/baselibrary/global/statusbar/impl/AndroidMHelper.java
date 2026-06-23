package com.baidu.baselibrary.global.statusbar.impl;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.Window;

/**
 * M系统黑字帮助类
 */
@SuppressLint("InlinedApi")
public class AndroidMHelper{
    /**
     *
     */
    public static boolean setStatusBarLightMode(Window window, boolean isFontColorDark) {
            if (isFontColorDark) {
                window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            return true;
    }

    public static boolean setTXTStatusBarLightMode(Window window, boolean isFontColorDark) {
        if (isFontColorDark) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return true;
    }


    public static boolean setStatusBarLightMode(View view, boolean isFontColorDark) {
        if (isFontColorDark) {
            view.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        return true;
    }

}
