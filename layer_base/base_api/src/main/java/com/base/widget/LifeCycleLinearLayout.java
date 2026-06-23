package com.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 添加自身回调的一些 callback。可用作容器
 */
public class LifeCycleLinearLayout extends LinearLayout {


    private Set<OnAttachCallback> mOnAttachCallbacks = new LinkedHashSet<>();
    private Set<OnMeasureCallback> mOnMeasureCallbacks = new HashSet<>();
    private Set<OnLayoutCallback> mOnLayoutCallbacks = new LinkedHashSet<>();;
    private Set<OnDrawCallback> mOnDrawCallbacks = new LinkedHashSet<>();


    public LifeCycleLinearLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public LifeCycleLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LifeCycleLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LifeCycleLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
    }

    public void addOnAttachCallback(OnAttachCallback callback){
        if(callback != null) {
            mOnAttachCallbacks.add(callback);
        }
    }

    public void removeAttachCallback(OnAttachCallback callback){
        if(callback != null) {
            mOnAttachCallbacks.remove(callback);
        }
    }

    public void addOnMeasureCallback(OnMeasureCallback callback) {
        if(callback != null) {
            mOnMeasureCallbacks.add(callback);
        }
    }

    public void removeOnMeasureCallback(OnMeasureCallback callback) {
        if(callback != null) {
            mOnMeasureCallbacks.remove(callback);
        }
    }

    public void addOnLayoutCallback(OnLayoutCallback callback) {
        if(callback != null) {
            mOnLayoutCallbacks.add(callback);
        }
    }

    public void removeOnLayoutCallback (OnLayoutCallback callback){
        if(callback != null) {
            mOnLayoutCallbacks.remove(callback);
        }
    }

    public void addOnDrawCallback(OnDrawCallback callback) {
        if(callback != null) {
            mOnDrawCallbacks.add(callback);
        }
    }

    public void removeOnDrawCallback(OnDrawCallback callback){
        if(callback != null) {
            mOnDrawCallbacks.remove(callback);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!mOnAttachCallbacks.isEmpty()) {
            for(OnAttachCallback callback : mOnAttachCallbacks) {
                callback.onAttachedToWindow(this);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(!mOnAttachCallbacks.isEmpty()) {
            for(OnAttachCallback callback : mOnAttachCallbacks) {
                callback.onDetachedFromWindow(this);
            }
        }
        clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!mOnMeasureCallbacks.isEmpty()) {
            for(OnMeasureCallback callback : mOnMeasureCallbacks) {
                callback.onMeasured();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(!mOnLayoutCallbacks.isEmpty()) {
            for (OnLayoutCallback onLayoutCallback : mOnLayoutCallbacks) {
                onLayoutCallback.onLayout();
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if(!mOnDrawCallbacks.isEmpty()) {
            for(OnDrawCallback callback : mOnDrawCallbacks) {
                callback.onDrawBefore();
            }
        }
        super.onDraw(canvas);
        if(!mOnDrawCallbacks.isEmpty()) {
            for(OnDrawCallback callback : mOnDrawCallbacks) {
                callback.onDrawed();
            }
        }
    }


    @Override
    public void requestLayout() {
        super.requestLayout();
//        Logger.printStackTrace();
    }


    private void clear(){
        mOnAttachCallbacks.clear();
        mOnMeasureCallbacks.clear();
        mOnLayoutCallbacks.clear();
        mOnDrawCallbacks.clear();
    }
}
