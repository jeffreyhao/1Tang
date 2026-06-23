package com.github.bean.database;

import android.text.TextUtils;

import com.baidu.baselibrary.util.json.GsonUtils;
import com.github.bean.database.table.ReadHistory;
import com.github.bean.database.table.ReadHistory_Table;
import com.github.bean.database.table.BookInfo;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理器
 */
public class DataBaseManager {
    private final static DataBaseManager dataBaseManager = new DataBaseManager();
    private DataBaseManager() { }
    /**
     * 单例
     *
     * @return 类对象
     */
    public static DataBaseManager getInstance() {
        return dataBaseManager;
    }

    /**
     * 保存阅读历史
     *
     * @param bookInfo 书籍信息
     */
    public void saveReadHistory(BookInfo bookInfo, String chapterName, int chapterPos) {
        if (null == bookInfo) {
            return;
        }
        ReadHistory readHistory = new ReadHistory();
        readHistory.bookId = bookInfo.getBook_id();
        readHistory.comBookId = bookInfo.getCom_book_id();
        readHistory.bookInfo = bookInfo;
        readHistory.readTime = System.currentTimeMillis();
        readHistory.chapterName = chapterName;
        readHistory.chapterPos = chapterPos;
        readHistory.save();
    }

    public void saveReadHistory(String bookId, String comBookId, String extBookId, String bookName, String bookCover, String chapterName, int chapterPos, boolean isShort) {
        ReadHistory readHistory = new ReadHistory();
        readHistory.bookId = bookId;
        readHistory.comBookId = bookId;
        BookInfo book = new BookInfo();
        book.setBook_id(bookId);
        book.setCom_book_id(comBookId);
        book.setExternal_cp_book_id(extBookId);
        book.setTitle(bookName);
        book.setCover(bookCover);
        book.setIs_short_story(isShort ? 1 : 0);
        readHistory.bookInfo = book;
        readHistory.readTime = System.currentTimeMillis();
        readHistory.chapterName = chapterName;
        readHistory.chapterPos = chapterPos;
        readHistory.save();
    }

    /**
     * 按照阅读时间降序排列获取阅读历史列表
     *
     * @return 阅读历史列表
     */
    public List<ReadHistory> getReadHistory() {
        List<ReadHistory> historyList = SQLite.select()
                .from(ReadHistory.class)
                .orderBy(ReadHistory_Table.readTime, false)
                .queryList();
        List<ReadHistory> tempList = new ArrayList<>();
        for (ReadHistory readHistory : historyList) {
            if (!TextUtils.isEmpty(readHistory.bookInfoJson)) {
                readHistory.bookInfo = GsonUtils.fromJson(readHistory.bookInfoJson, BookInfo.class);
                if(DbUtil.shouldHideBook(readHistory.bookId)) {
                    readHistory.delete();
                }else{
                    if(readHistory.bookInfo!=null&&!TextUtils.isEmpty(readHistory.bookInfo.getCover())) {
                        tempList.add(readHistory);
                    }
                }
            }
        }
        return tempList;
    }

    /**
     * 检查书籍是否在书架中
     *
     * @param bookId 书籍id
     * @return 是否
     */
    public boolean isBookInLibrary(String bookId) {
        List<BookInfo> result = SQLite.select()
                .from(BookInfo.class)
                .queryList();
        boolean inLibrary = false;
        for (BookInfo info : result) {
            if (info.getBook_id().equals(bookId)) {
                inLibrary = true;
                break;
            }
        }
        return inLibrary;
    }
}