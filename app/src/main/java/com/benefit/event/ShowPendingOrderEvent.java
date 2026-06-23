package com.benefit.event;

/**
 * Time: 2024/1/11
 * Author: lhc
 * Desc:
 */
public class ShowPendingOrderEvent {
    private boolean show;

    private int checkPendingNum;
    public ShowPendingOrderEvent(boolean show){
        this.show = show;
    }

    public ShowPendingOrderEvent(int checkPendingNum){
        this.checkPendingNum = checkPendingNum;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int isCheckPendingNum() {
        return checkPendingNum;
    }

    public void setCheckPendingNum(int checkPendingNum) {
        this.checkPendingNum = checkPendingNum;
    }
}
