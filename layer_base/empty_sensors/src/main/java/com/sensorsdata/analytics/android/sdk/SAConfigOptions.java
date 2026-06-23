package com.sensorsdata.analytics.android.sdk;

import java.util.List;

/**
 * Created by haojiangfeng on 2024/9/19.
 */
public class SAConfigOptions {

    /**
     * 获取 SAOptionsConfig 实例
     *
     * @param serverUrl，数据上报服务器地址
     */
    public SAConfigOptions(String serverUrl) {

    }

    /**
     * 设置 AutoTrackEvent 的类型，可通过 '|' 进行连接
     *
     * @param autoTrackEventType 开启的 AutoTrack 类型
     * @return SAOptionsConfig
     */
    public SAConfigOptions setAutoTrackEventType(int autoTrackEventType) {
        return this;
    }

    /**
     * 是否开启页面停留时长
     *
     * @param isTrackPageLeave 是否开启页面停留时长
     * @param isTrackFragmentPageLeave 是否采集 Fragment 页面停留时长，需开启页面停留时长采集
     * @return SAConfigOptions
     */
    public SAConfigOptions enableTrackPageLeave(boolean isTrackPageLeave, boolean isTrackFragmentPageLeave) {
        return this;
    }

    /**
     * 指定哪些 Activity/Fragment 不采集页面停留时长
     * 指定 Activity/Fragment 的格式为：****.class
     *
     * @param ignoreList activity/Fragment 列表
     * @return SAConfigOptions
     */
    public SAConfigOptions ignorePageLeave(List<Class<?>> ignoreList) {
        return this;
    }

    /**
     * 设置是否开启 AppCrash 采集，默认是关闭的
     *
     * @return SAOptionsConfig
     */
    public SAConfigOptions enableTrackAppCrash() {
        return this;
    }

    /**
     * 是否打印日志
     *
     * @param enableLog 是否开启打印日志
     * @return SAOptionsConfig
     */
    public SAConfigOptions enableLog(boolean enableLog) {
        return this;
    }

}
