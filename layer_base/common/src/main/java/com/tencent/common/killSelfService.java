package com.tencent.common;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
* 该服务只用来让APP重启，生命周期也仅仅是只是重启APP。重启完即自我杀死
* @author adison
* @date 2017/7/15 
* @time 下午6:27
*/

public class killSelfService extends Service {
    /**
     * 关闭应用后多久重新启动
     */
    private static long stopDelayed = 2000;

    private Handler mHandler;
    private String mPackageName;

    public killSelfService() {
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed = intent.getLongExtra("Delayed", 2000);
        mPackageName = intent.getStringExtra("PackageName");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(mPackageName);
                startActivity(LaunchIntent);
                killSelfService.this.stopSelf();
            }
        }, stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
