package com.xcyh.cheatsheet.app.impl;

import com.base.abs.IPreference;
import com.base.global.PreferencesUtil;

/**
 * Created by haojiangfeng on 2024/10/30.
 */
public class IPreferenceImpl implements IPreference {


    public static IPreferenceImpl get(){
        return new IPreferenceImpl();
    }


    @Override
    public int get(String key, int defValue) {
        return PreferencesUtil.get(key, defValue);
    }

    @Override
    public void put(String key, int value) {
        PreferencesUtil.put(key, value);
    }

    @Override
    public float get(String key, float defValue) {
        return PreferencesUtil.get(key, defValue);
    }

    @Override
    public void put(String key, float value) {
        PreferencesUtil.put(key, value);
    }

    @Override
    public boolean get(String key, boolean defValue) {
        return PreferencesUtil.get(key, defValue);
    }

    @Override
    public void put(String key, boolean value) {
        PreferencesUtil.put(key, value);
    }

    @Override
    public String get(String key, String defValue) {
        return PreferencesUtil.get(key, defValue);
    }

    @Override
    public void put(String key, String value) {
        PreferencesUtil.put(key, value);
    }


}
