package com.benefit.dialog.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.benefit.novelverse.R;

/**
 * APP弹窗基类
 */
public class BaseDialog extends DialogFragment {
    // FragmentManager
    public FragmentManager fragmentManager;
    // 弹窗层级，层级越高越需要先显示
    public int level = 0;
    // dialog标识
    public String tag = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.dialog);
    }

    /**
     * 初始化弹窗层级以及标识
     *
     * @param fragmentManager fragment管理器
     * @param level 优先级
     * @param tag dialog标识
     */
    public void initDialogLevelAndTag(FragmentManager fragmentManager, int level, String tag) {
        this.fragmentManager = fragmentManager;
        this.level = level;
        this.tag = tag;
    }

    /**
     * 显示Dialog
     *
     * @param manager fragment管理器
     * @param tag dialog标识
     */
    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        if (getDialog() == null || !getDialog().isShowing()) {
            // 判断是否已经显示该弹窗
            try {
                super.show(manager, tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 移除显示Dialog
     */
    @Override
    public void dismiss() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}