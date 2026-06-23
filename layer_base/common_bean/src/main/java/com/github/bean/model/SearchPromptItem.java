package com.github.bean.model;

import com.fold.recyclyerview.entity.MultiItemEntity;

/**
 * Created by haojiangfeng on 2025/12/8.
 */
public interface SearchPromptItem extends MultiItemEntity {

    @SearchPrompt.ItemType
    int getItemType();

    String getItemText();

}
