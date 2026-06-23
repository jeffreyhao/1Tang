package com.xcyh.cheatsheet.app.impl;

import com.baidu.baselibrary.util.UserManager;
import com.base.abs.IUser;

/**
 * Created by haojiangfeng on 2024/10/30.
 */
public class IUserImpl implements IUser {


    public static IUserImpl get(){
        return new IUserImpl();
    }


    @Override
    public String getUserId() {
        return UserManager.getUserId();
    }

    @Override
    public String getAccessToken() {
        return UserManager.getAccessToken();
    }

    @Override
    public void saveToken(String token) {
        UserManager.saveToken(token);
    }

    @Override
    public boolean isPaying() {
        return false;
    }

    @Override
    public boolean isPremium() {
        return false;
    }

    @Override
    public void requestUserInfo() {

    }
}
