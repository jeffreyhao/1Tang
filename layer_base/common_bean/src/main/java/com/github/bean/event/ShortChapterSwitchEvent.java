package com.github.bean.event;

public class ShortChapterSwitchEvent {

    public String bookId;
    public String chapterId;

    public ShortChapterSwitchEvent(String bookId, String chapterId) {
        this.bookId = bookId;
        this.chapterId = chapterId;
    }
}
