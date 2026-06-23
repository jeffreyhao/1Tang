package com.meta.ui.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * @author: yuhaibo
 * @time: 2018/10/25 5:34 PM.
 * 
 * Description: 解决横向滑动冲突
 */
public class CustomRecyclerView extends RecyclerView {

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // interceptTouch是自定义属性控制是否拦截事件
        if (true) {
            ViewParent parent = this;
            // 循环查找ViewPager, 请求ViewPager不拦截触摸事件
            try {
                do {
                } while (!((parent = parent.getParent()) instanceof ViewPager));
            } catch (Exception e) {
                return super.dispatchTouchEvent(ev);
            }
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

}