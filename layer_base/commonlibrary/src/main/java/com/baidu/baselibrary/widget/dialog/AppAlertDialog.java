package com.baidu.baselibrary.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.jess.baselibrary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppAlertDialog extends LoggerDialogFragment {


    private final static String DIALOG_TITLE = "DIALOG_TITLE";
    private final static String DIALOG_CONTENT = "DIALOG_CONTENT";
    private final static String DIALOG_CONFIRM_VALUE = "DIALOG_CONFIRM_VALUE";
    private final static String DIALOG_CANCELABLE = "DIALOG_CANCELABLE";
    private final static String DIALOG_NIGHT_MODE = "DIALOG_NIGHT_MODE";

    /**
     * 获取Dialog对象
     *
     * @param title 提示语
     * @param content 内容
     * @param confirmValue 确定按钮显示文本
     */
    public static AppAlertDialog newInstance(String title, String content, String confirmValue, boolean isNightMode) {
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_CONTENT, content);
        args.putString(DIALOG_CONFIRM_VALUE, confirmValue);
        args.putBoolean(DIALOG_NIGHT_MODE, isNightMode);
        AppAlertDialog tipDialog = new AppAlertDialog();
        tipDialog.setArguments(args);
        return tipDialog;
    }

    /**
     * 获取Dialog对象
     *
     * @param title 提示语
     * @param content 内容
     * @param confirmValue 确定按钮显示文本
     */
    public static AppAlertDialog newInstance(String title, String content, String confirmValue, boolean cancelable, boolean isNightMode) {
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_CONTENT, content);
        args.putString(DIALOG_CONFIRM_VALUE, confirmValue);
        args.putBoolean(DIALOG_CANCELABLE, cancelable);
        args.putBoolean(DIALOG_NIGHT_MODE, isNightMode);
        AppAlertDialog tipDialog = new AppAlertDialog();
        tipDialog.setArguments(args);
        return tipDialog;
    }

    public interface OnDialogClickListener {

        void onConfirm(LoggerDialogFragment dialog);
    }


    // dialog监听器
    private OnDialogClickListener listener;


    private boolean cancelable = true;


    @Override
    public String className() {
        return "AppAlertDialog";
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_dialog_app_alert, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "";
        String content = "";
        String confirmValue = "";
        Bundle bundle = getArguments();
        boolean isNightMode = false;
        if (null != bundle) {
            title = bundle.getString(DIALOG_TITLE);
            content = bundle.getString(DIALOG_CONTENT);
            confirmValue  = bundle.getString(DIALOG_CONFIRM_VALUE);
            cancelable  = bundle.getBoolean(DIALOG_CANCELABLE);
            isNightMode = bundle.getBoolean(DIALOG_NIGHT_MODE);
        }

        if(getDialog()!=null) {
            getDialog().setCancelable(cancelable);
            getDialog().setCanceledOnTouchOutside(cancelable);
        }

        if (isNightMode) {
            view.findViewById(R.id.module_dialog_app_tip_mask).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.module_dialog_app_tip_mask).setVisibility(View.GONE);
        }

        ((TextView) view.findViewById(R.id.module_dialog_app_tip_title)).setText(title);
        ((TextView) view.findViewById(R.id.module_dialog_app_tip_content)).setText(content);
        TextView confirmButton = view.findViewById(R.id.module_dialog_app_tip_sure);
        if (!TextUtils.isEmpty(confirmValue)) {
            confirmButton.setText(confirmValue);
        }

        confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm(this);
            }
        });
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
