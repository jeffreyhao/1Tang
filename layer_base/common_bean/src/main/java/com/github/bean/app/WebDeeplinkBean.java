package com.github.bean.app;

import androidx.annotation.NonNull;

/**
 * Time: 2024/1/30
 * Author: lhc
 * Desc:
 */
public class WebDeeplinkBean {
    private int sub_code;
    private String book;
    private String external_cp_book_id;
    private String cookie_id;
    private String pixel_id;
    private String fbp;
    private String fbc;
    private String screen;
    private String utm_ad_id;
    private String utm_ad_name;
    private String utm_act_id;
    private String utm_adset_id;
    private String utm_adset_name;
    private String utm_campaign;
    private String utm_campaign_id;
    private String utm_publisher_platform;

    public int getSub_code() {
        return sub_code;
    }

    public void setSub_code(int sub_code) {
        this.sub_code = sub_code;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getExternal_cp_book_id() {
        return external_cp_book_id;
    }

    public void setExternal_cp_book_id(String external_cp_book_id) {
        this.external_cp_book_id = external_cp_book_id;
    }

    public String getCookie_id() {
        return cookie_id;
    }

    public void setCookie_id(String cookie_id) {
        this.cookie_id = cookie_id;
    }

    public String getPixel_id() {
        return pixel_id;
    }

    public void setPixel_id(String pixel_id) {
        this.pixel_id = pixel_id;
    }

    public String getFbp() {
        return fbp;
    }

    public void setFbp(String fbp) {
        this.fbp = fbp;
    }

    public String getFbc() {
        return fbc;
    }

    public void setFbc(String fbc) {
        this.fbc = fbc;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getUtm_ad_id() {
        return utm_ad_id;
    }

    public void setUtm_ad_id(String utm_ad_id) {
        this.utm_ad_id = utm_ad_id;
    }

    public String getUtm_ad_name() {
        return utm_ad_name;
    }

    public void setUtm_ad_name(String utm_ad_name) {
        this.utm_ad_name = utm_ad_name;
    }

    public String getUtm_act_id() {
        return utm_act_id;
    }

    public void setUtm_act_id(String utm_act_id) {
        this.utm_act_id = utm_act_id;
    }

    public String getUtm_adset_id() {
        return utm_adset_id;
    }

    public void setUtm_adset_id(String utm_adset_id) {
        this.utm_adset_id = utm_adset_id;
    }

    public String getUtm_adset_name() {
        return utm_adset_name;
    }

    public void setUtm_adset_name(String utm_adset_name) {
        this.utm_adset_name = utm_adset_name;
    }

    public String getUtm_campaign() {
        return utm_campaign;
    }

    public void setUtm_campaign(String utm_campaign) {
        this.utm_campaign = utm_campaign;
    }

    public String getUtm_campaign_id() {
        return utm_campaign_id;
    }

    public void setUtm_campaign_id(String utm_campaign_id) {
        this.utm_campaign_id = utm_campaign_id;
    }

    public String getUtm_publisher_platform() {
        return utm_publisher_platform;
    }

    public void setUtm_publisher_platform(String utm_publisher_platform) {
        this.utm_publisher_platform = utm_publisher_platform;
    }

    @Override
    public String toString() {
        return "WebDeeplinkBean{" +
                "sub_code=" + sub_code +
                ", book='" + book + '\'' +
                ", external_cp_book_id='" + external_cp_book_id + '\'' +
                ", cookie_id='" + cookie_id + '\'' +
                ", pixel_id='" + pixel_id + '\'' +
                ", fbp='" + fbp + '\'' +
                ", fbc='" + fbc + '\'' +
                ", screen='" + screen + '\'' +
                ", utm_ad_id='" + utm_ad_id + '\'' +
                ", utm_ad_name='" + utm_ad_name + '\'' +
                ", utm_act_id='" + utm_act_id + '\'' +
                ", utm_adset_id='" + utm_adset_id + '\'' +
                ", utm_adset_name='" + utm_adset_name + '\'' +
                ", utm_campaign='" + utm_campaign + '\'' +
                ", utm_campaign_id='" + utm_campaign_id + '\'' +
                ", utm_publisher_platform='" + utm_publisher_platform + '\'' +
                '}';
    }
}
