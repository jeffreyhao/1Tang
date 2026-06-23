package com.base.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by haojiangfeng on 2025/12/19.
 */
public class LogRecyclerView extends RecyclerView {


    public LogRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LogRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
    }


    @Override
    public void smoothScrollBy(int dx, int dy, @Nullable Interpolator interpolator) {
        super.smoothScrollBy(dx, dy, interpolator);
    }

    @Override
    public void smoothScrollBy(int dx, int dy, @Nullable Interpolator interpolator, int duration) {
        super.smoothScrollBy(dx, dy, interpolator, duration);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }


    @Override
    public void smoothScrollBy(int dx, int dy) {
        super.smoothScrollBy(dx, dy);
//        Logger.printStackTrace();
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
    }
}
