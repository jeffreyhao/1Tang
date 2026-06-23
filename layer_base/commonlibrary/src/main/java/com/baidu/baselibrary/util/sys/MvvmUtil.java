package com.baidu.baselibrary.util.sys;

import android.view.View;

/**
 * Created by haojiangfeng on 2023/9/22.
 */
public class MvvmUtil {


    public static void setVisible(View view, boolean visible){
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
