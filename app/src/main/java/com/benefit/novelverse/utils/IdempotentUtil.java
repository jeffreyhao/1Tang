package com.benefit.novelverse.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Time: 2023/11/30
 * Author: lhc
 * Desc:
 */
public class IdempotentUtil {
    public static List<String> bookIdList = new ArrayList<>();
    public static Map<String,Long> chapterBrowseCache = new HashMap<>();
    public static Set<String> chapterScrollBrowseCache = new HashSet<>();
    public static Set<String> chapterScrollCache = new HashSet<>();
    public static boolean canReportUserInfo = true;
    /**
     * 判断是否可以执行加入书架
     * @param bookId
     * @return
     */
    public static boolean canAddToBookShelf(String bookId) {
        if(bookIdList.contains(bookId)) {
            return false;
        }
        bookIdList.add(bookId);
        return true;
    }

    public static boolean canUploadChapterBrowseEvent(String bookId, int chapterIndex) {
        String cacheKey = bookId+"-"+chapterIndex;
        Long lastUploadTime = chapterBrowseCache.get(cacheKey);
        if(chapterBrowseCache.containsKey(cacheKey)
                &&lastUploadTime!=null
                &&(System.currentTimeMillis()-lastUploadTime<10000)) {
            return false;
        }
        chapterBrowseCache.put(cacheKey, System.currentTimeMillis());
        return true;
    }

    public static boolean canUploadFBChapterBrowseEvent(String bookId, int chapterIndex) {
        String cacheKey = bookId+"-"+chapterIndex;
        Long lastUploadTime = chapterBrowseCache.get(cacheKey);
        if(chapterBrowseCache.containsKey(cacheKey)
                &&lastUploadTime!=null
                &&(System.currentTimeMillis()-lastUploadTime<10000)) {
            return false;
        }
        chapterBrowseCache.put(cacheKey, System.currentTimeMillis());
        return true;
    }

    public static boolean canUploadChapterScrollBrowseEvent(String bookId, int chapterIndex) {
        String cacheKey = bookId+"-"+chapterIndex;
        if(chapterScrollBrowseCache.contains(cacheKey)) {
            return false;
        }
        chapterScrollBrowseCache.add(cacheKey);
        return true;
    }

    public static boolean canUploadChapterScrollEvent(String bookId, int chapterIndex) {
        String cacheKey = bookId+"-"+chapterIndex;
        if(chapterScrollCache.contains(cacheKey)) {
            return false;
        }
        chapterScrollCache.add(cacheKey);
        return true;
    }

    public static boolean isCanReportUserInfo() {
        if(canReportUserInfo) {
            canReportUserInfo = false;
            return true;
        }
        return false;
    }

}
