package com.xcyh.cheatsheet.home.pager;

import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.fragment.CustomFragmentManager;
import com.xcyh.cheatsheet.home.list.HomeListFragment;

import java.util.List;

/**
 * Created by haojiangfeng on 2025/6/30.
 */
public class HomePagerAdapter extends CustomFragmentBuilderAdapter<HomePage> {


    public HomePagerAdapter(CustomFragmentManager coverFragmentManager, CustomFragment parentFragment) {
        super(coverFragmentManager, parentFragment);
    }

    @Override
    void addItems(List<HomePage> pageList) {
        pageList.add(new HomePage("title", HomeListFragment.class));
    }

}
