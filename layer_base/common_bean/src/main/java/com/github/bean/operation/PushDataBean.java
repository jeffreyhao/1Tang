package com.github.bean.operation;

import com.google.gson.annotations.SerializedName;

public class PushDataBean {
    public static final String PUSH_OPEN_PAY = "0";
    public static final String PUSH_OPEN_READ = "1";
    public static final String PUSH_OPEN_REWARD = "2";
    public static final String PUSH_OPEN_BOOKSHELF = "3";
    @SerializedName("push_id")
    public String pushId;
    @SerializedName("push_type")
    public String pushType;
    @SerializedName("book_id")
    public String bookId;
    @SerializedName("external_book_id")
    public String externalBookId;
    @SerializedName("language")
    public String language;
    @SerializedName("push_title")
    public String title;
    @SerializedName("push_body")
    public String pushBody;
    @SerializedName("push_image")
    public String image;
}
