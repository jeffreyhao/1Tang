package com.meta.ui.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

/**
 * @author: yuhaibo
 * @time: 2018/1/18 11:16.
 * projectName: xhhread-android.
 * Description:基本的NavigationBar
 */
public abstract class AbsNavigationBar<p extends AbsNavigationBar.Builder.AbsNavigationBarParams> implements INavigationBar {
    private p mBarParams;
    private View navigationBarView;

    public AbsNavigationBar(p barParams) {
        mBarParams = barParams;
        createAndBindView();
    }

    public p getParams() {
        return mBarParams;
    }

    /**
     * 创建和绑定View
     */
    private void createAndBindView() {
        if (mBarParams.mParent == null) {
            //获取Activity的跟布局
            ViewGroup activityRoot = (ViewGroup) ((Activity) mBarParams.mContext).getWindow().getDecorView();
            mBarParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }
        if (mBarParams.mParent == null) {
            return;
        }
        navigationBarView = LayoutInflater.from(mBarParams.mContext).inflate(bindHeadLayoutId(), mBarParams.mParent, false);
        mBarParams.mParent.addView(navigationBarView, 0);
        applyView();
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    protected TextView setText(int viewId, String text) {
        TextView textView = findViewById(viewId);
        if (!TextUtils.isEmpty(text)) {
            //文本内容不为空
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
        return textView;
    }

    /**
     * 设置图标Icon
     *
     * @param viewId
     * @param resId
     */
    protected void setIcon(int viewId, int resId) {
        View view = findViewById(viewId);
        if (view instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) view;
            if (resId == 0) {
                return;
            }
            toolbar.setNavigationIcon(resId);
            return;
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            imageView.setBackgroundResource(resId);
        }
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        if (view instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) view;
            toolbar.setNavigationOnClickListener(listener);
            return;
        }
        view.setOnClickListener(listener);
    }

    private <T extends View> T findViewById(int viewId) {
        return (T) navigationBarView.findViewById(viewId);
    }

    public abstract static class Builder {

        public Builder(Context context, ViewGroup parent) {
        }

        public abstract AbsNavigationBar builder();

        public static class AbsNavigationBarParams {
            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationBarParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
