package com.base.api;

import com.base.abs.IUser;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public class UserApi {

    public static IUser iUser;


    public static String getUserId(){
        if(iUser != null){
            return iUser.getUserId();
        }
        return "";
    }


    public static String getAccessToken(){
        if(iUser != null){
            return iUser.getAccessToken();
        }
        return "";
    }

    public static void saveToken(String token){
        if(iUser != null){
            iUser.saveToken(token);
        }
    }

    public static boolean isPaying(){
        if(iUser != null){
            return iUser.isPaying();
        }
        return false;
    }

    public static boolean isPremium(){
        if(iUser != null){
            return iUser.isPremium();
        }
        return false;
    }

    public static void requestUserInfo(){
        if(iUser != null){
            iUser.requestUserInfo();
        }
    }

}
