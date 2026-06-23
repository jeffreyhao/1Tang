package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

public class AutoZhifuBean {
    @SerializedName("author_name")
    public String authorName;
    @SerializedName("autobuy_status")
    public int autoBuyStatus;
    @SerializedName("book_id")
    public String bid;
    @SerializedName("com_book_id")
    public String comBookId;
    @SerializedName("book_title")
    public String bookName;
    @SerializedName("cover")
    public String cover;
}