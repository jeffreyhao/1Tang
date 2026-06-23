package com.base.api;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.base.global.GlobalUtil;

import java.lang.ref.WeakReference;

public final class GlobalContext {

    private static Context sApplicationContext;
    private static WeakReference<Activity> mTopActivityRef;


    public static void setApplicationContext(Context context) {
        sApplicationContext = context;
    }

    public static Context getContext() {
        return sApplicationContext;
    }

    @Nullable
    public static Activity getTopActivity(){
        Activity topActivity = null;
        if (mTopActivityRef != null) {
            topActivity = mTopActivityRef.get();
        }
        return topActivity == null ? GlobalUtil.getTopActivity() : topActivity;
    }

    public static void setTopActivity(Activity activity){
        mTopActivityRef = new WeakReference<>(activity);
    }

    public static void onActivityDestroyed(Activity activity) {
        // 如果销毁的是当前记录的栈顶 Activity，则清理引用
        if (mTopActivityRef != null && mTopActivityRef.get() == activity) {
            mTopActivityRef.clear();
            mTopActivityRef = null;
        }
    }


    public static Resources getResources() {
        return sApplicationContext.getResources();
    }

    /**
     * 可能有多语言问题
     */
    @Deprecated
    public static String getString(@StringRes int strRid) {
        Resources resources = getResources();
        return resources != null ? resources.getString(strRid) : "";
    }

    /**
     * 可能有多语言问题
     */
    @Deprecated
    public static String getString(@StringRes int strRid, Object... formatArgs) {
        Resources resources = getResources();
        return resources != null ? resources.getString(strRid, formatArgs) : "";
    }

    public static int getColor(@ColorRes int colorRid) {
        Resources resources = getResources();
        return resources != null ? resources.getColor(colorRid, getContext().getTheme()) : Color.WHITE;
    }


}