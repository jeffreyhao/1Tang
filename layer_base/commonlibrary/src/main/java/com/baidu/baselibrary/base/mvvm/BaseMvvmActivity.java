package com.baidu.baselibrary.base.mvvm;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.baidu.AppConfig;
import com.baidu.baselibrary.annotation.HookClick;
import com.baidu.baselibrary.annotation.PageConfig;
import com.baidu.baselibrary.base.callback.ActivityFinishListener;
import com.baidu.baselibrary.fragment.CustomFragmentActivity;
import com.baidu.baselibrary.global.Clazz;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.ClickId;
import com.baidu.baselibrary.util.UserManager;
import com.baidu.baselibrary.util.click.HookClickUtil;
import com.baidu.baselibrary.util.clz.ClassUtil;
import com.baidu.baselibrary.util.clz.ReflectUtil;
import com.baidu.baselibrary.util.sys.EventUtil;
import com.baidu.baselibrary.util.ui.AnimationUtil;
import com.baidu.baselibrary.util.ui.StatusBarUtil;
import com.baidu.baselibrary.widget.TitleBar;
import com.base.global.GlobalBuildConfig;
import com.base.net.bean.ApiException;
import com.base.util.AppUtil;
import com.base.util.collection.ListUtil;
import com.base.util.net.NetworkUtil;
import com.base.util.net.NetworkUtils;
import com.base.util.ui.FragmentUtil;
import com.base.util.ui.UIUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.jess.baselibrary.R;
import com.jess.baselibrary.databinding.LayoutBaseContentBinding;

import java.util.List;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


@HookClick
public abstract class BaseMvvmActivity<VB extends ViewDataBinding, VM extends ViewModel> extends CustomFragmentActivity {


    protected Activity mContext;
    protected LayoutBaseContentBinding mRootBinding;
    protected VB mBinding;
    protected VM mViewModel;

    protected ActivityFinishListener mActivityFinishListener = null;
    protected Subject<Lifecycle.Event> mLifecycleSubject = PublishSubject.create();

    protected ToastUtils toastUtils;
    protected boolean mNeedPaddingTop = true;
    protected boolean mIsTransparencyBar = true;
    protected boolean mIsFullScreen;
    protected boolean mResumed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        if(AppConfig.appStatus == -1 && !Clazz.isSplashActivity(getClass()) && !Clazz.isReadActivity(getClass())) {
            ALog.textSingle(getClass().getSimpleName() + ", appStatus=-1 -> restartApp()");
            AppUtil.restartApp(this);
            return;
        }
        getPageConfig();
        initBeforeSetContent();
        mRootBinding = DataBindingUtil.setContentView(this, R.layout.layout_base_content);
        if(getLayoutId()!=0) {
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), getLayoutId(), null, false);
        }
        if(getLayoutId()!=0) {
            addContentView();
            initViewModel();
            bindViewModel();
        }
        setData();
        this.initTitleBar(savedInstanceState);
    }

    private void getPageConfig() {
        PageConfig pageConfig = this.getClass().getAnnotation(PageConfig.class);
        if(pageConfig!=null) {
            mNeedPaddingTop = pageConfig.needPaddingTop();
            mIsTransparencyBar = pageConfig.transparencyBar();
            mIsFullScreen = pageConfig.isFullScreen();
        }
    }

    private void addContentView() {
        setPaddings();
        mRootBinding.rlRoot.addView(mBinding.getRoot(), 1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBinding.getRoot().getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.addRule(RelativeLayout.BELOW, R.id.titleBar);
    }

    @CallSuper
    protected void initViewModel() {
        Class<?> cls = ReflectUtil.getClassType(this, 1);
        try {
            if (cls != null) {
                mViewModel = (VM) new ViewModelProvider(this).get((Class<VM>) cls);
                observeViewModel();
            }
        } catch (Exception e) {
            ALog.exception(className(), "initViewModel", e);
        }
    }

    private void observeViewModel() {
        if (mViewModel instanceof BaseViewModel) {
            BaseViewModel baseViewModel = (BaseViewModel) mViewModel;
            baseViewModel.getUiState().observe(this, state -> {
                if (state == null || mRootBinding == null) return;
                showLoading(state.isLoading());
                if (state.isShowEmpty()) {
                    mRootBinding.emptyView.setContent(state.textResId, state.iconResId);
                    mRootBinding.emptyView.setVisibility(View.VISIBLE);
                } else {
                    mRootBinding.emptyView.setVisibility(View.GONE);
                }
            });
            baseViewModel.getTokenLoseEvent().observe(this, event -> {
                if (event != null && event.getContentIfNotHandled() != null) {
                    tokenLose();
                }
            });
        }
    }

    private void bindViewModel() {
        if (mBinding != null) {
            mBinding.setLifecycleOwner(this);
            int variableId = getViewModelVariableId();
            if (variableId != 0 && mViewModel != null) {
                mBinding.setVariable(variableId, mViewModel);
            }
        }
    }

    protected int getViewModelVariableId() {
        return 0;
    }

    private void setData() {
        this.initView();
        hookClickViews();
        this.initData();
        this.initListeners();
    }

    private void hookClickViews() {
        HookClick hookClick = this.getClass().getAnnotation(HookClick.class);
        if(hookClick!=null) {
            getWindow().getDecorView().post(()->{
                for(int item: hookClick.value()) {
                    HookClickUtil.hookView(findViewById(item));
                }
            });
        }
    }

    protected void initBeforeSetContent() {
        try {
            if(GlobalBuildConfig.isFlagSecure()) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            }
            if (mIsFullScreen) {
                supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            if (mIsTransparencyBar) {
                StatusBarUtil.transparencyBar(mContext);
                StatusBarUtil.setLightStatusBar(this, false);
            }
        } catch (Throwable e){
            ALog.exception(className(), "initBeforeSetContent", e);
        }
    }

    protected void setPaddings() {
        View root = mRootBinding.getRoot();
        int paddingTop = mNeedPaddingTop ? UIUtil.getStatusBarHeight(mContext) : 0;

        if(Build.VERSION.SDK_INT >= 35){
            ViewCompat.setOnApplyWindowInsetsListener(root, new androidx.core.view.OnApplyWindowInsetsListener() {
                @NonNull
                @Override
                public WindowInsetsCompat onApplyWindowInsets(@NonNull View view, @NonNull WindowInsetsCompat insets) {
                    int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
                    int navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;

                    if(GlobalBuildConfig.DEBUG){
                        ALog.textSingle(className(), "onApplyWindowInsets", "MainActivity, 顶部状态栏高度：" + statusBarHeight + ", 底部导航栏高度：" + navBarHeight);
                    }

                    root.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), navBarHeight);
                    return insets; // 返回原 Insets
                }
            });
        } else {
            root.setPadding(0, paddingTop, 0, 0);
        }
    }

    public VB getBinding() {
        return mBinding;
    }

    /**
     * 初始化布局文件
     */
    protected abstract int getLayoutId();

    /**
     * 初始化titleBar信息
     */
    protected abstract void initTitleBar(Bundle savedInstanceState);

    /**
     * 初始化布局
     */
    protected void initView() {
        mRootBinding.emptyView.setOnClickListener(v->{
            ALog.click(className(), "initListener", ClickId.ActivityEmptyView, "Click emptyView");
            if(NetworkUtils.isConnected()){
                this.onEmptyViewClick();
            }else{
                toast(getString(R.string.net_connect_tip));
            }
        });
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听事件
     */
    protected abstract void initListeners();

    /**
     * 失败页面点击事件
     */
    protected void onEmptyViewClick(){

    }

    protected void setTitle(String title) {
        mRootBinding.titleBar.setTitle(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (registerEventBus()) {
            EventUtil.register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResumed = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if (mViewModel instanceof BaseViewModel) {
                ((BaseViewModel) mViewModel).onLifecycleEvent(Lifecycle.Event.ON_DESTROY);
            }
            showLoading(false);
            EventUtil.unregister(this);
            mLifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
            mLifecycleSubject.onComplete();
            if(mActivityFinishListener != null){
                mActivityFinishListener.onActivityFinished();
            }
            mActivityFinishListener = null;
            mBinding = null;
            mRootBinding = null;
        } catch (Exception e) {
            ALog.exception(className(), "onDestroy", e);
        }
    }

    public void showLoading(boolean showLoading) {
        if(mRootBinding!=null) {
            mRootBinding.rlLoading.setVisibility(showLoading ? View.VISIBLE : View.GONE);
            if (showLoading) {
                mRootBinding.loadingView.playAnimation();
            }else {
                mRootBinding.loadingView.cancelAnimation();
            }
        }
    }

    protected void showEmptyView(boolean show) {
        mRootBinding.emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public Subject<Lifecycle.Event> getLifecycleEvent() {
        return mLifecycleSubject;
    }

    protected void showFragment(int containerId, Fragment fragment) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment item : fragments) {
            getSupportFragmentManager().beginTransaction().hide(item).commit();
        }
        if (fragment != null) {
            if(fragment.isAdded()){
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
            }else{
                getSupportFragmentManager().beginTransaction().add(containerId, fragment).commit();
            }

        }
    }

    protected void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    protected boolean registerEventBus() {
        return false;
    }

    public void tokenLose() {
        UserManager.tokenExpire(this);
    }

    /**
     * 请求失败
     * @param e
     */
    public void onRequestFail(ApiException e) {
        if(!NetworkUtil.isNetAvailable(mContext)) {
            mRootBinding.emptyView.setContent(R.string.error_net_tip, R.drawable.default_no_net);
        }else{
            mRootBinding.emptyView.setContent(
                    R.string.error_data_tip,
                    R.drawable.default_load_fail
            );
        }
    }

    public void showNoData(){
        mRootBinding.emptyView.setContent(
                R.string.error_empty_data_tip,
                R.drawable.default_no_data
        );
    }

    protected void toast(String text){
        if(toastUtils==null){
            toastUtils = ToastUtils.make().setGravity(Gravity.CENTER,0,0);
        }
        toastUtils.show(text);
    }


    public LayoutBaseContentBinding getRootBinding(){
        return mRootBinding;
    }

    public TitleBar getTitleBar(){
        return mRootBinding.titleBar;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return super.dispatchGenericMotionEvent(ev);
    }


    public void setActivityFinishListener(ActivityFinishListener listener) {
        this.mActivityFinishListener = listener;
        if(mActivityFinishListener != null && isDestroyed()){
            mActivityFinishListener.onActivityFinished();
        }
    }


    public void dismissAllDialogFragment(Fragment... ignoreFragments) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if(ListUtil.isNotEmpty(fragments)){
            for(Fragment fragment: fragments){
                if(fragment instanceof DialogFragment && FragmentUtil.contains(fragment, ignoreFragments)){
                    ((DialogFragment) fragment).dismissAllowingStateLoss();
                }
            }
        }
    }

    public void dismissAllDialogFragmentWithIgnore(List<Class> ignoreClasses) {
        if(ignoreClasses == null || ignoreClasses.size() == 0){
            dismissAllDialogFragment();
            return;
        }
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if(ListUtil.isNotEmpty(fragments)){
            for(Fragment fragment: fragments){
                if(fragment instanceof DialogFragment && !ClassUtil.inClassList(fragment, ignoreClasses)){
                    ((DialogFragment) fragment).dismissAllowingStateLoss();
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        AnimationUtil.overridePendingTransition(this, R.anim.anim_none, R.anim.push_right_out);
    }

}

