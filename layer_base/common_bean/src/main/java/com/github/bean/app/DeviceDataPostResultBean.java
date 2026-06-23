package com.github.bean.app;

import com.google.gson.annotations.SerializedName;

/**
 * Time: 2024/3/27
 * Author: lhc
 * Desc:
 */
public class DeviceDataPostResultBean {
    private boolean result;
    @SerializedName("caid")
    private String caId;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getCaId() {
        return caId;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }
}
