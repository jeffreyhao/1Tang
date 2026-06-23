package com.common.config.read;

import java.util.HashMap;
import java.util.Map;

public class ReadConfig {
    public static boolean autoPay = false;
    public static String curChapterId = "";
    public static String curPayChapterId = "";
    //记录扉页书籍封面图是否加载完成
    public static Map<String,Boolean> headPageBookCoverMap = new HashMap<>();
    public static String recommendActivityId = "";
    public static boolean isBookBlackListRequest = false;

    /**
     * 其他支付方式的限制开关。
     *      0-不限制
     *      1-限制
     */
    public static int rechargePlatformLimited = 1;

    public static boolean canScrollVertical = true;

    public static boolean isReadNetworkAvailable = true;
    /**
     * 最近一次阅读的书籍类型  0 普通 1 vip
     */
    public static int lastReadBookType;

    public static boolean enableRowSpaceAlpha = true;


    public static void clearTempData() {
        curPayChapterId = "";
        recommendActivityId = "";
    }


}