package com.github.bean.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by haojiangfeng on 2025/12/8.
 */
public class SearchCategory implements SearchPromptItem {

    @SerializedName("category_id")
    public String categoryId;

    @SerializedName("category_name")
    public String categoryName;

    @Override
    public int getItemType() {
        return SearchPrompt.TYPE_CATEGORY;
    }

    @Override
    public String getItemText() {
        return categoryName;
    }

}
