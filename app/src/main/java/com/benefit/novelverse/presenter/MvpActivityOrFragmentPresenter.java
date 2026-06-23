package com.benefit.novelverse.presenter;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benefit.novelverse.view.base.MvpView;

/**
 * Fragment Presenter层基类
 * @author adison
 * @date 2017/3/24
 * @time 上午12:06
 */
public abstract class MvpActivityOrFragmentPresenter<V extends MvpView> extends MvpPresenter<V> {
    public MvpActivityOrFragmentPresenter(V view) {
        super(view);
    }

    /**
     * 该方法在{@link Activity#onCreate(Bundle)} or {@link Fragment#onCreate(Bundle)} 调用
     * @param bundle
     */
    public void onCreate(Bundle bundle){};

    /**
     * 该方法在 {@link Activity#onSaveInstanceState(Bundle)} or{@link Activity#onCreate(Bundle)} or{@link Fragment#onSaveInstanceState(Bundle)}调用
     */
    public void onSaveInstanceState(Bundle outState) {
    }

    /**
     * 该方法在 {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    public void onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

    }

    /**
     * 该方法在 {@link Fragment#onViewCreated(View, Bundle)}调用
     *
     * @param view               The inflated view
     * @param savedInstanceState the bundle with the viewstate
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }


    /**
     * 该方法在 {@link Fragment#onActivityCreated(Bundle)}调用
     *
     * @param savedInstanceState The saved bundle
     */
    public void onActivityCreated(Bundle savedInstanceState) {
    }

    /**
     * 该方法在 {@link Activity#onStart()} or {@link Fragment#onStart()}调用
     */
    public void onStart() {
    }

    /**
     * 该方法在 {{@link Activity#onResume()} or {@link Fragment#onResume()}调用
     */
    public void onResume() {
    }

    /**
     * 该方法在 {@link Activity#onPause()} or {@link Fragment#onPause()}调用
     */
    public void onPause() {
    }

    /**
     * 该方法在  {@link Fragment#onStop()}调用
     */
    public void onStop() {
    }

    /**
     * 该方法在{@link Fragment#onDestroyView()}调用
     */
    public void onDestroyView() {
    }


    /**
     * 该方法在 {@link Activity#onDestroy()}} or {@link Fragment#onDestroy()}调用
     */
    public void onDestroy() {
    }


    /**
     * 该方法在{@link Fragment#onDetach()}调用
     */
    public void onDetach() {
    }
}
