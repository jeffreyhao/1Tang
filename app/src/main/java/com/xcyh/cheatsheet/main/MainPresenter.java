package com.xcyh.cheatsheet.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.baselibrary.request.EmptyPresenter;
import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.date.DateUtil;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.util.ui.ToastUtils;
import com.base.watcher.Watcher;
import com.base.watcher.WatcherEvent;
import com.blankj.utilcode.util.AppUtils;
import com.xcyh.cheatsheet.model.constant.Const;

import org.jetbrains.annotations.NotNull;


/**
 * Created by haojiangfeng on 2024/11/14.
 */
public class MainPresenter extends EmptyPresenter {

    private Activity mActivity;

    private long mTimeStampOnStop;

    private int mExitAppCount = 0;

    private final Runnable mExitRunnable = new Runnable() {
        @Override
        public void run() {
            mExitAppCount = 0;
        }
    };

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()!= null && intent.getAction().equals("android.intent.action.DATE_CHANGED")) {
                // 在这里处理零点时间到达的逻辑
//                Watcher.getInstance().notifyWatcher(AdvWatchEvent.EXTEND_MID_NIGHT, null);
                mActivity.recreate();
                App.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastCenter( "已到了" + DateUtil.getDateYMD() + "\n重新加载数据");
                    }
                }, 500);
            }
        }
    };




    public void setContext(@NotNull HomeActivity activity) {
        mActivity = activity;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        try {
            mActivity.registerReceiver(mBroadcastReceiver, filter, Const.PERMISSION_BROADCAST,null);
        } catch (Throwable e){
            LogUtil.e(e);
        }
    }


    public void onResume(){
        if(mTimeStampOnStop != 0){
            long current = System.currentTimeMillis();
            if(current - mTimeStampOnStop > DateUtil.MILLISECOND_PER_MINUTE * 5){
                Watcher.getInstance().notifyWatcher(WatcherEvent.EXTEND_5_MIN, null);
            }
        }
    }

    public void onStop(){
        mTimeStampOnStop = System.currentTimeMillis();
    }

    /**
     * 两秒内连续按两次，才算退出。防止误碰退出
     */
    public boolean onExitApp(){
        mExitAppCount++;
        if (mExitAppCount == 1) {
            ToastUtils.showToastCenter("再按一次退出");
            App.getHandler().postDelayed(mExitRunnable, 2000);
            return false;
        } else {
            App.getHandler().removeCallbacks(mExitRunnable);
            AppUtils.exitApp();
            return true;
        }
    }

}
