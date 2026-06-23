package com.baidu.baselibrary.util.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class StatusBarUtil {


    public static void transparencyBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

    }

    public static void changestatusbarColor(Activity paramActivity, int paramInt1) {
        if (Build.VERSION.SDK_INT >= 21) {
            paramActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            paramActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            paramActivity.getWindow().setStatusBarColor(paramInt1);
        }
    }


    public static boolean isHaveSoftKey(Activity activity) {
        Display d = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static int getBottomSoftKeysHeight(Activity activity) {
        Display d = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        return (realHeight - displayHeight);
    }


    /**
     * 设置状态栏字体颜色
     *
     * 该方法已适配 Android11。 2024.03.11
     */
    public static void setLightStatusBar(Activity activity, boolean dark) {
        if(activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android R (android 11, API 30) 使用下面的新api
            // 传入0则是清理状态,恢复高亮
            int state = dark ? 0 : WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
            activity.getWindow().getInsetsController().setSystemBarsAppearance(state, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            // 低于android R 使用兼容模式
            WindowInsetsControllerCompat controllerCompat = WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView());
            if(controllerCompat != null){
                controllerCompat.setAppearanceLightStatusBars(!dark);
            }
        }
    }

    public static void setLightNavigationBar(Activity activity, boolean dark) {
        if(activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android R (android 11, API 30) 使用下面的新api
            // 传入0则是清理状态,恢复高亮
            int state = dark ? 0 : WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS;
            activity.getWindow().getInsetsController().setSystemBarsAppearance(state, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        } else {
            // 低于android R 使用兼容模式
            WindowInsetsControllerCompat controllerCompat = WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView());
            if(controllerCompat != null){
                controllerCompat.setAppearanceLightNavigationBars(!dark);
            }
        }
    }



    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * hide底部导航条
     * add on 2024/07/02
     */
    public static void hideNavigationBar(Window window){
        if(window != null && window.getDecorView() != null){
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            WindowInsetsControllerCompat controller = ViewCompat.getWindowInsetsController(decorView);
            if(controller != null){
                controller.hide(WindowInsetsCompat.Type.navigationBars());
            }
        }
    }


    /**
     * 读取主题属性中的导航栏颜色（不一定是真正呈现的导航栏颜色）
     */
    public static int getNavigationBarColorFromTheme(Context context) {
        int navBarColor = 0;
        TypedValue typedValue = new TypedValue();
        // 检查主题是否有指定的导航栏颜色属性
        if (context.getTheme().resolveAttribute(android.R.attr.navigationBarColor, typedValue, true)) {
            navBarColor = typedValue.data;
        }
        return navBarColor;
    }

    public static void setStatusNavigationBar(Activity activity, boolean statusBarDark, boolean navigationBarDark) {
        if(activity == null || activity.getWindow() == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android R (android 11, API 30) 使用下面的新api
            // 传入0则是清理状态,恢复高亮
            int statusBarState = statusBarDark ? 0 : WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
            int navigationBarState = navigationBarDark ? 0 : WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS;
            WindowInsetsController windowInsetsController = activity.getWindow().getInsetsController();
            if(windowInsetsController != null) {
                activity.getWindow().getInsetsController().setSystemBarsAppearance(statusBarState, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
                activity.getWindow().getInsetsController().setSystemBarsAppearance(navigationBarState, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
            }
        } else {
            if(activity.getWindow().getDecorView() == null) {
                return;
            }
            // 低于android R 使用兼容模式
            WindowInsetsControllerCompat controllerCompat = WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView());
            if(controllerCompat != null){
                controllerCompat.setAppearanceLightStatusBars(!statusBarDark);
                controllerCompat.setAppearanceLightNavigationBars(!statusBarDark);
            }
        }
    }


}
