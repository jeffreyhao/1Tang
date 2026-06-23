package com.github.bean.app;

public class LanguageItemBean {
    private String id;
    private String language;
    private String des_local;
    private String des;
    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDes_local() {
        return des_local;
    }

    public void setDes_local(String des_local) {
        this.des_local = des_local;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
