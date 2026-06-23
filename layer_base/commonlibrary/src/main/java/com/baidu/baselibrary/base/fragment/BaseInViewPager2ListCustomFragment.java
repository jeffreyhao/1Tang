package com.baidu.baselibrary.base.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseListView;
import com.baidu.baselibrary.util.ui.RefreshUtils;
import com.base.net.bean.ApiException;
import com.base.util.collection.ListUtil;
import com.base.util.net.NetworkUtil;
import com.jess.baselibrary.R;
import com.jess.baselibrary.databinding.CommonListLayout2Binding;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseInViewPager2ListCustomFragment<T, P extends BasePresenter<?>>
        extends BindingCustomFragment<CommonListLayout2Binding, P>
        implements IBaseListView<List<T>> {

    private int mLimit = 10;
    private int mPage = 1;
    private List<T> mList = new ArrayList<>();

    protected boolean hasMoreData = false;
    protected boolean loadedOnline = false;

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
        return R.layout.common_list_layout2;
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
    public void initListener() {
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

        mBinding.emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mPage = 1;
                getData();
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
        loadedOnline = true;

        boolean showEmptyView = mPage == 1 && (list == null || list.isEmpty());

        mBinding.refreshLayout.setVisibility(showEmptyView ? View.GONE : View.VISIBLE);
        mBinding.emptyView.setVisibility(showEmptyView ? View.VISIBLE : View.GONE);

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
        if (ListUtil.isEmpty(mList)) {
            if (!NetworkUtil.isNetAvailable(mContext)) {
                mBinding.emptyView.setVisibility(View.VISIBLE);
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

    @Override
    public void onRequestCacheSuccess(List<T> t) {
        hasMoreData = false;
        mPage = 1;
        Collection<T> list = t;
        boolean showEmptyView = (list == null || list.isEmpty());

        mBinding.refreshLayout.setVisibility(showEmptyView ? View.GONE : View.VISIBLE);
        mBinding.emptyView.setVisibility(showEmptyView ? View.VISIBLE : View.GONE);

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
}
