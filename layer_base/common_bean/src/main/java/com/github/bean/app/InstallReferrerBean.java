package com.github.bean.app;

public class InstallReferrerBean {
    private String app;
    private long t;
    private InstallSourceBean source;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public InstallSourceBean getSource() {
        return source;
    }

    public void setSource(InstallSourceBean source) {
        this.source = source;
    }

    public static class InstallSourceBean {
        private String data;
        private String nonce;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }
    }

    public static class ReferrerBean {
        private String ad_id;
        private String ad_objective_name;
        private String adgroup_id;
        private String adgroup_name;
        private String campaign_id;
        private String campaign_name;
        private String campaign_group_id;
        private String campaign_group_name;
        private String account_id;
        private boolean is_instagram;
        private String publisher_platform;
        private String platform_position;


        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getAd_objective_name() {
            return ad_objective_name;
        }

        public void setAd_objective_name(String ad_objective_name) {
            this.ad_objective_name = ad_objective_name;
        }

        public String getAdgroup_id() {
            return adgroup_id;
        }

        public void setAdgroup_id(String adgroup_id) {
            this.adgroup_id = adgroup_id;
        }

        public String getAdgroup_name() {
            return adgroup_name;
        }

        public void setAdgroup_name(String adgroup_name) {
            this.adgroup_name = adgroup_name;
        }

        public String getCampaign_id() {
            return campaign_id;
        }

        public void setCampaign_id(String campaign_id) {
            this.campaign_id = campaign_id;
        }

        public String getCampaign_name() {
            return campaign_name;
        }

        public void setCampaign_name(String campaign_name) {
            this.campaign_name = campaign_name;
        }

        public String getCampaign_group_id() {
            return campaign_group_id;
        }

        public void setCampaign_group_id(String campaign_group_id) {
            this.campaign_group_id = campaign_group_id;
        }

        public String getCampaign_group_name() {
            return campaign_group_name;
        }

        public void setCampaign_group_name(String campaign_group_name) {
            this.campaign_group_name = campaign_group_name;
        }

        public String getAccount_id() {
            return account_id;
        }

        public void setAccount_id(String account_id) {
            this.account_id = account_id;
        }

        public boolean isIs_instagram() {
            return is_instagram;
        }

        public void setIs_instagram(boolean is_instagram) {
            this.is_instagram = is_instagram;
        }

        public String getPublisher_platform() {
            return publisher_platform;
        }

        public void setPublisher_platform(String publisher_platform) {
            this.publisher_platform = publisher_platform;
        }

        public String getPlatform_position() {
            return platform_position;
        }

        public void setPlatform_position(String platform_position) {
            this.platform_position = platform_position;
        }


        @Override
        public String toString() {
            return "ReferrerBean{" +
                    "ad_id='" + ad_id + '\'' +
                    ", ad_objective_name='" + ad_objective_name + '\'' +
                    ", adgroup_id='" + adgroup_id + '\'' +
                    ", adgroup_name='" + adgroup_name + '\'' +
                    ", campaign_id='" + campaign_id + '\'' +
                    ", campaign_name='" + campaign_name + '\'' +
                    ", campaign_group_id='" + campaign_group_id + '\'' +
                    ", campaign_group_name='" + campaign_group_name + '\'' +
                    ", account_id='" + account_id + '\'' +
                    ", is_instagram=" + is_instagram +
                    ", publisher_platform='" + publisher_platform + '\'' +
                    ", platform_position='" + platform_position + '\'' +
                    '}';
        }
    }
}
