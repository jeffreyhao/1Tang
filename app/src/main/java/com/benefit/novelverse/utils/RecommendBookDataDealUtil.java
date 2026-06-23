package com.benefit.novelverse.utils;

import android.text.TextUtils;

import com.base.util.collection.ListUtil;
import com.base.util.content.GsonUtil;
import com.benefit.utils.CacheUtil;
import com.github.bean.database.table.BookInfo;
import com.github.bean.operation.BookShelfRecommendBookBean;

import java.util.ArrayList;
import java.util.List;

public class RecommendBookDataDealUtil {

    private static int mCurIndex = 0;

    public static void setRecommendBooks(List<BookShelfRecommendBookBean> list) {
        List<BookInfo> recommendBooks = new ArrayList<>();
        String recommendBookClickIds = CacheUtil.getRecommendBookClickIds();
        for(int i=0; i<list.size(); i++) {
            BookShelfRecommendBookBean recommendGroup = list.get(i);
            if(recommendGroup==null) continue;
            int type = recommendGroup.getType();
            if(ListUtil.isNotEmpty(recommendGroup.getBooks())) {
                for(int j=0; j<recommendGroup.getBooks().size(); j++) {
                    BookInfo bookInfo = recommendGroup.getBooks().get(j);
                    if(!recommendBookClickIds.contains(bookInfo.getBook_id())) {
                        bookInfo.setMarketing_type(type);
                        bookInfo.setRecommendBook(true);
                        recommendBooks.add(bookInfo);
                    }
                }
            }
        }
        if(ListUtil.isNotEmpty(recommendBooks)) {
            CacheUtil.saveBookShelfRecommendBooks(GsonUtil.bean2json(recommendBooks));
        }
    }

    public static BookInfo getRecommendBook() {
        List<BookInfo> recommendBooks = CacheUtil.getBookShelfRecommendBooks();
        if(ListUtil.isEmpty(recommendBooks)) return null;
        if(mCurIndex>=recommendBooks.size()) {
            mCurIndex = 0;
        }
        BookInfo bookInfo = recommendBooks.get(mCurIndex);
        mCurIndex++;
        return bookInfo;
    }

    public static void removeRecommendBook(String bookId) {
        List<BookInfo> recommendBooks = CacheUtil.getBookShelfRecommendBooks();
        for(int i=0; i<recommendBooks.size(); i++) {
            if(TextUtils.equals(bookId,recommendBooks.get(i).getBook_id())) {
                recommendBooks.remove(i);
                break;
            }
        }
        CacheUtil.saveBookShelfRecommendBooks(GsonUtil.bean2json(recommendBooks));
        CacheUtil.saveRecommendBookClickId(bookId);
    }

    public static void clearRecommendBook() {
        CacheUtil.saveBookShelfRecommendBooks("");
        CacheUtil.saveRecommendBookClickId("");
    }
}
