package com.github.bean.book;

public class UploadPageRecordBean {
    private String pageName;
    private int viewTime;

    public UploadPageRecordBean(String pageName, int viewTime) {
        this.pageName = pageName;
        this.viewTime = viewTime;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public int getViewTime() {
        return viewTime;
    }

    public void setViewTime(int viewTime) {
        this.viewTime = viewTime;
    }
}
