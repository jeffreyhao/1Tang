package com.baidu.baselibrary.fragment.adapter;

import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.fragment.CustomFragmentManager;
import com.baidu.baselibrary.fragment.FragmentClient;
import com.baidu.baselibrary.fragment.view.NoSaveStateFrameLayout;
import com.baidu.baselibrary.log.ALog;
import com.jess.baselibrary.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;


public abstract class CustomFragmentPagerAdapter<T> extends PagerAdapter {


    protected List<CustomPageItem<T>> mList = new ArrayList<>();
    protected List<CustomPageItem<T>> mInvalidList = new ArrayList<>();

    protected CustomFragmentManager mCoverFragmentManager;
    protected CustomFragment mParentFragment;

    protected CustomFragment mCurrentFragment;


    public CustomFragmentPagerAdapter(CustomFragmentManager coverFragmentManager, CustomFragment parentFragment){
        mCoverFragmentManager = coverFragmentManager;
        mParentFragment = parentFragment;
    }



    public void setData(List<T> list){
        mInvalidList.clear();
        mInvalidList.addAll(mList);
        mList.clear();
        if(list != null && list.size() > 0){
            for(T item : list){
                CustomPageItem<T> pageItem = new CustomPageItem<>();
                pageItem.mItem = item;
                pageItem.title = "";
                mList.add(pageItem);
            }
        }
        notifyDataSetChanged();
    }

    public List<CustomPageItem<T>> getList(){
        return mList;
    }

    public abstract CustomFragment getItem(int position, CustomPageItem<T> pageItem);


    /*
     * 为给定位置创建页面。
     * 适配器负责将视图添加到这里给出的容器中，尽管它只需要确保在从 finishUpdate() 返回时完成此操作。
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CustomPageItem<T> pageItem = mList.get(position);
        T item = pageItem.mItem;
        FragmentClient fragmentClient = pageItem.mFragmentClient;

        CustomFragment fragment = null;
        Bundle savedInstanceState = null;
        if (fragmentClient != null) {
            fragment = fragmentClient.getFragment();
            savedInstanceState = fragmentClient.getSaveState();
        }
        if (fragment == null) {
            fragment = getItem(position, mList.get(position));
            Bundle bundle = fragment.getArguments();
            if(bundle == null){
                bundle = new Bundle();
            }
            bundle.putInt("tab_position", position);
            fragment.setArguments(bundle);
            fragment.setCustomFragmentManager(mCoverFragmentManager);
            fragment.setParentFragment(mParentFragment);
            fragmentClient = new FragmentClient(fragment);
            pageItem.mFragmentClient = fragmentClient;

            fragment.onAttach(mCoverFragmentManager.getContext());
            fragment.onCreate(savedInstanceState);
            ViewGroup view = fragment.onCreateView(LayoutInflater.from(mCoverFragmentManager.getContext()), container, null);
            if(view.getParent() == null){
                view = NoSaveStateFrameLayout.wrap(view);
            }

            fragment.setView(view);
            fragment.onViewCreated(view, savedInstanceState);
            fragment.onActivityCreated(savedInstanceState);
        }

        if (fragment.getView().getParent() != null) {
            ((ViewGroup) fragment.getView().getParent()).removeView(fragment.getView());
        }
        container.addView(fragment.getView());
        fragment.onViewStateRestored(savedInstanceState);
        fragment.getView().setTag(R.id.custom_viewpager_adapter_item, item);

        return fragment.getView();
    }


    /*
     * 显示给用户作为当前页面的item.
     * 当用户切换到一个新的页面时，ViewPager会调用这个方法
     */
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        CustomFragment fragment = null;
        if (mList != null && !mList.isEmpty()
                && mList.get(position) != null
                && mList.get(position).mFragmentClient != null) {
            fragment = mList.get(position).mFragmentClient.getFragment();
        }

        if (fragment != mCurrentFragment) {
            //调用当前 fragment 的pause、stop
            if (mCurrentFragment != null) {
                Lifecycle.State state = mCurrentFragment.getLifecycle().getCurrentState();
                ALog.textSingle("CustomFragmentPagerAdapter", "setPrimaryItem", "Lifecycle.State: " + state.name());
                if (container.isShown()) {
                    mCurrentFragment.onPause();
                    mCurrentFragment.onStop();
                }
            }
            if (fragment != null) {
                // 调用要显示的fragment的 start、resume
                if (container.isShown()) {
                    fragment.onStart();
                    fragment.onResume();
                }
            }
            mCurrentFragment = fragment;
            //系统会在container里的mDisappearingChildren数组里添加View，这里需要手动清除
            container.clearDisappearingChildren();
        }
    }

    /*
     * 删除给定位置的页面。
     * 适配器负责从其容器中删除视图，尽管它只需要确保在从 finishUpdate() 返回时完成此操作。
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if(!mInvalidList.isEmpty()){
            if(position < mInvalidList.size()){
                destroyFragment(mInvalidList.get(position).mFragmentClient);
            }
        } else {
            if(position < mList.size()){
                destroyFragment(mList.get(position).mFragmentClient);
            }
        }

        container.clearChildFocus((View) object);
        container.removeView((View) object);
        ((View) object).setTag(R.id.custom_viewpager_adapter_item, null);
    }

    private void destroyFragment(FragmentClient fragmentClient){
        if (fragmentClient != null && fragmentClient.getFragment() != null) {
            CustomFragment fragment = fragmentClient.getFragment();
            fragmentClient.onSaveInstanceState(fragment);
            fragment.onDetach();
            fragment.onPause();
            fragment.onStop();
            fragment.onDestroyView();
            fragment.onDestroy();
            fragmentClient.setFragment(null);
        }
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
        mInvalidList.clear();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CustomPageItem<T> pageItem = mList.get(position);
        FragmentClient fragmentClient = pageItem.mFragmentClient;
        return pageItem.title == null ? "" : pageItem.title;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public int getCount() {
        return mList.size();
    }



    public CustomFragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public CustomFragment getFragment(int position){
        if (mList == null || mList.isEmpty()
                || position <0 || position >= mList.size()
                || mList.get(position) == null
                || mList.get(position).mFragmentClient == null){
            return null;
        }
        return mList.get(position).mFragmentClient.getFragment();
    }


    @Override
    public void notifyDataSetChanged() {
        for (int i = 0; i < mList.size(); i++) {
            CustomPageItem<T> pageItem = mList.get(i);
            if (pageItem.mFragmentClient != null
                    && pageItem.mFragmentClient.getFragment() != null
                    && pageItem.mFragmentClient.getFragment().getView() != null) {
                pageItem.mFragmentClient.getFragment().getView().forceLayout();
            }
        }
        super.notifyDataSetChanged();
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        for (int i = 0; i < mList.size(); i++) {
            CustomPageItem<T> pageItem = mList.get(i);
            if (pageItem.mFragmentClient != null
                    && pageItem.mFragmentClient.getFragment() != null
                    && pageItem.mFragmentClient.getFragment().getView() != null) {
                pageItem.mFragmentClient.getFragment().getView().forceLayout();
            }
        }
        super.registerDataSetObserver(observer);
    }

    @Override
    public int getItemPosition(Object object) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).mFragmentClient != null
                    && mList.get(i).mFragmentClient.getFragment() != null
                    && mList.get(i).mFragmentClient.getFragment().getView() == object) {
                return super.getItemPosition(object);
            }
        }
        // return-POSITION_NONE，会触发 destroyItem()
        return PagerAdapter.POSITION_NONE;
    }

    /**
     * FIXMe
     */
    public void onSaveInstanceState(Bundle outState) {

        SparseArray<Bundle> bundleList = new SparseArray<>();
        for (int i = 0; i < mList.size(); i++) {
            FragmentClient fragmentClient = mList.get(i).mFragmentClient;
            Bundle bundle = null;
            if (fragmentClient != null && fragmentClient.getFragment() != null) {
                bundle = new Bundle();
                fragmentClient.getFragment().onSaveInstanceState(bundle);
            } else if (fragmentClient != null) {
                bundle = fragmentClient.getSaveState();
            }

            bundleList.put(i, bundle);
        }

        outState.putSparseParcelableArray("bundleList", bundleList);
    }


    /**
     * FIXMe
     */
    public void onViewStateRestored(final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        SparseArray<Bundle> bundleList = savedInstanceState.getSparseParcelableArray("bundleList");
        if (bundleList == null) {
            return;
        }

        for (int i = 0; i < bundleList.size(); i++) {
            FragmentClient client = new FragmentClient();
            client.setSaveState(bundleList.valueAt(i));
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        for (int i = 0; i < mList.size(); i++) {
            CustomPageItem<T> pageItem = mList.get(i);
            if (pageItem.mFragmentClient != null
                    && pageItem.mFragmentClient.getFragment() != null) {
                pageItem.mFragmentClient.getFragment().onConfigurationChanged(newConfig);
            }
        }
    }


    /**
     * 清空数据
     */
    public void clearChannelData() {
        FragmentClient fragmentClient;
        for (int i = 0; i < mList.size(); i++) {
            CustomPageItem<T> pageItem = mList.get(i);
            fragmentClient = pageItem.mFragmentClient;
            if (fragmentClient == null) {
                continue;
            }
            CustomFragment fragment = fragmentClient.getFragment();
            if (fragment != null) {
                fragment.onDetach();
                fragment.onPause();
                fragment.onStop();
                fragment.onDestroyView();
                fragment.onDestroy();
                pageItem.mFragmentClient = null;
            }
        }
    }

}
