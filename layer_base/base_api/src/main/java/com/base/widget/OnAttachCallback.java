package com.base.widget;

import android.view.ViewGroup;

public interface OnAttachCallback {
    void onAttachedToWindow(ViewGroup view);
    void onDetachedFromWindow(ViewGroup view);
}
