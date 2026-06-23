package com.baidu.baselibrary.widget.dialog;

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
import com.base.watcher.Watcher;
import com.base.watcher.WatcherEvent;
import com.jess.baselibrary.R;

/**
 * Created by haojiangfeng on 2023/8/8.
 */

public class BaseDialog extends Dialog implements IWatched {

    private final static float DEFAULT_DIM_AMOUNT = 0.5f;




    public BaseDialog(Context context) {
        super(context, R.style.dialog);
        setCanceledOnTouchOutside(true);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
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
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Watcher.getInstance().registerDataSetObserver(this);
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = getDialogWidth();
        lp.height = getDialogHeight();
        lp.gravity = getDialogGravity();
        lp.format = PixelFormat.TRANSLUCENT;
        lp.dimAmount = getDimAmount();
        lp.x = getOffsetX();
        lp.y = getOffsetY();
        w.setAttributes(lp);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Watcher.getInstance().unregisterObserver(this);
    }


    /**
     * 获取dialog的宽度，缺省为wrap_content
     *
     * @return
     */
    protected int getDialogWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    /**
     * 获取dialog的高度，缺省为wrap_content
     *
     * @return
     */
    protected int getDialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 获取dialog的背景透明度
     *
     * @return
     */
    protected float getDimAmount() {
        return DEFAULT_DIM_AMOUNT;
    }

    /**
     * 获取dialog的布局位置，缺省为居底
     *
     * @return
     */
    protected int getDialogGravity() {
        return Gravity.CENTER;
    }

    /**
     * 获取dialog的x方向相对于基准边的偏移量，如果dialog左对齐、右对齐时有效
     *
     * @return
     */
    protected int getOffsetX() {
        return 0;
    }

    /**
     * 获取dialog的y方向相对于基准边的偏移量，如果dialog顶对齐、底对齐时有效
     *
     * @return
     */
    protected int getOffsetY() {
        return 0;
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
