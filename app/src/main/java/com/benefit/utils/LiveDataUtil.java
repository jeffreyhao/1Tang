package com.benefit.utils;

import android.text.TextUtils;

import com.baidu.baselibrary.virtual.UniqueKey;
import com.tencent.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by haojiangfeng on 2024/7/26.
 */
public class LiveDataUtil {


    /**
     * 获取liveData中的list
     *
     * @param liveData LiveData
     * @param <T> bean
     * @return list
     */
    @Nullable
    public static <T>  List<T> getList(MutableLiveData<List<T>> liveData){
        return liveData.getValue();
    }

    @NonNull
    public static <T> List<T> getNonNullList(MutableLiveData<List<T>> liveData){
        List<T> list = liveData.getValue();
        return list == null ? new ArrayList<>() : list;
    }

    @NonNull
    public static <T> List<T> getCopyNonNullList(MutableLiveData<List<T>> liveData){
        List<T> list = liveData.getValue();
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }



    public static <T extends Comparable<T>> void remove(MutableLiveData<List<T>> liveData, T toMoveData){
        List<T> list = getCopyNonNullList(liveData);
        if(CollectionUtils.isNotEmpty(list)){
            T target = null;
            for(T bean : list){
                if(bean.compareTo(toMoveData) == 0){
                    target = bean;
                    break;
                }
            }
            if(target != null){
                list.remove(target);
                liveData.postValue(new ArrayList<>(list));
            }
        }
    }

    public static <T extends UniqueKey> void remove(MutableLiveData<List<T>> liveData, String toMoveKey){
        if(TextUtils.isEmpty(toMoveKey)){
            return;
        }
        List<T> list = getCopyNonNullList(liveData);
        if(CollectionUtils.isNotEmpty(list)){
            T target = null;
            for(T bean : list){
                if(!TextUtils.isEmpty(bean.getUniqueKey()) && bean.getUniqueKey().equals(toMoveKey)){
                    target = bean;
                    break;
                }
            }
            if(target != null){
                list.remove(target);
                liveData.postValue(new ArrayList<>(list));
            }
        }
    }


    public static <T extends UniqueKey> void add(MutableLiveData<List<T>> liveData, T item) {
        if(item == null){
            return;
        }
        List<T> list = getCopyNonNullList(liveData);
        if(CollectionUtils.isNotEmpty(list)){
            for(T bean : list){
                if(!TextUtils.isEmpty(bean.getUniqueKey()) && !TextUtils.isEmpty(item.getUniqueKey()) && bean.getUniqueKey().equals(item.getUniqueKey())){
                    return;
                }
            }
        }
        list.add(item);
        liveData.postValue(list);
    }



    public static <T> boolean isEmpty(MutableLiveData<List<T>> liveData){
        List<T> list = liveData.getValue();
        return list == null || list.isEmpty();
    }
}
