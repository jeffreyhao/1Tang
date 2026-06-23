package com.baidu.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.global.GlobalBuildConfig;
import com.base.util.ui.UIUtil;

public class ShortChapterPayWallView extends FrameLayout {

    private float itemHeightRate;
    private int mParentHeight;
    private int mDividerHeight;


    public ShortChapterPayWallView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ShortChapterPayWallView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShortChapterPayWallView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ShortChapterPayWallView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.itemHeightRate = 0.7f;
        this.mDividerHeight = UIUtil.dip2px(getContext(), 8);
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
            int desiredHeight = (int) (mParentHeight * itemHeightRate) - mDividerHeight;
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

    private void printLog(){
        int measuredHeight = getMeasuredHeight();
        if(GlobalBuildConfig.DEBUG) {
            LogUtil.w("ShortChapterPayWallView", "onMeasure-> height: " + measuredHeight);
        }
    }

}
