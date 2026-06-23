package com.base.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.base.api.GlobalContext;
import com.base.api.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * 设置存储工具类
 * @author adison
 * @date 2017/5/20
 * @time 下午4:48
 */
public final class PreferencesUtil {

    private static String defaultName = "Preferences";

    private static SharedPreferences getPreferences(String name) {
        return GlobalContext.getContext().getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }



    public static boolean get(String key, boolean defValue) {
        return get(defaultName, key, defValue);
    }

    public static int get(String key, int defValue) {
        return get(defaultName, key, defValue);
    }

    public static float get(String key, float defValue) {
        return get(defaultName, key, defValue);
    }

    public static long get(String key, long defValue) {
        return get(defaultName, key, defValue);
    }

    public static String get(String key, String defValue) {
        return get(defaultName, key, defValue);
    }

    public static Set<String> get(String key, Set<String> defValue) {
        return get(defaultName, key, defValue);
    }

    public static <C extends Serializable> C get(String key, C defValue) {
        return get(defaultName, key, defValue);
    }

    public static boolean get(String name, String key, boolean defValue) {
        return getPreferences(name).getBoolean(key, defValue);
    }

    public static int get(String name, String key, int defValue) {
        return getPreferences(name).getInt(key, defValue);
    }

    public static float get(String name, String key, float defValue) {
        return getPreferences(name).getFloat(key, defValue);
    }

    public static long get(String name, String key, long defValue) {
        return getPreferences(name).getLong(key, defValue);
    }

    public static String get(String name, String key, String defValue) {
        return getPreferences(name).getString(key, defValue);
    }

    public static Set<String> get(String name, String key, Set<String> defValue) {
        return getPreferences(name).getStringSet(key, defValue);
    }

    public static <C extends Serializable> C get(String name, String key, C defValue) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        C result = defValue;

        String value = getPreferences(name).getString(key, null);
        if (value != null) {
            try {
                byte[] decoded = Base64.decode(value.getBytes(), Base64.DEFAULT);
                bais = new ByteArrayInputStream(decoded);
                ois = new ObjectInputStream(bais);
                result = (C) ois.readObject();

            } catch (Exception e) {
                Logger.exception(e);
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        Logger.exception(e);
                    }
                }
                if (bais != null) {
                    try {
                        bais.close();
                    } catch (IOException e) {
                        Logger.exception(e);
                    }
                }
            }
        }

        return result;
    }


    public static void put(String key, boolean value) {
        put(defaultName, key, value);
    }

    public static void put(String key, int value) {
        put(defaultName, key, value);
    }

    public static void put(String key, float value) {
        put(defaultName, key, value);
    }

    public static void put(String key, long value) {
        put(defaultName, key, value);
    }

    public static void put(String key, String value) {
        put(defaultName, key, value);
    }

    public static void put(String key, Set<String> value) {
        put(defaultName, key, value);
    }

    public static <C extends Serializable> void put(String key, C value) {
        put(defaultName, key, value);
    }

    public static void put(String name, String key, boolean value) {
        getPreferences(name).edit().putBoolean(key, value).apply();
    }

    public static void put(String name, String key, int value) {
        getPreferences(name).edit().putInt(key, value).apply();
    }

    public static void put(String name, String key, float value) {
        getPreferences(name).edit().putFloat(key, value).apply();
    }

    public static void put(String name, String key, long value) {
        getPreferences(name).edit().putLong(key, value).apply();
    }

    public static void put(String name, String key, String value) {
        getPreferences(name).edit().putString(key, value).apply();
    }

    public static void put(String name, String key, Set<String> value) {
        getPreferences(name).edit().putStringSet(key, value).apply();
    }

    public static <C extends Serializable> void put(String name, String key, C value) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] encoded = Base64.encode(baos.toByteArray(), Base64.DEFAULT);
            getPreferences(name).edit().putString(key, new String(encoded)).apply();

        } catch (IOException e) {
            Logger.exception(e);
            Logger.sendWebHook(Thread.currentThread(), e, "PreferenceUtil.put()");

        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    Logger.exception(e);
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    Logger.exception(e);
                }
            }
        }
    }


    public static void remove(String key) {
        remove(defaultName, key);
    }

    public static void remove(String name, String key) {
        getPreferences(name).edit().remove(key).apply();
    }


    public static void clear() {
        clear(defaultName);
    }

    public static void clear(String name) {
        getPreferences(name).edit().clear().apply();
    }

    public static boolean contains(String key){
        return getPreferences(defaultName).contains(key);
    }
}