package com.baidu.baselibrary.global.crash;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.sys.LogUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;


/**
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。
 * UncaughtException处理类，当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 *
 * 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框，实现该接口并注册为程序中的默认未捕获异常处理。
 * 这样当未捕获异常发生时，就可以做些异常处理操作。例如：收集异常信息，发送错误报告 等。
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private List<Long> mIgnoreThreadList= new ArrayList<>();


//    static {
//        try {
//            System.loadLibrary("NativeCrash");
//        } catch (Throwable e) {
//            LOG.e(e);
//        }
//    }

    @SuppressLint("StaticFieldLeak")
    private static CrashHandler instance;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;  // 系统默认的UncaughtException处理类
    private String mCrashThread = null;//当前crash的线程名称，用于去重

    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param isNeedIgnoreError 是否忽略系统异常处理，push进程，通知栏进程等
     */
    public void init(Context ctx, boolean isNeedIgnoreError) {
        mContext = ctx;
        if (!isNeedIgnoreError) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
//        CrashUtil.CRASH_SAVE_ADDRESS = PathHelper.getLogDir();
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//    	Log.e("uncaughtException", "uncaughtException");
        if(isHasCrash(thread)){
            //已经处理过，直接交给系统或者退出
            if (this.mDefaultHandler != null) {
                this.mDefaultHandler.uncaughtException(thread, ex);
            } else {
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
            return;//可有可无，方便阅读，上面代码执行完就不会走了
        }

        handleException(ex);
        if(thread!=null&&mIgnoreThreadList.contains(thread.getId())){
            //忽略线程崩溃
            return;
        }
        boolean isMainProcess = true;

        try {
            ActivityManager am = (ActivityManager) App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
            String pkgName = App.getAppContext().getPackageName();
            int pid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo ra : run) {
                if (ra.pid == pid) {
                    if (!ra.processName.equals(pkgName)) {
                        isMainProcess = false;
                    }
                }
            }
        }catch (Throwable e){
            LogUtil.e(e);
        }

        if (!isMainProcess) {
            try {
                Process.killProcess(Process.myPid());
            } catch (Throwable e) {
                LogUtil.e(e);
            }
        }
        if (isMainProcess && mDefaultHandler != null) {//让系统默认的异常处理器来处理
//            PluginManager.onCrash(mContext);
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 抛出自定义捕捉信息
     * @param throwable
     */
    public static void throwCustomCrash(Throwable throwable) {
        //增加调试信息，和正常崩溃区别
//        if(!App.DEBUG){
//            Throwable wrapThowable=new Throwable("DEBUGt_CRASH",throwable);
//            instance.handleException(wrapThowable);
//        }else{
//            LogUtil.e(throwable);
//        }
    }

    /**
     * 底层使用，上传勿用，需要抛出自定义捕捉信息，使用{@link #throwCustomCrash(Throwable)}
     * @param throwable
     */
    public static void throwNativeCrash(Throwable throwable) {
//        if(!App.DEBUG){
//            instance.handleException(throwable);
//        }
    }



    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private void handleException(Throwable ex) {
        if (ex == null) {
            return;
        }

        // 收集设备信息
        try {
            CrashUtil.collectCrashInfo(mContext, ex);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    /**
     * 忽略线程崩溃处理
     * @param thread
     */
    public void wrapIgnoreThread(Thread thread){
        if(thread==null)return;
        mIgnoreThreadList.add(thread.getId());
        thread.setUncaughtExceptionHandler(this);
    }

    /**
     * 此线程是否已经处理过
     * @param thread 当前崩溃的线程
     * @return 此线程是否已经处理过
     */
    private synchronized boolean isHasCrash(Thread thread){
        if(mCrashThread == null || !mCrashThread.equals(thread.getName())) {
            mCrashThread = thread.getName();
            return false;
        }
        return true;
    }


}
