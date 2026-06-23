package com.base.util.collection;

/**
 * 可复制的
 *
 * Created by haojiangfeng on 2025/2/18.
 */
public interface Replicable<T> extends Cloneable{

    T copy();
}
