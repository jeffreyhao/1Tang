package com.base.abs;

public interface IReader {

    void setNightMode(boolean isNightMode);

    boolean isNightMode();

    void saveReadingRecord(Object readingRecord);

    void saveReadHistory(String bookId, String comBookId, String extBookId, String bookName, String bookCover, String chapterName, int chapterPos, boolean isShort);
}
