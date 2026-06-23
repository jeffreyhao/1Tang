package com.github.bean.model;

import androidx.annotation.Nullable;

public class CategoryItem {
    @Nullable
    private String background;
    private int id;
    private String name;
    private String book_category_id;
    private String tag_category_id; // lycannovel使用
    private String book_category;
    @Nullable
    private String parent_id;

    public CategoryItem(@Nullable String background, int id, String name, String book_category_id,
                        String tag_category_id, String book_category, @Nullable String parent_id) {
        this.background = background;
        this.id = id;
        this.name = name;
        this.book_category_id = book_category_id;
        this.tag_category_id = tag_category_id;
        this.book_category = book_category;
        this.parent_id = parent_id;
    }

    @Nullable
    public String getBackground() {
        return background;
    }

    public void setBackground(@Nullable String background) {
        this.background = background;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBook_category_id() {
        return book_category_id;
    }

    public void setBook_category_id(String book_category_id) {
        this.book_category_id = book_category_id;
    }

    public String getTag_category_id() {
        return tag_category_id;
    }

    public void setTag_category_id(String tag_category_id) {
        this.tag_category_id = tag_category_id;
    }

    public String getBook_category() {
        return book_category;
    }

    public void setBook_category(String book_category) {
        this.book_category = book_category;
    }

    @Nullable
    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(@Nullable String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCategoryId() {
        return book_category_id;
    }

    public String getCategoryName() {
        // Kotlin 代码中这里返回的是 book_category_id，可能是笔误。
        // 如果你希望返回 name，这里改为 return name; 否则保持如下。
        return book_category_id;
    }
}
