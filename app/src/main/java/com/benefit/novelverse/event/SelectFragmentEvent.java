package com.benefit.novelverse.event;


public class SelectFragmentEvent {

    private String pageCode;

    public SelectFragmentEvent(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }
}
