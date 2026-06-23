package com.xcyh.cheatsheet.home.pager;

import android.os.Bundle;

import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.fragment.CustomFragmentUtil;
import com.baidu.baselibrary.fragment.view.ICustomFragmentBuilder;

/**
 * Created by haojiangfeng on 2025/6/30.
 */
public class HomePage implements ICustomFragmentBuilder {


    private String title;
    private Class<? extends CustomFragment> clazz;

    public HomePage(String title, Class<? extends CustomFragment> clazz){
        this.title = title;
        this.clazz = clazz;
    }


    @Override
    public CustomFragment buildFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        return CustomFragmentUtil.newInstance(clazz, bundle);
    }
}
