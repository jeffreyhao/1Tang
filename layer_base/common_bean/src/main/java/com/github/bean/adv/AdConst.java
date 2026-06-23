package com.github.bean.adv;

/**
 * 广告用的常量
 */
public class AdConst {

    /**
     * 广告类型
     */
    public static class AdType {
        /** 激励广告 */
        public static final String REWARDED         = "rewarded";
        /** 开屏广告 */
        public static final String APPOPEN          = "appopen";
        /** 横幅广告 */
        public static final String BANNER           = "banner";
        /** 原生广告 */
        public static final String NATIVE           = "native";
        /** 插页广告 */
        public static final String INTERSTITIAL     = "interstitial";
    }

    /**
     * 广告状态
     */
    public static class AdStatus {
        /** 关闭 */
        public static final int OFF     = 0;
        /** 开启 */
        public static final int ON      = 1;
    }

}
