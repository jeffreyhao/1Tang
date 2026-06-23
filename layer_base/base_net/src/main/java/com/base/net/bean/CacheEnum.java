package com.base.net.bean;

public enum CacheEnum {


    /**
     *  只加载缓存
     */
    CACHE_ONLY,


    /**
     *  只加载网络
     */
    NET_ONLY,


    /**
     *  同时加载缓存和网络
     */
    CACHE_AND_NET,


    /**
     *  先加载缓存，无缓存再请求网络
     */
    CACHE_ELSE_NET,


    /**
     *  先请求网络，请求失败再加载缓存
     */
    NET_ELSE_CACHE;


}
