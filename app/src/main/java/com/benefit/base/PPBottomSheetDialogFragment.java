package com.benefit.base;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * BottomSheetDialogFragment基类
 *
 * https://blog.csdn.net/eyishion/article/details/112082804
 */
public class PPBottomSheetDialogFragment extends BottomSheetDialogFragment {
    /**
     * 设置NavigationBar背景色
     *
     * @param dialog BottomSheetDialog
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setWhiteNavigationBar(@NonNull Dialog dialog, int color) {
        Window window = dialog.getWindow();
        try {
            if (window != null) {
                DisplayMetrics metrics = new DisplayMetrics();
                window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                GradientDrawable dimDrawable = new GradientDrawable();

                GradientDrawable navigationBarDrawable = new GradientDrawable();
                navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
                navigationBarDrawable.setColor(color);

                Drawable[] layers = {dimDrawable, navigationBarDrawable};

                LayerDrawable windowBackground = new LayerDrawable(layers);
                windowBackground.setLayerInsetTop(1, metrics.heightPixels - 20);

                window.setBackgroundDrawable(windowBackground);
            }
        } catch (Exception | NoSuchMethodError e) {
            e.printStackTrace();
        }
    }
    public void dismissDialog() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            super.dismissAllowingStateLoss();
        }
    }
}