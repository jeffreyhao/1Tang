package com.meta.ui.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benefit.novelverse.R;


/**
 * @author: yuhaibo
 * @time: 2018/1/18 11:37.
 * projectName: xhhread-android.
 * Description:默认的NavigationBar
 */
public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationBarParams> {
    public TextView titleTextView;
    public TextView rightTextView;

    public DefaultNavigationBar(Builder.DefaultNavigationBarParams barParams) {
        super(barParams);
    }

    @Override
    public int bindHeadLayoutId() {
        return R.layout.defaulthead_layout;
    }

    @Override
    public void applyView() {
        //绑定效果
        titleTextView = setText(R.id.tv_toolbar_title, getParams().mTitle);
        rightTextView = setText(R.id.tv_rightText, getParams().mRightText);
        setIcon(R.id.iv_rightBt, getParams().mRightRes);
        setIcon(R.id.toolbar, getParams().mLeftRes);
        setOnClickListener(R.id.iv_rightBt, getParams().mRightClickListener);
        setOnClickListener(R.id.tv_rightText, getParams().mRightClickListener);
        setOnClickListener(R.id.default_toolbar, getParams().mLeftClickListener);
    }

    public static class Builder extends AbsNavigationBar.Builder {

        private DefaultNavigationBarParams p;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            p = new DefaultNavigationBarParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            p = new DefaultNavigationBarParams(context, null);
        }

        @Override
        public DefaultNavigationBar builder() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(p);
            return defaultNavigationBar;
        }

        //设置所有效果

        /**
         * 设置title
         *
         * @param title
         * @return
         */
        public DefaultNavigationBar.Builder setTitle(String title) {
            p.mTitle = title;
            return this;
        }

        /**
         * 设置右边的文字
         *
         * @param rightText
         * @return
         */
        public DefaultNavigationBar.Builder setRightText(String rightText) {
            p.mRightText = rightText;
            return this;
        }

        /**
         * 设置左边的图片
         *
         * @param leftRes
         * @return
         */
        public DefaultNavigationBar.Builder setLeftIcon(int leftRes) {
            p.mLeftRes = leftRes;
            return this;
        }

        /**
         * 设置右边的图片
         *
         * @param rightRes
         * @return
         */
        public DefaultNavigationBar.Builder setRightIcon(int rightRes) {
            p.mRightRes = rightRes;
            return this;
        }

        /**
         * 设置右边的点击事件
         *
         * @param rightClickListener
         * @return
         */
        public DefaultNavigationBar.Builder setRightClickListener(View.OnClickListener rightClickListener) {
            p.mRightClickListener = rightClickListener;
            return this;
        }

        /**
         * 设置左边的点击事件
         *
         * @param leftClickListener
         * @return
         */
        public DefaultNavigationBar.Builder setLeftClickListener(View.OnClickListener leftClickListener) {
            p.mLeftClickListener = leftClickListener;
            return this;
        }

        public static class DefaultNavigationBarParams extends AbsNavigationBar.Builder.AbsNavigationBarParams {
            //放所有效果
            private String mTitle;
            private String mRightText;
            private int mLeftRes;
            private int mRightRes;
            private View.OnClickListener mRightClickListener;
            private View.OnClickListener mLeftClickListener;

            public DefaultNavigationBarParams(final Context context, ViewGroup parent) {
                super(context, parent);
                mLeftClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //默认关闭Activity
                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(R.anim.bga_sbl_activity_backward_enter, R.anim.bga_sbl_activity_backward_exit);
                    }
                };
            }
        }
    }
}
