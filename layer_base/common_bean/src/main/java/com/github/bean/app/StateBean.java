package com.github.bean.app;

/**
 * Time: 2023/10/24
 * Author: lhc
 * Desc:
 */
public class StateBean {
    private int status;

    private boolean result;

    private int reason;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }
}
