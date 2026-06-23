package com.baidu.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.base.util.ui.UIUtil;

public class ShortChapterLoadingView extends FrameLayout {

    private int mParentHeight;
    private int mDividerHeight;

    public ShortChapterLoadingView(@NonNull Context context) {
        super(context);
        init();
    }

    public ShortChapterLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShortChapterLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ShortChapterLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mDividerHeight = UIUtil.dip2px(getContext(), 8);
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
            int targetHeight = mParentHeight - mDividerHeight;
            int heightSpec = MeasureSpec.makeMeasureSpec(targetHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
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

    public void resetParentHeight() {
        mParentHeight = 0;
    }
}
