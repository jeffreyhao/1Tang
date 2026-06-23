package com.github.bean.operation;

import com.github.bean.database.table.BookInfo;

import java.util.List;

public class BookShelfRecommendBookBean {
    private String id;
    private int type;
    private List<BookInfo> books;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<BookInfo> getBooks() {
        return books;
    }

    public void setBooks(List<BookInfo> books) {
        this.books = books;
    }
}
