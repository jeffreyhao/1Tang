package com.baidu.baselibrary.util.device;

import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.clz.ClassUtil;
import com.baidu.baselibrary.util.sys.LogUtil;

import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;


import com.baidu.baselibrary.global.statusbar.StatusBarHelper;
import com.base.util.DevicesUtil;
import com.base.util.ui.UIUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 屏幕适配等
 */
public class ScreenUtil {
    public static final String TAG = "notch";

    //异形屏适配常量
    public static final int mPaddingTopVertical = 0;         //竖屏模式下 顶部的padding
    public static final int mPaddingBottomVertical = 0;         //竖屏模式下 底部的padding
    public static final int mPaddingLeftRightVertical = 0;         //竖屏模式下 左右的padding
    public static final int mPaddingLeftRightLandscape = (int) (StatusBarHelper.getStatusBarHeight());      // 横屏模式下 左右两边的padding
    public static boolean mIsDiffScreen = false;//是不是异形屏 只在应用初始化判断一次即可
    public static boolean sIsDiffScreenAndroidP = false;//是不是是不是异形屏且系统是安卓P

    public static final int mMinPaddingTop = UIUtil.dip2px(7f);//阅读页顶部最小padding 防止内容被异形屏遮盖
    public static final int mDefaultPadding = StatusBarHelper.getStatusBarHeight();//默认的padding 距离
    public static final int mInforBarLRPadding= UIUtil.dip2px(9f);//异形屏下 信息栏左右的最小padding
    public static int sDiffType = 0;
    public static final int DIFF_OPPO = 1;
    public static final int DIFF_VIVO = 2;
    public static final int DIFF_HUAWEI = 3;
    public static final int DIFF_XIAOMI = 4;
    private static int sDiffWidth = 0;

    public static void init(){
        mIsDiffScreen = isDiffScreen();
    }

    /**
     * 是否是异形屏 此方法为判断是否为异形屏的方法
     * 可根据不同的厂商 添加对应的逻辑
     * @return
     */
    public static boolean isDiffScreen(){
        return isDiffScreenOppo() || isDiffScreenVivo() || isDiffScreenHuaWei() || isDiffScreenMiui() || isDiffScreenAndroidP();
    }

    /**
     * 得到异形屏状态下 上下左右 padding值  若手机不是异形屏 则返回{0,0,0,0}
     * 会根据当前activity 的横竖屏情况返回不同的值
     * @return padding值 数组，元素顺序为 上下左右
     */
    public static int[] getPaddingArray() {
        int []arr = {0,0,0,0};
        //如果不是异形屏 返回0000
        if (!mIsDiffScreen) {//有圆角 或者有凹槽 为异形屏
            return arr;
        }
        if (isScreenPortrait()) { //竖屏
            return getPortraitPadding();
        }else { //横屏
            return getLandscapePadding();
        }
    }

    /**
     * 得到异形屏手机 横屏padding
     * @return
     */
    public static int[] getLandscapePadding() {
        return new int[]{ScreenUtil.mPaddingLeftRightLandscape,0, 0,0};
    }

    /**
     * 得到异形屏手机 竖屏padding
     * @return
     */
    public static int[] getPortraitPadding() {
        if (App.mEnableShowSysBar) {//开启状态栏
            return new int[]{mPaddingLeftRightVertical,0,mPaddingLeftRightVertical, ScreenUtil.mPaddingBottomVertical};
        }else {
            return new int[]{mPaddingLeftRightVertical, ScreenUtil.mPaddingTopVertical,mPaddingLeftRightVertical, ScreenUtil.mPaddingBottomVertical};
        }
    }


    /** 获取屏幕是否是竖屏
     * @return
     */
    protected static boolean isScreenPortrait(){
        return App.isScreenPortrait;
    }

    /**
     * 得到顶部的padding
     * 如果不是异形屏返回0
     * 如果横屏 返回0
     * @return
     */
    public static int getTopPadding() {
        int topPadding = 0;
        //如果不是异形屏 返回0000
        if (!mIsDiffScreen) {//有圆角 或者有凹槽 为异形屏
            return topPadding;
        }
        if (isScreenPortrait() ||(App.isInMultiWindowMode && !App.isInMultiWindowBottom)) { //竖屏
            if (!App.mEnableShowSysBar) {//开启状态栏
                topPadding = mDefaultPadding;
            }
        }
        return topPadding;
    }

    /**
     * 得到左边的padding
     * 如果不是异形屏返回0
     * 如果横屏 返回状态栏高度
     * @return
     */
    public static int getLeftPadding() {
        int leftPadding = 0;
        //如果不是异形屏 返回0000
        if (!mIsDiffScreen) {//有圆角 或者有凹槽 为异形屏
            return leftPadding;
        }
        if (!isScreenPortrait() && !App.isInMultiWindowMode) { //横屏
            leftPadding = mDefaultPadding;
        }
        return leftPadding;
    }

    /**
     * 是否需要左边的padding
     * 如果竖屏不需要
     * 横屏需要
     * 用于部分控件 需要特殊调节边距
     * @return
     */
    public static boolean isNeedLeftPadding(){
        boolean result = false;
        if (mIsDiffScreen && !isScreenPortrait()) {
            result = true;
        }
        return result;
    }

    /**
     * 是否需要上边的padding
     * 如果横屏不需要
     * 显示系统状态栏不需要
     * @return
     */
    public static boolean isNeedTopPadding(){
        boolean result = false;
        if (mIsDiffScreen && !App.mEnableShowSysBar && (isScreenPortrait() || (App.isInMultiWindowMode && ! App.isInMultiWindowBottom))) {
            result = true;
        }
        return result;
    }

    public static int getDIffWidth(){
        return sDiffWidth;
    }
    /**
     * 判断手机是否有圆角 或者是否有凹槽（vivo 厂商提供的方法 其他room可能不是）
     * @return
     */
    public static boolean hasProperty(int mask){
        boolean hasProperty = false;
        try {
            Class<?> FtFeature = Class.forName("android.util.FtFeature");
            Method method = ClassUtil.getMethod(FtFeature, "isFeatureSupport", new Class[]{int.class});
            if (method != null) {
                hasProperty= (boolean) method.invoke(null,mask);
            }
        } catch (Exception e) {
//            LogUtil.e(e);
        }
        return hasProperty;
    }

    /**
     *手机是否有凹槽（vivo 厂商提供的方法 其他room可能不是）
     * @return
     */
    public static boolean hasGroove(){
        int mask = 0x00000020;
        return hasProperty(mask);
    }

    /**
     * vivo 异形屏判断方法
     * @return
     */
    public static boolean isDiffScreenVivo(){
        boolean ret = hasGroove();
        if(ret){
            sDiffType = DIFF_VIVO;
            sDiffWidth = DevicesUtil.getDisplayWidth()/3;
        }
        return ret;
    }

    /**
     * oppo 异形屏判断方法
     * @return
     */
    public static boolean isDiffScreenOppo(){
        boolean ret = App.getAppContext().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        if(ret){
            sDiffType = DIFF_OPPO;
            sDiffWidth = 324;//oppo的挖孔区的宽高为324*80px
        }

        return ret;
    }

    /**
     * android P 异形屏判断方法
     * @return
     */
    public static boolean isDiffScreenAndroidP(){
        return sIsDiffScreenAndroidP;
    }

    public static boolean isDiffScreenMiui(){
        boolean ret = "1".equals(DevicesUtil.getSystemProperty("ro.miui.notch"));
        if(ret){
            sDiffType = DIFF_XIAOMI;
            int resourceId = App.getResources().getIdentifier("notch_width", "dimen", "android");
            if (resourceId > 0) {
                sDiffWidth = App.getResources().getDimensionPixelSize(resourceId);
            }else {
                if("dipper".equals(Build.DEVICE)){//小米8
                    sDiffWidth = 560;//小米8为560*89px
                }else if("sirius".equals(Build.DEVICE)){//小米8 SE
                    sDiffWidth = 540;//小米8 SE为540*85px
                }else if("ursa".equals(Build.DEVICE)){//小米8 透明探索版
                    sDiffWidth = 560;//小米8 透明探索版为560*89px
                }else if("sakura".equals(Build.DEVICE)){//红米6 pro
                    sDiffWidth = 352;//红米6 pro为352*89px
                }
                sDiffWidth = 560;//小米8为560*89px
            }
        }
        return ret;
    }

    /**
     * 华为异形屏判断方法
     *
     * @return
     */
    public static boolean isDiffScreenHuaWei() {
        boolean temp = false;
        try {
            String result = DevicesUtil.getSystemProperty("ro.config.hw_notch_size");
            if (!TextUtils.isEmpty(result)) {
                String[] params = result.split(",");
                int length = params.length;
                sDiffType = DIFF_HUAWEI;
                if (length >= 4) {
                    sDiffWidth = Integer.valueOf(params[0]);
//                    String widthString = Util.getSystemProperty("persist.sys.rog.width");
//                    String heightString = Util.getSystemProperty("persist.sys.rog.height");
//                    if(!TextUtils.isEmpty(widthString) && !TextUtils.isEmpty(heightString)){
//                        int width = Integer.valueOf(widthString);
//                        int height = Integer.valueOf(heightString);
//                        int min = Math.min(width, height);
//                        if (DeviceInfor.DisplayWidth() != min) {
//                            sDiffWidth = sDiffWidth * min / DeviceInfor.DisplayWidth();
//                        }
//                    }
                } else {
                    sDiffWidth = DevicesUtil.getDisplayWidth() / 3;
                }
                temp = true;
            }
        } catch (Exception e) {
            LogUtil.e(e);
            sDiffWidth = DevicesUtil.getDisplayWidth() / 3;
        }
        return temp;
    }

    /**
     * 是否是左挖孔屏
     */
    public static boolean sIsLeftNotchScreen = false;

    /**
     * 检测是否是左挖孔屏，适配诸如华为nove4,三星等机型
     * @param contentView
     */
    public static void checkIfLeftNotchScreen(Window window, final View contentView) {
        if (sIsLeftNotchScreen) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 左挖孔屏适配，让布局延伸到刘海区显示
            setDisplayCutoutShortEdges(window);

            contentView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    DisplayCutout cutout = windowInsets.getDisplayCutout();
                    // 手机是刘海屏
                    if (cutout != null) {
                        // 返回Rect的列表，每个Rect都是显示屏上非功能区域的边界矩形。设备的每个短边最多只有一个非功能区域，而长边上则没有。
                        List<Rect> list = cutout.getBoundingRects();
                        if (list != null && !list.isEmpty()) {
                            Rect rect = list.get(0);
                            if (rect != null) {
                                sDiffWidth = rect.right - rect.left;

                                if (list.size() == 1) {
                                    // 目前能确定的是一块刘海区域的时候，可以适配
                                    sIsDiffScreenAndroidP = true;
                                } else {
                                    // 多块刘海区域的时候，不确定具体形态，遇到时再适配
                                    sIsDiffScreenAndroidP = false;
                                }
                                if (rect.right < DevicesUtil.getDisplayWidth() / 2) {
                                    sIsLeftNotchScreen = true;
                                }
                            }
                        }
                    }
                    return windowInsets;
                }
            });
        }
    }

    /**
     * 全屏幕下布局延伸到刘海区显示,android 9.0+支持
     *
     * @param window
     */
    public static void setDisplayCutoutShortEdges(Window window) {
        if (window == null) {
            return;
        }

        // LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 才可以使用刘海区显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = window.getAttributes();
            // 应用如果想让布局延伸到刘海区显示，设置 LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES属性使用刘海区显示
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
        }
    }
}
