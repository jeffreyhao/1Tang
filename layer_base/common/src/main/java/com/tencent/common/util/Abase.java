package com.tencent.common.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * 获取 {@link Context}, {@link Resources}, {@link AssetManager}, {@link Configuration} , {@link DisplayMetrics} 基类
 *
 * @author adison
 * @date 2017/5/20
 * @time 下午4:14
 */
public final class Abase {

    public static final String TAG="Abase";

    private static Context sContext;

    public static void initialize(@NonNull Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }

    public static Resources getResources() {
        return Abase.getContext().getResources();
    }

    public static Resources.Theme getTheme() {
        return Abase.getContext().getTheme();
    }

    public static AssetManager getAssets() {
        return Abase.getContext().getAssets();
    }

    public static Configuration getConfiguration() {
        return Abase.getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return Abase.getResources().getDisplayMetrics();
    }
}