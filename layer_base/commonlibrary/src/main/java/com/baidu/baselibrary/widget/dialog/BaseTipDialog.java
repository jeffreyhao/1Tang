package com.baidu.baselibrary.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jess.baselibrary.R;
import com.base.util.ui.UIUtil;

import androidx.annotation.NonNull;

/**
 * Created by haojiangfeng on 2023/8/8.
 */
public class BaseTipDialog extends BaseDialog{

    private final static String DIALOG_TITLE = "DIALOG_TITLE";
    private final static String DIALOG_CONTENT = "DIALOG_CONTENT";
    private final static String DIALOG_CANCEL_VALUE = "DIALOG_CANCEL_VALUE";
    private final static String DIALOG_CONFIRM_VALUE = "DIALOG_CONFIRM_VALUE";
    private final static String DIALOG_NIGHT_MODE = "DIALOG_NIGHT_MODE";



    // dialog监听器
    private OnDialogClickListener listener;

    private String mTitle;
    private String mContent;
    private String mConfirmValue;
    private String mCancelValue;
    private boolean mIsNightMode;
    private boolean mIsContentCenter;

    public interface OnDialogClickListener {
        void onConfirm();
        void onCancel();
    }



    /**
     * 获取Dialog对象
     *
     * @param title 提示语
     * @param content 内容
     * @param cancelValue 取消按钮显示文本
     * @param confirmValue 确定按钮显示文本
     */
    public static BaseTipDialog newInstance(Context context, String title, String content, String confirmValue, String cancelValue, boolean isNightMode) {
        BaseTipDialog tipDialog = new BaseTipDialog(context, title, content, confirmValue, cancelValue, isNightMode, false);
        return tipDialog;
    }

    public static BaseTipDialog newInstance(Context context, String title, String content, String confirmValue, String cancelValue, boolean isNightMode, boolean isContentCenter) {
        BaseTipDialog tipDialog = new BaseTipDialog(context, title, content, confirmValue, cancelValue, isNightMode, isContentCenter);
        return tipDialog;
    }

    public BaseTipDialog(Context context, String title, String content, String confirmValue, String cancelValue, boolean isNightMode, boolean isContentCenter) {
        super(context);
        this.mTitle = title;
        this.mContent = content;
        this.mConfirmValue = confirmValue;
        this.mCancelValue = cancelValue;
        this.mIsNightMode = isNightMode;
        this.mIsContentCenter = isContentCenter;
        build(context);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void build(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.common_dialog_app_tip, null);
        setContentView(rootView);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        if (mIsNightMode) {
            rootView.findViewById(R.id.module_dialog_app_tip_mask).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.module_dialog_app_tip_mask).setVisibility(View.GONE);
        }


        setCancelable(true);
        setCanceledOnTouchOutside(true);

        ((TextView) rootView.findViewById(R.id.module_dialog_app_tip_title)).setText(mTitle);
        TextView contentTextView = rootView.findViewById(R.id.module_dialog_app_tip_content);
        contentTextView.setText(mContent);
        if(mIsContentCenter){
            contentTextView.setGravity(Gravity.CENTER);
        } else {
            contentTextView.setGravity(Gravity.LEFT);
        }

        TextView cancelButton = rootView.findViewById(R.id.module_dialog_app_tip_cancel);
        if (!TextUtils.isEmpty(mCancelValue)) {
            cancelButton.setText(mCancelValue);
        }
        TextView confirmButton = rootView.findViewById(R.id.module_dialog_app_tip_sure);
        if (!TextUtils.isEmpty(mConfirmValue)) {
            confirmButton.setText(mConfirmValue);
        }

        cancelButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        });
        confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm();
            }
            dismiss();
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
