package com.baidu.baselibrary.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;


import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.jess.baselibrary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * APP弹窗基类
 */
public abstract class LoggerDialogFragment extends DialogFragment {



    protected abstract String className();



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ALog.lifeCycle(LogTag.Fragment, className(), "onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.lifeCycle(LogTag.Fragment, className(), "onCreate()");
        setStyle(STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ALog.lifeCycle(LogTag.Fragment, className(), "onViewCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        ALog.lifeCycle(LogTag.Fragment, className(), "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        ALog.lifeCycle(LogTag.Fragment, className(), "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        ALog.lifeCycle(LogTag.Fragment, className(), "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        ALog.lifeCycle(LogTag.Fragment, className(), "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ALog.lifeCycle(LogTag.Fragment, className(), "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.lifeCycle(LogTag.Fragment, className(), "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ALog.lifeCycle(LogTag.Fragment, className(), "onDetach()");
    }




    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Throwable e) {
            ALog.exception(className(), "show", e);
        }
        ALog.textSingle(className(), "", "show()");
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Throwable e) {
            ALog.exception(className(), "dismiss", e);
        }
        ALog.textSingle(className(), "", "dismiss()");
    }

    @Override
    public void dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Throwable e) {
            ALog.exception(className(), "dismissAllowingStateLoss", e);
        }
        ALog.textSingle(className(), "", "dismissAllowingStateLoss()");
    }
}