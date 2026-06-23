package com.github.bean.operation;

import com.google.gson.annotations.SerializedName;
import com.github.bean.model.BookStore;

import java.util.List;

public class RankListBean {
    @SerializedName("rankings_title")
    public List<String> rankingTitle;
    @SerializedName("rankings_book_list")
    public List<BookStore.DataBean> rankingBooksList;
    @SerializedName("books_list")
    public List<BookStore.DataBean> booksList;
    @SerializedName("ongoing_list")
    public List<BookStore.DataBean> onGoingList;
    @SerializedName("complete_list")
    public List<BookStore.DataBean> completeList;
}
