package com.base.api;

import com.base.abs.IBook;

public class BookApi {

    public static IBook iBook;


    public static void addToBookShelf(String bookId, String comBookId, String extBookId, String bookTitle, String bookCover, Object latestChapter, boolean isShort) {
        if(iBook != null) {
            iBook.addToBookShelf(bookId, comBookId, extBookId, bookTitle, bookCover, latestChapter, isShort);
        }
    }

    public static boolean isInBookShelf(String bookId) {
        if(iBook != null) {
            return iBook.isInBookShelf(bookId);
        }
        return false;
    }

    public static String getChapterFileName(String bookId, String chapterId, String updateTime){
        if(iBook != null) {
            return iBook.getChapterFileName(bookId, chapterId, updateTime);
        }
        return null;

    }

    public static void saveFile(String bookId, String chapterId, String time, Object responseBody){
        if(iBook != null) {
            iBook.saveFile(bookId, chapterId, time, responseBody);
        }
    }

    public static String readChapterContent(String bookId, String chapterId, String updateTime){
        if(iBook != null) {
            return iBook.readChapterContent(bookId, chapterId, updateTime);
        }
        return "";
    }

}
