package com.baidu.baselibrary.base;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.base.api.Logger;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 仅仅是个Activity。啥也不是。
 *
 * Created by niepan on 16/01/26.
 */
public class PureCompatActivity extends AppCompatActivity {


    public String className(){
        return getClass().getSimpleName();
    }

    @CallSuper
    @Override
    protected void onNewIntent(Intent intent) {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onNewIntent()");
            super.onNewIntent(intent);
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onCreate()");
            super.onCreate(savedInstanceState);
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onPostCreate()");
            super.onPostCreate(savedInstanceState);
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onStart() {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onStart()");
            super.onStart();
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onRestart() {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onRestart()");
            super.onRestart();
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onResume() {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onResume()");
            super.onResume();
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onPause() {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onPause()");
            super.onPause();
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onStop() {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onStop()");
            super.onStop();
        } catch (Throwable e){
            ALog.exception(e);
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        try {
            ALog.lifeCycle(LogTag.Activity, className(), "onDestroy()");
            super.onDestroy();
        } catch (Throwable e){
            ALog.exception(e);
        }
    }



    /** @noinspection deprecation*/
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        ALog.textSingle(className(), "startActivityForResult", getBaseContext() + ", startActivity() " + intent);
        try {
            super.startActivityForResult(intent, requestCode, options);
        } catch (Throwable e){
            Logger.exception(e);
            Logger.sendWebHook(Thread.currentThread(), e, className() + ".startActivityForResult(Intent, int, Bundle)");
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        ALog.textSingle(className(), "startActivityForResult", getBaseContext() + ", startActivity() " + intent);
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Throwable e){
            Logger.exception(e);
            Logger.sendWebHook(Thread.currentThread(), e, className() + ".startActivityForResult(Intent, int)");
        }
    }


    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        try {
            return super.startService(service);
        } catch (Throwable e){
            Logger.exception(e);
            return null;
        }
    }


    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        try {
            return super.bindService(service, conn, flags);
        } catch (Throwable e){
            Logger.exception(e);
            return false;
        }
    }


    @Override
    public void unbindService(ServiceConnection conn) {
        try {
            super.unbindService(conn);
        } catch (Throwable e){
            Logger.exception(e);
        }
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            super.unregisterReceiver(receiver);
        } catch (Throwable e){
            Logger.exception(e);
        }
    }

    /**
     * @return 是否在展示支付
     */
    public boolean isPayShowing(){
        return false;
    }

}
