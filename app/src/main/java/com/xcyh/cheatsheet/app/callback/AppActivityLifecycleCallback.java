package com.xcyh.cheatsheet.app.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.App;
import com.base.watcher.Watcher;
import com.base.watcher.WatcherEvent;
import com.benefit.utils.LanguageUtil;
import com.xcyh.cheatsheet.main.HomeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by haojiangfeng on 2023/11/29.
 */
public class AppActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    /**
     * 前台activity数量
     */

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        App.sLiveActivityCount ++;
        LanguageUtil.changeAppLanguage(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        App.sForegroundActivityCount ++;
        ALog.d("onActivityStarted(), " + activity.getClass().getSimpleName() + ", foreground activity count : " + App.sForegroundActivityCount);
        if(App.sForegroundActivityCount == 1){
            // app从后台切到前台
            Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_APP_SWITCH_FOREGROUND, null);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        App.sForegroundActivityCount--;
        ALog.d("onActivityStopped(), " + activity.getClass().getSimpleName() + ", foreground activity count : " + App.sForegroundActivityCount);
        if(App.sForegroundActivityCount <= 0){

            if(isMainActivityFinishing(activity)) {
                // 退出app
                Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_APP_EXIT_APP, null);

            } else {
                // app从前台切到后台
                Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_APP_SWITCH_BACKSTAGE, null);
            }

        }
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        App.sLiveActivityCount --;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    private boolean isMainActivityFinishing(Activity activity){
        if(App.sLiveActivityCount == 1 && activity instanceof HomeActivity && activity.isFinishing()){
            return true;
        }
        return false;
    }
}
