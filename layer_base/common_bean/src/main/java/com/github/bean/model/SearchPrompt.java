package com.github.bean.model;

import com.base.util.collection.ListUtil;
import com.github.bean.database.table.BookInfo;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2025/12/8.
 */
public class SearchPrompt {


    public static final int TYPE_CATEGORY   = 1;
    public static final int TYPE_BOOK       = 2;

    @IntDef({
            SearchPrompt.TYPE_CATEGORY,
            SearchPrompt.TYPE_BOOK
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemType {

    }



    @SerializedName("category_list")
    public List<SearchCategory> categoryList;

//    @SerializedName("tag_list")
//    public List tagList;

    @SerializedName("book_list")
    public List<BookInfo> bookList;



    public List<SearchPromptItem> buildList(){
        List<SearchPromptItem> list = new ArrayList<>();
        if(ListUtil.isEmpty(categoryList) && ListUtil.isEmpty(bookList)){
            return list;
        }
        if(categoryList != null) {
            list.addAll(categoryList);
        }
        if(bookList != null) {
            list.addAll(bookList);
        }
        return list;
    }
}
