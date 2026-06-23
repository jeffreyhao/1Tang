package com.base.abs;

public interface ICache {

    String getBookChapterVersion(String bookId);

    void saveBookChapterVersion(String bookId, String bookVersion);

    void saveReadRecord(String bookId, String comBookId, int readPosition);
}
