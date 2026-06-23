package com.baidu.baselibrary.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.baidu.baselibrary.annotation.Unused;
import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.ui.AnimationUtil;
import com.base.util.AppUtil;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.baselibrary.R;

import java.lang.ref.WeakReference;
import java.util.Random;


/**
 * Created by haojiangfeng on 2023/7/14.
 */
public class App {

    public interface IAccountBind{

        boolean isBind();
    }

    public static Application sApplication;
    public static Handler sHandler;
    public static boolean isScreenPortrait;

    @Unused
    public static boolean isInMultiWindowMode;
    @Unused
    public static boolean isInMultiWindowBottom;
    @Unused
    public static boolean mEnableShowSysBar;
    @Unused
    public static boolean mEnableNight;

    @Unused
    private static boolean mIsCanScrollToLeft = false;
    @Unused
    private static boolean mIsCanScrollToRight = false;

    /**
     * 非1U的书籍id
     */
    private static String readingBookId = "";
    private static String readingExternalBookId = "";
    private static String readingChapterId = "";

    public static IAccountBind accountBind;

    public static int sLiveActivityCount = 0;

    public static int sForegroundActivityCount = 0;


    public static WeakReference<Runnable> weakLoggedInRunnable;
    public static long loggedInRunnablePostDelayMills;




    public static void setApplication(Application application, Handler handler){
        sApplication = application;
        sHandler = handler;
        AppUtil.setHandler(handler);
    }

    public static Context getAppContext(){
        return sApplication;
    }

    public static Context getContext(){
        return sApplication;
    }


    public static Handler getHandler(){
        return sHandler;
    }


    public static boolean isMainThread(){
        return Looper.getMainLooper() == Looper.myLooper();
    }


    public static void runOnUiThread(Runnable runnable) {
        if(runnable != null){
            if(isMainThread()){
                runnable.run();
            } else {
                sHandler.post(runnable);
            }
        }
    }

    public static boolean post(Runnable runnable){
        if(runnable == null){
            return false;
        }
        return sHandler.post(runnable);
    }

    public static boolean postDelayed(Runnable runnable, long delay){
        if(runnable == null){
            return false;
        }
        return sHandler.postDelayed(runnable, delay);
    }

    /**
     * 随机延迟 0~maxDelaySeconds秒 执行任务
     *
     * @param runnable Runnable
     * @param maxDelaySeconds 最大延迟秒数
     */
    public static boolean randomPostDelayed(Runnable runnable, int maxDelaySeconds){
        if(runnable == null){
            return false;
        }
        return sHandler.postDelayed(runnable, new Random().nextInt(maxDelaySeconds) * 1000L);
    }


    public static boolean postAtFrontOfQueue(Runnable runnable) {
        if(runnable == null){
            return false;
        }
        return sHandler.postAtFrontOfQueue(runnable);
    }

    public static void removeRunnable(Runnable runnable){
        if(runnable == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(sHandler.hasCallbacks(runnable)){
                sHandler.removeCallbacks(runnable);
            }
        } else {
            sHandler.removeCallbacks(runnable);
        }
    }

    public static void setEnableGestrueScroll(boolean enable) {
        setEnableScrollToLeft(enable);
        setEnableScrollToRight(enable);
    }

    public static void setEnableScrollToLeft(boolean enable) {
        mIsCanScrollToLeft = enable;
    }

    public static void setEnableScrollToRight(boolean enable) {
        mIsCanScrollToRight = enable;
    }

    public static boolean getEnableScrollToLeft() {
        return mIsCanScrollToLeft;
    }

    public static boolean getEnableScrollToRigh() {
        return mIsCanScrollToRight;
    }



    public static Resources getResources() {
        return sApplication.getResources();
    }

    public static Resources.Theme getAppTheme() {
        return sApplication.getTheme();
    }

    public static String getString(int resId){
        return sApplication.getString(resId);
    }

    public static int getColor(int resId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return sApplication.getColor(resId);
        } else {
            return sApplication.getResources().getColor(resId);
        }
    }

    /**
     * 在这里做退出app的清理和重置
     */
    public static void clearOnMainExit() {
    }

    public static boolean isReading(){
        return !TextUtils.isEmpty(readingBookId);
    }

    public static boolean isReading(String bookId){
        return !TextUtils.isEmpty(readingBookId) && !TextUtils.isEmpty(bookId) && readingBookId.equals(bookId);
    }

    /**
     * 正在阅读的书籍id。这个书籍id是非IU开头的。
     * <br>
     * 注意：
     *  1. 神策统计用到的都是IU开头的，本方法不能用于神策统计。
     *  2. 严禁拼接1U参数。
     */
    public static String getReadingBookId(){
        return readingBookId;
    }

    public static String getReadingChapterId(){
        return readingChapterId;
    }

    /**
     * @param bid 非IU开头的书籍id
     */
    public static void setReadingBook(String bid, String externalBookId) {
        readingBookId = bid;
        readingExternalBookId = externalBookId;
    }

    public static void setReadingChapter(String chapterId) {
        readingChapterId = chapterId;
    }


    public static boolean isAppOnBackground(){
        return App.sForegroundActivityCount <= 0;
    }


    /**
     * 狼人包
     */
    public static boolean isWolfApp(){
        return GlobalBuildConfig.FLAVOR.equals("lycannovel")
                || GlobalBuildConfig.FLAVOR.equals("lycannovel");
    }


    public static void checkLoginRun(Runnable runnable){
        if(isBindAccount()){
            if(runnable != null){
                runnable.run();
            }
        } else {
            if(runnable != null){
                weakLoggedInRunnable = new WeakReference<>(runnable);
            }
        }
    }

    public static void checkLoginRunDelay(Runnable runnable, long mills){
        if(isBindAccount()){
            if(runnable != null){
                runnable.run();
            }
        } else {
            if(runnable != null){
                weakLoggedInRunnable = new WeakReference<>(runnable);
            }
        }
        loggedInRunnablePostDelayMills = mills;
    }

    public static void doWeakLoginRunnable(){
        if(weakLoggedInRunnable == null){
            return;
        }
        Runnable runnable = weakLoggedInRunnable.get();
        if(runnable == null){
            return;
        }
        if(loggedInRunnablePostDelayMills == 0){
            App.post(runnable);
        } else {
            App.postDelayed(runnable, loggedInRunnablePostDelayMills);
        }
        weakLoggedInRunnable = null;
        loggedInRunnablePostDelayMills = 0;
    }

    public static boolean isBindAccount(){
        return accountBind != null && accountBind.isBind();
    }

    public static void startActivity(Activity activity, Class<? extends Activity> targetClass, int... anim){
        if(activity == null){
            ALog.textSingle("activity is null");
            return;
        }
        Intent intent = new Intent(activity, targetClass);
        if(activity.isDestroyed() || activity.isFinishing()){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    Activity topActivity = ActivityUtils.getTopActivity();
                    if(topActivity != null && !topActivity.isDestroyed() && !topActivity.isFinishing()){
                        topActivity.startActivity(intent);
                        setOverridePendingTransition(activity, anim);
                    }
                }
            }, 500);
        } else {
            activity.startActivity(intent);
            setOverridePendingTransition(activity, anim);
        }
    }

    private static void setOverridePendingTransition(Activity activity, int... anim){
        if(anim != null && anim.length == 2){
            AnimationUtil.overridePendingTransition(activity, anim[0], anim[1]);
        } else {
            AnimationUtil.overridePendingTransition(activity, R.anim.push_left_in, R.anim.anim_none);
        }
    }




}
