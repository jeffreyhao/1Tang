package com.base.abs;


public interface IBook {

    void addToBookShelf(String bookId, String comBookId, String extBookId, String bookTitle, String bookCover, Object latestChapter, boolean isShort);

    boolean isInBookShelf(String bookId);

    String getChapterFileName(String bookId, String chapterId, String updateTime);

    void saveFile(String bookId, String chapterId, String time, Object responseBody);

    String readChapterContent(String bookId, String chapterId, String updateTime);

}
