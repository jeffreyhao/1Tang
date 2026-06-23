package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayResultBean {
    @SerializedName("cashBalance")
    public double cashBalance;
    @SerializedName("coins")
    public int coins;
    @SerializedName("bonus")
    public int bonus;
    @SerializedName("diamonds")
    public int diamonds;
    @SerializedName("current_balance")
    public int currentBalance;
    @SerializedName("download_urls")
    public List<DownloadUrlBean> downloadUrls;
    @SerializedName("secondary_balance")
    public int secondaryBalance;
    @SerializedName("book_id")
    public String bid;
    @SerializedName("download_url")
    public String downloadUrl;
    @SerializedName("chapter_content")
    public String chapterContent;

    public class DownloadUrlBean {
        @SerializedName("id")
        public int id;
        @SerializedName("cid")
        public String cid;
        @SerializedName("chapter_id")
        public String chapterId;
        @SerializedName("index")
        public int index;
        @SerializedName("url")
        public String url;
        @SerializedName("title")
        public String title;
        @SerializedName("create_time")
        public String create_time;
        @SerializedName("update_time")
        public String update_time;
        @SerializedName("chapter_content")
        public String chapterContent;
    }


    @Override
    public String toString() {
        return "PayResultBean{" +
                "cashBalance=" + cashBalance +
                ", coins=" + coins +
                ", bonus=" + bonus +
                ", diamonds=" + diamonds +
                ", currentBalance=" + currentBalance +
                ", secondaryBalance=" + secondaryBalance +
                ", bid='" + bid + '\'' +
                ", chapterContent='" + chapterContent + '\'' +
                '}';
    }
}