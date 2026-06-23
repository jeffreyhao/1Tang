package com.github.bean.zhifu;

public class PurchaseAnalyticsBean {
    private String item_id;
    private String item_name;

    public PurchaseAnalyticsBean(String item_id, String item_name) {
        this.item_id = item_id;
        this.item_name = item_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
