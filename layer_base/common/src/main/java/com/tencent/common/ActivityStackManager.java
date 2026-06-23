package com.tencent.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Activity栈管理类
 */
public class ActivityStackManager implements Application.ActivityLifecycleCallbacks {
    /**
     * 跟踪应用是否在前台
     */
    public static int sAppCount = 0;

    private static class ActivityStackHolder {
        private static final ActivityStackManager INSTANCE = new ActivityStackManager();
    }

    public static ActivityStackManager getInstance() {
        return ActivityStackManager.ActivityStackHolder.INSTANCE;
    }

    private ActivityStackManager() {
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 维护Activity 的list
     */
    private final List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    /**
     * 添加一个activity到管理list里
     *
     * @param activity Activity
     */
    public void pushActivity(Activity activity) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new UnsupportedOperationException("only support main thread!");
        }
        mActivitys.add(activity);
    }

    /**
     * 在activity list删除一个activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new UnsupportedOperationException("only support main thread!");
        }
        mActivitys.remove(activity);
    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     *
     * @return Activity
     */
    public Activity currentActivity() {
        if (mActivitys.isEmpty()) {
            return null;
        }
        return mActivitys.get(mActivitys.size() - 1);
    }


    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        finishActivity(activity);
    }


    /**
     * 结束指定的Activity
     *
     * @param activity 指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new UnsupportedOperationException("only support main thread!");
        }
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new UnsupportedOperationException("only support main thread!");
        }
        if (mActivitys.isEmpty()) {
            return;
        }
        Iterator<Activity> iterator = mActivitys.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls class名称
     *
     * @return Activity
     */
    public Activity findActivity(Class<?> cls) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new UnsupportedOperationException("only support main thread!");
        }
        Activity targetActivity = null;
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                targetActivity = activity;
                break;
            }
        }
        return targetActivity;
    }


    /**
     * 获取当前最顶部activity的实例
     *
     * @return activity
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * 获取当前最顶部的activity 名字
     *
     * @return activity
     */
    public String getTopActivityName() {
        Activity mBaseActivity;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new UnsupportedOperationException("only support main thread!");
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 获取倒数第二个 Activity
     *
     * @return activity
     */
    @Nullable
    public Activity getPenultimateActivity() {
        Activity activity = null;
        synchronized (mActivitys) {
            try {
                if (mActivitys.size() > 1) {
                    activity = mActivitys.get(mActivitys.size() - 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return activity;
    }


    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  监听到 Activity创建事件 将该 Activity 加入list
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        pushActivity(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        sAppCount++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        sAppCount--;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    /*
     *  监听到 Activity销毁事件 将该Activity 从list中移除
     */
    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        popActivity(activity);
    }
}
