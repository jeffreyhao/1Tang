package com.base.watcher;

/**
 * Created by haojiangfeng on 2024/11/29.
 */
public class WatcherEvent {

    /**
     * 启动App
     */
    public static final int EVENT_START_APP                         = 101;
    /**
     * 切换CONFIGURE
     */
    public static final int EVENT_ON_CONFIGURE_CHANGE				= 103;
    /**
     * dns切换
     */
    public static final int EVENT_SWITCH_DNS						= 104;
    /**
     * app从后台切到前台
     */
    public static final int EVENT_APP_SWITCH_FOREGROUND				= 105;
    /**
     * app从前台切到后台
     */
    public static final int EVENT_APP_SWITCH_BACKSTAGE				= 106;
    /**
     * 退出app
     */
    public static final int EVENT_APP_EXIT_APP						= 107;

    /**
     * 主tab页的滑动事件
     */
    public static final int EVENT_MAIN_SCROLL_START                 = 108;

    /**
     * 主tab页的滑动结束事件
     */
    public static final int EVENT_MAIN_SCROLL_IDLE                  = 109;

    /**
     * 跨天
     */
    public static final int EVENT_EXTEND_MID_NIGHT                  = 110;

    /**
     * Activity onResume()
     */
    public static final int EVENT_ACTIVITY_ON_RESUME                = 111;

    /**
     * 深链 dns切换
     */
    public static final int EVENT_DNS_DEEPLINK					    = 112;

    /**
     * app被清零
     */
    public static final int EVENT_APP_ACTIVITY_CLEARED				= 113;

    /**
     *  onStop-onResume 超过五分钟
     */
    public static final int EXTEND_5_MIN                            = 114;

    /**
     * 退出阅读页
     */
    public static final int EVENT_READ_DESTROY                      = 115;

    /**
     * 阅读页翻页方式，只算横翻和竖翻
     */
    public static final int EVENT_SWITCH_PAGE_MODE                  = 116;

    /**
     * 阅读页设置字体大小
     */
    public static final int EVENT_CHANGE_TEXT_SIZE                  = 117;

    /**
     * 阅读页 onPause
     */
    public static final int EVENT_READ_PAUSE                        = 118;

    /**
     * 阅读页 onResume
     */
    public static final int EVENT_READ_RESUME                       = 119;


}
