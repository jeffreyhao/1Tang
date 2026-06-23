package com.github.bean.adv;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 广告配置项Bean
 */
public class AdConfigItemBean implements Serializable {

    /** 广告位置（唯一标识），如 reward_task_center、native_chapter_end 等 */
    @SerializedName("ad_location")
    public String adLocation;

    /** 广告位置说明，如 "任务中心"、"阅读器章节结尾" 等 */
    @SerializedName("ad_location_name")
    public String adLocationName;

    /**
     * 广告类型
     * <ul>
     *     <li>rewarded - 激励广告</li>
     *     <li>appopen - 开屏广告</li>
     *     <li>banner - 横幅广告</li>
     *     <li>native - 原生广告</li>
     *     <li>interstitial - 插页广告</li>
     * </ul>
     */
    @SerializedName("ad_type")
    public String adType;

    /** 状态：1-开启，0-关闭 */
    @SerializedName("status")
    public int status;

    /** 奖励类型：time-时长，coins-金币 */
    @SerializedName("reward_type")
    public String rewardType;

    /** 奖励数量 */
    @SerializedName("reward_num")
    public int rewardNum;

    /** 广告次数/章节数量：次数限制（每日最多观看广告次数），或1天解锁的章节数（用户本地时间） */
    @SerializedName("times")
    public int times;

    /** 产品ID：all-全部开启，0-全部关闭 */
    @SerializedName("product_id")
    public List<?> productId;

    /** 广告间隔时长（单位：秒） */
    @SerializedName("interval_duration")
    public int intervalDuration;

    /** 间隔翻页数 */
    @SerializedName("page_interval")
    public int pageInterval;

    /** 翻页冷却时长（单位：秒） */
    @SerializedName("page_turning_cooldown_time")
    public int pageTurningCooldownTime;

    /** 广告数量：解锁1个章节需要的广告数 */
    @SerializedName("ads_unlock_chapter_count")
    public int unlockChapterAdsCount;

    /** 非广告章节，不展示广告的章节区间 */
    @SerializedName("non_advertising_chapters")
    public List<?> nonAdvertisingChapters;

    /** 无广告/加载异常：1天解锁的最多章节数 */
    @SerializedName("no_ad_chapters")
    public int noAdChapters;

    @SerializedName("recharge_rewarded_ad_click_limit")
    public int rechargeRewardedAdClickLimit;


}
