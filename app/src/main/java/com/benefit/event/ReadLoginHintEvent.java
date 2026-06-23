package com.benefit.event;

/**
 * Time: 2024/3/26
 * Author: lhc
 * Desc:
 */
public class ReadLoginHintEvent {
    private boolean isAlertHint;
    public ReadLoginHintEvent(boolean isAlertHint) {
        this.isAlertHint = isAlertHint;
    }

    public boolean isAlertHint() {
        return isAlertHint;
    }

    public void setAlertHint(boolean alertHint) {
        isAlertHint = alertHint;
    }
}
