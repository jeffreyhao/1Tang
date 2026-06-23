package com.baidu.baselibrary.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by haojiangfeng on 2024/6/19.
 */
public class FragmentCallbackDispatcher {

    private static final CopyOnWriteArrayList<FragmentLifecycleCallback> sLifecycleCallbacks = new CopyOnWriteArrayList<>();

    public static void registerFragmentLifecycleCallback(FragmentLifecycleCallback callback) {
        synchronized (sLifecycleCallbacks) {
            for (int i = 0, count = sLifecycleCallbacks.size(); i < count; i++) {
                if (sLifecycleCallbacks.get(i) == callback) {
                    return;
                }
            }
            sLifecycleCallbacks.add(callback);
        }
    }

    public static void unregisterFragmentLifecycleCallback(FragmentLifecycleCallback callback) {
        synchronized (sLifecycleCallbacks) {
            for (int i = 0, count = sLifecycleCallbacks.size(); i < count; i++) {
                if (sLifecycleCallbacks.get(i) == callback) {
                    sLifecycleCallbacks.remove(i);
                    break;
                }
            }
        }
    }


    static void onFragmentAttached(CustomFragmentManager fm, CustomFragment f, Context context) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentAttached(fm, f, context);
        }
    }

    static void onFragmentCreated(CustomFragmentManager fm, CustomFragment f, Bundle savedInstanceState) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentCreated(fm, f, savedInstanceState);
        }
    }

    static void onFragmentViewCreated(CustomFragmentManager fm, CustomFragment f, View v, Bundle savedInstanceState) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentViewCreated(fm, f, v, savedInstanceState);
        }
    }


    static void onFragmentStarted(CustomFragmentManager fm, CustomFragment f) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentStarted(fm, f);
        }
    }


    static void onFragmentResumed(CustomFragmentManager fm, CustomFragment f) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentResumed(fm, f);
        }
    }


    static void onFragmentPaused(CustomFragmentManager fm, CustomFragment f) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentPaused(fm, f);
        }
    }


    static void onFragmentStopped(CustomFragmentManager fm, CustomFragment f) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentStopped(fm, f);
        }
    }


    static void onFragmentSaveInstanceState(CustomFragmentManager fm, CustomFragment f, Bundle outState) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentSaveInstanceState(fm, f, outState);
        }
    }


    static void onFragmentViewDestroyed(CustomFragmentManager fm, CustomFragment f) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentViewDestroyed(fm, f);
        }
    }


    static void onFragmentDestroyed(CustomFragmentManager fm, CustomFragment f) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentDestroyed(fm, f);
        }
    }


    static void onFragmentDetached(CustomFragmentManager fm, CustomFragment f) {
        for(FragmentLifecycleCallback callback : sLifecycleCallbacks){
            callback.onFragmentDetached(fm, f);
        }
    }
}
