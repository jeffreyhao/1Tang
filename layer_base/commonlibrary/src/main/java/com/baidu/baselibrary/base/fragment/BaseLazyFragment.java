package com.baidu.baselibrary.base.fragment;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.log.ALog;

import java.util.List;

/**
 * @author lhc
 * @date 2022/5/9 13:17
 * @desc 懒加载fragment 基类
 */
public abstract class BaseLazyFragment<VB extends ViewDataBinding, P extends BasePresenter<?>>
        extends BaseFragment<VB, P> implements IFragmentVisibility {

    // True if the fragment is visible to the user.
    protected boolean mIsFragmentVisible = false;

    // True if the fragment is visible to the user for the first time.
    private boolean mIsFragmentVisibleFirst = true;

    @Override
    public void onResume() {
        super.onResume();
        determineFragmentVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        determineFragmentInvisible();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            determineFragmentInvisible();
        } else {
            determineFragmentVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            determineFragmentVisible();
        } else {
            determineFragmentInvisible();
        }
    }

    @Override
    public boolean isVisibleToUser() {
        return mIsFragmentVisible;
    }

    private void determineFragmentVisible() {
        Fragment parent = getParentFragment();
        if (parent instanceof BaseLazyFragment) {
            if (!((BaseLazyFragment<?, ?>) parent).isVisibleToUser()) {
                // Parent Fragment is invisible, child fragment must be invisible.
                return;
            }
        }

        if (isResumed() && !isHidden() && getUserVisibleHint() && !mIsFragmentVisible) {
            mIsFragmentVisible = true;
            onVisible();
            if (mIsFragmentVisibleFirst) {
                mIsFragmentVisibleFirst = false;
                onVisibleFirst();
            } else {
                onVisibleExceptFirst();
            }
            determineChildFragmentVisible();
        }
    }

    private void determineFragmentInvisible() {
        if (mIsFragmentVisible) {
            mIsFragmentVisible = false;
            onInvisible();
            determineChildFragmentInvisible();
        }
    }

    private void determineChildFragmentVisible() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseLazyFragment) {
                ((BaseLazyFragment<?, ?>) fragment).determineFragmentVisible();
            }
        }
    }

    private void determineChildFragmentInvisible() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseLazyFragment) {
                ((BaseLazyFragment<?, ?>) fragment).determineFragmentInvisible();
            }
        }
    }

    @Override
    public void setData() {
        intPresenter();
        initView();
        initListener();
    }

    @Override
    public void onVisible() {
        ALog.d(getClass().getSimpleName() + "==>onVisible");
    }

    @Override
    public void onInvisible() {
        ALog.d(getClass().getSimpleName() + "==>onInvisible");
    }

    @Override
    public void onVisibleFirst() {
        ALog.d(getClass().getSimpleName() + "==>onVisibleFirst");
        initData();
    }
}
