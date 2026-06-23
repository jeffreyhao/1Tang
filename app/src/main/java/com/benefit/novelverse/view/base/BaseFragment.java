package com.benefit.novelverse.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.benefit.novelverse.event.EventDispatcher;

/**
 * fragment基类
 */
public abstract class BaseFragment extends Fragment {
    protected static final String TAG = "Fragment";

    protected EventDispatcher mEventDispatcher;
    /**
     * 记录的登录状态
     */
    private boolean mLoginStatus;
    /**
     * 是否记录过登录状态
     */
    private boolean isRecordLoginStatus;

    /**
     * 是否可见状态 为了避免和{@link Fragment#isVisible()}冲突 换个名字
     */
    protected boolean isFragmentVisible;

    protected String mPagename = getClass().getSimpleName();

    protected FragmentActivity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (FragmentActivity) context;
        }
    }

    public FragmentActivity getAttachActivity() {
        if (mActivity != null) {
            return mActivity;
        } else {
            return (FragmentActivity) getActivity();
        }
    }

    /**
     * 是否注册事件分发
     * 默认绑定，子类不需要绑定的话复写此方法返回false
     *
     * @return true 绑定EventBus事件分发 false 不绑定.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    public static <T extends BaseFragment> T newInstance(Class<T> c, Bundle extras) {
        T instance = null;
        try {
            instance = c.newInstance();
            if (extras != null) {
                Bundle bundle = new Bundle();
                bundle.putAll(extras);
                instance.setArguments(extras);
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(inflater, container, savedInstanceState);
        if (mEventDispatcher == null && isRegisterEventBus()) {
            mEventDispatcher = EventDispatcher.get();
        }
        if (mEventDispatcher != null) {
            mEventDispatcher.register(this);
        }

        return view;
    }

    protected abstract View inflate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onVisibilityChangedToUser(!hidden, true, false);
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        onVisibilityChangedToUser(getUserVisibleHint(), false, true);
    }

    /**
     * 登录状态发生变化
     */
    protected void onLoginStatusChange() {
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((!isHidden() && getUserVisibleHint())) {
            onVisibilityChangedToUser(true, false, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((!isHidden() && getUserVisibleHint())) {
            onVisibilityChangedToUser(false, false, false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mEventDispatcher != null) {
            mEventDispatcher.unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser                      true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param isHappenedInSetUserVisibleHintMethod 本次回调发生在{@link #onHiddenChanged(boolean)}方法里；false：发生在onResume或onPause或setUserVisibleHint方法里
     * @param isHappenedInHiddenChanged            本次回调发生在{@link #setUserVisibleHint(boolean)}方法里；false：发生在onResume或onPause或者 onHiddenChanged方法里
     */
    protected void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInHiddenChanged, boolean isHappenedInSetUserVisibleHintMethod) {
        if (isVisibleToUser) {
            isFragmentVisible = true;
        } else {
            isFragmentVisible = false;
        }
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    /**
     * 设置页面名称用于统计,默认为类名
     *
     * @param pagename
     */
    public void setPagename(String pagename) {
        mPagename = pagename;
    }

    public boolean isAttach() {
        return getAttachActivity() != null && isAdded();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
