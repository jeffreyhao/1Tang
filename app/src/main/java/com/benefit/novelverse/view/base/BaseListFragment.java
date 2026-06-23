package com.benefit.novelverse.view.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.benefit.novelverse.R;
import com.benefit.novelverse.presenter.BaseListPresenter;
import com.meta.ui.widget.WrapLayoutManager;
import com.fold.recyclyerview.BaseQuickAdapter;
import com.fold.recyclyerview.flexibledivider.FlexibleDividerDecoration;
import com.fold.recyclyerview.flexibledivider.HorizontalDividerItemDecoration;
import com.meta.bean.model.api.APIError;
import com.tencent.common.util.Abase;
import com.tencent.common.util.ConvertUtils;
import com.tencent.common.util.EmptyUtils;
import com.tencent.common.util.StringUtils;
import com.tencent.common.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 列表基本Fragment
 */
public abstract class BaseListFragment<D, P extends BaseListPresenter, T extends BaseQuickAdapter> extends BaseLazyFragment<P>
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, ListPage<D, P> {

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected LottieAnimationView mAnimFrameLoading;

    protected T mListAdapter;

    protected RecyclerView.LayoutManager mLayoutManager;

    protected View mErrorView;

    protected boolean mHeadAndEmptyEnable;
    protected boolean mFootAndEmptyEnable;

    /**
     * 数据是否结束
     */
    protected boolean isEnd;

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    protected void onLoginStatusChange() {
        onRefresh();
    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = getLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mListAdapter = createAdapter();
        if(mListAdapter == null && getActivity() != null) {
            getActivity().finish();
            return;
        }
        mListAdapter.setHeaderAndEmpty(true);
        mRecyclerView.setAdapter(mListAdapter);
        mListAdapter.setEnableLoadMore(isLoadMoreEnable());
        if (isLoadMoreEnable()) {
            mListAdapter.setOnLoadMoreListener(this, mRecyclerView);
        }
        mListAdapter.setPreLoadNumber(3);
        mSwipeRefreshLayout = view.findViewById(R.id.list_swipeLayout);
        if (isShowLoading()) {
            mAnimFrameLoading = view.findViewById(R.id.list_progress);
            startLoadingView();
        }
        if (mSwipeRefreshLayout != null) {
            if (isSwipeLayoutEnable()) {
                mSwipeRefreshLayout.setOnRefreshListener(this);
                mSwipeRefreshLayout.setColorSchemeColors(Abase.getResources().getColor(R.color.colorPrimaryDark, mSwipeRefreshLayout.getContext().getTheme()));
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
        }
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    /**
     * layout id
     *
     * @return 布局Id
     */
    protected int getLayoutId() {
        if (!isSwipeLayoutEnable() && !isShowLoading()) {
            return R.layout.fragment_recylerview;
        } else if (!isSwipeLayoutEnable() && isShowLoading()) {
            return R.layout.fragment_loading_list;
        } else {
            return R.layout.fragment_list;
        }
    }

    /**
     * RecyclerView.ItemDecoration
     *
     * 如果不需要，可以复写返回null
     * 如果要更换样式，可以复写自定义{@link FlexibleDividerDecoration}
     */
    protected RecyclerView.ItemDecoration getItemDecoration() {
        if (mListAdapter instanceof FlexibleDividerDecoration.VisibilityProvider) {
            return new HorizontalDividerItemDecoration.Builder(getAttachActivity())
                    .color(Abase.getResources().getColor(R.color.divider_line_color, Abase.getTheme()))
                    .sizeResId(R.dimen.small_divider).margin(Abase.getResources().getDimensionPixelSize(R.dimen.spacing_medium), Abase.getResources().getDimensionPixelSize(R.dimen.spacing_medium))
                    .visibilityProvider((FlexibleDividerDecoration.VisibilityProvider) mListAdapter)
                    .build();
        }
        return null;
    }

    @Override
    public void initData() {
         if (mPresenter!=null && !mPresenter.isLoading() && mPresenter.isEmptyData()) {
            if (isShowLoading()) {
                startLoadingView();
            }
            isEnd=false;
            mPresenter.getDatas(getLimit(),true);
        }
    }

    /**
     * 空数据时是否隐藏 header
     *
     * @param headAndEmptyEnable 是否隐藏
     */
    public void setHeadAndEmptyEnable(boolean headAndEmptyEnable) {
        mHeadAndEmptyEnable = headAndEmptyEnable;
    }

    /**
     * 空数据时是否隐藏 footer
     *
     * @param footAndEmptyEnable 是否隐藏
     */
    public void setFootAndEmptyEnable(boolean footAndEmptyEnable) {
        mFootAndEmptyEnable = footAndEmptyEnable;
    }

    /**
     * 异常处理
     *
     * @param exception 异常
     */
    @SuppressWarnings("ResourceType")
    protected void handleError(APIError exception) {
        if (exception == null || getAttachActivity() == null
                || isDetached() || !isAttach()) return;
        if (mPresenter.isEmptyData()) {
            //没数据才展示error view
            mErrorView = setUpErrorView(exception);
            if (mErrorView == null) {
                mErrorView = View.inflate(getAttachActivity(),getErrorLayout(), null);
                TextView errorText = mErrorView.findViewById(R.id.error_text);
                ImageView errorImage = mErrorView.findViewById(R.id.error_img);
                TextView errorReloadText = mErrorView.findViewById(R.id.error_reload_text);
                if (exception.errorCode == APIError.ERROR_NO_NET) {
                    ViewUtils.setGone(errorReloadText,false);
                    errorImage.setImageResource(R.drawable.default_no_net);
                    errorText.setText(getResources().getString(R.string.error_net_tip));
                } else if (exception.errorCode == APIError.ERROR_NO_DATA_CODE || exception.errorCode == APIError.ERROR_LIST_EMPTY) {
                    errorImage.setImageResource(R.drawable.default_no_data);
                    ViewUtils.setGone(errorReloadText,true);
                    errorText.setText(getResources().getString(R.string.error_empty_data_tip));
                } else {
                    ViewUtils.setGone(errorReloadText,false);
                    errorImage.setImageResource(R.drawable.default_load_fail);
                    errorText.setText(getResources().getString(R.string.error_data_tip));
                }

                if(mListAdapter.getHeaderLayoutCount()>0&&mListAdapter.getHeaderLayout().getVisibility()==View.VISIBLE){
                    ViewGroup.MarginLayoutParams layoutParams=new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mErrorView.setPadding(0, ConvertUtils.dp2px(130),0, ConvertUtils.dp2px(50));
                    mErrorView.setLayoutParams(layoutParams);
                }

                errorReloadText.setOnClickListener(v -> onRefresh());
            }
//            if (ReadSettingManager.getInstance().isNightMode() && (getAttachActivity() instanceof ReadActivity)) {
//                mErrorView.findViewById(R.id.error_img).setAlpha(0.4f);
//                ((TextView) mErrorView.findViewById(R.id.error_text)).setTextColor(ContextCompat.getColor(getAttachActivity(), R.color.pp_color_666666));
//            } else {
                mErrorView.findViewById(R.id.error_img).setAlpha(1.0f);
                ((TextView) mErrorView.findViewById(R.id.error_text)).setTextColor(ContextCompat.getColor(getAttachActivity(), R.color.pp_color_999999));
//            }
            mListAdapter.setEmptyView(mErrorView);
            mListAdapter.setHeaderFooterEmpty(mHeadAndEmptyEnable, mFootAndEmptyEnable);
            isEnd = true;
            mListAdapter.setNewData(new ArrayList());
            mListAdapter.loadMoreEnd(goneLoadMoreEnd());
        } else if (!StringUtils.isTrimEmpty(exception.errorUserMsg)) {
            if (exception.errorCode == APIError.ERROR_NO_DATA_CODE || exception.errorCode == APIError.ERROR_LIST_EMPTY) {
                isEnd = true;
                if(mPresenter.isEmptyData()){
                    mListAdapter.setNewData(new ArrayList());
                }
                mListAdapter.loadMoreEnd(goneLoadMoreEnd());
            } else if (exception.errorCode == APIError.ERROR_NO_NET) {
                if(mListAdapter.isLoading()){
                    mListAdapter.loadMoreFail();
                }
            } else {
                if(mListAdapter.isLoading()){
                    mListAdapter.loadMoreFail();
                }
            }
        } else {
            if(mListAdapter.isLoading()){
                mListAdapter.loadMoreFail();
            }
        }
    }

    /**
     * 设置错误页面
     */
    public View setUpErrorView(APIError exception) {
        return null;
    }

    /**
     * 当列表滑动到倒数第N个Item的时候(默认是3)回调onLoadMoreRequested方法
     */
    public void setPreLoadNumber(int preLoadNumber) {
        mListAdapter.setPreLoadNumber(preLoadNumber);
    }


    @Override
    public void onLoadMoreRequested() {
        if (!mPresenter.isLoading()) {
            if (isEnd) {
                mListAdapter.loadMoreEnd(goneLoadMoreEnd());
            } else {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setEnabled(false);
                }
                mPresenter.getDatas(getLimit(),false);
            }
        }
    }


    @Override
    public void onRefresh() {
        if(mListAdapter==null){
            if (mSwipeRefreshLayout != null) {
                if (isSwipeLayoutEnable()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
            return;
        }
        isEnd = false;

        mListAdapter.setEnableLoadMore(false);
        if (mSwipeRefreshLayout != null) {
            if (isSwipeLayoutEnable() && !mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        }
        mPresenter.getDatas(getLimit(),true);
    }

    @Override
    public void refreshData(List<D> list, boolean forceRefresh, APIError excepiton) {
        if (isShowLoading()) {
            stopLoadingView();
        }
        if (mSwipeRefreshLayout != null) {
            if (isSwipeLayoutEnable()) {
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
            }
        }
        mListAdapter.setEnableLoadMore(isLoadMoreEnable());
        if (excepiton != null) {
            //有异常
            handleError(excepiton);

        } else {
            if (EmptyUtils.isNotEmpty(list)) {
                if (forceRefresh || mListAdapter.getData().isEmpty()) {
                    mListAdapter.setNewData(list);
                }
                mListAdapter.loadMoreComplete();
            } else {
                if (mPresenter.isEmptyData()) {
                    handleError(excepiton);
                } else {
                    mListAdapter.loadMoreFail();
                }
            }
        }
    }

    protected void stopLoadingView() {
        if (null == mAnimFrameLoading) {
            return;
        }
        mAnimFrameLoading.cancelAnimation();
        mAnimFrameLoading.setVisibility(View.GONE);
    }

    protected void startLoadingView() {
        if (null == mAnimFrameLoading) {
            return;
        }
        mAnimFrameLoading.playAnimation();
        mAnimFrameLoading.setVisibility(View.VISIBLE);
    }


    protected abstract T createAdapter();

    /**
     * 是否可以下拉刷新
     * 如果不需要，可以复写返回false
     */
    protected boolean isSwipeLayoutEnable() {
        return true;
    }

    /**
     * 是否需要展示loading
     * 如果不需要，可以复写返回false
     */
    protected boolean isShowLoading() {
        return true;
    }

    /**
     * 是否展示加载结束view
     */
    protected boolean goneLoadMoreEnd() {
        return false;
    }

    /**
     * 是否允许加载更多
     */
    protected boolean isLoadMoreEnable() {
        return true;
    }

    /**
     * 返回LayoutManager，默认为LinearLayoutManager
     */
    protected RecyclerView.LayoutManager getLayoutManager(){
        return new WrapLayoutManager(getAttachActivity());
    }

    /**
     * 每次获取的数据量，默认为8
     */
    protected int getLimit(){
        return BaseListPresenter.DEFAULT_LIMIT;
    }

    /**
     * 错误页面
     */
    protected int getErrorLayout(){
        return  R.layout.layout_error;
    }

    public T getListAdapter() {
        return mListAdapter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void refreshListData() {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }
}
