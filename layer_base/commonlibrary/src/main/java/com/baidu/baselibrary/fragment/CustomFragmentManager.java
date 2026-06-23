package com.baidu.baselibrary.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.baidu.baselibrary.global.statusbar.StatusBarHelper;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.ui.AnimationUtil;
import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.clz.ClassUtil;
import com.baidu.baselibrary.util.ui.StatusBarUtil;
import com.base.util.ui.UIUtil;
import com.baidu.baselibrary.fragment.callback.OnFragmentStateChangeListener;
import com.baidu.baselibrary.fragment.view.FragmentLayout;
import com.baidu.baselibrary.fragment.view.NoSaveStateFrameLayout;
import com.jess.baselibrary.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentHostCallback;
import androidx.viewpager.widget.ViewPager;


@SuppressLint("InlinedApi")
public class CustomFragmentManager implements OnFragmentStateChangeListener {

    private static boolean sInited = false;

    /**
     * 活跃的fragment数量，超过此数量的fragment其view会被回收
     */
    public static int ACTIVE_FRAGMENT_NUMBER = 2;
    private static int mWindowAnimations = R.style.customFragmentAnimation;

    private static Method msetDrawDuringWindowsAnimatingMethod;

    private static Method convertFromTranslucentMethod;
    private static Method convertToTranslucentMethod;



    private CustomFragmentActivity mActivity;
    @Nullable
    private FragmentLayout mContainer;
    private ViewGroup mFirstFragmentContainer;


    private ArrayList<FragmentClient> mFragmentList;
    private Object mFragmentListLock = new Object();
    private SparseArray<WeakReference<CustomFragment>> mRequstCodeFragmentMap;
    private ArrayList<FragmentWindowWrapLayout> mWindowWraps;


    private Handler mViewRootImplHandler;
    private Handler mHandler;

    private boolean mBaseGuestureEnable = true;

    private boolean mIsInAnimation = false;



    public interface CoverFragmentManagerDelegate {
        CustomFragmentManager getCustomFragmentManager();
    }

    public void onPostCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try {
                mActivity.getWindow().getDecorView().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Throwable e) {
                ALog.exception("CustomFragmentManager", "onPostCreate", e);
            }
        }
        mContainer = new FragmentLayout(mActivity);
        mContainer.enableGesture(mBaseGuestureEnable);
        mContainer.setOnCoverFragmentSateChange(this);
        mContainer.attachToActivity(mActivity);
        if (mViewRootImplHandler == null) {
            mViewRootImplHandler = (Handler) ClassUtil.getField(mActivity.getWindow().getDecorView().getParent(), "mHandler");
        }
    }

    public CustomFragmentManager(CustomFragmentActivity context) {
        mActivity = context;
//        mProxyHandlerCallback = new HookHandlerCallback();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public ArrayList<FragmentClient> getFragmentList() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }
        return mFragmentList;
    }

    public LayoutInflater getInflater() {
        return LayoutInflater.from(mActivity).cloneInContext(mActivity);
    }

    private FragmentClient getFragmentClient(int i) {
        synchronized (mFragmentListLock) {
            return getFragmentList().get(i);
        }
    }

    private FragmentClient removeFragmentClient(int i) {
        synchronized (mFragmentListLock) {
            return getFragmentList().remove(i);
        }
    }

    private boolean removeFragmentClient(FragmentClient fragmentClient) {
        synchronized (mFragmentListLock) {
            return getFragmentList().remove(fragmentClient);
        }
    }


    private void addFragmentClient(FragmentClient fragmentClient) {
        synchronized (mFragmentListLock) {
            getFragmentList().add(fragmentClient);
        }
    }

    private int containsView(View childView) {
        if (mFirstFragmentContainer == null || childView == null) return -1;
        while (childView.getParent() != null) {
            if (childView.getParent() == mFirstFragmentContainer) {
                return 0;
            } else {
                if (childView.getParent() instanceof View) {
                    childView = (View) childView.getParent();
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    /**
     * finish Fragment
     * @param fragment Fragment
     * @param isFinishActivity 当剩下最后一个fragment的时候，是否连同Activity一起finish
     */
    public void finishFragment(CustomFragment fragment, boolean isFinishActivity){
        if(isFinishActivity){
            View view = fragment.getView();
            if (containsView(view) > -1) {
                if (getFragmentCount() == 1) {
                    if (fragment.getRequestCode() != CustomFragment.RESULT_CANCELED || fragment.getResultCode() != CustomFragment.RESULT_CANCELED) {
                        fragment.getActivity().setResult(fragment.getResultCode(), fragment.getResultData());
                    }
                    fragment.getActivity().finish();
                } else {
                    ((ViewGroup) view.getParent()).removeView(view);
                    removeFragment(fragment);
                    fragment.onDestroyView();
                    fragment.onDestroy();
                    fragment.onDetach();
                }
            } else {
                if(view != null) {
                    ViewParent viewParent = view.getParent();
                    if (viewParent != null && viewParent instanceof ViewGroup) {
                        ((ViewGroup) viewParent).removeViewInLayout(view);
                        viewParent.requestLayout();
                    }
                }
                if (indexOfFragment(fragment) > -1) {
                    removeFragment(fragment);
                    fragment.onDestroyView();
                    fragment.onDestroy();
                    fragment.onDetach();
                    if (mContainer != null) {
                        mContainer.addOtherFragment();
                    }
                }
            }
        }else {
            View view = fragment.getView();
            if (view != null && view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            removeFragment(fragment);
            fragment.onDestroyView();
            fragment.onDestroy();
            fragment.onDetach();
        }
    }


    public void finishFragment(CustomFragment fragment) {
        finishFragment(fragment, true);
    }

    public void finishFragmentWithAnimation(CustomFragment fragment) {
        View view = fragment.getView();
        if (mContainer != null && view != null && mContainer.indexOfChild(view) > -1) {
            if (getTopFragment() == fragment) {
                mContainer.onBackPress();
            } else {
                finishFragment(fragment);
            }
        } else {
            finishFragment(fragment);
        }
    }

    /**
     * 移除Fragment在当前管理类中的引用
     *
     * @param fragment
     */
    private void removeFragment(CustomFragment fragment) {
        synchronized (mFragmentListLock) {
            int count = getFragmentList().size();
            for (int i = 0; i < count; i++) {
                if (getFragmentClient(i).getFragment() == fragment) {
                    removeFragmentClient(i);
                    if (i > 0) {
                        CustomFragment bottomFragment = getFragmentClient(i - 1).getFragment();
                        if (fragment != null && bottomFragment != null && (fragment.getRequestCode() != CustomFragment.RESULT_CANCELED || fragment.getResultCode() != CustomFragment.RESULT_CANCELED)) {
                            bottomFragment.onFragmentResult(fragment.getRequestCode(), fragment.getResultCode(), fragment.getResultData());
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 移除Fragment在当前管理类中的引用
     *
     * @param fragmentClient
     */
    private void removeFragment(FragmentClient fragmentClient) {
        CustomFragment fragment = fragmentClient.getFragment();
        int index = mFragmentList.indexOf(fragmentClient);
        if (removeFragmentClient(fragmentClient)) {
            if (index > 0 && fragment != null) {
                restoreFragmentView(getFragmentClient(index - 1));
                CustomFragment bottomFragment = getFragmentClient(index - 1).getFragment();
                if (bottomFragment != null && bottomFragment != null && (fragment.getRequestCode() != CustomFragment.RESULT_CANCELED || fragment.getResultCode() != CustomFragment.RESULT_CANCELED)) {
                    bottomFragment.onFragmentResult(fragment.getRequestCode(), fragment.getResultCode(), fragment.getResultData());
                }
            }
        }
    }


    /**
     * 自定义窗口动画启动
     *
     * @param fragment
     * @param animationResourceId
     */
    public void startFragmentWithAnimation(CustomFragment fragment, int animationResourceId) {
        int id = mWindowAnimations;
        mWindowAnimations = animationResourceId;
        startFragmentInner(fragment, true);
        mWindowAnimations = id;
    }

    public void startFragmentWithAnimation(CustomFragment fragment, ViewGroup baseContainer, int animationResourceId) {
        int id = mWindowAnimations;
        mWindowAnimations = animationResourceId;
        startFragment(fragment, baseContainer, true);
        mWindowAnimations = id;
    }

    public void startFragmentWithoutAnimation(CustomFragment fragment) {
        startFragmentInner(false, fragment, false);
    }


    public void startFragment(CustomFragment fragment) {
        startFragmentForResult(fragment, CustomFragment.RESULT_CANCELED);
    }

    public void startFragment(CustomFragment fragment, Activity activity) {
        startFragment(fragment, (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public void startFragment(CustomFragment fragment, ViewGroup baseContainer) {
        startFragment(fragment, baseContainer, true);
    }

    public void startFragment(CustomFragment fragment, ViewGroup baseContainer, boolean useAnimation) {
        mFirstFragmentContainer = baseContainer;
        fragment.setRequestCode(CustomFragment.RESULT_CANCELED);
        startFragmentInner(fragment, useAnimation);
    }

    public void startFragment(CustomFragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        startFragmentInner(fragment, true);
    }

    private void startFragmentInner(CustomFragment fragment, boolean useWindowAnimation) {
        startFragmentInner(true, fragment, useWindowAnimation);
    }


    public void startFragmentForResult(CustomFragment fragment, int requestCode) {
        fragment.setRequestCode(requestCode);
        startFragmentInner(fragment, true);
    }


    public void replaceFragment(CustomFragment fragment, ViewGroup container) {
        App.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fragment == null) {
                    return;
                }
                fragment.setCustomFragmentManager(CustomFragmentManager.this);
                fragment.onAttach(mActivity);
                fragment.onCreate(null);

                // addView
                ViewGroup view = fragment.onCreateView(fragment.getLayoutInflater(), container, null);
                if (view == null) {
                    return;
                }
                if (view.getParent() == null) {
                    view = NoSaveStateFrameLayout.wrap(view);
                }
                container.addView(view);
                fragment.setView(view);

                fragment.onViewCreated(view, null);
                fragment.onActivityCreated(null);
                addFragmentClient(new FragmentClient(fragment));
                checkHasRealseFragment();
            }
        });
    }

    /**
     * @param originFragment
     * @param useWindowAnimation
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void startFragmentInner(final boolean isUseAnim, final CustomFragment originFragment, final boolean useWindowAnimation) {
        App.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (originFragment == null) {
                    return;
                }
                CustomFragment fragment = originFragment;
                fragment.setCustomFragmentManager(CustomFragmentManager.this);
                fragment.onAttach(mActivity);
                fragment.onCreate(null);
                if (mFirstFragmentContainer != null && getFragmentCount() == 0) {
                    ViewGroup view = fragment.onCreateView(fragment.getLayoutInflater(), mFirstFragmentContainer, null);
                    if (view == null) {
                        mFirstFragmentContainer = null;
                        mIsInAnimation = false;
                        return;
                    }
                    if (view.getParent() == null) {
                        view = NoSaveStateFrameLayout.wrap(view);
                        mFirstFragmentContainer.addView(view);
                    } else {
                        mFirstFragmentContainer = (ViewGroup) mFirstFragmentContainer.getParent();
                    }
                    fragment.setView(view);
                    fragment.onViewCreated(view, null);
                    fragment.onActivityCreated(null);
                    addFragmentClient(new FragmentClient(fragment));
                    checkHasRealseFragment();

                } else {
                    if (mIsInAnimation && useWindowAnimation) {
                        return;
                    }
                    boolean useViewAnimaiton = false;
                    ViewGroup container = null;
                    if (isUseAnim && useWindowAnimation && fragment.isFullScreen()) {
                        mIsInAnimation = true;
                        try {
                            // addWindow
                            container = addWindow(fragment.onCreateAnimation(true));
                            container.setTag(fragment);
                        } catch (Exception e) {
                            ALog.exception("CustomFragmentManager", "startFragmentInner", e);
                            mIsInAnimation = false;
                            return;
                        }
                    } else {
                        container = mContainer;
                        if (mWindowWraps != null && mWindowWraps.size() > 0) {
                            container = mWindowWraps.get(mWindowWraps.size() - 1);
                            container.setTag(fragment);
                            useViewAnimaiton = false;
                        }else if(isUseAnim){
                            useViewAnimaiton = true;
                        }
                    }
                    NoSaveStateFrameLayout wrapView = new NoSaveStateFrameLayout(mActivity);
                    if (fragment.needSetFitsSystemWindows()) {
                        wrapView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    View view = fragment.onCreateView(fragment.getLayoutInflater(), wrapView, null);
                    if (view == null) {
                        mIsInAnimation = false;
                        return;
                    }
                    if (view.getParent() == null) {
                        wrapView.addView(view);
                    }
                    fragment.setView(wrapView);
                    if (container != null && wrapView.getParent() == null) {
                        container.addView(wrapView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        if(useViewAnimaiton) {
                            try {
                                int animaitonId = fragment.onCreateAnimation(true);
                                Animation animation = null;
                                if (animaitonId == 0) {
                                    animaitonId = R.anim.push_left_in;
                                }
                                if(animaitonId > 0) {
                                    try {
                                        animation = AnimationUtils.loadAnimation(mActivity, animaitonId);
                                    } catch (Throwable e) {
                                        ALog.exception("CustomFragmentManager", "startFragmentInner", e);
                                        animation = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_in);
                                    }
                                    animation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    checkHasRealseFragment();
                                                    if (getTopFragment() != null && mContainer != null) {
                                                        mContainer.enableGesture(getTopFragment().enableGesture());
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    wrapView.startAnimation(animation);
                                }
                            } catch (Throwable e){
                                ALog.exception("CustomFragmentManager", "startFragmentInner", e);
                            }
                        }
                    }
                    fragment.onViewCreated(wrapView, null);
                    fragment.onActivityCreated(null);
                    addFragmentClient(new FragmentClient(fragment));
                }
                onHide(1);
                onShow(0);
            }
        });
    }


    @Override
    public View getFragmentView(int indexBylast) {
        FragmentClient fragmentClient = getFragmentClientByLastIndex(indexBylast);
        return fragmentClient == null ? null : restoreFragmentView(fragmentClient);
    }

    @Override
    public int getFragmentIndexByLast(View view) {
        int count = getFragmentCount();
        for (int i = count - 1; i > 0; i--) {
            CustomFragment fragment = getFragmentByLastIndex(i);
            if (fragment != null && fragment.getView() == view) {
                return i;
            }
        }
        return -1;
    }

    public View restoreFragmentView(FragmentClient fragmentClient) {
        CustomFragment fragment;
        if (fragmentClient.getFragment() != null) {
            View view = fragmentClient.getFragment().getView();
            if (mFirstFragmentContainer != null && view != null && view.getParent() == null && fragmentClient == getFragmentClientByIndex(0)) {
                mFirstFragmentContainer.addView(view);
            }
            return view;
        }
        try {
            //先使用缓存
            Class fragmentClass = null;
            fragmentClass = fragmentClient.getFragmentClass();
            if (fragmentClass == null) {
                fragmentClass = getContext().getClassLoader().loadClass(fragmentClient.getFragmentClassNameString());
            }
            fragment = (CustomFragment) fragmentClass.newInstance();
        } catch (Throwable e) {
            ALog.exception("CustomFragmentManager", "restoreFragmentView", e);
            removeFragment(fragmentClient);
            return null;
        }
        fragment.setCustomFragmentManager(this);
        fragment.setArguments(fragmentClient.getArgument());
        FragmentHostCallback mFragmentHostCallback = new FragmentHostCallback(getContext(), new Handler(), 0) {
            @Override
            public Object onGetHost() {
                return null;
            }
        };
//        ClassUtil.setField(fragment, "mHost", mFragmentHostCallback);
//        Util.setField(fragment, "mActivity", getContext());
        fragment.onAttach((Activity) getContext());
        fragment.onCreate(fragmentClient.getSaveState());
        ViewGroup view;
        ViewGroup wrapView;
        if (mFirstFragmentContainer != null && fragmentClient == getFragmentClientByIndex(0)) {
            view = fragment.onCreateView(fragment.getLayoutInflater(), mFirstFragmentContainer, fragmentClient.getSaveState());
//            ClassUtil.setField(fragment, "mView", view);
            fragment.setView(view);
            fragment.onViewCreated(view, fragmentClient.getSaveState());
            fragment.onActivityCreated(fragmentClient.getSaveState());
            fragmentClient.setFragment(fragment);
            wrapView = mFirstFragmentContainer;
            if (view.getParent() == null) {
                wrapView.addView(view);
            }
        } else {
            wrapView = new NoSaveStateFrameLayout(mActivity);
            view = fragment.onCreateView(fragment.getLayoutInflater(), wrapView, fragmentClient.getSaveState());
            if (view != wrapView) {
                wrapView.addView(view);
            }
//            ClassUtil.setField(fragment, "mView", wrapView);
            fragment.setView(wrapView);
            fragment.onViewCreated(wrapView, fragmentClient.getSaveState());
            fragment.onActivityCreated(fragmentClient.getSaveState());
            fragmentClient.setFragment(fragment);
            if (fragment.needSetFitsSystemWindows() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                wrapView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        //不能删除，因为在onViewStateRestored前得让每个控件有自己的宽高
        //手动进行measure和layout，这样调用addViewInlayout后就可以不触发递归，从而提高性能
        if(mContainer != null){
            try {
                View child = mContainer.getChildAt(0);
                int widthSpec = View.MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), View.MeasureSpec.EXACTLY);
                ViewGroup.LayoutParams lp = wrapView.getLayoutParams();
                if (lp == null) {
                    lp = new ViewGroup.LayoutParams(child.getMeasuredWidth(), child.getMeasuredHeight());
                }
                lp.width = child.getMeasuredWidth();
                if (mContainer != null && fragmentClient == getFragmentClientByIndex(0)) {
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                } else {
                    lp.height = child.getMeasuredHeight();
                }
                wrapView.setLayoutParams(lp);
                wrapView.measure(widthSpec, heightSpec);
                wrapView.layout(0, 0, wrapView.getMeasuredWidth(), wrapView.getMeasuredHeight());
            } catch (Throwable e) {
                ALog.exception("CustomFragmentManager", "restoreFragmentView", e);
            }
        }

        if (fragmentClient.getSaveState() != null) {
            fragmentClient.getSaveState().setClassLoader(fragment.getClass().getClassLoader());
        }
        fragment.onViewStateRestored(fragmentClient.getSaveState());

        return wrapView;
    }

    /**
     * 替换掉指定的fragment
     */
    public void replaceFragment(final CustomFragment oldFragment, final CustomFragment newFragment) {
        App.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //parent不为null，由parent来替换
                if (oldFragment.getParentFragment() != null && oldFragment.getParentFragment() instanceof CustomFragment) {
                    ((CustomFragment) oldFragment.getParentFragment()).replaceInnerFragment(oldFragment, newFragment);
                } else {
                    //否则开新的页面替换
                    newFragment.setCustomFragmentManager(CustomFragmentManager.this);
                    newFragment.setParentFragment(oldFragment.getParentFragment());
                    newFragment.onAttach(mActivity);
                    newFragment.onCreate(null);
                    boolean isBaseFragment;
                    if (mFirstFragmentContainer != null && mFirstFragmentContainer.getChildCount() > 0 && getFragmentCount() == 1 && oldFragment == getFragmentByIndex(0)) {
                        isBaseFragment = true;
                        ViewGroup view = newFragment.onCreateView(newFragment.getLayoutInflater(), mFirstFragmentContainer, null);
                        if(view.getParent() == null){
                            view = NoSaveStateFrameLayout.wrap(view);
                        }
                        newFragment.setView(view);
                        if (view.getParent() == null) {
//                            if (mFirstFragmentContainer.getContext() instanceof BaseActivity) {
//                                BaseActivity activityBase = (BaseActivity) mFirstFragmentContainer.getContext();
//                                activityBase.resetStatusBar(); // TODO 2023.7.14
//                            }
                            mFirstFragmentContainer.removeView(oldFragment.getView());
                            mFirstFragmentContainer.addView(view);
                        }
                        newFragment.onViewCreated(view, null);
                        newFragment.onActivityCreated(null);
                    } else {
                        isBaseFragment = false;
                        ViewGroup container = null;
                        NoSaveStateFrameLayout wrapView = new NoSaveStateFrameLayout(mActivity);
                        if (newFragment.needSetFitsSystemWindows() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            wrapView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }
                        container = mContainer;
                        if (mWindowWraps != null && mWindowWraps.size() > 0) {
                            container = mWindowWraps.get(mWindowWraps.size() - 1);
                            container.setTag(newFragment);
                        }
                        if (container == null) {
                            return;
                        }

                        View view = newFragment.onCreateView(newFragment.getLayoutInflater(), wrapView, null);
                        if (view.getParent() == null) {
                            wrapView.addView(view);
                        }
                        newFragment.setView(wrapView);
                        if (wrapView.getParent() == null) {
                            container.addView(wrapView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                        }
                        newFragment.onViewCreated(wrapView, null);
                        newFragment.onActivityCreated(null);
                    }
                    addFragmentClient(new FragmentClient(newFragment));
                    onHide(1);
                    onShow(0);

                    if (isBaseFragment) {
                        oldFragment.onDestroyView();
                        oldFragment.onDestroy();
                        oldFragment.onDetach();
                        removeFragment(oldFragment);
                    } else {
                        finishFragment(oldFragment);
                    }
                }
            }
        });
    }

    /**
     * 获取顶端的fragment
     *
     * @return
     */
    @Override
    public CustomFragment getTopFragment() {
        return getFragmentByLastIndex(0);
    }

    /**
     * 顶端的fragment是否包含SurfaceView
     *
     * @return
     */
    public boolean hasSurfaceView() {
        CustomFragment topFragment = getTopFragment();
        return topFragment != null && topFragment.hasSurfaceView();
    }

    /**
     * 获取fragment
     *
     * @return
     */
    @Override
    public CustomFragment getFragmentByLastIndex(int index) {
        synchronized (mFragmentListLock) {
            int count = getFragmentList().size();
            if (index < 0 || index >= count) return null;
            return getFragmentClient(count - 1 - index).getFragment();
        }
    }

    /**
     * 获取fragment
     *
     * @return
     */
    public CustomFragment getFragmentByIndex(int index) {
        synchronized (mFragmentListLock) {
            int count = getFragmentList().size();
            if (index < 0 || index >= count) return null;
            return getFragmentClient(index).getFragment();
        }
    }


    /**
     * 获取fragment
     *
     * @return
     */
    public FragmentClient getFragmentClientByLastIndex(int index) {
        synchronized (mFragmentListLock) {
            int count = getFragmentList().size();
            if (index < 0 || index >= count) return null;
            return getFragmentClient(count - 1 - index);
        }
    }

    /**
     * 获取fragment
     *
     * @return
     */
    public FragmentClient getFragmentClientByIndex(int index) {
        synchronized (mFragmentListLock) {
            int count = getFragmentList().size();
            if (index < 0 || index >= count) return null;
            return getFragmentClient(index);
        }
    }

    /**
     * 返回fragment在树中的序号
     *
     * @param fragment
     * @return 序号
     */
    public int indexOfFragment(CustomFragment fragment) {
        synchronized (mFragmentListLock) {
            FragmentClient fragmentClient;
            int size = getFragmentList().size();
            for (int i = 0; i < size; i++) {
                fragmentClient = getFragmentClient(i);
                if (fragmentClient.mFragment == fragment) return i;
            }
            return -1;
        }
    }

    @Override
    public boolean onBackPress() {
        InputMethodManager m = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m.isActive() && mContainer != null) {
            m.hideSoftInputFromWindow(mContainer.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        CustomFragment fragment = getTopFragment();
        if (fragment != null) {
            if (fragment.onBackPress()) {
                return true;
            }
            if (mContainer != null) {
                return mContainer.onBackPress();
            }
        }
        return false;
    }

    @Override
    public void onShow(int indexByLast) {
        CustomFragment fragment = getFragmentByLastIndex(indexByLast);
        if (fragment != null) {
            fragment.onStart();
            fragment.onResume();
            mActivity.getWindow().setSoftInputMode(fragment.getInputMode());
            if(fragment.needSetFitsSystemWindows()){
                StatusBarUtil.setStatusNavigationBar(mActivity, fragment.isStatusDarkStyle(), fragment.isNavigationDarkStyle());
            }
        }
    }

    @Override
    public void onHide(int indexByLast) {
        FragmentClient fragment = getFragmentClientByLastIndex(indexByLast);
        if (fragment != null && fragment.getFragment() != null) {
            fragment.getFragment().onPause();
            fragment.getFragment().onStop();
            fragment.onSaveInstanceState(fragment.getFragment());
        }
    }

    @Override
    public void onDestroy(View view) {
        int indexByLast = destroy(view);
        if (indexByLast == 0) {
            InputMethodManager m = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (m.isActive() && mContainer != null) {
                m.hideSoftInputFromWindow(mContainer.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            onShow(0);
        }
        if (getFragmentCount() == 0 && (mFirstFragmentContainer != null ||
                mFirstFragmentContainer == null && indexByLast < 0)) {
            mActivity.finish();
            AnimationUtil.overridePendingTransition(mActivity, 0, 0);
        }
    }


    private int destroy(View view) {
        int index = -1;
        for (int i = 0; i < getFragmentList().size(); i++) {
            FragmentClient fragmentClient = getFragmentClientByLastIndex(i);
            if (fragmentClient != null) {
                CustomFragment fragment = fragmentClient.getFragment();
                if (fragment != null &&
                        (fragment.getView() == view) ||
                        ((view instanceof ViewGroup) &&
                                UIUtil.containView((ViewGroup) view, fragment.getView()))) {
                    fragment.setIsFinishing(true);
                    fragment.onPause();
                    fragment.onStop();
                    fragment.onDestroyView();
                    fragment.onDestroy();
                    fragment.onDetach();
                    removeFragment(fragmentClient);
                    index = i;
                    break;
                }
            }
        }
        int activeFragmentCount = checkActiveFragmentCount();

        if (mContainer != null) {
            if (getTopFragment() != null) {
                if(getFragmentCount() ==1 && mFirstFragmentContainer != null){
                    mContainer.enableGesture(mBaseGuestureEnable & getTopFragment().enableGesture());
                }else {
                    mContainer.enableGesture(getTopFragment().enableGesture());
                }
            } else {
                mContainer.enableGesture(activeFragmentCount > 0 && mBaseGuestureEnable);
            }
        }
        return index;
    }

    @Override
    public void onDestroy(int indexByLast) {
        FragmentClient fragmentClient = getFragmentClientByLastIndex(indexByLast);
        if(fragmentClient == null)return;
        CustomFragment fragment = fragmentClient.getFragment();
        if (fragment != null) {
            fragment.onDestroyView();
            fragment.onDestroy();
            fragment.onDetach();
        }
        removeFragmentClient(fragmentClient);
    }

    /**
     * 获取可以保留的页面个数
     *
     * @return
     */
    private int checkActiveFragmentCount() {
        int fragmentCount = getFragmentCount();
        int num = 0;
        int i = 0;
        CustomFragment fragment;
        for (; i < fragmentCount; i++) {
            FragmentClient fragmentClient = getFragmentClientByLastIndex(i);
            restoreFragmentView(fragmentClient);
            fragment = fragmentClient.getFragment();
            if (fragment.isFullScreen()) {
                num++;
                if (num == ACTIVE_FRAGMENT_NUMBER) {
                    return i + 1;
                }
            }
        }
        return i;
    }

    /**
     * 检查是否存在可以释放的Fragment
     */
    public void checkHasRealseFragment() {
        int canActCount = checkActiveFragmentCount();
        if (getFragmentCount() > canActCount) {
            FragmentClient fragmentClient;
            for (int i = getFragmentCount() - 1 - canActCount; i >= 0; i--) {
                fragmentClient = getFragmentClientByIndex(i);
                //只有可以回收的才回收，目前书城是不可以回收的
                if (fragmentClient != null && fragmentClient.mFragment != null) {
                    if (fragmentClient.mFragment.getView() != null &&
                            fragmentClient.mFragment.getView().getParent() != null) {
                        ((ViewGroup) fragmentClient.mFragment.getView().getParent()).removeView(fragmentClient.mFragment.getView());
                    }
                    if (fragmentClient.mFragment.canRecycle()) {
                        fragmentClient.realseMemory();
                    }
                }
            }
        }
    }

    public void onStart() {
        CustomFragment f = getTopFragment();
        if (f != null) {
            f.onStart();
        }
    }

    public void onStop() {
        CustomFragment f = getTopFragment();
        if (f != null) {
            f.onStop();
        }
    }

    /**
     * 获得交点
     */
    public void onResume() {
        CustomFragment f = getTopFragment();
        if (f != null) {
            f.onResume();
        }
    }

    /**
     * 失去焦点
     */
    public void onPause() {
        CustomFragment f = getTopFragment();
        if (f != null) {
            f.onPause();
        }
    }

    private void clearAllWindows() {
        if (mWindowWraps != null) {
            for (FragmentWindowWrapLayout wrapView : mWindowWraps) {
                try {
                    wrapView.resetIsAddedInViewrootImplClass();
                    mActivity.getWindowManager().removeView(wrapView);
                } catch (Throwable e){
                    ALog.exception("CustomFragmentManager", "clearAllWindows", e);
                }
            }
            mWindowWraps.clear();
        }
    }

    public void onDestroy() {
        clearAllWindows();
        try {
            CustomFragment f;
            for (int i = getFragmentList().size() - 1; i >= 0; i--) {
                f = removeFragmentClient(i).getFragment();
                if (f != null) {
                    f.setIsFinishing(true);
                    f.onDestroyView();
                    f.onDestroy();
                    f.onDetach();
                }
            }
        } catch (Throwable e) {
            ALog.exception("CustomFragmentManager", "onDestroy", e);
        }
    }

    /**
     * @return 是否允许添加层
     */
    public boolean isEnableAddLayer() {
        return true;
    }

    public CustomFragmentActivity getContext() {
        return mActivity;
    }

    public boolean isCoverViewShow() {
        return getFragmentList().size() > 1;
    }

    public void setGuestEnable(boolean enable) {
        if (mContainer != null) {
            mContainer.setCanScroll(enable);
        }
    }

    @Override
    public int getFragmentCount() {
        return getFragmentList().size();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CustomFragment fragment = null;
        if (mRequstCodeFragmentMap != null) {
            WeakReference<CustomFragment> wr = mRequstCodeFragmentMap.get(requestCode);
            if (wr != null) {
                fragment = wr.get();
            }
            mRequstCodeFragmentMap.remove(requestCode);
        }

        if (fragment == null) {
            fragment = getTopFragment();
        }

        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private ViewGroup addWindow(int animation)  {
        final FragmentWindowWrapLayout newContainer = new FragmentWindowWrapLayout(mActivity);
        if (mContainer != null) {
            mContainer.setCanScroll(false);
//            mContainer.setSkipDraw(true);
        }
        if (animation == 0) {
            animation = mWindowAnimations;
        }

        WindowManager.LayoutParams nl = new WindowManager.LayoutParams();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            nl.copyFrom(mActivity.getWindow().getAttributes());
//        }
        nl.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_SPLIT_TOUCH |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

        nl.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        nl.windowAnimations = animation;
        nl.format = PixelFormat.TRANSLUCENT;

        if(mContainer != null){
            View child0 = mContainer.getChildAt(0);
            if (child0 != null && child0.getHeight() != 0 && child0.getWidth() != 0) {
                nl.height = mContainer.getChildAt(0).getHeight();
                nl.width = mContainer.getChildAt(0).getWidth();
                int location[] = new int[2];
                mContainer.getChildAt(0).getLocationInWindow(location);
                nl.x = location[0];
                nl.y = location[1];
            }
        }
        nl.gravity = Gravity.TOP | Gravity.LEFT;
//        if (!getTopFragment().isFullScreen()) {
//            nl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;//使用window自带的背景半透明渐变效果
//            nl.dimAmount = 0.4f;
//        }
        //fix #58861
        try {
            mActivity.getWindowManager().addView(newContainer, nl);
        } catch (Exception e) {
            ALog.exception("CustomFragmentManager", "addWindow", e);
            try {
                newContainer.resetIsAddedInViewrootImplClass();
                mActivity.getWindowManager().removeView(newContainer);
                newContainer.removeAllViews();
                if(mContainer != null){
                    mContainer.setCanScroll(true);
                }
//                mContainer.setSkipDraw(false);
            } catch (Throwable e1) {
                ALog.exception("CustomFragmentManager", "addWindow-e1", e1);
            }
            throw e;
        }
        if (mWindowWraps == null) {
            mWindowWraps = new ArrayList<>();
        }
        mWindowWraps.add(newContainer);
//        setStatusBarMode(SkinUtil.needAddStatusCover());
        if (!sInited) {
            msetDrawDuringWindowsAnimatingMethod = ClassUtil.getMethod(newContainer.getParent().getClass(), "setDrawDuringWindowsAnimating", boolean.class);
            sInited = true;
        }
        if (msetDrawDuringWindowsAnimatingMethod != null) {
            try {
                msetDrawDuringWindowsAnimatingMethod.invoke(newContainer.getParent(), true);
            } catch (Throwable e2) {
                ALog.exception("CustomFragmentManager", "addWindow-e2", e2);
            }
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            if (mViewRootImplHandler == null) {
//                mViewRootImplHandler = (Handler) Util.getField(mActivity.getWindow().getDecorView().getParent(), "mHandler");
//            }
//            Util.setField(Util.getField(newContainer.getParent(), "mHandler"), "mCallback", mProxyHandlerCallback);
//        }
//        BaseFragment topFragment = getTopFragment();
//        if (topFragment != null && topFragment.isAnimating() != true) {
//            View view = topFragment.getView();
//            if (view != null && view instanceof OnAnimatingListener) {
//                ((OnAnimatingListener) view).onAnimating(true);
//            }
//        }
        newContainer.setOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                newContainer.setOnPreDrawListener(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mContainer != null){
                            mContainer.setCanScroll(true);
                        }
//                        mContainer.setSkipDraw(false);
                        removeLastWindow(newContainer);
                    }
                }, 500);
                return false;
            }
        });
        return newContainer;
    }

//    Runnable mRemoveLastWindowRunable = new Runnable() {
//        @Override
//        public void run() {
//            MetaApplication.getInstance().getHandler().removeCallbacks(this);
//            removeLastWindow();
//        }
//    };

    private void removeLastWindow(final FragmentWindowWrapLayout windowWrapView) {
        CustomFragment fragment;
        fragment = (CustomFragment) windowWrapView.getTag();
        if (fragment == null) return;
        if(mContainer == null){
            ALog.textSingle("CustomFragmentManager", "", "");
            return;
        }
        View child = fragment.getView();
        if (mWindowWraps != null && mWindowWraps.size() > 0) {
            mWindowWraps.remove(windowWrapView);
        }
        if (child != null && child.getParent() instanceof FragmentWindowWrapLayout) {
            ((FragmentWindowWrapLayout) child.getParent()).detachAllViewsFromParentExt();
            if(child.isLayoutRequested()){
                mContainer.addView(child);
            }else {
                mContainer.addViewInLayout(child, -1, child.getLayoutParams(), true);
            }
            if(!child.hasFocus() && child.findFocus() == null) {
                child.requestFocus();
            }
            setViewPagerNotFirstLayout(child);
        }
        if (getTopFragment() != null) {
            mContainer.enableGesture(getTopFragment().enableGesture());
        }

        mContainer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            windowWrapView.resetIsAddedInViewrootImplClass();
                            mActivity.getWindowManager().removeView(windowWrapView);
                        } catch(Throwable e1){
                            ALog.exception("CustomFragmentManager", "removeLastWindow-e1", e1);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        windowWrapView.resetIsAddedInViewrootImplClass();
                                        mActivity.getWindowManager().removeViewImmediate(windowWrapView);
                                    } catch (Throwable e2){
                                        ALog.exception("CustomFragmentManager", "removeLastWindow-e2", e2);
                                    }
                                }
                            }, 20);
                        }
                        mIsInAnimation = false;
                        checkHasRealseFragment();
                    }
                },20);
                return false;
            }
        });
        ViewCompat.postInvalidateOnAnimation(mContainer);
    }

    private static void setViewPagerNotFirstLayout(View view) {
        if (view != null && ClassUtil.instanceOfClass(view.getClass(), ViewGroup.class)) {
            if (ClassUtil.instanceOfClass(view.getClass(), ViewPager.class)) {
//                ClassUtil.setField(view, "mFirstLayout", false);
            } else {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                    setViewPagerNotFirstLayout(viewGroup.getChildAt(i));
                }
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return getTopFragment() != null && getTopFragment().dispatchKeyEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return getTopFragment() != null && getTopFragment().onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return getTopFragment() != null && getTopFragment().onKeyUp(keyCode, event);
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        if (getTopFragment() != null) {
            getTopFragment().onMultiWindowModeChanged(isInMultiWindowMode);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return getTopFragment() != null && getTopFragment().dispatchTouchEvent(ev);
    }

    public void clearTop() {
        if (mContainer != null) {
            mContainer.clearTop();
        }
    }

    public void setStatusBarMode(boolean isDark) {
//        if (!ThemeUtil.needAddStatusCover() && isDark) {
//            isDark = false;
//        }
//        if (mWindowWraps != null && mWindowWraps.size() > 0) {
//            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mWindowWraps.get(mWindowWraps.size() -1 ).getLayoutParams();
//            StatusBarUtil.setStatusBarMode(mActivity.getWindowManager(), params, mWindowWraps.get(mWindowWraps.size() -1 ), isDark);
//        }
//        StatusBarUtil.setStatusBarMode(mActivity, isDark);
    }

    public void setStatusBarMode(CustomFragment fragment, boolean isDark) {
        if (getTopFragment() != fragment) return;
        if (isDark) {
            isDark = false;
        }
        if (mWindowWraps != null && mWindowWraps.size() > 0) {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mWindowWraps.get(mWindowWraps.size() - 1).getLayoutParams();
            StatusBarHelper.setStatusBarMode(mActivity.getWindowManager(), params, mWindowWraps.get(mWindowWraps.size() - 1), isDark);
        }
        StatusBarHelper.setStatusBarMode(mActivity, isDark);
    }

    @Override
    public void setAnimating(boolean isAnimating) {
        if (getTopFragment() != null && getTopFragment().isAnimating() != isAnimating) {
            getTopFragment().setIsAnimating(isAnimating);
        }

        CustomFragment secondFragment = getFragmentByLastIndex(1);
        if (secondFragment != null && secondFragment.isAnimating() != isAnimating) {
            secondFragment.setIsAnimating(isAnimating);
        }
    }

    @Override
    public boolean isContainerNull() {
        return mFirstFragmentContainer == null;
    }

    @Override
    public boolean isContainTopFragment() {
        return mWindowWraps == null || mWindowWraps.size() == 0;
    }

    public void startActivityForResult(CustomFragment fragment, Intent intent, int requestCode) {
        if (mRequstCodeFragmentMap == null) {
            mRequstCodeFragmentMap = new SparseArray<>();
        }
        mRequstCodeFragmentMap.put(requestCode, new WeakReference<>(fragment));
        mActivity.startActivityForResult(intent, requestCode);
    }

    @Nullable
    public ViewGroup getContainer() {
        return mContainer;
    }

    public void setNightView(View nightView) {
        if(mContainer != null){
            mContainer.setNightView(nightView);
        }
    }

    public void setGestureEnable(boolean enable) {
        mBaseGuestureEnable = enable;
        if (mContainer != null) {
            mContainer.enableGesture(enable);
        }
    }

    public void registerFragmentLifecycleCallback(FragmentLifecycleCallback callback) {
        FragmentCallbackDispatcher.registerFragmentLifecycleCallback(callback);
    }

    public void unregisterFragmentLifecycleCallback(FragmentLifecycleCallback callback) {
        FragmentCallbackDispatcher.unregisterFragmentLifecycleCallback(callback);
    }
}
