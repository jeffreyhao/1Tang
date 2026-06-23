package com.github.bean.zhifu;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.api.GlobalContext;
import com.base.util.collection.ListUtil;
import com.common.bean.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haojiangfeng on 2025/2/10.
 */
public class SubscribePayBean implements Serializable, Cloneable {

    @SerializedName("id")
    public int id;

    @SerializedName("recharge_money")
    public double recharge_money;

    @SerializedName("recharge_face")
    public int recharge_face;

    @SerializedName("recharge_give")
    public int recharge_give;
    @SerializedName("recharge_diamond")
    public int recharge_diamond;
    @SerializedName("recharge_type")
    public int recharge_type;

    @SerializedName("favourable_percent")
    public int favourable_percent;

    @SerializedName("is_show")
    public int is_show;

    @SerializedName("recharge_platform")
    public int recharge_platform;

    @SerializedName("platform_product_id")
    public String platform_product_id;
    @SerializedName("platform_product_dec")
    public String platform_product_dec;

    @SerializedName("selected")
    public boolean selected;
    @SerializedName("create_time")
    public String create_time;
    @SerializedName("update_time")
    public String update_time;
    @SerializedName("is_paying")
    public int is_paying;
    @SerializedName("is_hot")
    public boolean is_hot;
    @SerializedName("subscribe_status")
    public boolean subscribe_status;
    @SerializedName("is_first_purchase")
    public boolean is_first_purchase;
    @SerializedName("subscription_end_time")
    public String subscription_end_time;
    @SerializedName("task_desc")
    public String task_desc;
    @SerializedName("claim_bonux_distribution")
    public List<Integer> claim_bonux_distribution;

    @SerializedName("activity_cycle")
    public int activity_cycle;

    /** 任务 ID **/
    @SerializedName("task_id")
    public String task_id;

    /** 任务类型，0 每日任务，1 新用户任务，2 订阅任务 **/
    @SerializedName("task_type")
    public int task_type;

    /** 具体的任务类型，2 代表订阅任务 **/
    @SerializedName("specific_type")
    public int specific_type;

    /** 会员首期折扣商品 (google 支付) **/
    @SerializedName("discount_id")
    public String discount_id;

    /** 会员首期折扣价格 **/
    @SerializedName("discount_price")
    public String discount_price;

    /** 角标信息 **/
    @SerializedName("tips")
    public String tips;

    /** 排序 **/
    @SerializedName("sort")
    public String sort;

    /** 广告语 **/
    @SerializedName("ad_info")
    public String ad_info;

    @SerializedName("checked")
    public boolean checked = false;

    /** 划线价格 **/
    @SerializedName("strikethrough_money")
    public double strikethroughMoney;

    @SerializedName("trial_days")
    public int trial_days;

    @SerializedName("trial_price")
    public double trial_price;

    @SerializedName("has_recharged_this_rule")
    public int has_recharged_this_rule;



    public String productPrice;

    public long limitMills;


    public SubscribePayBean copy() {
        try {
            SubscribePayBean newBean = (SubscribePayBean) this.clone();
            newBean.checked = false;
            return newBean;
        } catch (Throwable e) {
            LogUtil.e(e);
            return new SubscribePayBean();
        }
    }

    public boolean isSubscribed(){
        return subscribe_status;
    }

    public int getFavourablePercentInt() {
        return favourable_percent;
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
     * 每日奖励
     */
    public int getDailyBonus(){
        if(ListUtil.isNotEmpty(claim_bonux_distribution)){
            return claim_bonux_distribution.get(0);
        } else {
            return 0;
        }
    }

    /**
     * 所有的奖励 = 活动奖励 + 支付奖励
     */
    public int getTotalBonus(){
        return getActivityBonus() + recharge_give;
    }

    /**
     * 全部可获得的金币 = 支付金币 + 支付奖励 + 活动奖励
     */
    public int getTotalCoinsBonus(){
        return recharge_face + recharge_give + getActivityBonus();
    }

    public String getShowPrice(Context context){
        if (!TextUtils.isEmpty(productPrice)) {
            return productPrice + context.getString(R.string.per_week);
        } else {
            return "US$" + recharge_money + context.getString(R.string.per_week);
        }
    }

    public String getOriginalPrice(Context context){
        if (!TextUtils.isEmpty(productPrice)) {
            return productPrice + context.getString(R.string.per_week);
        } else {
            return "US$" + recharge_money + context.getString(R.string.per_week);
        }
    }

    public String getNonNullTaskId() {
        return task_id == null ? "" : task_id;
    }

    public String ruleInfo(){
        return id + "&" + platform_product_dec;
    }

    public void setLimitMills(long mills) {
        this.limitMills = mills;
    }

    public long getLimitConfig(){
        try {
            if(ad_info == null || ad_info.equals("") || ad_info.equalsIgnoreCase("null")){
                return 0;
            }
            long mills = Long.parseLong(ad_info);
            return mills;
        } catch (Throwable e){
            e.printStackTrace();
        }
        return 0;
    }

    public int getFirstGive(){
        return recharge_give + getDailyBonus();
    }

    public String getOriginPrice() {
        return strikethroughMoney == 0 ? "" : "US$" + strikethroughMoney;
    }


    public String getCycleString(){
        switch (activity_cycle) {
            case 1:
                return GlobalContext.getContext().getString(R.string.per_day);
            case 7:
                return GlobalContext.getContext().getString(R.string.per_week);
            case 30:
                return GlobalContext.getContext().getString(R.string.per_month);
            case 180:
            case 181:
            case 182:
            case 183:
                return GlobalContext.getContext().getString(R.string.sevel_month, 6);
            case 360:
            case 365:
                return GlobalContext.getContext().getString(R.string.per_year);
            default:
                return "";
        }
    }

    public String getPremiumCycleString(){
        switch (activity_cycle) {
            case 1:
                return GlobalContext.getContext().getString(R.string.daily);
            case 7:
                return GlobalContext.getContext().getString(R.string.weekly);
            case 30:
                return GlobalContext.getContext().getString(R.string.monthly);
            case 180:
            case 181:
            case 182:
            case 183:
                return GlobalContext.getContext().getString(R.string.sevel_month, 6);
            case 360:
            case 365:
                return GlobalContext.getContext().getString(R.string.yearly);
            default:
                return "";
        }
    }

    public double discountPrice(){
        try {
            return Double.parseDouble(discount_price);
        } catch (Throwable e) {
            return 0.0d;
        }
    }

    public double getPayPrice(){
        boolean hasFirstPurchaseDiscountPrice = is_first_purchase && !TextUtils.isEmpty(discount_price);
        double price = trial_price == 0
                ? (hasFirstPurchaseDiscountPrice ? discountPrice() : recharge_money)
                : trial_price;
        return price;
    }

    public boolean hasRechargedRule(){
        return has_recharged_this_rule == 1;
    }
}
