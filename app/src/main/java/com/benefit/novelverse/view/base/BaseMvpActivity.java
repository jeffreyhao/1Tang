package com.benefit.novelverse.view.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.benefit.novelverse.presenter.MvpActivityOrFragmentPresenter;
import com.xcyh.cheatsheet.main.splash.BaseActivity;


/**
 * MVP Activity基类
 * @author adison
 * @date 2017/5/22
 * @time 上午12:26
 */
public abstract class BaseMvpActivity<P extends MvpActivityOrFragmentPresenter> extends BaseActivity implements MvpView<P> {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter != null) {
            mPresenter.onCreate(savedInstanceState);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter.destroyUI();
        }
    }

    public P getPresenter() {
        return mPresenter;
    }
}
