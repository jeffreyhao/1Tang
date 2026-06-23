package com.meta.ui.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.view.NestedScrollingChild;

/**
 * Created by adison on 2017/12/2.
 */

public class NestedScrollingWebView extends AdvancedWebView implements NestedScrollingChild {
    private static final String TAG = "NestedScrollingWebView";
    public NestedScrollingWebView(Context context) {
        super(context);
    }

    public NestedScrollingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollingWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 修复requestDisallowInterceptTouchEvent()方法在SwipeRefreshLayout中不生效的坑爹问题
     * @return
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                /**
                 * SwipeRefreshLayout包裹webview时，如果webview加载的html页有固定表头和上下滚动的表格，会造成滑动冲突，
                 * 下滑会一直调用刷新而不是html页的数据滚动
                 * 每次按下的时候，如果在0,0坐标，让它滚动到0,1，这样就会告诉SwipeRefreshLayout他还在滑动，就不会触发刷新事件
                 */
                if(this.getScrollY() <= 0){
                    this.scrollTo(0,1);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
