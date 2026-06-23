package com.benefit.event;

import com.github.bean.database.table.BookChapter;

public class ReadContentEvent {

    public boolean isLoadSuccess;
    public BookChapter chapter;

    public ReadContentEvent(boolean loadSuccess, BookChapter chapter) {
        this.isLoadSuccess = loadSuccess;
        this.chapter = chapter;
    }


}
