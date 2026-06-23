package com.base.api;

import com.base.abs.INight;
import com.base.abs.IReader;

public class ReaderApi {

    public static IReader iReader;


    public static boolean isNightMode(){
        if(iReader != null){
            return iReader.isNightMode();
        }
        return false;
    }


    public static void setNightMode(boolean isNightMode) {
        if(iReader != null){
            iReader.setNightMode(isNightMode);
        }
    }

    public static void saveReadingRecord(Object readingRecord) {
        if(iReader != null){
            iReader.saveReadingRecord(readingRecord);
        }
    }

    public static void saveReadHistory(String bookId, String comBookId, String extBookId, String bookName, String bookCover, String chapterName, int chapterPos, boolean isShort) {
        if(iReader != null) {
            iReader.saveReadHistory(bookId, comBookId, extBookId, bookName, bookCover, chapterName, chapterPos, isShort);
        }
    }
}
