package com.baidu.baselibrary.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;

import androidx.annotation.NonNull;

/**
 * Created by haojiangfeng on 2023/8/8.
 */
public class BaseAlertDialog extends BaseDialog{


    // dialog监听器
    private OnDialogClickListener listener;

    private String mTitle;
    private String mContent;
    private String mConfirmValue;
    private boolean mCancelable;
    private boolean mIsNightMode;

    public interface OnDialogClickListener {
        void onConfirm(BaseDialog dialog);
    }




    /**
     * 获取Dialog对象
     *
     * @param title 提示语
     * @param content 内容
     * @param confirmValue 确定按钮显示文本
     */
    public static BaseAlertDialog newInstance(Context context, String title, String content, String confirmValue, boolean isNightMode) {
        BaseAlertDialog tipDialog = new BaseAlertDialog(context, title, content, confirmValue, true, isNightMode);
        return tipDialog;
    }

    /**
     * 获取Dialog对象
     *
     * @param title 提示语
     * @param content 内容
     * @param confirmValue 确定按钮显示文本
     */
    public static BaseAlertDialog newInstance(Context context, String title, String content, String confirmValue, boolean cancelable, boolean isNightMode) {
        BaseAlertDialog tipDialog = new BaseAlertDialog(context, title, content, confirmValue, cancelable, isNightMode);
        return tipDialog;
    }





    public BaseAlertDialog(Context context, String title, String content, String confirmValue, boolean cancelable, boolean isNightMode) {
        super(context);
        this.mTitle = title;
        this.mContent = content;
        this.mConfirmValue = confirmValue;
        this.mCancelable = cancelable;
        this.mIsNightMode = isNightMode;
        build(context);
    }

    public BaseAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        build(context);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void build(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.common_dialog_app_alert, null);
        setContentView(rootView);
        if (mIsNightMode) {
            rootView.findViewById(R.id.module_dialog_app_tip_mask).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.module_dialog_app_tip_mask).setVisibility(View.GONE);
        }

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setCancelable(mCancelable);
        setCanceledOnTouchOutside(mCancelable);


        ((TextView) rootView.findViewById(R.id.module_dialog_app_tip_title)).setText(mTitle);
        ((TextView) rootView.findViewById(R.id.module_dialog_app_tip_content)).setText(mContent);

        TextView confirmButton = rootView.findViewById(R.id.module_dialog_app_tip_sure);
        if (!TextUtils.isEmpty(mConfirmValue)) {
            confirmButton.setText(mConfirmValue);
        }
        confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm(this);
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
    }

    @Override
    protected int getDialogWidth() {
        return UIUtil.dip2px(290);
    }

    /**
     * 添加Dialog监听器
     *
     * @param listener 监听器
     */
    public void setOnDialogClickListener(@NonNull OnDialogClickListener listener) {
        this.listener = listener;
    }



}
