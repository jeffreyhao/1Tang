package com.baidu.baselibrary.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.baidu.baselibrary.util.App;


public class SpGlobal {

    public final static String SHAREPREFERENCES_NAME = "com.galaxy.read.SharedPreferences.global";

    private static SpGlobal mSPTHelper = null;
    private SharedPreferences mSPT;
    private Editor mEditor;

    private SpGlobal(){
        open();
    }

    public static SpGlobal getInstance() {
        if (mSPTHelper == null) {
            synchronized (SpGlobal.class){
                if (mSPTHelper == null) {
                    mSPTHelper = new SpGlobal();
                }
            }
        }
        return mSPTHelper;
    }

    public SharedPreferences getSharedPreferences() {
        return mSPT;
    }

    public void init() {

    }

    public void init(Context context) {
        open(context);
    }

    @SuppressLint("CommitPrefEdits")
    private void open(Context context) {
        if (mSPT == null) {
            mSPT = context.getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_PRIVATE);
            mEditor = mSPT.edit();
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void open() {
        if (mSPT == null) {
            mSPT = App.getAppContext().getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_PRIVATE);
            mEditor = mSPT.edit();
        }
    }

    public synchronized String getString(String key, String defValue) {
        open();
        return mSPT.getString(key, defValue);
    }

    public synchronized void setString(String key, String value) {
        open();
        mEditor.putString(key, value);
        try {
            mEditor.commit();
        }catch (Exception e){

        }
    }

    public synchronized int getInt(String key, int defValue) {
        open();
        return mSPT.getInt(key, defValue);
    }

    public synchronized void setInt(String key, int value) {
        open();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public synchronized long getLong(String key, long defValue) {
        open();
        return mSPT.getLong(key, defValue);
    }

    public synchronized void setLong(String key, long value) {
        open();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public synchronized float getFloat(String key, float defValue) {
        open();
        return mSPT.getFloat(key, defValue);
    }

    public synchronized void seFloat(String key, float value) {
        open();
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public synchronized boolean getBoolean(String key, boolean defValue) {
        open();
        boolean flag = mSPT.getBoolean(key, defValue);
        return mSPT.getBoolean(key, defValue);
    }

    public synchronized void setBoolean(String key, boolean value) {
        open();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }
}
