package com.benefit.event;

public class SelectTabEvent {
    private int tab;
    public SelectTabEvent(int tab) {
        this.tab = tab;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }
}
