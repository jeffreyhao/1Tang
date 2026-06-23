package com.xcyh.cheatsheet.home.pager;

import com.baidu.baselibrary.base.fragment.BindingCustomFragment;
import com.baidu.baselibrary.request.EmptyPresenter;
import com.benefit.novelverse.R;
import com.benefit.novelverse.databinding.FragmentHomeBinding;

/**
 * Created by haojiangfeng on 2025/4/22.
 */
public class HomeFragment extends BindingCustomFragment<FragmentHomeBinding, EmptyPresenter> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        HomePagerAdapter adapter = new HomePagerAdapter(getCustomFragmentManager(), this);
        mBinding.viewPager.setAdapter(adapter);
    }
}
