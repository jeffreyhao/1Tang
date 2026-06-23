package com.base.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2025/3/12.
 */

@IntDef({
        LaunchType.LAUNCH_TYPE_NORMAL,
        LaunchType.LAUNCH_TYPE_DEEPLINK,
        LaunchType.LAUNCH_TYPE_RESUME
})
@Retention(RetentionPolicy.SOURCE)
public @interface LaunchType {

    int LAUNCH_TYPE_NORMAL      = 1;
    int LAUNCH_TYPE_DEEPLINK    = 2;
    int LAUNCH_TYPE_RESUME      = 3;

}
