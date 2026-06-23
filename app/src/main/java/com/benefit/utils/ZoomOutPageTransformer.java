package com.benefit.utils;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.8f;

    private static final float defaultScale = 0.8f;

    public void transformPage(View view, float position) {
        //获取屏幕宽度
        int pageWidth = view.getWidth();
        //获取屏幕高度
        int pageHeight = view.getHeight();

        if (position < -1) {
            view.setScaleX(defaultScale);
            view.setScaleY(defaultScale);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            if (position < 0) {
                view.setPivotX(pageWidth);
            } else {
                view.setPivotX(pageWidth);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setPivotY(pageHeight);
        } else {
            view.setScaleX(defaultScale);
            view.setScaleY(defaultScale);
        }
    }
}