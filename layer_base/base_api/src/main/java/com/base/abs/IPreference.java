package com.base.abs;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public interface IPreference {


    int get(String key, int defValue);
    void put(String key, int value);


    float get(String key, float defValue);
    void put(String key, float value);


    boolean get(String key, boolean defValue);
    void put(String key, boolean value);


    String get(String key, String defValue);
    void put(String key, String value);



}
