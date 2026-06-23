package com.common.config.bookshelf;

import com.common.config.FeatureConfig;

public class BookShelfConfig {

    public static boolean enableVip = FeatureConfig.isPremiumApp;
    public static boolean enableReward = !FeatureConfig.isPremiumApp;


}
