package com.base.abs;

import android.app.Activity;
import android.os.Handler;

import retrofit2.Retrofit;

/**
 * Created by haojiangfeng on 2024/11/1.
 */
public interface IApp {

    Handler getGlobalHandler();

    Retrofit getApiManagerRetrofit();

    void startDetailActivity(Activity activity, String bookId, String externalBookId);
}
