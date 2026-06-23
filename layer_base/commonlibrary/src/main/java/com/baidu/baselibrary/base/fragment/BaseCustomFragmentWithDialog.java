package com.baidu.baselibrary.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseView;
import com.baidu.baselibrary.fragment.CustomFragmentActivity;
import com.baidu.baselibrary.fragment.dialog.CustomFragmentWithDialog;
import com.baidu.baselibrary.request.EmptyPresenter;
import com.baidu.baselibrary.util.clz.ReflectUtil;
import com.baidu.baselibrary.util.UserManager;
import com.base.net.bean.ApiException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;



public abstract class BaseCustomFragmentWithDialog<VB extends ViewDataBinding,P extends BasePresenter> extends CustomFragmentWithDialog {
    public Subject<Lifecycle.Event> mLifecycleSubject = PublishSubject.create();

    protected ViewGroup mRootView;
    protected VB mBinding;
    protected P mPresenter;

    public BaseCustomFragmentWithDialog(CustomFragmentActivity context) {
        super(context);
    }

    public BaseCustomFragmentWithDialog(CustomFragmentActivity context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }


    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initBeforeCreateView();
        mBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
        this.mRootView = (ViewGroup) mBinding.getRoot();
        initView();
        return mRootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    public VB getBinding() {
        return mBinding;
    }


    private void initView() {

    }

    private void setData() {
        intPresenter();
        this.initData();
        initTitleBar();
        this.initListener();
    }

    private void intPresenter() {
        Class<?> cls = ReflectUtil.getClassType(this,1);
        try {
            if (cls != null) {
                mPresenter = (P) cls.newInstance();
                if(!(mPresenter instanceof EmptyPresenter)) {
                    mPresenter.attachView((IBaseView) this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract int getLayoutId();

    /**
     * 方法功能描述:初始化监听事件
     */
    protected abstract void initListener();

    /**
     * 方法功能描述:初始化title信息
     */
    protected abstract void initTitleBar();

    /**
     * 方法功能描述:初始化数据
     */
    protected abstract void initData();

    protected void initBeforeCreateView() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(Lifecycle.Event.ON_START);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
    }


    public Subject<Lifecycle.Event> getLifecycleEvent() {
        return mLifecycleSubject;
    }

    public void showFragment(String tag) {
//        if(mContext instanceof CustomFragmentActivity){
//            ((CustomFragmentActivity) mContext).getCustomFragmentManager().startFragment();
//        } else if(mContext instanceof FragmentActivity) {
//            ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().add(this, tag).commitAllowingStateLoss();
//        }
        if(mActivity!=null && !mActivity.isFinishing() && !mActivity.isDestroyed()) {
            super.show();
        }
    }

    public void tokenLose(){
        UserManager.tokenExpire(mActivity);
    }

    public void onRequestFail(ApiException e) {
        if(e.getCode()==10003) {
            tokenLose();
        }
    }

    public void showLoading(boolean show){
//        if (mActivity instanceof BaseActivity)
//            ((BaseActivity) mActivity).showLoading(show);
    }

    protected OnViewClickListener listener;
    public interface OnViewClickListener {
        void onViewClick(int id,Object... params);
    }
    public BaseCustomFragmentWithDialog setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
        return this;
    }



}