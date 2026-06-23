package com.benefit.event;

public class AddBookShelfEvent {
    private String bookId;

    public AddBookShelfEvent(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
