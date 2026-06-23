package com.xcyh.cheatsheet.app.callback;

import com.benefit.utils.defenceLib.handler.IExceptionHandler;

/**
 * Created by haojiangfeng on 2023/11/29.
 */
public class ExceptionHandler implements IExceptionHandler {

    @Override
    public void onCaughtException(Thread thread, Throwable throwable, boolean isSafeMode) {

    }

    @Override
    public void onEnterSafeMode() {

    }

    @Override
    public void onMayBeBlackScreen(Throwable throwable) {

    }


}
