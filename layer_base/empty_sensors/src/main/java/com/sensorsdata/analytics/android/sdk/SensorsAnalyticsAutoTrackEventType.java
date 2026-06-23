package com.sensorsdata.analytics.android.sdk;

/**
 * Created by haojiangfeng on 2024/9/19.
 */

public interface SensorsAnalyticsAutoTrackEventType {
    /**
     * 空类型
     */
    int TYPE_NONE = 0;
    /**
     * App 启动事件
     */
    int APP_START = 1;

    /**
     * App 退出事件
     */
    int APP_END = 1 << 1;

    /**
     * 控件点击事件
     */
    int APP_CLICK = 1 << 2;

    /**
     * 页面浏览事件
     */
    int APP_VIEW_SCREEN = 1 << 3;
}
