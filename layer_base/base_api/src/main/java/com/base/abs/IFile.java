package com.base.abs;

import java.io.File;

public interface IFile {

    byte[] decryptXORForCatalog(byte[] data);

    String uncompressToString(byte[] bytes);

    boolean isChapterCached(String bookId, String chapterId, String time);

    File getBookFile(String bookId, String chapterId, String time);

    String readFile(File file);

    void delete(File file);
    void delete(String filePathName);
}
