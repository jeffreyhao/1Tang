package com.benefit.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ProjectName: reader-android
 */
public abstract class PPBasePageFragment extends BasePageFragment {
    // 根布局
    protected View mRootView;

    public PPBasePageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView != null) {
            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
            if (null != viewGroup) {
                viewGroup.removeView(mRootView);
            }
            return mRootView;
        }
        mRootView = inflater.inflate(getResId(), null);
        return mRootView;
    }

    protected abstract int getResId();
}
