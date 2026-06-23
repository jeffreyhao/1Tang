package com.base.api;

import com.base.abs.IRequestUrl;

public class UrlApi {

    public static IRequestUrl iUrl;


    public static void reset() {
        if(iUrl != null){
            iUrl.reset();
        }
    }


    public static String shortBookListUrl() {
        if(iUrl != null){
            return iUrl.shortBookListUrl();
        }
        return "";
    }

    public static String bookDetailUrl(){
        if(iUrl != null){
            return iUrl.bookDetailUrl();
        }
        return "";
    }

    public static String shortFirstChaptersUrl(){
        if(iUrl != null){
            return iUrl.shortFirstChaptersUrl();
        }
        return "";
    }

    public static String chapterDirUrl(){
        if(iUrl != null){
            return iUrl.chapterDirUrl();
        }
        return "";
    }


    public static String checkChapterUrl(){
        if(iUrl != null){
            return iUrl.checkChapterUrl();
        }
        return "";
    }

    public static String chapterContentUrl(){
        if(iUrl != null){
            return iUrl.chapterContentUrl();
        }
        return "";
    }

    public static String chapterDirAllUrl(){
        if(iUrl != null){
            return iUrl.chapterDirAllUrl();
        }
        return "";
    }

    public static String chapterDirPageUrl(){
        if(iUrl != null){
            return iUrl.chapterDirPageUrl();
        }
        return "";
    }

    public static String freeChapterListUrl() {
        if(iUrl != null){
            return iUrl.freeChapterListUrl();
        }
        return "";
    }

    public static String chapterListUrl() {
        if(iUrl != null){
            return iUrl.chapterListUrl();
        }
        return "";
    }

    public static String purchaseChapterUrl() {
        if(iUrl != null){
            return iUrl.purchaseChapterUrl();
        }
        return "";
    }
}
