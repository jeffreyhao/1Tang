package com.baidu.baselibrary.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.baselibrary.base.PureCompatActivity;
import com.baidu.baselibrary.global.WeakHandler;
import com.baidu.baselibrary.global.statusbar.StatusBarHelper;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.ui.AnimationUtil;
import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.jess.baselibrary.R;

import androidx.annotation.NonNull;


/**
 * activity基类，包装了一个handler，以及activity切换时对APP中当前activity的修改
 * 所有activity都会处理的消息在handleMessage处理，特定activity处理的消息通过重写onHandleMessage进行处理
 */
public class CustomFragmentActivity  extends PureCompatActivity implements CustomFragmentManager.CoverFragmentManagerDelegate, Handler.Callback {



	protected CustomFragmentManager mFragmentManager = new CustomFragmentManager(this);


	public WeakHandler mHandler;



	public CustomFragmentActivity(){
	}


	public WeakHandler getHandler() {
		if(getCustomFragmentManager() != null &&
				getCustomFragmentManager().getTopFragment() != null&&
				getCustomFragmentManager().getTopFragment().getHandler() != null){
			return getCustomFragmentManager().getTopFragment().getHandler();
		}
		if(mHandler == null){
			mHandler = new WeakHandler();
		}
		return mHandler;
	}


	@Override
	public boolean handleMessage(@NonNull Message msg) {
		return false;
	}


	public CustomFragmentManager getCustomFragmentManager() {
		return mFragmentManager;
	}

	protected boolean isGestureEnable(){
		return true;
	}

	public void setGestureEnable(boolean enable) {
		mFragmentManager.setGestureEnable(enable);
	}

	/**
	 * 在super.onCreate之前调用
	 */
	protected void initBeforeSuperOnCreate() {

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initBeforeSuperOnCreate();
		super.onCreate(savedInstanceState);
	}


	@Override
	protected void onStart() {
		super.onStart();
		if(getCustomFragmentManager()  != null) {
			getCustomFragmentManager().onStart();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(getCustomFragmentManager() != null){
			getCustomFragmentManager().onStart();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(getCustomFragmentManager() != null){
			getCustomFragmentManager().onResume();
		}
	}


	@Override
	protected void onPause() {
		super.onPause();
		if(getCustomFragmentManager() != null) {
			getCustomFragmentManager().onPause();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(getCustomFragmentManager() != null) {
			getCustomFragmentManager().onStop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(getCustomFragmentManager() != null) {
			getCustomFragmentManager().onDestroy();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if(getParent()==null){
            mFragmentManager.onPostCreate();
            mFragmentManager.setGestureEnable(isGestureEnable());
		}
	}

	@Override
	public View findViewById(int id) {
		if(id == android.R.id.content && getCustomFragmentManager() != null && getCustomFragmentManager().getTopFragment() != null){
			return getCustomFragmentManager().getTopFragment().getView();
		}
		if(getParent()!=null){
			return super.findViewById(id);
		}
		View v = super.findViewById(id);
		return v;
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
        return getCustomFragmentManager() != null && getCustomFragmentManager().dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        return getCustomFragmentManager() != null && getCustomFragmentManager().onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
        return getCustomFragmentManager() != null && getCustomFragmentManager().onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
        return getCustomFragmentManager() != null && getCustomFragmentManager().dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

	@Override
	public void onBackPressed() {
		if(getCustomFragmentManager() != null && getCustomFragmentManager().onBackPress()){
			ALog.lifeCycle(LogTag.Activity, className(), "customFragmentManager.onBackPress()");
			return;
		}
		try {
			super.onBackPressed();
			ALog.lifeCycle(LogTag.Activity, className(), "super.onBackPressed()");
		} catch (Exception e){
			ALog.exception("CustomFragmentActivity", "onBackPressed", e);
		}
	}

	@Override
	public void finish() {
		super.finish();
		AnimationUtil.overridePendingTransition(this, R.anim.push_right_in, R.anim.push_right_out);
	}

	public void finishWithoutAnimation(){
		super.finish();
		AnimationUtil.overridePendingTransition(this, 0, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(getCustomFragmentManager() != null) {
			getCustomFragmentManager().onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		ALog.textSingle(className(), "onMultiWindowModeChanged", "onMultiWindowModeChanged ("+isInMultiWindowMode+") ");
		super.onMultiWindowModeChanged(isInMultiWindowMode);
		if (isInMultiWindowMode) {
//			App.showToast(R.string.may_not_work_in_split_window);
		}
		App.isInMultiWindowMode = isInMultiWindowMode;
		App.isInMultiWindowBottom = isInMultiWindowBottom();
		onCustomMultiWindowChanged(isInMultiWindowMode);
		getCustomFragmentManager().onMultiWindowModeChanged(isInMultiWindowMode);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		ALog.textSingle(className(), "onConfigurationChanged", "onConfigurationChanged:" + newConfig);
		if(getCustomFragmentManager() != null){
			CustomFragment baseFragment = getCustomFragmentManager().getTopFragment();
			if(baseFragment != null){
				baseFragment.onConfigurationChanged(newConfig);
			}
		}
		super.onConfigurationChanged(newConfig);
		App.postDelayed(new Runnable() {
			@Override
			public void run() {
				App.isInMultiWindowBottom = isInMultiWindowBottom();
			}
		},200);
		if(App.isInMultiWindowMode){
//			tryDismissDialog();
		}
		switch (newConfig.orientation) {
			case Configuration.ORIENTATION_LANDSCAPE :// 横屏
				App.isScreenPortrait = false;
				break;
			case Configuration.ORIENTATION_PORTRAIT :// 竖屏
				App.isScreenPortrait = true;
				break;
		}
	}

	/**
	 * 分屏状态发生变化时，触发的函数，应该由 {@link #onMultiWindowModeChanged(boolean)}
	 * 和其他自定义的 API LEVEL 21 以下的监听函数调用。
	 * <strong>需要进行分屏适配的 Activity 应覆写此方法。</strong>
	 *
	 * @param isInMultiWindowMode
	 */
	public void onCustomMultiWindowChanged(boolean isInMultiWindowMode){

	}

	/**
	 * 判断当前是否处于分屏时竖屏方向的下半屏。
	 *
	 * @return
	 */
	public boolean isInMultiWindowBottom(){
		if(App.isInMultiWindowMode){
			int[] location = new int[2];
			getWindow().getDecorView().getLocationOnScreen(location);
			LogUtil.i("isInMultiWindowBottom","isInMultiWindowBottom location[1]:"+location[1]+" getStatusBarHeight:"+ StatusBarHelper.getStatusBarHeight());
			return location[1] > StatusBarHelper.getStatusBarHeight();
		}else{
			return false;
		}
	}


	//插件代码 用包.类 做兼容处理 外部访问当前页面是否支持沉浸式 应调用此方法来判断
	public final boolean isTransparentStatusBarAble() {
		return isSupportTranslucentBar()
				&& (StatusBarHelper.mCurrentStatusBarType >= StatusBarHelper.OTHER || StatusBarHelper.mCurrentStatusBarType <= 0);
	}

	/**
	 * 当前页面是否允许支持沉浸式 子类可重写该方法
	 * @return
	 */
	protected boolean isSupportTranslucentBar(){
		return true;
	}








}
