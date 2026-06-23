package com.baidu.baselibrary.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.baidu.baselibrary.base.module.AppDelegate;
import com.baidu.baselibrary.util.App;
import com.base.module.AppLifecycles;
import com.base.util.thread.ExecutorsUtils;


/**
 * @author lhc
 * @date 2022/5/9 8:58
 * @desc Application 基类，组件化开发时Application继承
 */
public class BaseApplication extends Application {

    private AppLifecycles mAppDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegate(this);
        }
        this.mAppDelegate.attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onCreate(this);
        }

        // 后台线程的初始化
        ExecutorsUtils.getInstance().getAppExecutors().diskIO().execute(() -> {
                    if (mAppDelegate != null) {
                        this.mAppDelegate.onCreateInBackground(BaseApplication.this);
                    }
                });

        // 延迟初始化
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAppDelegate != null) {
                    BaseApplication.this.mAppDelegate.onPostCreated(BaseApplication.this);
                }
            }
        }, 5000);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mAppDelegate != null) {
            this.mAppDelegate.onConfigurationChanged(newConfig);
        }
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this);
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mAppDelegate != null) {
            this.mAppDelegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (mAppDelegate != null) {
            this.mAppDelegate.onTrimMemory(level);
        }
    }

}
