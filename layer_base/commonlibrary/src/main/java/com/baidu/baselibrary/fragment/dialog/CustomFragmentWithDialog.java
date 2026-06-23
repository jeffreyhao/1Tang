package com.baidu.baselibrary.fragment.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.fragment.CustomFragmentActivity;


/**
 * Created by haojiangfeng on 2023/11/28.
 */
public class CustomFragmentWithDialog extends CustomFragment {

    protected CustomDialog mDialog;

    protected CustomFragmentActivity mActivity;

    protected DialogInterface.OnDismissListener dismissListener;


    public CustomFragmentWithDialog(CustomFragmentActivity context) {
        mActivity = context;
        mDialog = new CustomDialog(context);
    }

    public CustomFragmentWithDialog(CustomFragmentActivity context, int themeResId) {
        mActivity = context;
        mDialog = new CustomDialog(context, themeResId);
    }

    public CustomDialog getDialog(){
        return mDialog;
    }

    public void setOnDialogDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        dismiss();
    }

    @Override
    public void finishWithoutAnimation() {
        dismiss();
    }


    public boolean isCancelable(){
        return mDialog.isCancelable();
    }

    /**
     * 获取dialog的宽度，缺省为wrap_content
     */
    public int getDialogWidth() {
        return mDialog.getDialogWidth();
    }

    public void setDialogWidth(int width){
        mDialog.setDialogWidth(width);
    }


    /**
     * 获取dialog的高度，缺省为wrap_content
     */
    public int getDialogHeight() {
        return mDialog.getDialogHeight();
    }

    public void setDialogHeight(int height){
        mDialog.setDialogHeight(height);
    }


    /**
     * 获取dialog的背景透明度
     */
    public float getDimAmount() {
        return mDialog.getDimAmount();
    }

    public void setDimAmount(float dimAmout){
        mDialog.setDimAmount(dimAmout);
    }

    /**
     * 获取dialog的布局位置，缺省为居底
     */
    public int getDialogGravity() {
        return mDialog.getDialogGravity();
    }

    public void setDialogGravity(int gravity){
        mDialog.setDialogGravity(gravity);
    }

    /**
     * 获取dialog的x方向相对于基准边的偏移量，如果dialog左对齐、右对齐时有效
     */
    public int getOffsetX() {
        return mDialog.getOffsetX();
    }

    public void setOffsetX(int offsetX){
        mDialog.setOffsetX(offsetX);
    }

    /**
     * 获取dialog的y方向相对于基准边的偏移量，如果dialog顶对齐、底对齐时有效
     */
    public int getOffsetY() {
        return mDialog.getOffsetY();
    }

    public void setOffsetY(int offsetY){
        mDialog.setOffsetY(offsetY);
    }

    public int getAnimations(){
        return mDialog.getWindowAnimations();
    }

    public void setAnimations(int animations){
        mDialog.setWindowAnimations(animations);
    }


    public void show(){
        onCreate(null);
        View contentView = onCreateView(mActivity.getLayoutInflater(), null, null);
        mDialog.setContentView(contentView);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(dismissListener != null){
                    dismissListener.onDismiss(dialog);
                }
            }
        });
        onViewCreated(contentView, null);
        mDialog.show();
        onResume();
    }

    public void dismiss(){
        onPause();
        if(mDialog != null){
            mDialog.dismiss();
        }
        onStop();
        onDestroy();
        super.finishWithoutAnimation();
    }


    public void dismissAllowingStateLoss(){
        dismiss();
    }

}
