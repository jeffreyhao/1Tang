package com.baidu.baselibrary.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.baselibrary.base.BaseActivity;
import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseView;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.sys.EventUtil;
import com.baidu.baselibrary.util.clz.ReflectUtil;
import com.baidu.baselibrary.util.UserManager;
import com.base.net.bean.ApiException;
import com.base.util.ui.UIUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.jess.baselibrary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
* @author lhc
* @date 2022/5/9 9:01
* @desc fragment 基类
*/
public abstract class BaseFragment<VB extends ViewDataBinding, P extends BasePresenter> extends Fragment {
    public Subject<Lifecycle.Event> mLifecycleSubject = PublishSubject.create();
    protected ViewGroup mRootView;
    protected View mLoadingParentView;
    protected LottieAnimationView mLoadingView;
    protected Activity mContext;
    protected VB mBinding;
    protected P mPresenter;
    protected ToastUtils mToastUtils;

    @Override
    @NonNull
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        ALog.lifeCycle(LogTag.Fragment, className(), "onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ALog.lifeCycle(LogTag.Fragment, className(), "onDetach()");
    }

    public String className(){
        return getClass().getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.lifeCycle(LogTag.Fragment, className(), "onCreate()");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
            this.mRootView = (ViewGroup) mBinding.getRoot();
            setData();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ALog.lifeCycle(LogTag.Fragment, className(), "onViewCreated()");
    }

    protected void setData() {
        this.intPresenter();
        this.initView();
        this.initLoadingView();
        this.initData();
        this.initListener();
    }

    protected void intPresenter() {
        int length = ReflectUtil.getActualTypeArgsLength(this);
        Class<?> cls = ReflectUtil.getClassType(this, length>0?length-1:0);
        try {
            if (cls != null) {
                mPresenter = (P) cls.newInstance();
                if(this instanceof IBaseView) {
                    mPresenter.attachView((IBaseView) this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    protected abstract int getLayoutId();

    protected void initView() {

    }

    /**
     * 初始化loadingView
     */
    protected void initLoadingView(){
        if(autoAddLoadingView()
                && (mRootView instanceof RelativeLayout || mRootView instanceof FrameLayout)){
            View loadingView = LayoutInflater.from(mContext).inflate(R.layout.loading_view, mRootView,false);
            mLoadingParentView = loadingView.findViewById(R.id.rl_loading);
            mLoadingView = loadingView.findViewById(R.id.loading_view);
            if(mRootView instanceof LinearLayout){
                mRootView.addView(loadingView,0);
            }else{
                mRootView.addView(loadingView);
            }
        }else{
            if(mRootView!=null){
                mLoadingParentView = mRootView.findViewById(R.id.rl_loading);
                mLoadingView = mRootView.findViewById(R.id.loading_view);
            }
        }
    }

    /**
     * 方法功能描述:初始化监听事件
     */
    protected abstract void initListener();


    /**
     * 方法功能描述:初始化数据
     */
    protected abstract void initData();

    protected boolean autoAddLoadingView(){
        return true;
    }

    /**
     * 使用 activity loading
     * @return
     */
    protected boolean useActivityLoading(){
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (needRegisterEventBus()){
            EventUtil.register(this);
        }
        ALog.lifeCycle(LogTag.Fragment, className(), "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        ALog.lifeCycle(LogTag.Fragment, className(), "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        ALog.lifeCycle(LogTag.Fragment, className(), "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        ALog.lifeCycle(LogTag.Fragment, className(), "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.lifeCycle(LogTag.Fragment, className(), "onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
        EventUtil.unregister(this);
        ALog.lifeCycle(LogTag.Fragment, className(), "onDestroyView()");
    }


    public void showLoading(boolean showLoading) {
        if(useActivityLoading()){
            if(mContext instanceof BaseActivity){
                ((BaseActivity)mContext).showLoading(showLoading);
            }
        }else{
            if(mLoadingParentView!=null) {
                mLoadingParentView.setVisibility(showLoading ? View.VISIBLE : View.GONE);
                if(mLoadingView!=null){
                    if (showLoading) {
                        mLoadingView.playAnimation();
                    }else {
                        mLoadingView.cancelAnimation();
                    }
                }
            }
        }
    }

    public void tokenLose() {
        UserManager.tokenExpire(mContext);
    }

    public void onRequestFail(ApiException e) {
        if(e.getCode()==10003) {
            tokenLose();
        }
    }

    public Subject<Lifecycle.Event> getLifecycleEvent() {
        return mLifecycleSubject;
    }

    protected boolean needRegisterEventBus() {
        return false;
    }

    protected void toast(String text){
        if(mToastUtils==null){
            mToastUtils = ToastUtils.make().setGravity(Gravity.CENTER,0,0);
        }
        mToastUtils.show(text);
    }

    protected void toastTop(String text, int topOffset){
        ToastUtils toastUtils = ToastUtils.make().setGravity(Gravity.TOP, 0, UIUtil.getStatusBarHeight(App.getAppContext()) + topOffset);
        toastUtils.show(text);
    }

    protected void toastBottom(String text, int bottomOffset){
        ToastUtils toastUtils = ToastUtils.make().setGravity(Gravity.BOTTOM, 0, bottomOffset);
        toastUtils.show(text);
    }


}