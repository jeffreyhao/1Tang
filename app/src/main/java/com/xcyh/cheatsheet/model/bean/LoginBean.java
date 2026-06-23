package com.xcyh.cheatsheet.model.bean;

import java.util.List;

import androidx.annotation.Keep;

/**
 * Created by haojiangfeng on 2024/11/14.
 */
@Keep
public class LoginBean {

    /*
     {
         "code": 0,
         "msg": "success",
         "data": {
             "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjozNiwiZXhwIjoxNzQyNjMxNzg5LCJpc3MiOiJsYXVuY2gifQ.VOgw3hFmb9JtS4aV-u-ACp7oye4Tkf9K13dIK7Qe1ik",
             "userInfo": {
                 "userId": 36,
                 "userName": "haojiangfeng",
                 "dashboard": 0,
                 "role": [
                    "admin"
                 ],
                 "isAnalystAdmin": true
             }
         }
     }
     */

    /**
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjozNiwiZXhwIjoxNzQyNjE0NjkxLCJpc3MiOiJsYXVuY2gifQ.it-9bwhuFANhFlDPhZf5h-DcpXDmOdmf-Fen3jib5T8
     * userInfo : {"userId":36,"userName":"haojiangfeng","dashboard":0,"role":["admin"],"isAnalystAdmin":true}
     */

    private String token;
    private UserInfoBean userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    @Keep
    public static class UserInfoBean {
        /**
         * userId : 36
         * userName : haojiangfeng
         * dashboard : 0
         * role : ["admin"]
         * isAnalystAdmin : true
         */

        private int userId;
        private String userName;
        private int dashboard;
        private boolean isAnalystAdmin;
        private List<String> role;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getDashboard() {
            return dashboard;
        }

        public void setDashboard(int dashboard) {
            this.dashboard = dashboard;
        }

        public boolean isIsAnalystAdmin() {
            return isAnalystAdmin;
        }

        public void setIsAnalystAdmin(boolean isAnalystAdmin) {
            this.isAnalystAdmin = isAnalystAdmin;
        }

        public List<String> getRole() {
            return role;
        }

        public void setRole(List<String> role) {
            this.role = role;
        }
    }

}
