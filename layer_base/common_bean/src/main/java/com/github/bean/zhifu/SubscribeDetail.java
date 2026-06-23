package com.github.bean.zhifu;

import com.base.util.collection.ListUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haojiangfeng on 2025/2/12.
 */
public class SubscribeDetail implements Serializable {


    /**
     * recharge_money : 6.4
     * task_desc : $19.9/week
     * task_id : 96
     * task_type : 2
     * specific_type : 2
     * claim_bonux_distribution : [840,840,840,840,840,840,840]
     * create_time : 2025-02-14 17:56:14
     * update_time : 2025-02-14 17:56:17
     * recharge_face : 200
     * recharge_give : 100
     * recharge_type : 3
     * favourable_percent : 50
     * platform_product_id : test_subscription_week_19.99
     * activity_cycle : 7
     * recharge_platform : [1]
     * claim_bonus_record : []
     * is_expired: 0
     * collection_record : [0,0,0]
     * invalid_time : 2025-02-24 10:53:10
     */



    /** 充值金额 **/
    @SerializedName("recharge_money")
    private double recharge_money;

    /** 活动描述 **/
    @SerializedName("task_desc")
    private String task_desc;

    /** 任务 ID **/
    @SerializedName("task_id")
    private int task_id;

    /** 任务类型，0 每日任务，1 新用户任务，2 订阅任务 **/
    @SerializedName("task_type")
    private int task_type;

    /** 具体的任务类型，2 代表订阅任务 **/
    @SerializedName("specific_type")
    private int specific_type;

    /** 奖励分配方式 **/
    @SerializedName("claim_bonux_distribution")
    private List<Integer> claim_bonux_distribution;

    /** 创建时间 **/
    @SerializedName("create_time")
    private String create_time;

    /** 更新时间 **/
    @SerializedName("update_time")
    private String update_time;

    /** 给定的金币个数 **/
    @SerializedName("recharge_face")
    private int recharge_face;

    /** 赠送的奖励个数 **/
    @SerializedName("recharge_give")
    private int recharge_give;

    /** 充值类型为订阅活动 **/
    @SerializedName("recharge_type")
    private int recharge_type;

    /** 活动赠送百分比 **/
    @SerializedName("favourable_percent")
    private int favourable_percent;

    /** 平台商品 ID **/
    @SerializedName("platform_product_id")
    private String platform_product_id;

    /** 活动周期，单位为天 **/
    @SerializedName("activity_cycle")
    private String activity_cycle;

    /** 充值平台，1 谷歌 GooglePay，5 苹果 ios 支付 **/
    @SerializedName("recharge_platform")
    private List<Integer> recharge_platform;

    /** 订阅活动失效时间 **/
    @SerializedName("invalid_time")
    private String invalid_time;


    /** 订阅活动收集记录 **/
    @SerializedName("collection_record")
    private List<Integer> collection_record;

    /** 订阅活动是否过期 **/
    @SerializedName("is_expired")
    private int is_expired;

    /** 订阅奖金领取记录 **/
//    @SerializedName("claim_bonus_record")
//    private List<SubscribeDailyReward> claim_bonus_record;

    public double getRecharge_money() {
        return recharge_money;
    }

    public void setRecharge_money(double recharge_money) {
        this.recharge_money = recharge_money;
    }

    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public int getSpecific_type() {
        return specific_type;
    }

    public void setSpecific_type(int specific_type) {
        this.specific_type = specific_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getRecharge_face() {
        return recharge_face;
    }

    public void setRecharge_face(int recharge_face) {
        this.recharge_face = recharge_face;
    }

    public int getRecharge_give() {
        return recharge_give;
    }

    public void setRecharge_give(int recharge_give) {
        this.recharge_give = recharge_give;
    }

    public int getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(int recharge_type) {
        this.recharge_type = recharge_type;
    }

    public int getFavourable_percent() {
        return favourable_percent;
    }

    public void setFavourable_percent(int favourable_percent) {
        this.favourable_percent = favourable_percent;
    }

    public String getPlatform_product_id() {
        return platform_product_id;
    }

    public void setPlatform_product_id(String platform_product_id) {
        this.platform_product_id = platform_product_id;
    }

    public String getActivity_cycle() {
        return activity_cycle;
    }

    public void setActivity_cycle(String activity_cycle) {
        this.activity_cycle = activity_cycle;
    }

    public String getInvalid_time() {
        return invalid_time;
    }

    public void setInvalid_time(String invalid_time) {
        this.invalid_time = invalid_time;
    }

    public List<Integer> getClaim_bonux_distribution() {
        return claim_bonux_distribution;
    }

    public void setClaim_bonux_distribution(List<Integer> claim_bonux_distribution) {
        this.claim_bonux_distribution = claim_bonux_distribution;
    }

    public List<Integer> getRecharge_platform() {
        return recharge_platform;
    }

    public void setRecharge_platform(List<Integer> recharge_platform) {
        this.recharge_platform = recharge_platform;
    }

    public int getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(int is_expired) {
        this.is_expired = is_expired;
    }

    public List<Integer> getCollection_record() {
        return collection_record;
    }

    public void setCollection_record(List<Integer> collection_record) {
        this.collection_record = collection_record;
    }


//    public String getClaim_bonus_record() {
//        return claim_bonus_record;
//    }
//
//    public void setClaim_bonus_record(String claim_bonus_record) {
//        this.claim_bonus_record = claim_bonus_record;
//    }


    @Override
    public String toString() {
        return "SubscribeDetail{" +
                "recharge_money=" + recharge_money +
                ", task_desc='" + task_desc + '\'' +
                ", task_id=" + task_id +
                ", task_type=" + task_type +
                ", specific_type=" + specific_type +
                ", claim_bonux_distribution=" + claim_bonux_distribution +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", recharge_face=" + recharge_face +
                ", recharge_give=" + recharge_give +
                ", recharge_type=" + recharge_type +
                ", favourable_percent=" + favourable_percent +
                ", platform_product_id='" + platform_product_id + '\'' +
                ", activity_cycle='" + activity_cycle + '\'' +
                ", recharge_platform=" + recharge_platform +
                ", invalid_time='" + invalid_time + '\'' +
                ", collection_record=" + collection_record +
                ", is_expired=" + is_expired +
                '}';
    }

    public int getActivityCycleInt() {
        try {
            return Integer.parseInt(activity_cycle);
        } catch (Throwable e){
            return 7;
        }
    }

    private boolean isCurrentDayReceived = false;
    private boolean isOutOfDate = false;

    public List<SubscribeDailyReward> buildDailyList(){
        List<SubscribeDailyReward> rewards = new ArrayList<>();
        if(ListUtil.isEmpty(getClaim_bonux_distribution())){
            isOutOfDate = true;
            return rewards;
        }

        int cycle = getActivityCycleInt();
        int bonus = 0;
        for(int i = 0; i < cycle; i++){
            SubscribeDailyReward reward = new SubscribeDailyReward();
            reward.day = i+1;
            if(i < getClaim_bonux_distribution().size()){
                if(bonus == 0){
                    bonus = getClaim_bonux_distribution().get(i);
                }
                reward.bonus = "+" + getClaim_bonux_distribution().get(i);
            } else {
                reward.bonus = "+" + bonus;
            }
            if(i < getCollection_record().size()){
                reward.state = getCollection_record().get(i);
                if(i == getCollection_record().size() - 1){ // 当天的
                    reward.checked = true;
                    if(reward.state == SubscribeDailyReward.STATE_RECEIVED){
                        isCurrentDayReceived = true;
                    } else {
                        isCurrentDayReceived = false;
                        reward.state = SubscribeDailyReward.TYPE_NOT_ARRIVED;
                    }
                }
            } else {
                reward.state = SubscribeDailyReward.TYPE_NOT_ARRIVED;
            }
            rewards.add(reward);
        }
        return rewards;
    }

    public int getDailyBonus(){
        if(ListUtil.isNotEmpty(claim_bonux_distribution)){
            return claim_bonux_distribution.get(0);
        } else {
            return 0;
        }
    }

    public int getActivityBonus(){
        if(ListUtil.isNotEmpty(claim_bonux_distribution)){
            int total = 0;
            for(int bonus : claim_bonux_distribution){
                total += bonus;
            }
            return total;
        } else {
            return 0;
        }
    }

    /**
     * 注意：必须在 buildDailyList 之后调用。
     */
    public boolean isCurrentDayReceived(){
        return isCurrentDayReceived;
    }

    /**
     * 所有的奖励 = 活动奖励 + 支付奖励
     */
    public int getTotalBonus(){
        return getActivityBonus() + recharge_give;
    }

    /**
     * 注意：必须在 buildDailyList 之后调用。
     */
    public boolean isOutOfDate(){
        return isOutOfDate;
    }

    public boolean isExpired() {
        return is_expired == 1;
    }

}
