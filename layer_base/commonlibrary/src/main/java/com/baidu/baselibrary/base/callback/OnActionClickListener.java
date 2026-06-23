package com.baidu.baselibrary.base.callback;

import android.view.View;

import androidx.annotation.Nullable;

public interface OnActionClickListener {

    int ACTION_DEFAULT          = -1;

    int ACTION_SUBMIT           = 1;
    int ACTION_CANCEL           = 2;



    void onActionClick(View v, int action, @Nullable Object object);
}
