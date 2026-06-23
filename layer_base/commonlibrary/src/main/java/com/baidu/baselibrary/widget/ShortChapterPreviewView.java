package com.baidu.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


public class ShortChapterPreviewView extends LinearLayout {

    private float itemHeightRate;
    private int mParentHeight;

    public ShortChapterPreviewView(@NonNull Context context) {
        super(context);
        init();
    }

    public ShortChapterPreviewView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShortChapterPreviewView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ShortChapterPreviewView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.itemHeightRate = 0.3f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mParentHeight <= 0) {
            View parentScrollView = findParentScrollable();
            if(parentScrollView != null) {
                mParentHeight = parentScrollView.getMeasuredHeight();
            }
        }
        if (mParentHeight > 0) {
            int desiredHeight = (int) (mParentHeight * itemHeightRate);
            int heightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        printLog();
    }


    // 获取外层可滑动的父容器（ViewPager2 或 RecyclerView）
    private View findParentScrollable() {
        View v = (View) getParent();
        while (v != null) {
            if (v instanceof ViewPager2 || v instanceof RecyclerView) return v;
            v = (View) v.getParent();
        }
        return null;
    }

    private void printLog() {

    }
}
