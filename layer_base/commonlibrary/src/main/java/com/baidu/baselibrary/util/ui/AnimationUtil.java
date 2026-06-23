package com.baidu.baselibrary.util.ui;

import android.app.Activity;

/**
 * 动画特效工具类
 * Created by haojiangfeng on 2023/7/14.
 */
public class AnimationUtil {


    public static void overridePendingTransition(Activity activity, int in, int out) {
        if (activity == null) return;
        activity.overridePendingTransition(in, out);
    }


}
