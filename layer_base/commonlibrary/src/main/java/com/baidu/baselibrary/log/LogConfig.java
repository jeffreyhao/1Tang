package com.baidu.baselibrary.log;

import android.content.Context;
import android.os.Environment;

import com.baidu.baselibrary.log.annotate.LogFilePrefix;
import com.baidu.baselibrary.log.annotate.LogLevel;
import com.baidu.baselibrary.log.process.ALogProxy;
import com.baidu.baselibrary.log.process.LogConst;

import java.io.File;

import androidx.annotation.IntRange;

/**
 *  Alog 配置项
 */
public class LogConfig {

    public String mDefaultDir;// The default storage directory of log.
    public String mDir;       // The storage directory of log.
    public String mDirName            = "log";
    public String mFilePrefix          = LogFilePrefix.STANDARD;// The file prefix of log.
    public boolean mLogSwitch         = true;  // The switch of log.
    public boolean mLog2ConsoleSwitch = true;  // The logcat's switch of log.
    public String mGlobalTag          = null;  // The global tag of log.
    public boolean mTagIsSpace        = true;  // The global tag is space.
    public boolean mLogHeadSwitch     = true;  // The head's switch of log.
    public boolean mLog2FileSwitch    = false; // The file's switch of log.
    public boolean mLogBorderSwitch   = true;  // The border's switch of log.
    public boolean mSingleTagSwitch   = true;  // The single tag of log.
    public int     mConsoleFilter     = ALog.V;     // The console's filter of log.
    public int     mFileFilter        = ALog.V;     // The file's filter of log.
    public int     mStackDeep         = 1;     // The stack's deep of log.
    public int     mStackOffset       = 0;     // The stack's offset of log.
    public int     mSaveDays          = -1;    // The save days of log.

    public Context sAppContext;


    public LogConfig(Context context) {
        sAppContext = context.getApplicationContext();
        if (mDefaultDir != null) return;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && sAppContext.getExternalCacheDir() != null)
            mDefaultDir = sAppContext.getExternalCacheDir() + LogConst.Sep.FILE_SEP + mDirName + LogConst.Sep.FILE_SEP;
        else {
            mDefaultDir = sAppContext.getCacheDir() + LogConst.Sep.FILE_SEP + mDirName + LogConst.Sep.FILE_SEP;
        }
    }

    public LogConfig setLogSwitch(final boolean logSwitch) {
        mLogSwitch = logSwitch;
        return this;
    }

    public LogConfig setConsoleSwitch(final boolean consoleSwitch) {
        mLog2ConsoleSwitch = consoleSwitch;
        return this;
    }

    public LogConfig setGlobalTag(final String tag) {
        if (ALogProxy.isSpace(tag)) {
            mGlobalTag = "";
            mTagIsSpace = true;
        } else {
            mGlobalTag = tag;
            mTagIsSpace = false;
        }
        return this;
    }

    public LogConfig setLogHeadSwitch(final boolean logHeadSwitch) {
        mLogHeadSwitch = logHeadSwitch;
        return this;
    }

    public LogConfig setLog2FileSwitch(final boolean log2FileSwitch) {
        mLog2FileSwitch = log2FileSwitch;
        return this;
    }

    public LogConfig setDir(final String dir) {
        if (ALogProxy.isSpace(dir)) {
            mDir = null;
        } else {
            mDir = dir.endsWith(LogConst.Sep.FILE_SEP) ? dir : dir + LogConst.Sep.FILE_SEP;
        }
        return this;
    }

    public LogConfig setDir(final File dir) {
        mDir = dir == null ? null : dir.getAbsolutePath() + LogConst.Sep.FILE_SEP;
        return this;
    }

    public LogConfig setFilePrefix(final String filePrefix) {
        if (ALogProxy.isSpace(filePrefix)) {
            mFilePrefix = LogFilePrefix.STANDARD;
        } else {
            mFilePrefix = filePrefix;
        }
        return this;
    }

    public LogConfig setBorderSwitch(final boolean borderSwitch) {
        mLogBorderSwitch = borderSwitch;
        return this;
    }

    public LogConfig setSingleTagSwitch(final boolean singleTagSwitch) {
        mSingleTagSwitch = singleTagSwitch;
        return this;
    }

    public LogConfig setConsoleFilter(@LogLevel final int consoleFilter) {
        mConsoleFilter = consoleFilter;
        return this;
    }

    public LogConfig setFileFilter(@LogLevel final int fileFilter) {
        mFileFilter = fileFilter;
        return this;
    }

    public LogConfig setStackDeep(@IntRange(from = 1) final int stackDeep) {
        mStackDeep = stackDeep;
        return this;
    }

    public LogConfig setStackOffset(@IntRange(from = 0) final int stackOffset) {
        mStackOffset = stackOffset;
        return this;
    }

    public LogConfig setSaveDays(@IntRange(from = 1) final int saveDays) {
        mSaveDays = saveDays;
        return this;
    }

    public final <T> LogConfig addFormatter(final IFormatter<T> iFormatter) {
        ALogProxy.addFormatter(iFormatter);
        return this;
    }


    @Override
    public String toString() {
        return "switch: " + mLogSwitch
                + LogConst.Sep.LINE_SEP + "console: " + mLog2ConsoleSwitch
                + LogConst.Sep.LINE_SEP + "tag: " + (mTagIsSpace ? "null" : mGlobalTag)
                + LogConst.Sep.LINE_SEP + "head: " + mLogHeadSwitch
                + LogConst.Sep.LINE_SEP + "file: " + mLog2FileSwitch
                + LogConst.Sep.LINE_SEP + "dir: " + (mDir == null ? mDefaultDir : mDir)
                + LogConst.Sep.LINE_SEP + "filePrefix: " + mFilePrefix
                + LogConst.Sep.LINE_SEP + "border: " + mLogBorderSwitch
                + LogConst.Sep.LINE_SEP + "singleTag: " + mSingleTagSwitch
                + LogConst.Sep.LINE_SEP + "consoleFilter: " + LogConst.T[mConsoleFilter - ALog.V]
                + LogConst.Sep.LINE_SEP + "fileFilter: " + LogConst.T[mFileFilter - ALog.V]
                + LogConst.Sep.LINE_SEP + "stackDeep: " + mStackDeep
                + LogConst.Sep.LINE_SEP + "stackOffset: " + mStackOffset
                + LogConst.Sep.LINE_SEP + "saveDays: " + mSaveDays;
    }

}
