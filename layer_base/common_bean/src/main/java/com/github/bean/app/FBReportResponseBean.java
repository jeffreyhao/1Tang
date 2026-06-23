package com.github.bean.app;

import java.util.List;

/**
 * Time: 2024/5/6
 * Author: lhc
 * Desc:
 */
public class FBReportResponseBean {

    private int code;
    private String msg;

    private Response body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Response getBody() {
        return body;
    }

    public void setBody(Response body) {
        this.body = body;
    }

    public static class Response {
        private String book;
        private String cookie_id;
        private String pixel_id;
        private String fbp;
        private String fbc;
        private String utm_ad_id;
        private String utm_ad_name;
        private String utm_act_id;
        private String utm_adset_id;
        private String utm_adset_name;
        private String utm_campaign;
        private String utm_campaign_id;
        private String utm_publisher_platform;
        private RuleBean black_list;
        private RuleBean white_list;

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
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

        public RuleBean getBlack_list() {
            return black_list;
        }

        public void setBlack_list(RuleBean black_list) {
            this.black_list = black_list;
        }

        public RuleBean getWhite_list() {
            return white_list;
        }

        public void setWhite_list(RuleBean white_list) {
            this.white_list = white_list;
        }
    }


    public static class RuleBean {
        private List<String> sdk;
        private List<String> api;
        private List<String> api_2_sdk;
        private List<String> sdk_2_api;

        public List<String> getSdk() {
            return sdk;
        }

        public void setSdk(List<String> sdk) {
            this.sdk = sdk;
        }

        public List<String> getApi() {
            return api;
        }

        public void setApi(List<String> api) {
            this.api = api;
        }

        public List<String> getApi_2_sdk() {
            return api_2_sdk;
        }

        public void setApi_2_sdk(List<String> api_2_sdk) {
            this.api_2_sdk = api_2_sdk;
        }

        public List<String> getSdk_2_api() {
            return sdk_2_api;
        }

        public void setSdk_2_api(List<String> sdk_2_api) {
            this.sdk_2_api = sdk_2_api;
        }
    }
}
