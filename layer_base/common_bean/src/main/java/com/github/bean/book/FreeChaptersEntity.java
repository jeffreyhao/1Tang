package com.github.bean.book;


import com.github.bean.database.table.BookChapter;

import java.util.List;

public class FreeChaptersEntity {
    private int count;
    private List<BookChapter> chapters;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BookChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<BookChapter> chapters) {
        this.chapters = chapters;
    }
}
