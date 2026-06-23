package com.benefit.novelverse.event;


import com.github.bean.database.table.BookChapter;

import java.util.List;

public class BatchPurchaseEvent {
    private List<BookChapter> chapters;

    public BatchPurchaseEvent(List<BookChapter> chapters) {
        this.chapters = chapters;
    }

    public List<BookChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<BookChapter> chapters) {
        this.chapters = chapters;
    }
}
