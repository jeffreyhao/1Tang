package com.benefit.event;

public class GetChapterContentEvent {
    private String cId;
    private String url;
    public GetChapterContentEvent(String cId, String url) {
        this.cId = cId;
        this.url = url;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
