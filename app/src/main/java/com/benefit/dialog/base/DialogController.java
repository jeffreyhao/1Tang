package com.benefit.dialog.base;

import android.content.Context;

import com.baidu.baselibrary.widget.dialog.AppAlertDialog;
import com.baidu.baselibrary.widget.dialog.AppTipDialog;
import com.baidu.baselibrary.widget.dialog.BaseAlertDialog;
import com.baidu.baselibrary.widget.dialog.BaseTipDialog;
import com.benefit.dialog.ChangeSexSheetDialogFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * App 弹窗管理器
 */
public class DialogController {
    private static final DialogController dialogController = new DialogController();

    private DialogController() {

    }

    /**
     * 弹窗管理器单例
     *
     * @return 实例对象
     */
    public static DialogController getInstance() {
        return dialogController;
    }

    /**
     * 显示提示dialog
     *
     * @param fragmentManager Fragment管理器
     * @param title             标题
     * @param content            内容
     * @param confirmValue    提示确定按钮显示文本
     * @param listener        回调监听
     */
    public void showAppAlertDialog(FragmentManager fragmentManager, String title, String content,
                                 String confirmValue, AppAlertDialog.OnDialogClickListener listener) {
        String tag = "dialog_app_alert";
        AppAlertDialog appTipDialog = AppAlertDialog.newInstance(title, content, confirmValue, false);
        appTipDialog.setOnDialogClickListener(listener);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment instanceof BaseDialog) {
            return;
        }
        appTipDialog.show(fragmentManager, tag);
    }

    /**
     * 显示提示dialog
     *
     * @param fragmentManager Fragment管理器
     * @param title             标题
     * @param content            内容
     * @param confirmValue    提示确定按钮显示文本
     * @param listener        回调监听
     */
    public void showAppAlertDialog(FragmentManager fragmentManager, String title, String content,
                                   String confirmValue, boolean cancelable, AppAlertDialog.OnDialogClickListener listener) {
        String tag = "dialog_app_alert";
        AppAlertDialog appTipDialog = AppAlertDialog.newInstance(title, content, confirmValue, cancelable, false);
        appTipDialog.setOnDialogClickListener(listener);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment instanceof BaseDialog) {
            return;
        }
        appTipDialog.show(fragmentManager, tag);
    }


    /**
     * 显示提示dialog
     *
     * @param fragmentManager Fragment管理器
     * @param title             标题
     * @param content            内容
     * @param cancelValue     提示取消按钮显示文本
     * @param confirmValue    提示确定按钮显示文本
     * @param listener        回调监听
     */
    public void showAppTipDialog(FragmentManager fragmentManager, String title, String content, String cancelValue,
                                 String confirmValue, AppTipDialog.OnDialogClickListener listener) {
        String tag = "dialog_exit_read";
        AppTipDialog appTipDialog = AppTipDialog.newInstance(title, content, cancelValue, confirmValue, false);
        appTipDialog.setOnDialogClickListener(listener);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment instanceof BaseDialog) {
            return;
        }
        appTipDialog.show(fragmentManager, tag);
    }

    /**
     * 显示提示dialog
     *
     * @param fragmentManager Fragment管理器
     * @param sex             性别
     * @param listener        回调监听
     */
    public void showChangeSexDialog(FragmentManager fragmentManager, int sex, ChangeSexSheetDialogFragment.OnDialogClickListener listener) {
        String tag = "dialog_change_sex";
        ChangeSexSheetDialogFragment changeSexSheetDialogFragment = ChangeSexSheetDialogFragment.newInstance(sex);
        changeSexSheetDialogFragment.setOnDialogClickListener(listener);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment instanceof BaseDialog) {
            return;
        }
        changeSexSheetDialogFragment.show(fragmentManager, tag);
    }






    /**
     * 显示提示dialog
     *
     * @param context           Context
     * @param title             标题
     * @param content           内容
     * @param cancelValue       提示取消按钮显示文本
     * @param confirmValue      提示确定按钮显示文本
     * @param listener          回调监听
     */
    public void showBaseTipDialog(Context context, String title, String content, String cancelValue,
                                 String confirmValue, BaseTipDialog.OnDialogClickListener listener) {
        BaseTipDialog appTipDialog = BaseTipDialog.newInstance(context, title, content, cancelValue, confirmValue, false);
        appTipDialog.setOnDialogClickListener(listener);
        appTipDialog.show();
    }

    /**
     * @param isContentCenter 是否内容居中
     */
    public void showBaseTipDialog(Context context, String title, String content, String cancelValue,
                                  String confirmValue, boolean isContentCenter, BaseTipDialog.OnDialogClickListener listener) {
        BaseTipDialog appTipDialog = BaseTipDialog.newInstance(context, title, content, cancelValue, confirmValue, false, isContentCenter);
        appTipDialog.setOnDialogClickListener(listener);
        appTipDialog.show();
    }


    /**
     * 显示提示dialog
     *
     * @param context           Context
     * @param title             标题
     * @param content           内容
     * @param confirmValue      提示确定按钮显示文本
     * @param listener          回调监听
     */
    public void showBaseAlertDialog(Context context, String title, String content,
                                   String confirmValue, BaseAlertDialog.OnDialogClickListener listener) {
        String tag = "dialog_app_alert";
        BaseAlertDialog appTipDialog = BaseAlertDialog.newInstance(context, title, content, confirmValue, false);
        appTipDialog.setOnDialogClickListener(listener);
        appTipDialog.show();
    }


}