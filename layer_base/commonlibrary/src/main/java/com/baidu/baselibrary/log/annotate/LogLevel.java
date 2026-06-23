package com.baidu.baselibrary.log.annotate;

import com.baidu.baselibrary.log.ALog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 用来指定类型
 */
@IntDef({ALog.V, ALog.D, ALog.I, ALog.W, ALog.E, ALog.A})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLevel {

}
