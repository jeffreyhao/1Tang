package com.baidu.baselibrary.fragment.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.util.AppUtil;
import com.base.watcher.IWatched;
import com.base.watcher.WatcherEvent;
import com.base.watcher.Watcher;
import com.jess.baselibrary.R;

/**
 * Created by haojiangfeng on 2023/8/8.
 */

public class CustomDialog extends Dialog implements IWatched {

    private final static float DEFAULT_DIM_AMOUNT = 0.5f;



    private int mDialogWidth;
    private int mDialogHeight;
    private int mDialogGravity;
    private int mDialogFormat;
    private float mDialogDimAmount;
    private int mWindowAnimations;
    private int mOffsetX;
    private int mOffsetY;

    private boolean mCancelable = true;

    public CustomDialog(Context context) {
        super(context, R.style.dialog);
        setCanceledOnTouchOutside(true);
        init();
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        init();
    }

    private void init(){
        mDialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        mDialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialogGravity = Gravity.CENTER;
        mDialogFormat = PixelFormat.TRANSLUCENT;
        mDialogDimAmount = DEFAULT_DIM_AMOUNT;
        mWindowAnimations = 0;
        mOffsetX = 0;
        mOffsetY = 0;
    }


    /**
     * 设置dialog在屏幕中的布局参数；
     *
     *      gravity     在屏幕中的位置
     *
     *      width       dialog的宽度
     *
     *      height      dialog的高度
     *
     *      OffsetX     x方向偏移量
     *
     *      OffsetY     y方向偏移量
     */
    @SuppressLint("WrongConstant")
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Watcher.getInstance().registerDataSetObserver(this);
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = mDialogWidth;
        lp.height = mDialogHeight;
        lp.gravity = mDialogGravity;
        lp.format = mDialogFormat;
        lp.dimAmount = mDialogDimAmount;
        lp.windowAnimations = mWindowAnimations;
        lp.x = mOffsetX;
        lp.y = mOffsetY;
        w.setAttributes(lp);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Watcher.getInstance().unregisterObserver(this);
    }


    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
    }

    public boolean isCancelable(){
        return mCancelable;
    }


    public int getWindowAnimations() {
        return mWindowAnimations;
    }

    public void setWindowAnimations(int windowAnimations) {
        this.mWindowAnimations = windowAnimations;
    }

    /**
     * 获取dialog的宽度，缺省为wrap_content
     *
     * @return
     */
    public int getDialogWidth() {
        return mDialogWidth;
    }

    public void setDialogWidth(int width){
        mDialogWidth = width;
    }


    /**
     * 获取dialog的高度，缺省为wrap_content
     *
     * @return
     */
    public int getDialogHeight() {
        return mDialogHeight;
    }

    public void setDialogHeight(int height){
        mDialogHeight = height;
    }


    /**
     * 获取dialog的背景透明度
     *
     * @return
     */
    public float getDimAmount() {
        return mDialogDimAmount;
    }

    public void setDimAmount(float dimAmout){
        mDialogDimAmount = dimAmout;
    }

    /**
     * 获取dialog的布局位置，缺省为居底
     *
     * @return
     */
    public int getDialogGravity() {
        return mDialogGravity;
    }

    public void setDialogGravity(int gravity){
        mDialogGravity = gravity;
    }

    /**
     * 获取dialog的x方向相对于基准边的偏移量，如果dialog左对齐、右对齐时有效
     *
     * @return
     */
    public int getOffsetX() {
        return mOffsetX;
    }

    public void setOffsetX(int offsetX){
        mOffsetX = offsetX;
    }

    /**
     * 获取dialog的y方向相对于基准边的偏移量，如果dialog顶对齐、底对齐时有效
     *
     * @return
     */
    public int getOffsetY() {
        return mOffsetY;
    }

    public void setOffsetY(int offsetY){
        mOffsetY = offsetY;
    }

    @Override
    public void show() {
        // 解决badToken
        if (!AppUtil.isActivityAvailable(getContext())){
            return;
        }
        try {
            super.show();
        }catch (Throwable e){
            LogUtil.e(e);
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        }catch (Throwable e){
            LogUtil.e(e);
        }
    }


    @Override
    public void notifyWatcher(int event, Object object) {
        switch (event){
            case WatcherEvent.EVENT_ON_CONFIGURE_CHANGE:
                Configuration newConfig = (Configuration) object;
                onConfigurationChanged(newConfig);
                break;
        }
    }

    public void onConfigurationChanged(Configuration newConfig){

    }



}
