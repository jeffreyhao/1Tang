package com.baidu.baselibrary.fragment.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;


import com.baidu.baselibrary.fragment.callback.OnAnimatingListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;


/**
 * Pre-Honeycomb versions of the platform don't have {@link View#setSaveFromParentEnabled(boolean)},
 * so instead we insert this between the view and its parent.
 */
public class NoSaveStateFrameLayout extends FrameLayout implements OnAnimatingListener {

    private boolean mIsAnimating = false;

    public NoSaveStateFrameLayout(@NonNull Context context) {
        super(context);
    }

    public NoSaveStateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSaveStateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NoSaveStateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public static ViewGroup wrap(View child) {
        NoSaveStateFrameLayout wrapper = new NoSaveStateFrameLayout(child.getContext());
        ViewGroup.LayoutParams childParams = child.getLayoutParams();
        if (childParams == null) {
            childParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        wrapper.setLayoutParams(childParams);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        child.setLayoutParams(lp);
        wrapper.addView(child);
        return wrapper;
    }


    /**
     * 只保存自己的状态
     */
    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    /**
     * 把保存的工作交给子控件处理
     */
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if(mIsAnimating){
            return null;
        }
        return super.invalidateChildInParent(location, dirty);
    }

    @Override
    public void onAnimating(boolean isAnimating) {
        mIsAnimating = isAnimating;
        if(!mIsAnimating) {
            if(getChildCount() >0){
                ViewCompat.postInvalidateOnAnimation(getChildAt(0));
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
