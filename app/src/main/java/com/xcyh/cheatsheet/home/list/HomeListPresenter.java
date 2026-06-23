package com.xcyh.cheatsheet.home.list;

import android.app.Activity;
import android.content.Intent;

import com.baidu.baselibrary.base.BasePresenter;
import com.xcyh.cheatsheet.pages.applink.AppLinkTestActivity;
import com.xcyh.cheatsheet.pages.scheme.SchemeTestActivity;

/**
 * Created by haojiangfeng on 2025/6/30.
 */
public class HomeListPresenter extends BasePresenter<IHomeListView> {


    public void gotoSchemeActivity(Activity activity){
        Intent intent = new Intent(activity, SchemeTestActivity.class);
        activity.startActivity(intent);
    }

    public void gotoAppLinkActivity(Activity activity){
        Intent intent = new Intent(activity, AppLinkTestActivity.class);
        activity.startActivity(intent);
    }

}
