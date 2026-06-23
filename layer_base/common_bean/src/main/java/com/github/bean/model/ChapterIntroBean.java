package com.github.bean.model;

import com.google.gson.annotations.SerializedName;
import com.github.bean.qianbao.RechargePlatformBean;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChapterIntroBean implements Serializable {

    @SerializedName("bid")
    public String bid;
    @SerializedName("bonus")
    public int bonus;
    @SerializedName("cid")
    public String cid;
    @SerializedName("conins")
    public int conins;
    @SerializedName("brief")
    public String content;
    @SerializedName("cs_chapter_num")
    public int csChapterNum;
    @SerializedName("cs_discount")
    public double csDiscount;
    @SerializedName("cs_id")
    public int csId;
    @SerializedName("cs_need_pay")
    public int csNeedPay;
    @SerializedName("discount")
    public int discount;
    @SerializedName("free")
    public boolean free;
    @SerializedName("id")
    public int id;
    @SerializedName("min_amount")
    public int minAmount;
    @SerializedName("money")
    public String money;
    @SerializedName("need_pay")
    public boolean needPay;
    @SerializedName("not_enough")
    public boolean notEnough;

    /** 现价 **/
    @SerializedName("present_price")
    public int price;

    /** 原价 **/
    @SerializedName("price")
    public int originalPrice;

    @SerializedName("recharge_id")
    public int rechargeId;

    @SerializedName("purchase_rule")
    public PurchaseRuleDTO purchaseRule;
    @SerializedName("recommend_recharge_rule")
    public RecommendRechargeRuleDTO recommendRechargeRule;
    @SerializedName("default_recharge")
    public RechargePlatformBean defaultRecharge;
    @SerializedName("recharge_rules")
    public List<RechargeRulesBean> rechargeRules;
    @SerializedName("title")
    public String title;

    @SerializedName("is_paying")
    public int isPaying;

    /** 折扣（100为没有折扣） **/
    @SerializedName("use_discount")
    public int useDiscount;

    /**
     *  普通充值列表
     */
    @SerializedName("pay_list")
    public ArrayList<RechargePlatformBean> payList;
    @SerializedName("activity_info")
    public ArrayList<RechargePlatformBean> activityInfo;

    /**
     *  金币订阅充值列表
     */
    @SerializedName("sub_activity")
    public ArrayList<RechargePlatformBean> subActivity;

    /**
     *  会员订阅充值列表
     */
    @SerializedName("premium_info")
    public ArrayList<RechargePlatformBean> premiumInfo;

    @SerializedName("is_vip")
    public int isVip;


    public static class PurchaseRuleDTO implements Serializable{
        public int id;
        public int chapterCount;
        public String discountPercent;
        public int isShow;
    }

    public static class RecommendRechargeRuleDTO implements Serializable{
        public String id;
        public String rechargeMoney;
        public String rechargeFace;
        public int rechargeGive;
        public int rechargeType;
        public String favourablePercent;
        public int isShow;
        public int rechargePlatform;
        public String platformProductId;
    }


    public static class RechargeRulesBean implements Serializable{
        @SerializedName("discount")
        public int discount;
        @SerializedName("platform_product_id")
        public String platformProductId;
        @SerializedName("id")
        public int id;
        @SerializedName("is_show")
        public int isShow;
        @SerializedName("recharge_face")
        public int rechargeFace;
        @SerializedName("recharge_give")
        public int rechargeGive;
        @SerializedName("recharge_money")
        public float rechargeMoney;
        @SerializedName("recharge_type")
        public int rechargeType;
        @SerializedName("recharge_platform")
        public int recharge_platform;
        public double favourable_percent;

        @SerializedName("selected")
        public boolean checked;
        @SerializedName("is_hot")
        public boolean is_hot;
        public String productPrice;
    }
}