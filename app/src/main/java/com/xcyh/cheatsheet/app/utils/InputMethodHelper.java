package com.xcyh.cheatsheet.app.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


/**
 * 输入法监听
 */
public class InputMethodHelper {



    public interface OnKeyboardVisibilityListener {

        void onKeyboardVisible(boolean isVisible);
    }


    private Activity mActivity;
    private View mChildOfContent;
    private int usableHeightPrevious;
    private OnKeyboardVisibilityListener listener;
    private boolean keyboardVisible = false;


    public InputMethodHelper(Activity activity) {
        mActivity = activity;
        FrameLayout content = activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
    }

    public void registerKeyboardVisibilityListener(OnKeyboardVisibilityListener listener) {
        this.listener = listener;
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // 键盘弹出
                setKeyboardVisible(true);
            } else {
                // 键盘关闭
                setKeyboardVisible(false);
            }
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    private void setKeyboardVisible(boolean visible) {
        if (keyboardVisible != visible) {
            keyboardVisible = visible;
            if (listener!= null) {
                listener.onKeyboardVisible(visible);
            }
        }
    }

}