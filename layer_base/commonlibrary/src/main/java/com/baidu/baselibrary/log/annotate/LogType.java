package com.baidu.baselibrary.log.annotate;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2024/6/13.
 */
@IntDef({LogType.TEXT, LogType.FILE, LogType.JSON, LogType.XML, LogType.EXCEPTION, LogType.STACKTRACE})
@Retention(RetentionPolicy.SOURCE)
public @interface LogType {

    int TEXT            = 0x10;
    int JSON            = 0x20;
    int XML             = 0x30;
    int EXCEPTION       = 0x40;
    @Deprecated
    int FILE            = 0x50; // 普通文件
    int STACKTRACE      = 0x60;

}
