package com.base.util.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.base.api.GlobalContext;
import com.blankj.utilcode.util.ActivityUtils;

import java.lang.reflect.Field;

/**
 * 创建人:lhc
 * 创建时间:2017/8/11 14:39
 * 功能描述:处理Ui相关问题
 */
public class UIUtil {

    private static int statusbarheight;


    public static int dip2px(float dipValue) {
        return dip2px(GlobalContext.getContext(), dipValue);
    }

    public static int px2dp(float px) {
        return px2dp(GlobalContext.getContext(), px);
    }

    /**
     * @param context
     * @param dipValue
     * @return
     * @brief DP转像素
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    static DisplayMetrics dm = new DisplayMetrics();
    /**
     * 方法功能描述:获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 方法功能描述:获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getDensityDpi(){
        Activity topActivity = ActivityUtils.getTopActivity();
        if(null==topActivity) return 0;
        topActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    public static int getRealHeight(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static boolean checkDeviceHasNavigationBar(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * Android P 刘海屏判断
     * @param activity
     * @return
     */
    public static DisplayCutout isAndroidP(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 28){
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null)
                return windowInsets.getDisplayCutout();
        }
        return null;
    }

    public static int getStatusBarHeight2(Context context) {
        if (statusbarheight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusbarheight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusbarheight == 0) {
            statusbarheight = UIUtil.dip2px(context,25);
        }
        return statusbarheight;
    }

    public static Configuration getConfiguration() {
        return GlobalContext.getContext().getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return GlobalContext.getContext().getResources().getDisplayMetrics();
    }


    /**
     * 计算两个点之间的距离
     *
     * @param startPoint
     * @param endPoint
     * @return
     */
    public static int calculateA2B(Point startPoint, Point endPoint) {
        int xDist = startPoint.x - endPoint.x;
        int yDist = startPoint.y - endPoint.y;
        return (int) Math.sqrt(xDist * xDist + yDist * yDist);
    }

    /**
     * 计算两个点相对于屏幕坐标的夹角
     *
     * @param startPoint
     * @param endPoint
     * @return
     */
    public static float calculateGradient(Point startPoint, Point endPoint) {
        int xDist = startPoint.x - endPoint.x;
        int yDist = startPoint.y - endPoint.y;
        return (float) xDist / (float) yDist;
    }

    public static float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f;
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    public static boolean containView(ViewGroup container, View child) {
        int size = container.getChildCount();
        for (int i = 0; i < size; i++) {
            View view = container.getChildAt(i);
            if (view == child) return true;
            if ((view instanceof ViewGroup) && containView((ViewGroup) view, child)) {
                return true;
            }
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if(activity==null) return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void setVisible(View view, boolean visible) {
        if(view != null){
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置水波纹效果（圆形水波纹，适合单个icon使用）
     * @param context Context
     * @param target view
     */
    public static void setActionBarBackGround(Context context, View target) {
        if(context != null && target != null){
            int resId = getBackgroundResource(context, android.R.attr.actionBarItemBackground);
            if(resId != 0){
                target.setBackgroundResource(resId);
            }
        }
    }

    /**
     * 设置水波纹效果（方格水波纹，适合item使用）
     * @param context Context
     * @param target view
     */
    public static void setSelectedItemBackGround(Context context, View target) {
        if(context != null && target != null){
            int resId = getBackgroundResource(context, android.R.attr.selectableItemBackground);
            if(resId != 0){
                target.setBackgroundResource(resId);
            }
        }
    }

    /**
     * 设置水波纹效果（不设边界的圆形水波纹，部分控件会呈现类似鼓形的水波纹效果）
     * @param context
     * @param target
     */
    public static void setSelectedItemBackGroundBorderless(Context context, View target) {
        if(context != null && target != null){
            int resId = getBackgroundResource(context, android.R.attr.selectableItemBackgroundBorderless);
            if(resId != 0){
                target.setBackgroundResource(resId);
            }
        }
    }


    public static int getBackgroundResource(Context context, int attr){
        try {
            TypedValue tv = new TypedValue();
            if (context != null) {
                Resources.Theme theme = context.getTheme();
                if (theme != null) {
                    theme.resolveAttribute(attr, tv, true);
                    return tv.resourceId;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void replaceView(ViewGroup viewGroup, View child, View target){
        if(viewGroup == null || child == null || target == null) {
            return;
        }
        int index = -1;
        for(int i = 0; i < viewGroup.getChildCount(); i ++) {
            View item = viewGroup.getChildAt(i);
            if(item == child) {
                index = i;
                break;
            }
        }
        if(index == -1) {
            viewGroup.addView(target);
        } else {
            viewGroup.removeViewAt(index);
            viewGroup.addView(target, index);
        }
    }

}