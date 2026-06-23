package com.baidu.baselibrary.base.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseListView;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.util.ui.RefreshUtils;
import com.baidu.baselibrary.util.ui.ViewUtil; // 假设 setVisible 扩展方法改成工具类调用
import com.base.net.bean.ApiException;
import com.base.util.collection.ListUtil;
import com.base.util.net.NetworkUtil;
import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;
import com.jess.baselibrary.databinding.CommonListLayoutBinding;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseListFragment<T, P extends BasePresenter<?>>
        extends BaseFragment<CommonListLayoutBinding, P>
        implements IBaseListView<List<T>> {

    private int mPage = 1;
    private int mLimit = 10;
    private List<T> mList = new ArrayList<>();
    private boolean hasMoreData = false;

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        this.mPage = page;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        this.mList = list;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        this.mLimit = limit;
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list_layout;
    }

    @Override
    public void initView() {
        super.initView();
        mBinding.recyclerView.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration decoration = getItemDecoration();
        if (decoration != null) {
            mBinding.recyclerView.addItemDecoration(decoration);
        }
        mBinding.recyclerView.setAdapter(createAdapter());
    }

    @Override
    public void initData() {
        mBinding.recyclerView.setOnceRequestLimit(mLimit);
        getData();
    }

    @Override
    public void initListener() {
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (NetworkUtil.isNetAvailable(mContext)) {
                    mPage = 1;
                    mBinding.recyclerView.setCurPage(mPage);
                    getData();
                } else {
                    finishRefreshOrLoadMore();
                    toastTop(getString(R.string.net_connect_tip),
                            UIUtil.dip2px(mContext, 40f));
                }
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (NetworkUtil.isNetAvailable(mContext)) {
                    if (hasMoreData) {
                        if (mBinding.recyclerView.isRequestPageInit(mPage)) {
                            mBinding.recyclerView.requestPage(mPage);
                            getData();
                        }
                    } else {
                        mBinding.refreshLayout.finishLoadMore();
                    }
                } else {
                    finishRefreshOrLoadMore();
                    toastBottom(getString(R.string.net_connect_tip),
                            UIUtil.dip2px(mContext, 50f));
                }
            }
        });

        mBinding.emptyView.setOnClickListener(v -> {
            if (NetworkUtil.isNetAvailable(mContext)) {
                ViewUtil.setVisible(v, false);
                mPage = 1;
                mBinding.recyclerView.setCurPage(mPage);
                getData();
            } else {
                toast(getString(R.string.net_connect_tip));
            }
        });

        mBinding.recyclerView.setOnLoadMoreListener(() -> {
            LogUtil.d("isLoadingMore=====>");
            if (hasMoreData) {
                mBinding.recyclerView.requestPage(mPage);
                getData();
            }
        });
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    public abstract RecyclerView.Adapter<?> createAdapter();

    public abstract void getData();

    public abstract void onGetDataSuccess(List<T> t);

    @Override
    public void onRequestSuccess(List<T> t) {
        Collection<T> list = t;
        hasMoreData = false;
        boolean showEmptyView = mPage == 1 && (list == null || list.isEmpty());
        ViewUtil.setVisible(mBinding.refreshLayout, !showEmptyView);
        ViewUtil.setVisible(mBinding.emptyView, showEmptyView);
        if (showEmptyView) {
            mBinding.emptyView.setContent(R.string.common_no_data, R.drawable.default_no_data);
        }
        if (mPage == 1) {
            mBinding.recyclerView.setCurPage(1);
        }
        mBinding.recyclerView.finishRequest(mPage);
        if (list != null && !list.isEmpty()) {
            if (mPage == 1) {
                mList.clear();
            }
            mList.addAll(list);
            onGetDataSuccess(t);
            if (list.size() >= mLimit) {
                hasMoreData = true;
                mPage++;
            }
        }
        RefreshUtils.setRefreshFooter(hasMoreData, mBinding.refreshLayout);
    }

    @Override
    public void onRequestFail(ApiException e) {
        super.onRequestFail(e);
        if (mPage == 1) {
            if (ListUtil.isEmpty(mList)) {
                if (!NetworkUtil.isNetAvailable(mContext)) {
                    mBinding.emptyView.setContent(R.string.error_net_tip, R.drawable.default_no_net);
                } else {
                    mBinding.emptyView.setContent(R.string.error_data_tip, R.drawable.default_load_fail);
                }
            } else {
                if (!NetworkUtil.isNetAvailable(mContext)) {
                    toast(getResources().getString(R.string.error_net_tip));
                } else {
                    toast(getResources().getString(R.string.error_data_tip));
                }
            }
        }
        mBinding.recyclerView.requestFail(mPage);
    }

    @Override
    public void showLoading(boolean show) {
        RefreshState state = mBinding.refreshLayout.getState();
        if (state == RefreshState.PullDownToRefresh
                || state == RefreshState.Refreshing
                || state == RefreshState.Loading) {
            return;
        }
        super.showLoading(show);
    }

    @Override
    public void onRequestCacheSuccess(List<T> t) {
        hasMoreData = false;
        mPage = 1;
        mBinding.recyclerView.setCurPage(1);
        Collection<T> list = t;
        boolean showEmptyView = (list == null || list.isEmpty());
        UIUtil.setVisible(mBinding.refreshLayout, !showEmptyView);
        UIUtil.setVisible(mBinding.emptyView, showEmptyView);
        if (showEmptyView) {
            mBinding.emptyView.setContent(R.string.common_no_data, R.drawable.default_no_data);
        } else {
            mList.clear();
            if (list != null) {
                mList.addAll(list);
            }
            onGetDataSuccess(t);
        }
        RefreshUtils.setRefreshFooter(false, mBinding.refreshLayout);
    }

    @Override
    public void onRequestCacheFail(ApiException e) {
        // none
    }

    public void finishRefreshOrLoadMore() {
        mBinding.refreshLayout.finishRefresh();
        mBinding.refreshLayout.finishLoadMore();
    }
}
