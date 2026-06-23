package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

/**
 * 订阅-领取奖金记录
 *
 * Created by haojiangfeng on 2025/2/14.
 */
public class SubscribeReceiveBean {

    /** 订阅活动名称 **/
    @SerializedName("task_desc")
    public String task_desc;

    /** 订阅领取奖金记录 **/
    @SerializedName("give_amount")
    public String give_amount;

    /** 领取时间 **/
    @SerializedName("local_obtain_time")
    public String local_obtain_time;

    /** 结束时间 **/
    @SerializedName("local_invalid_time")
    public String local_invalid_time;

}
