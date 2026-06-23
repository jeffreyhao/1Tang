package com.tencent.common.util;

import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import android.view.View;


import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * view 工具类
 * @author adison
 * @date 2017/5/20
 * @time 下午4:39
 */
public final class ViewUtils {
    /**
     * 设置背景
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {
        if (view == null)
            return;

        if (APILevel.require(16)) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置背景
     * @param view
     * @param drawableRes
     */
    public static void setBackground(View view, @DrawableRes int drawableRes) {
        if (view == null)
            return;

        if (APILevel.require(16)) {
            view.setBackground(Abase.getResources().getDrawable(drawableRes));
        } else {
            view.setBackgroundDrawable(Abase.getResources().getDrawable(drawableRes));
        }
    }

    /**
     * 设置view visibility属性是gone还是visible
     * <p/>
     * 如果view visibility当前属性是invisible，该方法无效
     *
     * @param view
     * @param gone
     * @return view
     */
    public static <V extends View> V setGone(final V view, final boolean gone) {
        if (view != null)
            if (gone) {
                if (GONE != view.getVisibility())
                    view.setVisibility(GONE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    /**
     * 设置view visibility属性是invisible还是visible
     * <p/>
     * 如果view visibility当前属性是gone，该方法无效
     *
     * @param view
     * @param invisible
     * @return view
     */
    public static <V extends View> V setInvisible(final V view, final boolean invisible) {
        if (view != null)
            if (invisible) {
                if (INVISIBLE != view.getVisibility())
                    view.setVisibility(INVISIBLE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }


    public static void setVisible(View view, boolean visible){
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}