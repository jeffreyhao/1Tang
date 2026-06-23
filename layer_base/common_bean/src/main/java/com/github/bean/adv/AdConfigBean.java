package com.github.bean.adv;

import com.google.gson.annotations.SerializedName;

/**
 * 广告配置Bean
 * <p>
 * 包含各个广告位的配置信息
 */
public class AdConfigBean {

    /** 任务中心 - 激励视频广告 */
    @SerializedName("reward_task_center")
    public AdConfigItemBean rewardTaskCenter;

    /** 阅读器章节结尾 - 原生广告 */
    @SerializedName("native_chapter_end")
    public AdConfigItemBean nativeChapterEnd;

    /** 阅读解锁章节 - 激励视频广告 */
    @SerializedName("reward_reader_unlock")
    public AdConfigItemBean readerUnlock;

    /** 启动开屏 - 开屏广告 */
    @SerializedName("startup_screen")
    public AdConfigItemBean startupScreen;

    /** 阅读器底部banner - Banner广告 */
    @SerializedName("banner_reader_bottom")
    public AdConfigItemBean readerBanner;

    /** 书架底部banner - Banner广告 */
    @SerializedName("banner_library_bottom")
    public AdConfigItemBean libraryBanner;

    /** 阅读器翻页插入广告 - 原生广告 */
    @SerializedName("native_reader_content")
    public AdConfigItemBean nativeReaderContent;

    /** 书架内原生 - 原生广告 */
    @SerializedName("native_library")
    public AdConfigItemBean nativeLibrary;

    /** 阅读器翻章节插入广告 - 插屏广告 */
    @SerializedName("interstitial_chapter_turning")
    public AdConfigItemBean interstitialTurning;

    /** 普通包阅读解锁章节 - 激励视频广告 */
    @SerializedName("normal_package_reward_reader_unlock")
    public AdConfigItemBean normalUnlock;





}
