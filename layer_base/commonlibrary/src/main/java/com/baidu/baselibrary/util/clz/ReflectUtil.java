package com.baidu.baselibrary.util.clz;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: lhc
 * @Date: 2018/8/21 14:08
 * @Desc:
 */
public class ReflectUtil {
    public static Class<?> getClassType(Object obj) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] args = ((ParameterizedType) genType).getActualTypeArguments();
        return args!=null&&args.length>0?(Class<?>) args[0]:null;
    }

    public static Class<?> getClassType(Object obj,int index) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] args = ((ParameterizedType) genType).getActualTypeArguments();
        return args.length>index?(Class<?>) args[index]:null;
    }

    public static int getActualTypeArgsLength(Object obj) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] args = ((ParameterizedType) genType).getActualTypeArguments();
        return args.length;
    }
}
