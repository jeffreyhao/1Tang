package com.github.bean.model;

public class ReadTimeSyncResult {
    private int today;
    private int total;
    private int unbrokenDay;

    public ReadTimeSyncResult(int today, int total, int unbrokenDay) {
        this.today = today;
        this.total = total;
        this.unbrokenDay = unbrokenDay;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUnbrokenDay() {
        return unbrokenDay;
    }

    public void setUnbrokenDay(int unbrokenDay) {
        this.unbrokenDay = unbrokenDay;
    }
}
