package com.baidu.baselibrary.fragment.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public abstract class PreDrawFrameLayout extends FrameLayout{


    ArrayList <ViewTreeObserver.OnPreDrawListener> mOnPreDrawListeners = new ArrayList<>();

    public PreDrawFrameLayout(@NonNull Context context) {
        super(context);
    }

    public PreDrawFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreDrawFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PreDrawFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    public abstract void setCanScroll(boolean canScroll);

    public abstract int getScrollDuration();

    public abstract boolean onBackPress();

    public abstract void clearTop();

    public abstract void enableGesture(boolean enable);

    public void addOnPreDrawListener(ViewTreeObserver.OnPreDrawListener listener){
        mOnPreDrawListeners.add(listener);
    }

    public void removeOnPreDrawListener(ViewTreeObserver.OnPreDrawListener listener){
        mOnPreDrawListeners.remove(listener);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        while (mOnPreDrawListeners.size() > 0){
            mOnPreDrawListeners.remove(mOnPreDrawListeners.size() - 1).onPreDraw();
        }
        super.dispatchDraw(canvas);
    }

}
