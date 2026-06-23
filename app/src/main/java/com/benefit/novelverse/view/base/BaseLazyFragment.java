package com.benefit.novelverse.view.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.benefit.novelverse.presenter.MvpActivityOrFragmentPresenter;

/**
 * 若把初始化内容放到initData实现
 * 就是采用Lazy方式加载的Fragment
 * 若不需要Lazy加载则initData方法内留空
 *
 * 注1:
 * 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 *
 * 注2:
 * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 * eg:
 * transaction.hide(aFragment);
 * transaction.show(aFragment);
 *
 * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
 * 一般用于PagerAdapter需要同时刷新全部子Fragment的场景
 * 不要new 新的 PagerAdapter 而采取reset数据的方式
 * 所以要求Fragment重新走initData方法
 * 故使用 {@link BaseLazyFragment#setForceLoad(boolean)}来让Fragment下次执行initData
 */
public abstract class BaseLazyFragment<P extends MvpActivityOrFragmentPresenter> extends BaseMvpFragment<P> {

    /**
     * 标志位，View已经初始化完成。
     */
    protected boolean isPrepared;
    /**
     * 是否第一次加载
     */
    protected boolean isFirstLoad = true;
    /**
     * <pre>
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     * 一般用于PagerAdapter需要刷新各个子Fragment的场景
     * 不要new 新的 PagerAdapter 而采取reset数据的方式
     * 所以要求Fragment重新走initData方法
     * 故使用 {@link BaseLazyFragment#setForceLoad(boolean)}来让Fragment下次执行initData
     * </pre>
     */
    protected boolean forceLoad = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirstLoad = true;
        View view=super.onCreateView(inflater, container, savedInstanceState);
        initViews(view);
        isPrepared = true;
        lazyLoad();
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInHiddenChanged, boolean isHappenedInSetUserVisibleHintMethod) {
        super.onVisibilityChangedToUser(isVisibleToUser, isHappenedInHiddenChanged, isHappenedInSetUserVisibleHintMethod);
        if(isVisibleToUser){
            lazyLoad();
        }
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared() && (!isLazyLoad()||isFragmentVisible())) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                initData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }

    /**
     * 被ViewPager移出的Fragment 下次显示时会从getArguments()中重新获取数据
     * 所以若需要刷新被移除Fragment内的数据需要重新put数据 eg:
     * Bundle args = getArguments();
     * if (args != null) {
     * args.putParcelable(KEY, info);
     * }
     */
    public void initVariables(Bundle bundle) {}

    public abstract void initData();


    protected abstract void initViews(View view);

    public boolean isPrepared() {
        return isPrepared;
    }

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    /**
     * 是否是懒加载
     * @return
     */
    protected boolean isLazyLoad(){
        return true;
    }


}
