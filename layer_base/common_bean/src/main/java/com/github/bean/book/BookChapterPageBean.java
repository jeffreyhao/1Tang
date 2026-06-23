package com.github.bean.book;

/**
 * Time: 2024/1/9
 * Author: lhc
 * Desc:
 */
public class BookChapterPageBean {
    private int code;
    private String message;

    private BookChapterDirectory body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BookChapterDirectory getBody() {
        return body;
    }

    public void setBody(BookChapterDirectory body) {
        this.body = body;
    }
}
