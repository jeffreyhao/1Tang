package com.common.config.discover;

import com.common.config.FeatureConfig;

/**
 * Created by haojiangfeng on 2024/2/19.
 */
public class DiscoverConfig {


    public static int INIT_INDEX_DISCOVER = 0;


    public static boolean isShowGenresTabTitle = false;

    public static boolean enableVip     = FeatureConfig.isPremiumApp;
    public static boolean enableReward = !FeatureConfig.isPremiumApp;

    public static boolean isLongIndicator = false;

}
