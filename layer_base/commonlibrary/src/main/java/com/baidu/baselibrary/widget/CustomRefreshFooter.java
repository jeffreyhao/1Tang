package com.baidu.baselibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.jess.baselibrary.R;
import com.base.util.ui.UIUtil;

/**
* @author lhc
* @date 2022/5/10 9:21
* @desc 自定义刷新footer
*/

@SuppressLint("RestrictedApi")
public class CustomRefreshFooter extends LinearLayout implements RefreshFooter {
    private TextView mHeaderText;//标题文本
    private ProgressBar mProgressBar;

    public CustomRefreshFooter(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        mProgressBar = new ProgressBar(context);
        mProgressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context,R.drawable.progress_dialog_anim));
        addView(mProgressBar, UIUtil.dip2px(context,22), UIUtil.dip2px(context,22));
        mHeaderText = new TextView(context);
        mHeaderText.setTextColor(getResources().getColor(R.color.Cr_21, getContext().getTheme()));
        mHeaderText.setText(getResources().getString(R.string.common_loadmore));
        mHeaderText.setVisibility(View.GONE);
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(UIUtil.dip2px(context,48));
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }


    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        mHeaderText.setText(getResources().getString(R.string.common_loading));
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        mHeaderText.setText(getResources().getString(R.string.common_loading));
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        mHeaderText.setText(getResources().getString(R.string.common_loading));
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }
}
