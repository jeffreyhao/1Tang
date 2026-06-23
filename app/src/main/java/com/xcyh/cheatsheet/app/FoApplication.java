package com.xcyh.cheatsheet.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.multidex.MultiDex;

import com.baidu.baselibrary.base.BaseApplication;
import com.baidu.baselibrary.util.App;
import com.base.api.GlobalContext;
import com.base.net.ApiBase;
import com.benefit.utils.DomainUtil;
import com.bumptech.glide.Glide;
import com.tencent.common.util.Abase;

public class FoApplication extends BaseApplication {

    private static FoApplication instance;

    public static FoApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FoApplication has not been initialized yet.");
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        AppInit.initBuildConfig(base);

        instance = this;

        Handler sHandler = new Handler(Looper.getMainLooper());
        App.setApplication(instance, sHandler);
        Abase.initialize(this);
        GlobalContext.setApplicationContext(this);

        // super 调用会初始化各个 module 的配置：ConfigModule
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // app 初始化
        AppInit.initOnApplicationCreate(this);

        // 延迟 5s 初始化
        AppInit.initOnApplicationCreatePost(this);

        ApiBase.backupDomain = DomainUtil.getBackupDomain();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppInit.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }
}
