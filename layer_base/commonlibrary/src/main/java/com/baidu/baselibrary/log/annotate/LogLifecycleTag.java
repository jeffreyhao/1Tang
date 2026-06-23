package com.baidu.baselibrary.log.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

/**
 * Created by haojiangfeng on 2024/6/13.
 */
@StringDef({
        LogTag.Application,
        LogTag.AppProgress,
        LogTag.Activity,
        LogTag.Fragment
})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLifecycleTag {

}
