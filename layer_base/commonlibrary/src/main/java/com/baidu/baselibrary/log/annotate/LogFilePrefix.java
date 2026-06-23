package com.baidu.baselibrary.log.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

/**
 *  alog 的 模块
 */

@StringDef({LogFilePrefix.CSH, LogFilePrefix.DEEPLINK, LogFilePrefix.DEVELOP, LogFilePrefix.COMMON, LogFilePrefix.STANDARD})
@Retention(RetentionPolicy.SOURCE)
public @interface LogFilePrefix {


    /**
     * crash日志
     */
    String CSH          = "crash";

    /**
     * 深链模块
     */
    String DEEPLINK     = "deeplink";

    /**
     * 开发用
     */
    String DEVELOP      = "develop";

    /**
     * 通用用户日志
     */
    String COMMON       = "common";

    /**
     * 标准化日志
     */
    String STANDARD     = "standard";

}
