package com.xcyh.cheatsheet.home.pager;

import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.fragment.CustomFragmentManager;
import com.baidu.baselibrary.fragment.adapter.CustomFragmentPagerAdapter;
import com.baidu.baselibrary.fragment.adapter.CustomPageItem;
import com.baidu.baselibrary.fragment.view.ICustomFragmentBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haojiangfeng on 2025/6/30.
 */
public abstract class CustomFragmentBuilderAdapter<T extends ICustomFragmentBuilder> extends CustomFragmentPagerAdapter<T> {


    private List<T> mPageList;


    public CustomFragmentBuilderAdapter(CustomFragmentManager coverFragmentManager, CustomFragment parentFragment) {
        super(coverFragmentManager, parentFragment);
        mPageList = new ArrayList<>();
        addItems(mPageList);
        setData(mPageList);
    }

    abstract void addItems(List<T> pageList);


    @Override
    public CustomFragment getItem(int position, CustomPageItem<T> pageItem) {
        return mPageList.get(position).buildFragment();
    }


    public int size(){
        return mPageList.size();
    }





}
