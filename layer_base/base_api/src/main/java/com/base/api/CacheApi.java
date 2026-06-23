package com.base.api;

import com.base.abs.ICache;

public class CacheApi {

    public static ICache iCache;


    public static String getBookChapterVersion(String bookId) {
        if(iCache != null) {
            return iCache.getBookChapterVersion(bookId);
        }
        return null;
    }

    public static void saveBookChapterVersion(String bookId, String bookVersion) {
        if(iCache != null) {
            iCache.saveBookChapterVersion(bookId, bookVersion);
        }
    }

    public static void saveReadRecord(String bookId, String comBookId, int readPosition) {
        if(iCache != null) {
            iCache.saveReadRecord(bookId, comBookId, readPosition);
        }
    }



}
