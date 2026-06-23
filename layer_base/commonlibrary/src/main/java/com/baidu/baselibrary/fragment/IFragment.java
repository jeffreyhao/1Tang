package com.baidu.baselibrary.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment框架的Base可以改用此接口实现。
 *
 * Created by haojiangfeng on 2022/2/8.
 */
public interface IFragment {



    void onSaveInstanceState(Bundle outState);
    void onViewStateRestored(Bundle savedInstanceState);


    void onAttach(Activity activity);
    void onCreate(Bundle savedInstanceState);
    void onViewCreated(View view, Bundle savedInstanceState);
    ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    void onActivityCreated(Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroyView();
    void onDestroy();
    void onDetach();



    boolean onBackPress();

    boolean canRecycle();

    boolean isDetached();


    void finish();
    void finishWithoutAnimation();


    void startActivity(Intent intent);
    void startActivityForResult(Intent intent, int requestCode);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onFragmentResult(int requestCode, int resultCode, Intent data);


    void setResult(int resultCode);
    void setResult(int resultCode, Intent data);
    Intent getResultData();
    int getResultCode();


    Bundle getArguments();
    void setArguments(Bundle args);

    void setCustomFragmentManager(CustomFragmentManager manager);
    CustomFragmentManager getCustomFragmentManager();

    ViewGroup getView();
    void setView(ViewGroup view);


    Activity getActivity();
    Resources getResources();
    View findViewById(int id);
    LayoutInflater getLayoutInflater();





//    void getHandler();










}
