package com.base.api;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.base.abs.IApp;

import retrofit2.Retrofit;


/**
 * Created by haojiangfeng on 2024/11/1.
 */
public class AppApi {


    public static IApp iApp;

    public static Handler getHandler(){
        if(iApp != null){
            return iApp.getGlobalHandler();
        }
        return new Handler(Looper.getMainLooper());
    }

    public static Retrofit getApiManagerRetrofit() {
        if(iApp != null){
            return iApp.getApiManagerRetrofit();
        }
        return null;
    }

    public static void startDetailActivity(Activity activity, String bookId, String externalBookId) {
        if(iApp != null){
            iApp.startDetailActivity(activity, bookId, externalBookId);
        }
    }


}
