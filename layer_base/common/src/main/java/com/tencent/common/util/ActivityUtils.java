package com.tencent.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;

import com.fold.common.R;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/23
 *     desc  : Activity相关工具类
 * </pre>
 */
public final class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断是否存在Activity
     *
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isActivityExists(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(Abase.getContext().getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(Abase.getContext().getPackageManager()) == null ||
                Abase.getContext().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 启动Activity
     *
     * @param activity activity
     * @param cls      activity类
     */
    public static void startActivity(Activity activity, Class<?> cls) {
        if (activity == null) return;

        startActivity(activity, null, activity.getPackageName(), cls.getName(), null);
    }

    /**
     * 启动Activity
     *
     * @param extras   extras
     * @param activity activity
     * @param cls      activity类
     */
    public static void startActivity(Bundle extras, Activity activity, Class<?> cls) {
        if (activity == null) return;

        startActivity(activity, extras, activity.getPackageName(), cls.getName(), null);
    }

    /**
     * 启动Activity
     *
     * @param activity  activity
     * @param cls       activity类
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void startActivity(Activity activity, Class<?> cls, int enterAnim, int exitAnim) {
        if (activity == null) return;
        startActivity(activity, null, activity.getPackageName(), cls.getName(), null);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 启动Activity
     *
     * @param activity  activity
     * @param cls       activity类
     */
    public static void startActivityRightIn(Activity activity, Class<?> cls) {
        if (activity == null) return;
        startActivity(activity, null, activity.getPackageName(), cls.getName(), null);
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.anim_none);
    }

    /**
     * 启动Activity
     *
     * @param extras    extras
     * @param activity  activity
     * @param cls       activity类
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void startActivity(Bundle extras, Activity activity, Class<?> cls, int enterAnim, int exitAnim) {
        if (activity == null) return;

        startActivity(activity, extras, activity.getPackageName(), cls.getName(), null);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 启动Activity
     *
     * @param activity activity
     * @param cls      activity类
     * @param options  跳转动画
     */
    public static void startActivity(Activity activity, Class<?> cls, Bundle options) {

        startActivity(activity, null, activity.getPackageName(), cls.getName(), options);
    }

    /**
     * 启动Activity
     *
     * @param extras   extras
     * @param activity activity
     * @param cls      activity类
     * @param options  跳转动画
     */
    public static void startActivity(Bundle extras, Activity activity, Class<?> cls, Bundle options) {
        startActivity(activity, extras, activity.getPackageName(), cls.getName(), options);
    }

    /**
     * 启动Activity
     *
     * @param pkg 包名
     * @param cls 全类名
     */
    public static void startActivity(String pkg, String cls) {
        startActivity(Abase.getContext(), null, pkg, cls, null);
    }

    /**
     * 启动Activity
     *
     * @param extras extras
     * @param pkg    包名
     * @param cls    全类名
     */
    public static void startActivity(Bundle extras, String pkg, String cls) {
        startActivity(Abase.getContext(), extras, pkg, cls, extras);
    }

    /**
     * 启动Activity
     *
     * @param pkg     包名
     * @param cls     全类名
     * @param options 动画
     */
    public static void startActivity(String pkg, String cls, Bundle options) {
        startActivity(Abase.getContext(), null, pkg, cls, options);
    }

    /**
     * 启动Activity
     *
     * @param extras  extras
     * @param pkg     包名
     * @param cls     全类名
     * @param options 动画
     */
    public static void startActivity(Bundle extras, String pkg, String cls, Bundle options) {
        startActivity(Abase.getContext(), extras, pkg, cls, options);
    }

    private static void startActivity(Context context, Bundle extras, String pkg, String cls, Bundle options) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) intent.putExtras(extras);
        intent.setComponent(new ComponentName(pkg, cls));
        boolean isActivity = context instanceof Activity;
        if (!isActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (options == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent);
        } else {
            context.startActivity(intent, options);
        }
        if(isActivity){
            overridePendingTransition((Activity) context, R.anim.push_left_in, R.anim.anim_none);
        }
    }

    /**
     * 带参数进行Activity跳转,参数需要一个Map类型
     *
     * @param context        上下文环境
     * @param targetActivity 目标Activity的Class
     * @param params         跳转所带的参数
     */
    public static void startActivity(Context context, Class<? extends Activity> targetActivity, Map<String, Serializable> params) {
        if (null != params) {
            Intent intent = new Intent(context, targetActivity);
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 获取launcher activity
     *
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = Abase.getContext().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo aInfo : info) {
            if (aInfo.activityInfo.packageName.equals(packageName)) {
                return aInfo.activityInfo.name;
            }
        }
        return "no " + packageName;
    }


    /**
     * 获取栈顶Activity  ⚠️这个Activity的判断不准
     *
     * @return 栈顶Activity
     */
    @Deprecated
    public static Activity getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (ArrayMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass(); // class android.app.ActivityThread$ActivityClientRecord
//                Field pausedField = activityRecordClass.getDeclaredField("paused"); // boolean android.app.ActivityThread$ActivityClientRecord.paused
//                pausedField.setAccessible(true);
//                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity"); // android.app.Activity android.app.ActivityThread$ActivityClientRecord.activity
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTopActivityName(){
        List runningTasks = null;
        try {
            ActivityManager activityManager = (ActivityManager) Abase.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                runningTasks = activityManager.getRunningTasks(1);
            }

            if (runningTasks != null && runningTasks.size() > 0) {
                ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) runningTasks.get(0);
                ComponentName componentName = runningTaskInfo.topActivity;
                if (componentName != null) {
                    return componentName.getClassName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void overridePendingTransition(Activity activity, int in, int out) {
        if (activity == null) return;
        activity.overridePendingTransition(in, out);
    }

}
