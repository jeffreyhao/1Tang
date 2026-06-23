package com.baidu.baselibrary.log;


/**
 * format转换器
 */
public interface IFormatter<T> {

    String format(T t);
}