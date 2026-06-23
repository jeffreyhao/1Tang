package com.github.bean.event;

public class ShowLoadingEvent {
    private int isShow;
    private boolean isMakeUpSuccess;

    public ShowLoadingEvent(int isShow) {
        this.isShow = isShow;
    }

    public ShowLoadingEvent(boolean isMakeUpSuccess) {
        this.isMakeUpSuccess = isMakeUpSuccess;
    }

    public int isShow() {
        return isShow;
    }

    public void setShow(int show) {
        isShow = show;
    }

    public boolean isMakeUpSuccess() {
        return isMakeUpSuccess;
    }

    public void setMakeUpSuccess(boolean makeUpSuccess) {
        isMakeUpSuccess = makeUpSuccess;
    }
}
