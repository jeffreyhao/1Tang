package com.xcyh.cheatsheet.main.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;

import com.baidu.AppConfig;
import com.base.global.PreferencesUtil;
import com.benefit.novelverse.R;
import com.benefit.novelverse.databinding.ActivitySplashBinding;
import com.gyf.immersionbar.ImmersionBar;
import com.xcyh.cheatsheet.main.HomeActivity;
import com.xcyh.cheatsheet.model.bean.Pitcher;
import com.xcyh.cheatsheet.model.constant.SpKey;
import com.xcyh.cheatsheet.model.constant.Url;

/**
 * 开屏
 * SplashActivity 不要继承 CommonLibrary 下的 BaseActivity
 */
public class LaunchActivity extends BaseActivity {

    private ActivitySplashBinding mBinding;
    private CountDownTimer mCountDownTimer;

    private final Runnable timeOutRunnable = new Runnable() {
        @Override
        public void run() {
            gotoMain();
        }
    };

    @Override
    public String className() {
        return "SplashActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig.appStatus = 1;

        if (TextUtils.isEmpty(Pitcher.getInstance().getToken())) {
            Bundle bundle = new Bundle();
            // login->MainActivity
            startActivity(new Intent(this, HomeActivity.class), bundle);
            finish();
        } else {
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

            ImmersionBar.with(this).reset()
                    .transparentStatusBar().transparentNavigationBar()
                    .statusBarDarkFont(true).navigationBarDarkIcon(true)
                    .init();

            init();
        }
    }

    private void init() {
        doRequest();
        doNext();
    }

    private void doRequest() {
        // 请求逻辑 TODO
    }

    private void doNext() {
//        mHandler.postDelayed(timeOutRunnable, 2000);
        gotoMain();
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        super.onDestroy();
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 跳转到 MainActivity
     */
    private void gotoMain() {
        Url.BASE_URL = PreferencesUtil.get(SpKey.BASE_URL, Url.BASE_URL);
        Url.reset();

        Bundle bundle = new Bundle();
        startActivity(new Intent(this, HomeActivity.class), bundle);
        finish();
    }
}
