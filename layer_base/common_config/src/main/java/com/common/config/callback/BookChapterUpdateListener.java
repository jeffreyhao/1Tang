package com.common.config.callback;

import com.github.bean.database.table.BookChapter;

import java.util.List;

/**
 * Time: 2023/12/30
 * Author: lhc
 * Desc:
 */
public interface BookChapterUpdateListener {
    void onGetChapterList(String bookId, List<BookChapter> list);
    void notifyAddChapter(String bookId, List<BookChapter> list);
    void notifyGetChapterListError();
}
