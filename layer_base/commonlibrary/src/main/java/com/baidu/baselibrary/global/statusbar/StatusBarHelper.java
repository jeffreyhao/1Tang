package com.baidu.baselibrary.global.statusbar;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.baselibrary.global.Const;
import com.baidu.baselibrary.global.SpGlobal;
import com.baidu.baselibrary.global.statusbar.impl.AndroidMHelper;
import com.baidu.baselibrary.global.statusbar.impl.FlymeHelper;
import com.baidu.baselibrary.global.statusbar.impl.MIUIHelper;
import com.baidu.baselibrary.global.statusbar.impl.OppoHelper;
import com.baidu.baselibrary.global.statusbar.impl.VivoHelper;
import com.baidu.baselibrary.util.App;


/**
 * 状态栏工具类
 */
@SuppressWarnings("UnusedParameters")
public class StatusBarHelper {

    //未初始化状态 缺省等于改值
    public static final int NONE = 0;
    //<4.4
    public static final int ANDROID_BELOW_KITKAT = 1;
    //[4.4,6.0) 且不是MIUI、FLYME、YUNOS、VIVO、OPPO
    public static final int OTHER = 2;
    //6.0以上努比亚 但该机型第三方应用状态栏不支持设置成黑色icon 需要当做6.0处理 同时状态栏需要罩一层蒙层
    public static final int NUBIA_ABOVE_M = 3;
    //[6.0,+∞) >该值是可以直接改状态栏色值的 可以伸进去
    public static final int ANDROID_ABOVE_M = 4;
    public static final int MIUI_BELOW_M = 5;
    public static final int MIUI_ABOVE_M = 6;
    public static final int FLYME = 7;
    public static final int YUNOS = 8;
    public static final int VIVO = 9;
    public static final int OPPO = 10;

    public static int mCurrentStatusBarType;


    /** 系统状态栏的高度*/
    public static int MENU_HEAD_HEI							= 0;

    public static int MENU_FOOT_HEI							= 0;


    static {
        mCurrentStatusBarType = SpGlobal.getInstance().getInt(Const.KEY_STATUS_BAR_TYPE, NONE);
    }

    /**
     * 当前手机是否支持设置状态栏字体颜色
     *
     * @return true：支持
     */
    public static boolean supportSetStatusBarMode() {
        return ANDROID_ABOVE_M <= mCurrentStatusBarType;
    }

    /**
     * 当前手机是否需要状态栏蒙层
     *
     * @return true：需要
     */
    public static boolean needDrawStatusBarCover() {
        return OTHER == mCurrentStatusBarType || NUBIA_ABOVE_M == mCurrentStatusBarType;
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return mCurrentStatusBarType
     */
    public static int setStatusBarMode(Activity activity, boolean isFontColorDark) {
        Window window = activity.getWindow();
        if (mCurrentStatusBarType == NONE) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mCurrentStatusBarType = ANDROID_BELOW_KITKAT;
            } else if (FlymeHelper.setStatusBarLightMode(window, isFontColorDark)) {
                mCurrentStatusBarType = FLYME;
            } else if (MIUIHelper.setStatusBarLightMode(window, isFontColorDark)) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    mCurrentStatusBarType = MIUI_ABOVE_M;
                }else {
                    mCurrentStatusBarType = MIUI_BELOW_M;
                }
//            } else if (YunOSHelper.setStatusBarLightMode(window, isFontColorDark)) {
//                mCurrentStatusBarType = YUNOS;
            } else if (VivoHelper.setStatusBarLightMode()) {
                mCurrentStatusBarType = VIVO;
            } else if (OppoHelper.setStatusBarLightMode(window, isFontColorDark)) {
                mCurrentStatusBarType = OPPO;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AndroidMHelper.setStatusBarLightMode(window, isFontColorDark);
                if ("nubia".equalsIgnoreCase(Build.BRAND)) {
                    mCurrentStatusBarType = NUBIA_ABOVE_M;
                } else {
                    mCurrentStatusBarType = ANDROID_ABOVE_M;
                }
            } else {
                mCurrentStatusBarType = OTHER;
            }
            SpGlobal.getInstance().setInt(Const.KEY_STATUS_BAR_TYPE, mCurrentStatusBarType);
        } else if (mCurrentStatusBarType == FLYME) {
            FlymeHelper.setStatusBarLightMode(window, isFontColorDark);
        } else if (mCurrentStatusBarType == MIUI_BELOW_M || mCurrentStatusBarType == MIUI_ABOVE_M) {
            MIUIHelper.setStatusBarLightMode(window, isFontColorDark);
//        } else if (mCurrentStatusBarType == YUNOS) {
//            YunOSHelper.setStatusBarLightMode(window, isFontColorDark);
        } else if (mCurrentStatusBarType == OPPO) {
            OppoHelper.setStatusBarLightMode(window, isFontColorDark);
        } else if (mCurrentStatusBarType == VIVO || mCurrentStatusBarType == NUBIA_ABOVE_M || mCurrentStatusBarType == ANDROID_ABOVE_M) {
            AndroidMHelper.setStatusBarLightMode(window, isFontColorDark);
        }
        return mCurrentStatusBarType;
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return mCurrentStatusBarType
     */
    public static int setTXTStatusBarMode(Window window, boolean isFontColorDark) {
        if (mCurrentStatusBarType == FLYME) {
            FlymeHelper.setTXTStatusBarLightMode(window, isFontColorDark);
        } else if (mCurrentStatusBarType == MIUI_BELOW_M || mCurrentStatusBarType == MIUI_ABOVE_M) {
            MIUIHelper.setTXTStatusBarLightMode(window, isFontColorDark);
//        } else if (mCurrentStatusBarType == YUNOS) {
//            YunOSHelper.setStatusBarLightMode(window, isFontColorDark);
        } else if (mCurrentStatusBarType == OPPO) {
            OppoHelper.setTXTStatusBarLightMode(window, isFontColorDark);
        } else if (mCurrentStatusBarType == VIVO || mCurrentStatusBarType == NUBIA_ABOVE_M || mCurrentStatusBarType == ANDROID_ABOVE_M) {
            AndroidMHelper.setTXTStatusBarLightMode(window, isFontColorDark);
        }
        return mCurrentStatusBarType;
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return mCurrentStatusBarType
     */
    public static int setStatusBarMode(WindowManager wm, WindowManager.LayoutParams params, View view, boolean isFontColorDark) {
        if (mCurrentStatusBarType == NONE) {
            if (FlymeHelper.setStatusBarLightMode(wm, params, view, isFontColorDark)) {
                mCurrentStatusBarType = FLYME;
            } else if (MIUIHelper.setStatusBarLightMode(wm, params, view, isFontColorDark)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    mCurrentStatusBarType = MIUI_ABOVE_M;
                }else {
                    mCurrentStatusBarType = MIUI_BELOW_M;
                }
//            } else if (YunOSHelper.setStatusBarLightMode(view, isFontColorDark)) {
//                mCurrentStatusBarType = YUNOS;
            } else if (VivoHelper.setStatusBarLightMode()) {
                mCurrentStatusBarType = VIVO;
            } else if (OppoHelper.setStatusBarLightMode(view, isFontColorDark)) {
                mCurrentStatusBarType = OPPO;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AndroidMHelper.setStatusBarLightMode(view, isFontColorDark);
                if ("nubia".equalsIgnoreCase(Build.BRAND)) {
                    mCurrentStatusBarType = NUBIA_ABOVE_M;
                } else {
                    mCurrentStatusBarType = ANDROID_ABOVE_M;
                }
            } else {
                mCurrentStatusBarType = OTHER;
            }
            SpGlobal.getInstance().setInt(Const.KEY_STATUS_BAR_TYPE, mCurrentStatusBarType);
        } else if (mCurrentStatusBarType == FLYME) {
            FlymeHelper.setStatusBarLightMode(wm, params, view, isFontColorDark);
        } else if (mCurrentStatusBarType == MIUI_BELOW_M || mCurrentStatusBarType == MIUI_ABOVE_M) {
            MIUIHelper.setStatusBarLightMode(wm, params, view, isFontColorDark);
//        } else if (mCurrentStatusBarType == YUNOS) {
//            YunOSHelper.setStatusBarLightMode(view, isFontColorDark);
        } else if (mCurrentStatusBarType == OPPO) {
            OppoHelper.setStatusBarLightMode(view, isFontColorDark);
        } else if (mCurrentStatusBarType == VIVO || mCurrentStatusBarType == NUBIA_ABOVE_M || mCurrentStatusBarType == ANDROID_ABOVE_M) {
            AndroidMHelper.setStatusBarLightMode(view, isFontColorDark);
        }
        return mCurrentStatusBarType;
    }

    public static void initBaseStatusBar(Activity activity, int paddingTop, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && activity != null) {
            Window window = activity.getWindow();
            View content = window.getDecorView().findViewById(android.R.id.content);
            if(content != null) {
                content.setPadding(0, content.getPaddingTop() + paddingTop, 0, 0);
            }
            if (mCurrentStatusBarType == MIUI_BELOW_M || mCurrentStatusBarType == FLYME || mCurrentStatusBarType == YUNOS) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mCurrentStatusBarType == VIVO && color != Color.TRANSPARENT) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    public static boolean setWindowStatusBarColor(Window window, int color) {
        try {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            //底部导航栏
            //window.setNavigationBarColor(color);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
           return false;
        }
    }

    /**
     * 根据当前是否是夜间模式获取对应状态栏颜色,遮罩颜色，默认VIew会伸下去
     *
     * @return
     */
    public static int getStatusBarCoverColor() {
        int color = 0;
//        //支持更改状态栏字体或者深色皮肤
//        if (mCurrentStatusBarType >= ANDROID_ABOVE_M || !APP.needAddStatusCover()) {
//            color = APP.getResources().getColor(R.color.theme_bookshelf_statusbar_color);
//        } else {
//            color = APP.getResources().getColor(R.color.theme_statusbar_cover_color);
//        }
        return color;
    }

    /**
     * 根据当前夜间模式开关，获取夜间模式下顶部title的颜色
     *
     * @param dayColor 白天的颜色
     * @return 夜间关返回 dayColor,夜间开 蒙层颜色（和dayColor无关）
     */
    public static int getStatusBarCoverColor(int dayColor) {
//        if (ConfigMgr.getInstance().getGeneralConfig().mEnableNight) {
//            return Color.argb(Math.round(255F * Config_Read.NIGHT_PERCENT_DIM), 0, 0, 0);
//        } else {
            return dayColor;
//        }
    }

    private static int STATUS_BAR_HEIGHT;

    public static int getStatusBarHeight() {
        if (STATUS_BAR_HEIGHT <= 0) {
            int resourceId = App.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                STATUS_BAR_HEIGHT = App.getAppContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return STATUS_BAR_HEIGHT;
    }

}
