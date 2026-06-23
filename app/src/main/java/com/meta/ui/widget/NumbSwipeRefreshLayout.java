package com.meta.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * NumbSwipeRefreshLayout
 */
public class NumbSwipeRefreshLayout extends SwipeRefreshLayout {
    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    private int x = 0;
    private int y = 0;



    public NumbSwipeRefreshLayout(Context context) {
        super(context);
    }

    public NumbSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                x = (int) event.getX();
                y = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    return false;
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }

        return super.onInterceptTouchEvent(event);
    }
}
