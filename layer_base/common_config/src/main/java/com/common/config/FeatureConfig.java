package com.common.config;

public class FeatureConfig {

    /**
     * 会员类型
     */
    public static @PremiumType int sPremiumType = PremiumType.NONE;

    /**
     * 是否纯会员app.  <=>  {@link #sPremiumType} = {@link PremiumType#PURE_PREMIUM}
     */
    public static boolean isPremiumApp = false;

    /**
     * 是否有会员功能. <=>  {@link #sPremiumType} = {@link PremiumType#BOTH_PREMIUM}
     */
    public static boolean hasPremium = false;

    /**
     * 是否使用分包功能，使用分包接口字段 is_pkg = 1 默认 0
     */
    public static int bookDistributionType = 0;



}
