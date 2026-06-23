package com.baidu.baselibrary.fragment;

import android.os.Bundle;

import androidx.collection.ArrayMap;


/**
 * FragmentClient
 */
public class FragmentClient {

    String mFragmentClassNameString = null;
    CustomFragment mFragment = null;
    Bundle mArgument = null;
    Bundle mSaveState = null;
    //class缓存，加快创建Fragment
    private static ArrayMap<String, Class> gClassCache = new ArrayMap<>();

    public FragmentClient(){

    }

    public FragmentClient(CustomFragment fragment) {
        mFragment = fragment;
        mFragmentClassNameString = fragment.getClass().getName();
        synchronized (FragmentClient.class) {
            gClassCache.put(mFragmentClassNameString, fragment.getClass());
        }
        mArgument = fragment.getArguments();
    }

    public void onSaveInstanceState(CustomFragment fragment) {
        Bundle bundle = new Bundle();
        fragment.onSaveInstanceState(bundle);
        if (!bundle.isEmpty()) {
            mSaveState = bundle;
        }
    }

    public void realseMemory() {
        if (mFragment != null) {
            mFragment.onDestroyView();
            mFragment.onDestroy();
            mFragment.onDetach();
        }
        mFragment = null;
    }

    public Class getFragmentClass() {
        synchronized (FragmentClient.class) {
            return gClassCache.get(mFragmentClassNameString);
        }
    }

    public static Class getCacheClass(String className){
        if(className == null)return null;
        synchronized (FragmentClient.class){
            return gClassCache.get(className);
        }
    }

    public static void putCacheClass(String className, Class cacheClass){
        if(className == null || cacheClass == null)return;
        synchronized (FragmentClient.class){
            gClassCache.put(className, cacheClass);
        }
    }


    public String getFragmentClassNameString() {
        return mFragmentClassNameString;
    }

    public CustomFragment getFragment() {
        return mFragment;
    }

    public Bundle getArgument() {
        return mArgument;
    }

    public Bundle getSaveState() {
        return mSaveState;
    }

    public void setSaveState(Bundle saveState) {
        this.mSaveState=saveState;
    }

    public void setFragment(CustomFragment fragment) {
        this.mFragment = fragment;
    }

    public static void clearCache() {
        synchronized (FragmentClient.class) {
            gClassCache.clear();
        }
    }
}
