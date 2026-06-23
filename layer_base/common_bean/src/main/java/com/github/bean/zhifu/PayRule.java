package com.github.bean.zhifu;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.util.ui.ViewUtil;
import com.base.api.GlobalContext;
import com.base.util.collection.ListUtil;
import com.common.bean.R;
import com.github.bean.qianbao.RechargePlatformBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 支付档位
 */
public class PayRule implements Serializable, Parcelable {


    @SerializedName("id")
    public int id;

    /** Coins **/
    @SerializedName("recharge_face")
    public int rechargeFace;

    /** Bonus **/
    @SerializedName("recharge_give")
    public int rechargeGive;

    /** 价格 **/
    @SerializedName("recharge_money")
    public double rechargeMoney;

    /** 划线价格 **/
    @SerializedName("strikethrough_money")
    public double strikethroughMoney;

    /** 折扣 **/
    @SerializedName("discount")
    public int discount;

    @SerializedName("recharge_diamond")
    public int rechargeDiamond;
    @SerializedName("favourable_percent")
    public double favourablePercent;
    @SerializedName("platform_product_id")
    public String platformProductId;
    @SerializedName("is_show")
    public int isShow;
    @SerializedName("recharge_type")
    public int rechargeType;

    @SerializedName("recharge_platform")
    public int rechargePlatform;

    @SerializedName("platform_product_dec")
    public String platform_product_dec;

    @SerializedName("create_time")
    public String create_time;

    @SerializedName("update_time")
    public String update_time;

    @SerializedName("selected")
    public boolean selected;

    @SerializedName("is_hot")
    public boolean is_hot;

    @SerializedName("is_paying")
    public int is_paying;


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



    @SerializedName("subscribe_status")
    public boolean subscribe_status;
    @SerializedName("subscription_end_time")
    public String subscription_end_time;
    @SerializedName("is_first_purchase")
    public boolean is_first_purchase;
    @SerializedName("claim_bonux_distribution")
    public ArrayList<Integer> claimBonus_distribution;
    @SerializedName("activity_cycle")
    public int activity_cycle;
    /** 任务 ID **/
    @SerializedName("task_id")
    public String task_id;
    /** 任务类型，0 每日任务，1 新用户任务，2 订阅任务 **/
    @SerializedName("task_type")
    public int task_type;
    @SerializedName("task_desc")
    public String task_desc;
    /** 具体的任务类型，2 代表订阅任务 **/
    @SerializedName("specific_type")
    public int specific_type;


    @SerializedName("trial_days")
    public int trial_days;

    @SerializedName("trial_price")
    public double trial_price;

    @SerializedName("has_recharged_this_rule")
    public int has_recharged_this_rule;




    public long limitMills;

    public String productPrice;

    /**
     * 本地字段：所属的支付平台
     */
    public RechargePlatformBean bindPlatform;
    /**
     * 本地字段：所属的支付类型
     */
    public int bindPayClass;


    public PayRule() {

    }

    protected PayRule(Parcel in) {
        id = in.readInt();
        rechargeFace = in.readInt();
        rechargeGive = in.readInt();
        rechargeMoney = in.readDouble();
        discount = in.readInt();
        rechargeDiamond = in.readInt();
        favourablePercent = in.readDouble();
        platformProductId = in.readString();
        isShow = in.readInt();
        rechargeType = in.readInt();
        rechargePlatform = in.readInt();
        platform_product_dec = in.readString();
        create_time = in.readString();
        update_time = in.readString();
        selected = in.readByte() != 0;
        is_hot = in.readByte() != 0;
        is_paying = in.readInt();
        productPrice = in.readString();

        discount_id = in.readString();
        discount_price = in.readString();
        tips = in.readString();
        sort = in.readString();
        ad_info = in.readString();

        subscribe_status = in.readByte() != 0;
        subscription_end_time = in.readString();
        is_first_purchase = in.readByte() != 0;
        claimBonus_distribution = in.readArrayList(Integer.class.getClassLoader());
        activity_cycle = in.readInt();
        task_id = in.readString();
        task_type = in.readInt();
        task_desc = in.readString();
        specific_type = in.readInt();
        limitMills = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(rechargeFace);
        dest.writeInt(rechargeGive);
        dest.writeDouble(rechargeMoney);
        dest.writeInt(discount);
        dest.writeInt(rechargeDiamond);
        dest.writeDouble(favourablePercent);
        dest.writeString(platformProductId);
        dest.writeInt(isShow);
        dest.writeInt(rechargeType);
        dest.writeInt(rechargePlatform);
        dest.writeString(platform_product_dec);
        dest.writeString(create_time);
        dest.writeString(update_time);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeByte((byte) (is_hot ? 1 : 0));
        dest.writeInt(is_paying);
        dest.writeString(productPrice);

        dest.writeString(discount_id);
        dest.writeString(discount_price);
        dest.writeString(tips);
        dest.writeString(sort);
        dest.writeString(ad_info);

        dest.writeByte((byte) (subscribe_status ? 1 : 0));
        dest.writeString(subscription_end_time);
        dest.writeByte((byte) (is_first_purchase ? 1 : 0));
        dest.writeList(claimBonus_distribution);
        dest.writeInt(activity_cycle);
        dest.writeString(task_id);
        dest.writeInt(task_type);
        dest.writeString(task_desc);
        dest.writeInt(specific_type);
        dest.writeLong(limitMills);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PayRule> CREATOR = new Creator<PayRule>() {
        @Override
        public PayRule createFromParcel(Parcel in) {
            return new PayRule(in);
        }

        @Override
        public PayRule[] newArray(int size) {
            return new PayRule[size];
        }
    };



    public void setLimitMills(long mills) {
        this.limitMills = mills;
    }


    public long getLimitConfig(){
        try {
            if(ad_info == null || ad_info.isEmpty() || ad_info.equalsIgnoreCase("null")){
                return 0;
            }
            return Long.parseLong(ad_info);
        } catch (Throwable e){
            ALog.exception(e);
        }
        return 0;
    }


    public boolean isValid(){
        return id != 0 && rechargeMoney != 0;
    }



    public PayRule copy() {
        try {
            PayRule newBean = (PayRule) this.clone();
            newBean.selected = false;
            return newBean;
        } catch (Throwable e) {
            LogUtil.e(e);
            return new PayRule();
        }
    }

    public boolean isSubscribed(){
        return subscribe_status;
    }

    public int getFavourablePercentInt() {
        return (int) favourablePercent;
    }

    public int getActivityBonus(){
        if(ListUtil.isNotEmpty(claimBonus_distribution)){
            int total = 0;
            for(int bonus : claimBonus_distribution){
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
        if(ListUtil.isNotEmpty(claimBonus_distribution)){
            return claimBonus_distribution.get(0);
        } else {
            return 0;
        }
    }

    /**
     * 所有的奖励 = 活动奖励 + 支付奖励
     */
    public int getTotalBonus(){
        return getActivityBonus() + rechargeGive;
    }

    /**
     * 全部可获得的金币 = 支付金币 + 支付奖励 + 活动奖励
     */
    public int getTotalCoinsBonus(){
        return rechargeFace + rechargeGive + getActivityBonus();
    }

    public String getShowPrice(Context context){
        if (!TextUtils.isEmpty(productPrice)) {
            return productPrice + getCycleString(context);
        } else {
            return "US$" + rechargeMoney + getCycleString(context);
        }
    }

    public String getOriginalPrice(Context context){
        if (!TextUtils.isEmpty(productPrice)) {
            return productPrice + getCycleString(context);
        } else {
            return "US$" + rechargeMoney + getCycleString(context);
        }
    }

    public String getNonNullTaskId() {
        return task_id == null ? "" : task_id;
    }

    public String ruleInfo(){
        return id + "&" + platform_product_dec;
    }

    public int getFirstGive(){
        return rechargeGive + getDailyBonus();
    }


    public String getNovelDailyPrice(){
        String discountPrice = "";
        if(productPrice != null) {
            if(productPrice.contains("-")) {
                String[] priceArr = productPrice.split("-");
                if(priceArr.length > 1) {
                    discountPrice = priceArr[1];
                }
            }else{
                discountPrice = productPrice;
            }
        }
        if (TextUtils.isEmpty(discountPrice)) {
            discountPrice = "US$" + discount_price;
        }

        String period = "";
        String taskDesc = task_desc;
        if(!TextUtils.isEmpty(taskDesc)) {
            String[] split = taskDesc.split("\n");
            if(split.length > 1) {
                period = split[0];
            }
        }

        // discountPrice: US$2.99
        // discountPrice:1 week
        return discountPrice + "/" + period;
    }

    public String getNovelMusePrice(Context context){
        return "US$" + rechargeMoney + getCycleString(context);
    }

    public String getCycleString(Context context){
        switch (activity_cycle) {
            case 1:
                return context.getString(R.string.per_day);
            case 7:
                return context.getString(R.string.per_week);
            case 30:
                return context.getString(R.string.per_month);
            case 180:
            case 181:
            case 182:
            case 183:
                return context.getString(R.string.sevel_month, 6);
            case 360:
            case 365:
                return context.getString(R.string.per_year);
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

    public String getCycleString(){
        return getCycleString(GlobalContext.getContext());
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
                ? (hasFirstPurchaseDiscountPrice ? discountPrice() : rechargeMoney)
                : trial_price;
        return price;
    }

    public boolean hasRechargedRule(){
        return has_recharged_this_rule == 1;
    }

    public String toSimpleString() {
        return "PayRule{" +
                "platformProductId='" + platformProductId + '\'' +
                ", id=" + id +
                ", platform_product_dec='" + platform_product_dec + '\'' +
                ", rechargeFace=" + rechargeFace +
                ", rechargeGive=" + rechargeGive +
                ", rechargeMoney=" + rechargeMoney +
                ", rechargePlatform=" + rechargePlatform +
                '}';
    }
}