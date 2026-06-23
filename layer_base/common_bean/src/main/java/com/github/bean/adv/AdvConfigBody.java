package com.github.bean.adv;


public class AdvConfigBody {


    /**
     * ad_config : {"reward_task_center":{"ad_location":"reward_task_center","ad_location_name":"任务中心","ad_type":"rewarded","status":0,"reward_type":"","reward_num":0,"times":0,"product_id":[],"interval_duration":0,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":0,"non_advertising_chapters":[],"no_ad_chapters":0},"native_chapter_end":{"ad_location":"native_chapter_end","ad_location_name":"阅读器章节结尾","ad_type":"native","status":0,"reward_type":"","reward_num":0,"times":0,"product_id":[],"interval_duration":0,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":0,"non_advertising_chapters":[],"no_ad_chapters":0},"reward_reader_unlock":{"ad_location":"reward_reader_unlock","ad_location_name":"阅读解锁章节","ad_type":"rewarded","status":1,"reward_type":"","reward_num":0,"times":0,"product_id":["10029","2"],"interval_duration":0,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":2,"non_advertising_chapters":[],"no_ad_chapters":10},"startup_screen":{"ad_location":"startup_screen","ad_location_name":"启动开屏","ad_type":"appopen","status":1,"reward_type":"","reward_num":0,"times":0,"product_id":["10029"],"interval_duration":10,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":0,"non_advertising_chapters":[],"no_ad_chapters":0},"banner_reader_bottom":{"ad_location":"banner_reader_bottom","ad_location_name":"阅读器底部banner","ad_type":"banner","status":1,"reward_type":"","reward_num":0,"times":0,"product_id":["10029"],"interval_duration":0,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":0,"non_advertising_chapters":[],"no_ad_chapters":0},"banner_library_bottom":{"ad_location":"banner_library_bottom","ad_location_name":"书架底部banner","ad_type":"banner","status":1,"reward_type":"","reward_num":0,"times":0,"product_id":["10029"],"interval_duration":0,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":0,"non_advertising_chapters":[],"no_ad_chapters":0},"native_reader_content":{"ad_location":"native_reader_content","ad_location_name":"阅读器翻页插入广告","ad_type":"native","status":1,"reward_type":"","reward_num":0,"times":0,"product_id":["10029"],"interval_duration":0,"page_interval":3,"page_turning_cooldown_time":5,"ads_unlock_chapter_count":0,"non_advertising_chapters":[1,3],"no_ad_chapters":0},"native_library":{"ad_location":"native_library","ad_location_name":"书架内原生","ad_type":"native","status":1,"reward_type":"","reward_num":0,"times":0,"product_id":["10029"],"interval_duration":0,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":0,"non_advertising_chapters":[],"no_ad_chapters":0},"interstitial_chapter_turning":{"ad_location":"interstitial_chapter_turning","ad_location_name":"阅读器翻章节插入广告","ad_type":"interstitial","status":1,"reward_type":"","reward_num":0,"times":0,"product_id":["10029"],"interval_duration":300,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":0,"non_advertising_chapters":[1,3],"no_ad_chapters":0},"normal_package_reward_reader_unlock":{"ad_location":"normal_package_reward_reader_unlock","ad_location_name":"普通包阅读解锁章节","ad_type":"rewarded","status":1,"reward_type":"","reward_num":0,"times":1,"product_id":["10018"],"interval_duration":0,"page_interval":0,"page_turning_cooldown_time":0,"ads_unlock_chapter_count":2,"non_advertising_chapters":[],"no_ad_chapters":0}}
     * config_version : d31f05782173d0a76af51ca862ea305d
     */

    private AdConfigBean ad_config;
    private String config_version;

    public AdConfigBean getAd_config() {
        return ad_config;
    }

    public void setAd_config(AdConfigBean ad_config) {
        this.ad_config = ad_config;
    }

    public String getConfig_version() {
        return config_version;
    }

    public void setConfig_version(String config_version) {
        this.config_version = config_version;
    }

}
