package com.base.api;

import com.base.abs.ICache;
import com.base.abs.IFile;

import java.io.File;

public class FileApi {

    public static IFile iFile;


    public static byte[] decryptXORForCatalog(byte[] data) {
        if(iFile != null) {
            return iFile.decryptXORForCatalog(data);
        }
        return null;
    }

    public static String uncompressToString(byte[] bytes) {
        if(iFile != null) {
            return iFile.uncompressToString(bytes);
        }
        return null;
    }

    public static boolean isChapterCached(String bookId, String chapterId, String time) {
        if(iFile != null) {
            return iFile.isChapterCached(bookId, chapterId, time);
        }
        return false;
    }

    public static File getBookFile(String bookId, String chapterId, String time){
        if(iFile != null) {
            return iFile.getBookFile(bookId, chapterId, time);
        }
        return null;
    }


    public static String readFile(File file){
        if(iFile != null) {
            return iFile.readFile(file);
        }
        return null;
    }


    public static void delete(File file){
        if(iFile != null) {
            iFile.delete(file);
        }
    }

    public static void delete(String file){
        if(iFile != null) {
            iFile.delete(file);
        }
    }

}
