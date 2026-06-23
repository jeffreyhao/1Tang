package com.baidu.baselibrary.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.global.WeakHandler;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.fragment.callback.OnAnimatingListener;
import com.baidu.baselibrary.fragment.toolbar.IToolbar;
import com.baidu.baselibrary.fragment.toolbar.MetaToolbar;
import com.baidu.baselibrary.fragment.toolbar.ToolbarManager;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;


/**
 * Created by haojiangfeng on 2023/7/14.
 */
public class CustomFragment implements IFragment, IToolbar, Handler.Callback, LifecycleOwner, Fragment {

    public interface OnFinishCallback {
        void onFinish(CustomFragment fragment);
    }


    public static final int RESULT_CANCELED    = 0;

    private boolean mIsFinishing = false;                       //标记是否在销毁的过程中
    private boolean mIsAnimating = false;                       //标记是否在执行动画或者拖动中
    private boolean mIsImmersive = true;
    private boolean mIsAdded    = false;
    private boolean mIsDetached = true;
    private boolean mIsResumed  = false;
    private boolean mIsStopped  = true;

    protected MetaToolbar mToolbar;
    protected ViewGroup mView;
    protected FragmentActivity mActivity;
    protected Bundle mArguments;

    protected int mResultCode = RESULT_CANCELED;                  //退出时设置的ResultCode
    protected int mRequestCode = RESULT_CANCELED;                 //其他页面进入当前界面时携带的request code
    protected Intent mResultData = null;                          //回退到上一个Fragment需要传递的数据


    protected CustomFragment mParentFragment;
    /**
     * BaseFragment的管理器
     */
    protected CustomFragmentManager mFragmentManager;

    private WeakHandler mHandler;

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private OnFinishCallback mOnFinishCallback;


    public CustomFragment(){
    }

    public void setOnFinishCallback(OnFinishCallback callback){
        this.mOnFinishCallback = callback;
    }

    public String className(){
        return getClass().getSimpleName();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return mParentCallback != null && mParentCallback.get() != null && mParentCallback.get().handleMessage(msg);
    }

    /**
     * 返回是否叠加超过一定层次后回收当前fragment
     */
    @Override
    public boolean canRecycle() {
        return true;
    }

    @Override
    public boolean isDetached() {
        return false;
    }


    public Activity getContext(){
        return mActivity;
    }

    public FragmentActivity getActivity(){
        return mActivity;
    }

    public ViewGroup getView(){
        return mView;
    }

    public void setView(ViewGroup view){
        mView = view;
    }

    public Resources getResources() {
        return mActivity == null ? App.getResources() : mActivity.getResources();
    }

    public String getString(int id){
        return getResources().getString(id);
    }

    public String getString(int id, Object... formatArgs){
        return getResources().getString(id, formatArgs);
    }

    public LayoutInflater getLayoutInflater() {
        return mActivity.getLayoutInflater();
    }


    // TODO: 2023/7/17
    private WeakReference<Handler.Callback> mParentCallback;

    /**
     * 该方法应该在onAttach之后调用
     *
     * @param callback
     */
    public void setParentCallback(Handler.Callback callback) {
        mParentCallback = new WeakReference<>(callback);
    }

    public WeakHandler getHandler() {
        if (mHandler == null) {
            mHandler = new WeakHandler(this);
        }
        return mHandler;
    }



    @Override
    public void onAttach(Activity activity) {
        if(GlobalBuildConfig.LOG_DEBUG){
            ALog.lifeCycle(LogTag.Fragment, className(), "onAttach()");
        }
        mActivity = (FragmentActivity) activity;
        if (activity instanceof Handler.Callback) {
            mParentCallback = new WeakReference<>((Handler.Callback) activity);
        }
        mIsImmersive = ((CustomFragmentActivity) activity).isTransparentStatusBarAble();
        mIsAdded = true;
        mIsDetached = false;

        try {
            FragmentCallbackDispatcher.onFragmentAttached(mFragmentManager, this, activity);
        } catch (Throwable e){
            ALog.exception(className(), "onAttach", e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ALog.lifeCycle(LogTag.Fragment, className(), "onCreate()");
        try {
            FragmentCallbackDispatcher.onFragmentCreated(mFragmentManager, this, savedInstanceState);
            mLifecycleRegistry.addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source,
                                           @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_STOP) {
                        if (mView != null) {
                            mView.cancelPendingInputEvents();
                        }
                    }
                }
            });
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        } catch (Throwable e){
            ALog.exception(className(), "onCreate", e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ALog.lifeCycle(LogTag.Fragment, className(), "onViewCreated()");
    }

    @Override
    public void onStart() {
        ALog.lifeCycle(LogTag.Fragment, className(), "onStart()");
        mIsStopped = false;
        try {
            FragmentCallbackDispatcher.onFragmentStarted(mFragmentManager, this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        } catch (Throwable e){
            ALog.exception(className(), "onStart", e);
        }
    }

    @Override
    public void onResume() {
        ALog.lifeCycle(LogTag.Fragment, className(), "onResume()");
        mIsResumed = true;
        mIsStopped = false;
        try {
            FragmentCallbackDispatcher.onFragmentResumed(mFragmentManager, this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        } catch (Throwable e){
            ALog.exception(className(), "onResume", e);
        }
    }

    @Override
    public void onPause() {
        ALog.lifeCycle(LogTag.Fragment, className(), "onPause()");
        mIsResumed = false;
        try {
            FragmentCallbackDispatcher.onFragmentPaused(mFragmentManager, this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        } catch (Throwable e){
            ALog.exception(className(), "onPause", e);
        }
    }

    @Override
    public void onStop() {
        ALog.lifeCycle(LogTag.Fragment, className(), "onStop()");
        mIsResumed = false;
        mIsStopped = true;
        try {
            FragmentCallbackDispatcher.onFragmentStopped(mFragmentManager, this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        } catch (Throwable e){
            ALog.exception(className(), "onStop", e);
        }
    }

    @Override
    public void onDestroyView() {
        ALog.lifeCycle(LogTag.Fragment, className(), "onDestroyView()");
        mIsResumed = false;
    }

    @Override
    public void onDestroy() {
        ALog.lifeCycle(LogTag.Fragment, className(), "onDestroy()");

        try {
            FragmentCallbackDispatcher.onFragmentDestroyed(mFragmentManager, this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        } catch (Throwable e){
            ALog.exception(className(), "onDestroy", e);
        }
    }

    @Override
    public void onDetach() {
        ALog.lifeCycle(LogTag.Fragment, className(), "onDetach()");

        mIsDetached = true;
        mIsAdded = false;
    }


    @Override
    public void finish() {
        if(getCustomFragmentManager() != null){
            App.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getCustomFragmentManager().finishFragmentWithAnimation(CustomFragment.this);
                }
            });
        }
        onFinish();
    }

    /**
     * 关闭当前的Fragment，并且没有动画
     */
    @Override
    public void finishWithoutAnimation(){
        if(getCustomFragmentManager() != null){
            App.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getCustomFragmentManager().finishFragment(CustomFragment.this);
                }
            });
        }
        onFinish();
    }

    private void onFinish(){
        if(mOnFinishCallback != null) {
            mOnFinishCallback.onFinish(this);
        }
    }

    @Override
    public View findViewById(int id) {
        if(getView() != null){
            return getView().findViewById(id);
        }else{
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        FragmentCallbackDispatcher.onFragmentSaveInstanceState(mFragmentManager, this, outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
    }

    @Override
    public Bundle getArguments() {
        return mArguments;
    }

    @Override
    public void setArguments(Bundle args) {
        mArguments = args;
    }


    @Override
    public void startActivity(Intent intent) {
        mActivity.startActivity(intent, null);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            if (mFragmentManager != null ) {
                mFragmentManager.startActivityForResult(this, intent, requestCode);
            } else {
                mActivity.startActivityForResult(intent, requestCode, null);
            }
        } catch (Throwable e) {
            ALog.exception("CustomFragment", "startActivityForResult", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
    }


    public void setParentFragment(CustomFragment fragment) {
        mParentFragment = fragment;
    }

    public CustomFragment getParentFragment() {
        return mParentFragment;
    }


    @Override
    public boolean onBackPress() {
        return false;
    }

    /**
     * 效果等同于Activity中的setResult
     * @param resultCode
     */
    @Override
    public final void setResult(int resultCode) {
        synchronized (this) {
            mResultCode = resultCode;
            mResultData = null;
        }
    }

    /**
     * 效果等同于Activity中的setResult
     * @param resultCode
     */
    @Override
    public final void setResult(int resultCode, Intent data) {
        synchronized (this) {
            mResultCode = resultCode;
            mResultData = data;
        }
    }

    public Intent getResultData() {
        return mResultData;
    }

    public int getResultCode() {
        return mResultCode;
    }

    public void setResultCode(int resultCode) {
        this.mResultCode = resultCode;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    public void setRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
    }

    @Override
    public CustomFragmentManager getCustomFragmentManager() {
        return mFragmentManager;
    }

    @Deprecated
    public CustomFragmentManager getSupportFragmentManager(){
        return getCustomFragmentManager();
    }

    @Override
    public void setCustomFragmentManager(CustomFragmentManager manager) {
        mFragmentManager = manager;
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }


    /**
     * 是否需要框架调用setFitsSystemWindows来适配沉浸模式，默认为需要
     * @return
     */
    public boolean needSetFitsSystemWindows(){
        return true;
    }

    /**
     * 状态栏是否为暗色样式
     */
    public boolean isStatusDarkStyle(){
        return false;
    }

    /**
     * 导航栏是否为暗色样式
     */
    public boolean isNavigationDarkStyle(){
        return false;
    }

    /**
     * 如果需要自定义动画重写该方法，重写后跟手滑动将被禁用
     * @param enter  是否为进入
     * @return  进入返回window动画 例如 R.style.coverFragmentAnimation，
     *      退出返回view动画，例如 R.anim.options_panel_exit
     */
    public int onCreateAnimation(boolean enter) {
        return 0;
    }

    protected  boolean enableGesture() {
        return onCreateAnimation(true) == 0 && onCreateAnimation(false) == 0;
    }



    /**
     * 框架调用，其他可忽略该方法
     * @param mIsFinishing
     */
    protected void setIsFinishing(boolean mIsFinishing) {
        this.mIsFinishing = mIsFinishing;
    }


    public boolean isDestroyed(){
        try {
            if (getActivity() != null && getActivity().isDestroyed()) {
                return true;
            }
        } catch (Throwable e){
            ALog.exception("CustomFragment", "isDestroyed", e);
        }
        return mIsFinishing;
    }

    public boolean isFinishing(){
        try {
            if (getActivity() != null && getActivity().isFinishing()) {
                return true;
            }
        } catch (Throwable e){
            ALog.exception("CustomFragment", "isFinishing", e);
        }
        return mIsFinishing;
    }

    /**
     * 框架调用，其他类可忽略
     * @param isAnimating
     */
    protected final void setIsAnimating(boolean isAnimating){
        this.mIsAnimating = isAnimating;
        onAnimation(mIsAnimating);
    }

    /**
     * 当动画或者拖动开始和结束的时候回调这个方法
     * @param isAnimating
     */
    protected void onAnimation(boolean isAnimating){
        if(!isAnimating || (isAnimating && getCustomFragmentManager().getTopFragment() != this)) {
            View view = getView();
            if (view != null && view instanceof OnAnimatingListener) {
                ((OnAnimatingListener) view).onAnimating(isAnimating);
            }
        }
    }

    /**
     * 表明是否在动画中或者拖动中
     * @return
     */
    public boolean isAnimating(){
        return mIsAnimating;
    }


    /**
     * 获取当前fragment是否被显示
     * @return
     */
    public boolean isShowing(){
        try {
            return getView() != null && getView().isShown();
        } catch (Throwable e){
            ALog.exception("CustomFragment", "isShowing", e);
            return false;
        }
    }


    public void replaceInnerFragment(final CustomFragment oldFragment, final CustomFragment newFragment){
        if(oldFragment == null || newFragment == null || oldFragment == newFragment){
            return;
        }
        if(oldFragment.getView() == null || oldFragment.getView().getParent() == null){
            return;
        }
        newFragment.setCustomFragmentManager(getCustomFragmentManager());
        newFragment.setParentFragment(oldFragment.getParentFragment());

        ViewGroup oldFragmentView = (ViewGroup) oldFragment.getView();
        ViewGroup container = (ViewGroup) oldFragment.getView().getParent();

        if(!newFragment.isAdded()){
            newFragment.onAttach(mActivity);
            newFragment.onCreate(null);
            ViewGroup view = newFragment.onCreateView(newFragment.getLayoutInflater(), container, null);
            newFragment.setView(view);
            newFragment.onViewCreated(view, null);
            newFragment.onActivityCreated(null);
        }

        int childIndex = container.indexOfChild(oldFragmentView);
        container.removeView(oldFragmentView);
        container.addView(newFragment.getView(), childIndex);

        showFragment(newFragment);
        hideFragment(oldFragment);
    }

    /**
     * 滚动到顶部
     */
    public void onSmoothScrollToTop(){

    }

    public boolean getIsImmersive() {
        return mIsImmersive;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
    /**
     *
     * @return fragment是否全屏,只要能看到底下的fragment则认为不是全屏的
     * */
    public boolean isFullScreen(){
        return true;
    }

    /**
     *
     * @return fragment当前页面是否包含surfaceView 如果包含 在此页面打开新页面需要启动新的Activity
     * */
    public boolean hasSurfaceView(){
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>Note:</strong> If you override this method you must call
     * <code>super.onMultiWindowModeChanged</code> to correctly dispatch the event
     * to support fragments attached to this activity.</p>
     *
     * @param isInMultiWindowMode True if the activity is in multi-window mode.
     */
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
    }

    protected void initToolbar(View rootView){
        ToolbarManager toolbarManager = new ToolbarManager();
        mToolbar = toolbarManager.createFragmentToolbar(this,rootView);
        if(mToolbar == null){
            return;
        }else{
            toolbarManager.initToolbar(getIsImmersive());
        }
    }


    public void assembleToolbar() {
    }

    public boolean onToolMenuItemClick(MenuItem item) {
        return false;
    }

    public void onNavigationClick(View view){
        finish();
    }

    /**
     * 用于区分同一个Fragment下的不同界面
     */
    public String getFragmentKey() {
        return "";
    }

    protected void tryDismissDialog() {
    }

    public int getInputMode(){
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
    }


    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (mControl != null && mControl.hasShowMenu()){
//            mControl.dispathKey(event);
//        }
        return false;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean isTranslucentStatus() {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU){
//            if(mControl != null && !mControl.hasShowMenu() && onMenuOpened()){
//                return true;
//            }
        }
        return false;
    }


    /**
     * 是否允许手势，和 {@link #enableGesture()}的U区别是这个状态是动态读取，而{@link #enableGesture()}
     * 只在创建fragment时有效
     * @return
     */
    public boolean enableScrollRight(){
        return true;
    }


    /**
     * 标记当前的fragment是否含有webView，如果有webView则UI上需要做出不同的处理
     * WebView不支持setTranslationX，这个方法有些手机会导致webView变形挤压
     * @return
     */
    public boolean hasWebView(){
        return false;
    }


    protected boolean isUseToolbar(){
        return false;
    }


    public boolean isAdded(){
        return mIsAdded;
    }

    /**
     * ⚠️ 注意
     *  1. 没有支持该回调
     *  2. 如果支持到，可能会与onResume、onPause、onStop等同时调用
     */
    @Deprecated
    public void onHiddenChanged(boolean hidden) {
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }


    public boolean isVisible(){
        return mIsResumed;
    }

    private static void showFragment(CustomFragment fragment){
        if(fragment == null){
            return;
        }
        fragment.onStart();
        fragment.onResume();
    }

    private static void hideFragment(CustomFragment fragment){
        if(fragment == null){
            return;
        }
        fragment.onPause();
        fragment.onStop();
    }


    public void registerFragmentLifecycleCallback(FragmentLifecycleCallback callback) {
        FragmentCallbackDispatcher.registerFragmentLifecycleCallback(callback);
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallback callback) {
        FragmentCallbackDispatcher.unregisterFragmentLifecycleCallback(callback);
    }

    @Override
    public boolean isResumed() {
        return mIsResumed;
    }

    public boolean isStopped(){
        return mIsStopped;
    }

    @Override
    public boolean isHidden() {
        return !isShowing();
    }

    @Override
    public boolean getUserVisibleHint() {
        return isShowing();
    }
}
