package com.xcyh.cheatsheet.app.impl;

import android.app.Activity;
import android.os.Handler;

import com.baidu.baselibrary.util.App;
import com.base.abs.IApp;

import retrofit2.Retrofit;

/**
 * Created by haojiangfeng on 2024/10/30.
 */
public class IAppImpl implements IApp {

    public static IAppImpl get(){
        return new IAppImpl();
    }

    @Override
    public Handler getGlobalHandler() {
        return App.getHandler();
    }

    @Override
    public Retrofit getApiManagerRetrofit() {
        return null;
    }

    @Override
    public void startDetailActivity(Activity activity, String bookId, String externalBookId) {

    }
}
