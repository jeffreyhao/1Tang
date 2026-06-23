package com.baidu.baselibrary.fragment.dialog;


import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.baidu.baselibrary.base.fragment.BaseCustomFragment;
import com.baidu.baselibrary.fragment.CustomFragment;

public abstract class CustomDialogFragment<F extends BaseCustomFragment> {


    protected FragmentActivity mActivity;
    protected CustomDialog mDialog;
    protected F mFragment;

    protected DialogInterface.OnShowListener mOnShowListener;
    protected DialogInterface.OnDismissListener mOnDismissListener;
    protected DialogInterface.OnCancelListener mOnCancelListener;
    protected DefaultLifecycleObserver mActivityLifecycleObserver;
    protected View mContentView;


    public CustomDialogFragment(FragmentActivity context) {
        mActivity = context;
        initDialog();
    }

    private void initDialog(){
        mDialog = new CustomDialog(mActivity);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if(mOnShowListener != null) {
                    mOnShowListener.onShow(dialog);
                }
                onDialogShow();
            }
        });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mOnDismissListener != null) {
                    mOnDismissListener.onDismiss(dialog);
                }
                onDialogDismiss();
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(mOnCancelListener != null) {
                    mOnCancelListener.onCancel(dialog);
                }
                onDialogCancel();
            }
        });
    }


    public void show(){
        if(mActivity == null) {
            dismiss();
            return;
        }

        // 创建 fragment ｜ onAttach()、onCreate()
        buildFragment();

        // 创建 contentView | onCreateView()、onViewCreated()
        buildContentView();

        // show dialog
        mDialog.show();
        mFragment.onStart();
        mFragment.onResume();
        registerLifeCycleCallback();
    }

    private void buildFragment(){
        mFragment = initFragment();
        if(mFragment == null) {
            dismiss();
            return;
        }
        mFragment.setOnFinishCallback(new CustomFragment.OnFinishCallback() {
            @Override
            public void onFinish(CustomFragment fragment) {
                dismiss();
            }
        });
        mFragment.onAttach(mActivity);
        mFragment.onCreate(null);
        onFragmentCreated();
    }

    private void buildContentView(){
        View fragmentView = mFragment.onCreateView(mActivity.getLayoutInflater(), null, null);
        mContentView = createContentView(mActivity.getLayoutInflater(), fragmentView);
        if(mContentView == null) {
            dismiss();
            return;
        }
        mDialog.setContentView(mContentView);
        mFragment.onViewCreated(fragmentView, null);
        onContentViewCreated();
    }


    public void dismiss(){
        unregisterLifeCycleCallback();
        if(mFragment != null){
            if(mFragment.isResumed()) {
                mFragment.onPause();
            }
            if(!mFragment.isStopped()) {
                mFragment.onStop();
            }
            mFragment.onDestroyView();
            mFragment.onDestroy();
        }
        if(mDialog != null){
            mDialog.dismiss();
        }
        mContentView = null;
        mActivity = null;
        mFragment = null;
    }

    private void registerLifeCycleCallback() {
        mActivityLifecycleObserver = new DefaultLifecycleObserver() {

            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                mFragment.onResume();
            }

            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                dismiss();
            }
        };
        mActivity.getLifecycle().addObserver(mActivityLifecycleObserver);
    }

    private void unregisterLifeCycleCallback() {
        mActivity.getLifecycle().removeObserver(mActivityLifecycleObserver);
    }

    public void setOnShowListener(DialogInterface.OnShowListener listener){
        this.mOnShowListener = listener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener){
        this.mOnDismissListener = listener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener){
        this.mOnCancelListener = listener;
    }


    public FragmentActivity getActivity(){
        return mActivity;
    }

    public CustomDialog getDialog(){
        return mDialog;
    }

    public F getFragment(){
        return mFragment;
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



    protected abstract F initFragment();

    protected void onFragmentCreated() {

    }

    protected abstract View createContentView(LayoutInflater inflater, View fragmentView);

    protected void onContentViewCreated(){

    }


    protected void onDialogShow(){

    }

    protected void onDialogDismiss(){

    }

    protected void onDialogCancel(){

    }

}
