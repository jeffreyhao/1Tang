package com.baidu.baselibrary.base.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.baselibrary.base.BaseActivity;
import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseView;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.ClickId;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.request.EmptyPresenter;
import com.baidu.baselibrary.util.sys.EventUtil;
import com.baidu.baselibrary.util.clz.ReflectUtil;
import com.baidu.baselibrary.util.UserManager;
import com.baidu.baselibrary.widget.dialog.LoggerDialogFragment;
import com.base.net.bean.ApiException;
import com.jess.baselibrary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
* @author lhc
* @date 2022/5/9 9:00
* @desc dialogFragment基类
*/
public abstract class BaseDialogFragment<VB extends ViewDataBinding,P extends BasePresenter> extends LoggerDialogFragment {
    public Subject<Lifecycle.Event> mLifecycleSubject = PublishSubject.create();
    protected ViewGroup mRootView;
    protected Activity mContext;
    protected VB mBinding;
    protected P mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        if (needRegisterEventBus()){
            EventUtil.register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (needRegisterEventBus()){
            EventUtil.unregister(this);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(getDialogStyle(), getDialogTheme());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initBeforeCreateView();
        mBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
        this.mRootView = (ViewGroup) mBinding.getRoot();
        this.mRootView.setClickable(true);
        setWindowParams();
        setDialog();
        setData();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getDialog()!=null&&getDialog().getWindow()!=null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(getWidth(), getHeight());
            if(getAnimations()!=-1) {
                getDialog().getWindow().setWindowAnimations(getAnimations());
            }
            if(getDialog().getWindow().getDecorView()!=null) {
                getDialog().getWindow().getDecorView().setOnTouchListener((v, event) -> {
                    if(canceledOnTouchOutside()&&isCancelable()) {
                        dismissAllowingStateLoss();
                    }
                    return true;
                });
            }
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK) {
                        if(cancelable() && isCancelable()) {
                            ALog.click(LogTag.Fragment, "onKey", ClickId.DialogKeyBack, KeyEvent.keyCodeToString(keyCode) + ", dialog onKey()->dismiss()");
                            dismiss();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }

    protected void setDialog() {
        Dialog dialog = getDialog();
        if(dialog!=null) {
            dialog.setCancelable(cancelable());
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside());
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(dismissListener!=null)
                        dismissListener.onDismiss();
                }
            });
        }
    }

    public VB getBinding() {
        return mBinding;
    }

    protected boolean cancelable() {
        return true;
    }

    protected boolean canceledOnTouchOutside() {
        return true;
    }

    protected int getDialogStyle() {
        return DialogFragment.STYLE_NO_TITLE;
    }

    /**
     * @return 弹窗主题样式
     * ⚠️注意：
     *      这个style里面设置的window动画，不会生效。
     *      需要定制动画，请使用 {@link #getWindowAnimations()}
     */
    protected int getDialogTheme() {
        return R.style.DialogFragmentStyle;
    }

    protected void setWindowParams() {
        Window window = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        if(window!=null) {
            window.setBackgroundDrawable(getBackGroundDrawable());
            DisplayMetrics dm = new DisplayMetrics();
            mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
            window.setAttributes(getLayoutParams(window));
        }
    }

    protected WindowManager.LayoutParams getLayoutParams(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = getGravity();
        params.width = getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.dimAmount = getDimAmount();
        params.windowAnimations = getWindowAnimations();
        return params;
    }

    protected int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected Drawable getBackGroundDrawable() {
        return new ColorDrawable(getResources().getColor(android.R.color.transparent, getContext().getTheme()));
    }

    protected @StyleRes int getWindowAnimations(){
        return R.style.Animation_AppCompat_Dialog;
    }

    protected float getDimAmount() {
        return 0.5f;
    }

    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    protected int getAnimations() {
        return -1;
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

    protected boolean needRegisterEventBus() {
        return false;
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


    public Subject<Lifecycle.Event> getLifecycleEvent() {
        return mLifecycleSubject;
    }

    public void showFragment(Object context, String tag) {
        if(context instanceof FragmentActivity) {
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().add(this, tag).commitAllowingStateLoss();
        }else if(context instanceof Fragment) {
            ((Fragment)context).getChildFragmentManager().beginTransaction().add(this,tag).commitAllowingStateLoss();
        }
    }

    public void tokenLose(){
        UserManager.tokenExpire(mContext);
    }

    public void onRequestFail(ApiException e) {
        if(e.getCode()==10003) {
            tokenLose();
        }
    }

    public void showLoading(boolean show){
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).showLoading(show);
    }

    protected OnViewClickListener listener;
    public interface OnViewClickListener {
        void onViewClick(int id,Object... params);
    }
    public BaseDialogFragment setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
        return this;
    }


    protected OnDismissListener dismissListener;
    public interface OnDismissListener {
        void onDismiss();
    }
    public BaseDialogFragment setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

}