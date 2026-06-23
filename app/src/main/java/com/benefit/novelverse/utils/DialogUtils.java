package com.benefit.novelverse.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.baidu.baselibrary.util.App;
import com.benefit.novelverse.R;
import com.tencent.common.ActivityStackManager;
import com.tencent.common.animation.FadeEnter.FadeEnter;
import com.tencent.common.animation.FadeExit.FadeExit;
import com.tencent.common.util.Abase;
import com.meta.ui.widget.ProgressWheelDialog;

/**
 * 弹窗工具类
 */
public class DialogUtils {
    @SuppressLint("StaticFieldLeak")
    private static ProgressWheelDialog mProgressDialog;

    private DialogUtils() {

    }

    /**
     * 展示默认style弹窗
     */
    public static void showProgressDialog(Context context, String title) {
        showProgressDialog(context, title, false);
    }


    /**
     * 展示默认style弹窗
     */
    public static void showProgressDialog(Context context, String title, boolean cancelable) {
        if (context == null) return;

        App.post(() -> {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            if (context instanceof Activity
                    && (((Activity) context).isDestroyed() || ((Activity) context).isFinishing())) return ;
            mProgressDialog = new ProgressWheelDialog(context);
            mProgressDialog.setDialogProgressText(title);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.showAnim(new FadeEnter())
                    .dismissAnim(new FadeExit())
                    .widthScale(0).heightScale(0).show();
        });
    }

    public static boolean isProgressDialogShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public static void showProgressDialog() {
        showProgressDialog(ActivityStackManager.getInstance().currentActivity(), Abase.getResources().getString(R.string.loading));
    }

    public static void showProgressDialog(boolean cancelAble) {
        showProgressDialog(ActivityStackManager.getInstance().currentActivity(), Abase.getResources().getString(R.string.loading), cancelAble);
    }

    public static void dismissProgressDialog() {
        App.post(() -> {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        });
    }
}