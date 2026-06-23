package com.benefit.event;

public class CancelPayEvent {


    public String ruleId;
    public int platform;
    public String bookId;

    public CancelPayEvent(String ruleId, int platform, String bookId){
        this.ruleId = ruleId;
        this.platform = platform;
        this.bookId = bookId;
    }


}
