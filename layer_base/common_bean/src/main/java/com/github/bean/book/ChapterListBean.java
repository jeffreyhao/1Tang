package com.github.bean.book;

import com.github.bean.database.table.BookChapter;
import java.util.List;

public class ChapterListBean {

    private int chapter_count;
    private int read_chapter_index;
    private int read_chapter_page;
    private int read_finished;
    private List<BookChapter> chapters;

    public ChapterListBean() {
    }

    public ChapterListBean(int chapter_count, int read_chapter_index, int read_chapter_page, int read_finished, List<BookChapter> chapters) {
        this.chapter_count = chapter_count;
        this.read_chapter_index = read_chapter_index;
        this.read_chapter_page = read_chapter_page;
        this.read_finished = read_finished;
        this.chapters = chapters;
    }

    public int getChapter_count() {
        return chapter_count;
    }

    public void setChapter_count(int chapter_count) {
        this.chapter_count = chapter_count;
    }

    public int getRead_chapter_index() {
        return read_chapter_index;
    }

    public void setRead_chapter_index(int read_chapter_index) {
        this.read_chapter_index = read_chapter_index;
    }

    public int getRead_chapter_page() {
        return read_chapter_page;
    }

    public void setRead_chapter_page(int read_chapter_page) {
        this.read_chapter_page = read_chapter_page;
    }

    public int getRead_finished() {
        return read_finished;
    }

    public void setRead_finished(int read_finished) {
        this.read_finished = read_finished;
    }

    public List<BookChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<BookChapter> chapters) {
        this.chapters = chapters;
    }
}
