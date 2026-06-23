package com.benefit.event;

/**
 * 神策统计用
 * Created by haojiangfeng on 2024/7/9.
 */
public class PendingRestoreResultSensorsEvent {

    public String order;
    public String bookId;
    public boolean isSuccess;


    public PendingRestoreResultSensorsEvent(String order, String bookId, boolean isSuccess){
        this.order = order;
        this.bookId = bookId;
        this.isSuccess = isSuccess;
    }

}
