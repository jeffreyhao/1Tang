package com.github.bean.book;

import com.github.bean.database.table.BookChapter;

import java.util.List;

/**
 * Time: 2023/12/31
 * Author: lhc
 * Desc:
 */
public class BookChapterDirectory {
    private List<BookChapter> directory;
    private int version;

    public List<BookChapter> getDirectory() {
        return directory;
    }

    public void setDirectory(List<BookChapter> directory) {
        this.directory = directory;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
