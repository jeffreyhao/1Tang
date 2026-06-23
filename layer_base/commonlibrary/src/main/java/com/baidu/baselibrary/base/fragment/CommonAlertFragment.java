package com.baidu.baselibrary.base.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.baselibrary.request.EmptyPresenter;
import com.baidu.baselibrary.util.App;
import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;
import com.jess.baselibrary.databinding.FragmentCommonAlertBinding;

public class CommonAlertFragment extends BaseDialogFragment<FragmentCommonAlertBinding, EmptyPresenter> {
    private CharSequence titleContent,hintContent,cancelContent,confirmContent;
    private int hintContentColor,cancelContentColor,confirmContentColor;
    private int confirmButtonBg, cancelButtonBg;
    private boolean cancelableOnTouchOutside = true;
    private boolean isShowWarning = true;
    private boolean isShowCancelButton = true;

    public static CommonAlertFragment getInstance(Bundle args, OnViewClickListener listener) {
        CommonAlertFragment fragment = new CommonAlertFragment(listener);
        fragment.setArguments(args);
        return fragment;
    }

    public CommonAlertFragment() {

    }

    public CommonAlertFragment(OnViewClickListener listener) {
        this.listener = listener;
    }

    public String className(){
        return "CommonAlertFragment";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_alert;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected int getWidth() {
        return getContext()!=null? UIUtil.getScreenWidth(getContext())-UIUtil.dip2px(getContext(),60) : ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected boolean canceledOnTouchOutside() {
        return cancelableOnTouchOutside;
    }

    @Override
    protected void initListener() {
        mBinding.tvCancel.setOnClickListener(v->{
            if(listener!=null)
                listener.onViewClick(R.id.tv_cancel);
            dismiss();
        });
        mBinding.tvConfirm.setOnClickListener(v->{
            if(listener!=null)
                listener.onViewClick(R.id.tv_confirm);
            dismiss();
        });
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        if(isShowWarning) {
            mBinding.tvWarning.setVisibility(View.VISIBLE);
            int dp20 = App.getAppContext().getResources().getDimensionPixelOffset(R.dimen.dp_20);
            mBinding.tvHint.setPadding(0, dp20, 0, dp20);
        } else {
            mBinding.tvWarning.setVisibility(View.GONE);
            int dp32 = App.getAppContext().getResources().getDimensionPixelOffset(R.dimen.dp_32);
            mBinding.tvHint.setPadding(0, dp32, 0, dp32);
        }
        mBinding.tvCancel.setVisibility(isShowCancelButton?View.VISIBLE:View.GONE);
        mBinding.line2.setVisibility(isShowCancelButton?View.VISIBLE:View.GONE);
        if(!TextUtils.isEmpty(titleContent))
            mBinding.tvWarning.setText(titleContent);
        if(!TextUtils.isEmpty(hintContent))
            mBinding.tvHint.setText(hintContent);
        if(!TextUtils.isEmpty(cancelContent))
            mBinding.tvCancel.setText(cancelContent);
        if(!TextUtils.isEmpty(confirmContent))
            mBinding.tvConfirm.setText(confirmContent);
        if(hintContentColor!=0)
            mBinding.tvHint.setTextColor(hintContentColor);
        if(cancelContentColor!=0)
            mBinding.tvCancel.setTextColor(cancelContentColor);
        if(confirmContentColor!=0)
            mBinding.tvConfirm.setTextColor(confirmContentColor);
        if(cancelButtonBg!=0)
            mBinding.tvCancel.setBackgroundResource(cancelButtonBg);
        if(confirmButtonBg!=0)
            mBinding.tvConfirm.setBackgroundResource(confirmButtonBg);
    }

    public CommonAlertFragment setTitleContent(String titleContent) {
        this.titleContent = titleContent;
        return this;
    }

    public CommonAlertFragment setHintContent(CharSequence hint) {
        this.hintContent = hint;
        return this;
    }

    public CommonAlertFragment setHintContentColor(int color) {
        this.hintContentColor = color;
        return this;
    }

    public CommonAlertFragment setCancelButtonText(String cancelContent) {
        this.cancelContent = cancelContent;
        return this;
    }

    public CommonAlertFragment setCancelButtonColor(int color) {
        this.cancelContentColor = color;
        return this;
    }

    public CommonAlertFragment setCancelButtonBg(int cancelButtonBg) {
        this.cancelButtonBg = cancelButtonBg;
        return this;
    }

    public CommonAlertFragment setConfirmButtonText(String confirmContent) {
        this.confirmContent = confirmContent;
        return this;
    }

    public CommonAlertFragment setConfirmButtonColor(int color) {
        this.confirmContentColor = color;
        return this;
    }

    public CommonAlertFragment setConfirmButtonBg(int confirmButtonBg){
        this.confirmButtonBg = confirmButtonBg;
        return this;
    }

    public CommonAlertFragment setCancelableOnTouchOutside(boolean cancelableOnTouchOutside) {
        this.cancelableOnTouchOutside = cancelableOnTouchOutside;
        return this;
    }

    public CommonAlertFragment showWarning(boolean isShowWarning) {
        this.isShowWarning = isShowWarning;
        return this;
    }

    public CommonAlertFragment showCancelButton(boolean isShowCancelButton) {
        this.isShowCancelButton = isShowCancelButton;
        return this;
    }

    public CommonAlertFragment setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
        return this;
    }

    public CommonAlertFragment setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

}
