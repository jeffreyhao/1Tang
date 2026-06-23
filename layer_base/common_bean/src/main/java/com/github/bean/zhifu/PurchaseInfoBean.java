package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PurchaseInfoBean {
    @SerializedName("account_balance")
    public int accountBalance;
    @SerializedName("account_bonus")
    public int accountBonus;
    @SerializedName("account_coins")
    public int accountConins;
    @SerializedName("all_rest")
    public int allRest;
    @SerializedName("chapter_rules")
    public List<ChapterShowsBean> chapterShows;

    public static class ChapterShowsBean {
        @SerializedName("chapter_count")
        public int chapterNum;
        @SerializedName("chapter_ids")
        public List<Integer> chpaterIds;
        @SerializedName("chapter_indexes")
        public List<Integer> chapterIndexes;
        @SerializedName("discount_percent")
        public double discount;
        @SerializedName("id")
        public int id;
        @SerializedName("is_show")
        public int isShow;
        @SerializedName("not_enough")
        public int notEnough;
        @SerializedName("original_price")
        public int originalPrice;
        @SerializedName("present_price")
        public int presentPrice;
        @SerializedName("recommend_rule")
        public RecommendBean recommendBean;

        public static class RecommendBean {
            @SerializedName("favourable_percent")
            public double discount;
            @SerializedName("platform_product_id")
            public String platformProductId;
            @SerializedName("recharge_face")
            public int rechargeFace;
            @SerializedName("recharge_give")
            public int rechargeGive;
            @SerializedName("recharge_money")
            public String rechargeMoney;
            @SerializedName("recharge_diamond")
            public String rechargeDiamond;
            @SerializedName("recharge_rule_id")
            public int rechargeId;
        }
    }


    public List<PurchaseInfoBean.ChapterShowsBean> getReadPurchaseList(){
        if(chapterShows == null || chapterShows.isEmpty()) {
            return null;
        }

        // 按照chapterNum从小到大排列
        chapterShows.sort(Comparator.comparingInt(item -> item.chapterNum));

        // 限制展示个数
        List<PurchaseInfoBean.ChapterShowsBean> list = new ArrayList<>();
        int size = chapterShows.size();
        for(int i = 0; i < size; i++) {
            PurchaseInfoBean.ChapterShowsBean item = chapterShows.get(i);
            if(item.isShow == 0) {
                continue;
            }
            list.add(item);
        }

        int limitShowCount = 6;
        if(list.size() > limitShowCount) {
            // 保留前四个和最后两个，中间的删掉
            list.subList(4, list.size() - 2).clear();
        }
        return list;
    }

}