package com.benefit.event;

import com.github.bean.database.table.BookInfo;

/**
 * Time: 2023/11/25
 * Author: lhc
 * Desc:
 */
public class RefreshCatalogBookInfoEvent {
    private BookInfo bookInfo;

    public RefreshCatalogBookInfoEvent() {
    }

    public RefreshCatalogBookInfoEvent(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
}
