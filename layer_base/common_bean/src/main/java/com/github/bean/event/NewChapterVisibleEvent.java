package com.github.bean.event;

import com.github.bean.database.table.BookChapter;

public class NewChapterVisibleEvent {

    public BookChapter bookChapter;

    public NewChapterVisibleEvent(BookChapter bookChapter) {
        this.bookChapter = bookChapter;
    }

}
