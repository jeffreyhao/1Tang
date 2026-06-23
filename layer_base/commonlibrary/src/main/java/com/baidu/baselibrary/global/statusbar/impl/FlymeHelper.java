package com.baidu.baselibrary.global.statusbar.impl;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.baidu.baselibrary.util.sys.LogUtil;

import java.lang.reflect.Field;


/**
 * Flyme 深色字体帮助类
 */
public class FlymeHelper {
    /**
     * 设置沉浸式窗口，设置成功后，状态栏则透明显示
     *
     * @param window    需要设置的窗口
     * @param immersive 是否把窗口设置为沉浸
     * @return boolean 成功执行返回true
     */
    public static boolean setImmersedWindow(Window window, boolean immersive) {
        boolean result = false;
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            int trans_status = 0;
            Field flags;
            if (Build.VERSION.SDK_INT < 19) {
                try {
                    trans_status = 1 << 6;
                    flags = lp.getClass().getDeclaredField("meizuFlags");
                    flags.setAccessible(true);
                    int value = flags.getInt(lp);
                    if (immersive) {
                        value = value | trans_status;
                    } else {
                        value = value & ~trans_status;
                    }
                    flags.setInt(lp, value);
                    result = true;
                } catch (Exception e) {
                }
            } else {
                lp.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                window.setAttributes(lp);
                result = true;
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setStatusBarLightMode(Window window, boolean isFontColorDark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isFontColorDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
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
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setTXTStatusBarLightMode(Window window, boolean isFontColorDark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isFontColorDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
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

    public static boolean setStatusBarLightMode(WindowManager wm, WindowManager.LayoutParams params, View view, boolean isFontColorDark) {
        try {
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(params);
            if (isFontColorDark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(params, value);
            wm.updateViewLayout(view, params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AndroidMHelper.setStatusBarLightMode(view, isFontColorDark);
            }
            return true;
        } catch (Throwable e) {
            LogUtil.e(e);
        }
        return false;
    }

}
