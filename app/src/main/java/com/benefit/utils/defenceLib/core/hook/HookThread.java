package com.benefit.utils.defenceLib.core.hook;

import android.os.Looper;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.benefit.utils.defenceLib.core.DefenseCore;
import com.benefit.utils.defenceLib.handler.ExceptionDispatcher;

public class HookThread implements IHook {

    private boolean isHooked;

    private ExceptionDispatcher mExceptionDispatcher;

    private Thread.UncaughtExceptionHandler mOriginHandler;

    public HookThread(ExceptionDispatcher exceptionDispatcher) {
        mExceptionDispatcher = exceptionDispatcher;
    }

    @Override
    public void hook() {
        if (isHooked()) {
            return;
        }
        mOriginHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                ALog.crash("HookThread", "onCaughtException", e);

                if (mExceptionDispatcher != null) {
                    mExceptionDispatcher.uncaughtExceptionHappened(t, e, DefenseCore.isInSafeMode());
                }
                if (t == Looper.getMainLooper().getThread()) {
                    ALog.keyValue("HookThread", "uncaughtException", new String[]{LogTag.SWITCH, LogTag.Switch_Looper}, "Looper", "MainLooper.looper()");

                    DefenseCore.maybeChoreographerException(e, mExceptionDispatcher);
                    DefenseCore.enterSafeModeKeepLoop(mExceptionDispatcher);
                } else {
                    ALog.keyValue("HookThread", "uncaughtException", new String[]{LogTag.SWITCH, LogTag.Switch_Looper}, "Looper", "Thread is Not main thread.");
                }
            }
        });
        isHooked = true;
    }

    @Override
    public void unHook() {
        if (!isHooked()) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(mOriginHandler);
        isHooked = false;
    }

    @Override
    public boolean isHooked() {
        return isHooked;
    }
}
