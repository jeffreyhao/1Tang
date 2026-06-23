package com.github.bean.book;

public class BookAnalyticsBean {
    private String book_id;
    private String book_name;

    public BookAnalyticsBean(String book_id, String book_name) {
        this.book_id = book_id;
        this.book_name = book_name;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }
}
