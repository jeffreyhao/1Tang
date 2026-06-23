package com.xcyh.cheatsheet.app.config;


import android.content.Context;

import com.baidu.baselibrary.global.Clazz;
import com.baidu.baselibrary.main.MainTabConfig;
import com.base.api.AppApi;
import com.base.api.LanguageApi;
import com.base.api.Logger;
import com.base.api.PreferenceApi;
import com.base.api.UserApi;
import com.base.api.VersionApi;
import com.base.global.GlobalBuildConfig;
import com.base.net.cache.CacheConfig;
import com.base.res.Res;
import com.benefit.novelverse.R;
import com.xcyh.cheatsheet.app.FoApplication;
import com.xcyh.cheatsheet.app.impl.IAppImpl;
import com.xcyh.cheatsheet.app.impl.ICacheTimeImpl;
import com.xcyh.cheatsheet.app.impl.ILanguageImpl;
import com.xcyh.cheatsheet.app.impl.ILogImpl;
import com.xcyh.cheatsheet.app.impl.IPreferenceImpl;
import com.xcyh.cheatsheet.app.impl.IUserImpl;
import com.xcyh.cheatsheet.app.impl.IVersionImpl;
import com.xcyh.cheatsheet.home.pager.HomeFragment;
import com.xcyh.cheatsheet.main.HomeActivity;
import com.xcyh.cheatsheet.main.login.LoginActivity;
import com.xcyh.cheatsheet.demo.DemoFragment;
import com.xcyh.cheatsheet.main.splash.LaunchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haojiangfeng on 2023/8/31.
 */
public class MainConfig {

    public static void configOnApplicationAttach(Context base){


        Clazz.initApplicationClass(FoApplication.class);

        Clazz.mainActivityClass = HomeActivity.class;
        Clazz.loginActivityClass = LoginActivity.class;
        Clazz.splashActivityClass = LaunchActivity.class;

        Clazz.classDiscoverFragment = HomeFragment.class;
        Clazz.classProfileFragment = DemoFragment.class;


        Res.setContext(base);
        Res.setAppName(GlobalBuildConfig.APPLICATION_ID, GlobalBuildConfig.FLAVOR);

        initInterfaceImplementation();
    }

    /**
     * 初始化全局接口实现
     */
    private static void initInterfaceImplementation() {
        LanguageApi.iLanguage = ILanguageImpl.get();
        Logger.iLog = ILogImpl.get();
        PreferenceApi.iPreference = IPreferenceImpl.get();
        UserApi.iUser = IUserImpl.get();
        VersionApi.iVersion = IVersionImpl.get();
        AppApi.iApp = IAppImpl.get();
        CacheConfig.iCacheTime = ICacheTimeImpl.get();
    }

    public static void configOnApplicationCreate(){
        MainTabConfig.setMainTabConfig(new MainTabConfig.OnMainTabConfigCallback() {
            @Override
            public List<MainTabConfig.ItemStyle> config() {
                List<MainTabConfig.ItemStyle> list = new ArrayList<>();

                MainTabConfig.ItemStyle item1 = new MainTabConfig.ItemStyle();
                item1.tabNameRes = R.string.nav_home;
                item1.clazz = Clazz.classDiscoverFragment;
                item1.textColorRec = R.color.bottom_nav_item_selector;
                item1.imageDrawableRes = R.drawable.ic_home_selector;
                item1.animatorRes = R.animator.animator_tab_select;
                list.add(item1);

                MainTabConfig.ItemStyle item2 = new MainTabConfig.ItemStyle();
                item2.tabNameRes = R.string.nav_demo;
                item2.clazz = Clazz.classProfileFragment;
                item2.textColorRec = R.color.bottom_nav_item_selector;
                item2.imageDrawableRes = R.drawable.ic_mine_selector;
                item2.animatorRes = R.animator.animator_tab_select;
                list.add(item2);

                return list;
            }
        });
    }



}