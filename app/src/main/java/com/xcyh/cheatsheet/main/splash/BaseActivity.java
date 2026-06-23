package com.xcyh.cheatsheet.main.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.AppConfig;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.base.util.AppUtil;
import com.benefit.novelverse.R;
import com.benefit.novelverse.event.EventDispatcher;
import com.benefit.novelverse.utils.DialogUtils;
import com.tencent.common.swipeback.BGAKeyboardUtil;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements LifecycleOwner {
    protected static final String TAG = "Activity";
    private LifecycleRegistry mLifecycleRegistry;
    protected EventDispatcher mEventDispatcher;
    protected Handler mHandler = new Handler();
    protected Toolbar mToolbar;



    public String className(){
        return getClass().getSimpleName();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ALog.lifeCycle(LogTag.Activity, className(), "onCreate()");
        super.onCreate(savedInstanceState);
        if(AppConfig.appStatus==-1&&!(this instanceof LaunchActivity)) {
            ALog.textSingle(getClass().getSimpleName() + "-->appStatus=-1");
            AppUtil.restartApp(this);
            return;
        }
        getWindow().setBackgroundDrawable(null);
        initHead();
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);

        try {
            if (mEventDispatcher == null && isRegisterEventBus()) {
                mEventDispatcher = EventDispatcher.get();
                mLifecycleRegistry.addObserver(mEventDispatcher);
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        ALog.d(this.getClass().getSimpleName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        ALog.lifeCycle(LogTag.Activity, className(), "onNewIntent()");
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ALog.d(className(), "onStart()");
        if(mLifecycleRegistry!=null) {
            mLifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
        }
    }

    @Override
    protected void onRestart() {
        ALog.lifeCycle(LogTag.Activity, className(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        ALog.lifeCycle(LogTag.Activity, className(), "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ALog.d(className(), "onPause()");
    }

    @Override
    protected void onStop() {
        ALog.lifeCycle(LogTag.Activity, className(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ALog.lifeCycle(LogTag.Activity, className(), "onDestroy()");
        super.onDestroy();
        try{
            if(mLifecycleRegistry!=null) {
                mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
            }
            if(mHandler!=null) {
                mHandler.removeCallbacksAndMessages(null);
            }
            DialogUtils.dismissProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否注册事件分发
     *
     * 默认不绑定，子类需要绑定的话复写此方法返回true，绑定的同时mEventDispatcher会自动赋值
     *
     * @return true 绑定EventBus事件分发 false 不绑定.
     */
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        getToolbar();
    }

    /*初始化头部*/
    protected void initHead() {
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }
            }
        }
        return mToolbar;
    }

    public void setToolbarAsUp() {
        setToolbarAsUp(null);
    }

    protected void setToolbarAsUp(View.OnClickListener clickListener) {
        // 初始化toolbar
        getToolbar();
        if (mToolbar == null) return;
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setContentInsetsRelative(0, 0);
        mToolbar.setContentInsetStartWithNavigation(com.tencent.common.util.ConvertUtils.dp2px(14));
        mToolbar.setNavigationContentDescription(R.string.close_and_go_back);
        if (clickListener != null) {
            mToolbar.setNavigationOnClickListener(clickListener);
        } else {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        BGAKeyboardUtil.closeKeyboard(this);
        finish();
//        overridePendingTransition(R.anim.bga_sbl_activity_backward_enter, R.anim.bga_sbl_activity_backward_exit);
    }
}
