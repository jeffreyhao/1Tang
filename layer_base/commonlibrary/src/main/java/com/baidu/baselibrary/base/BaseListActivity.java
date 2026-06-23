package com.baidu.baselibrary.base;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.baselibrary.util.ui.ViewUtil;
import com.base.util.collection.ListUtil;
import com.base.util.net.NetworkUtil;
import com.baidu.baselibrary.util.ui.RefreshUtils;
import com.base.net.bean.ApiException;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.jess.baselibrary.R;
import com.jess.baselibrary.databinding.CommonListLayoutBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseListActivity<T, P extends BasePresenter<?>>
        extends BaseActivity<CommonListLayoutBinding, P>
        implements IBaseView<List<T>> {

    protected int mPage = 1;
    protected int mLimit = 10;
    protected List<T> mList = new ArrayList<>();
    private boolean hasMoreData = false;

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
        getData();
    }

    @Override
    public void initListeners() {
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPage = 1;
                getData();
            }

            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (hasMoreData) {
                    getData();
                } else {
                    mBinding.refreshLayout.finishLoadMore();
                }
            }
        });
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    protected abstract RecyclerView.Adapter<?> createAdapter();

    protected abstract void getData();

    protected abstract void onGetDataSuccess(List<T> t);

    @Override
    public void onRequestSuccess(List<T> t) {
        Collection<T> list = t;
        hasMoreData = false;
        boolean showEmptyView = mPage == 1 && (list == null || list.isEmpty());
        ViewUtil.setVisible(mBinding.refreshLayout, !showEmptyView);
        ViewUtil.setVisible(mBinding.emptyView, showEmptyView);

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
        if (mPage == 1 && ListUtil.isEmpty(mList)) {
            if (!NetworkUtil.isNetAvailable(mContext)) {
                mBinding.emptyView.setContent(R.string.error_net_tip, R.drawable.default_no_net);
            } else {
                mBinding.emptyView.setContent(R.string.error_data_tip, R.drawable.default_load_fail);
            }
        }
        mBinding.recyclerView.requestFail(mPage);
    }

    @Override
    public void showLoading(boolean show) {
        if (mBinding.refreshLayout.getState() == RefreshState.PullDownToRefresh
                || mBinding.refreshLayout.getState() == RefreshState.Refreshing
                || mBinding.refreshLayout.getState() == RefreshState.Loading) {
            return;
        }
        super.showLoading(show);
    }
}
