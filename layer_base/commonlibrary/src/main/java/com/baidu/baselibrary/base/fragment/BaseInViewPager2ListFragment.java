package com.baidu.baselibrary.base.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.jess.baselibrary.R;
import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseView;
import com.jess.baselibrary.databinding.CommonListLayout2Binding;
import com.base.util.net.NetworkUtil;
import com.baidu.baselibrary.util.ui.RefreshUtils;
import com.baidu.baselibrary.util.ui.ViewUtil; // 假设 setVisible 是 ViewUtil 里的静态方法
import com.base.net.bean.ApiException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseInViewPager2ListFragment<T, P extends BasePresenter<?>> extends BaseFragment<CommonListLayout2Binding, P>
        implements IBaseView<List<T>> {

    protected int mPage = 1;
    protected int mLimit = 10;
    protected List<T> mList = new ArrayList<>();
    private boolean hasMoreData = false;

    @Override
    public int getLayoutId() {
        return R.layout.common_list_layout2;
    }

    @Override
    public void initView() {
        super.initView();
        mBinding.recyclerView.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            mBinding.recyclerView.addItemDecoration(itemDecoration);
        }
        mBinding.recyclerView.setAdapter(createAdapter());
    }

    @Override
    public void initData() {
        getData();
    }

    @Override
    public void initListener() {
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getData();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasMoreData) {
                    getData();
                } else {
                    mBinding.refreshLayout.finishLoadMore();
                }
            }
        });

        mBinding.emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.setVisible(v, false);
                mPage = 1;
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
            if (!NetworkUtil.isNetAvailable(mContext)) {
                mBinding.emptyView.setContent(R.string.error_net_tip, R.drawable.default_no_net);
            } else {
                mBinding.emptyView.setContent(R.string.error_data_tip, R.drawable.default_load_fail);
            }
        }
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
}
