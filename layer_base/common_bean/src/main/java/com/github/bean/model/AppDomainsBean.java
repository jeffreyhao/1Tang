package com.github.bean.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Time: 2024/6/3
 * Author: lhc
 * Desc:
 */
public class AppDomainsBean {
    @SerializedName("type")
    private int type; // 1 app 2 se 3 st
    @SerializedName("default_domain")
    private String defaultDomain;
    @SerializedName("backup_domain")
    private List<String> backUpDomain;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDefaultDomain() {
        return defaultDomain;
    }

    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    public List<String> getBackUpDomain() {
        return backUpDomain;
    }

    public void setBackUpDomain(List<String> backUpDomain) {
        this.backUpDomain = backUpDomain;
    }
}
