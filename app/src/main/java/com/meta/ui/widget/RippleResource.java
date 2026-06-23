package com.meta.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

import com.base.res.Res;
import com.benefit.novelverse.R;

/**
 * Created by haojiangfeng on 2024/12/26.
 */
public class RippleResource {


    /**
     * 圆形水波纹
     */
    public static int rippleCircleResource;
    /**
     * 方形水波纹
     */
    public static int rippleBlockResource;
    /**
     * 无边界水波纹
     */
    public static int rippleBorderlessResource;


    public static void init(){
        String brand = Build.BRAND == null ? "" : Build.BRAND.toLowerCase();
        switch (brand){
            case "xiaomi":
                RippleResource.rippleCircleResource = R.drawable.bg_ripple_circle;
                RippleResource.rippleBlockResource = R.drawable.bg_ripple_block;
                RippleResource.rippleBorderlessResource = R.drawable.bg_ripple_borderless;
                break;
            default:
                RippleResource.rippleCircleResource = getBackgroundResource(Res.getContext(), android.R.attr.actionBarItemBackground);
                RippleResource.rippleBlockResource = getBackgroundResource(Res.getContext(), android.R.attr.selectableItemBackground);
                RippleResource.rippleBorderlessResource = getBackgroundResource(Res.getContext(), android.R.attr.selectableItemBackgroundBorderless);
                break;
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

}
