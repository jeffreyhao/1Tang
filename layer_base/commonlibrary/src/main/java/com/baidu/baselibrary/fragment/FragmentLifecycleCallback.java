package com.baidu.baselibrary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;


/**
 * Created by haojiangfeng on 2024/6/19.
 */
public interface FragmentLifecycleCallback {

    void onFragmentAttached(CustomFragmentManager fm, CustomFragment f, Context context);

    void onFragmentCreated(CustomFragmentManager fm, CustomFragment f, Bundle savedInstanceState);

    void onFragmentViewCreated(CustomFragmentManager fm, CustomFragment f, View v, Bundle savedInstanceState);

    void onFragmentStarted(CustomFragmentManager fm, CustomFragment f);

    void onFragmentResumed(CustomFragmentManager fm, CustomFragment f);

    void onFragmentPaused(CustomFragmentManager fm, CustomFragment f);

    void onFragmentStopped(CustomFragmentManager fm, CustomFragment f);

    void onFragmentSaveInstanceState(CustomFragmentManager fm, CustomFragment f, Bundle outState);

    void onFragmentViewDestroyed(CustomFragmentManager fm, CustomFragment f);

    void onFragmentDestroyed(CustomFragmentManager fm, CustomFragment f);

    void onFragmentDetached(CustomFragmentManager fm, CustomFragment f);
}
