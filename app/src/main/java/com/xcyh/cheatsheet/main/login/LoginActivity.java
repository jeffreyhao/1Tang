package com.xcyh.cheatsheet.main.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import com.baidu.baselibrary.annotation.PageConfig;
import com.baidu.baselibrary.base.BaseActivity;
import com.baidu.baselibrary.util.ui.AnimationUtil;
import com.baidu.baselibrary.util.App;
import com.base.global.GlobalBuildConfig;
import com.base.net.bean.ApiException;
import com.benefit.novelverse.R;
import com.benefit.novelverse.databinding.ActivityLoginBinding;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.common.util.ToastUtils;
import com.xcyh.cheatsheet.model.bean.LoginBean;
import com.xcyh.cheatsheet.main.HomeActivity;
import com.xcyh.cheatsheet.model.constant.Url;

/**
 * Created by haojiangfeng on 2024/11/14.
 */
@PageConfig(needPaddingTop = false, transparencyBar = false, isFullScreen = true)
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginPresenter> implements ILoginView {



    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initTitleBar(Bundle savedInstanceState) {
        mRootBinding.titleBar.setVisibility(View.GONE);
        ImmersionBar.with(this)
                .reset()
                .transparentBar()
                .fullScreen(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    @Override
    protected void initListeners() {
        mBinding.ivEye.setOnClickListener(v->{
            if(mBinding.etPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD){
                mBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mBinding.ivEye.setImageResource(R.drawable.ic_eye_close);
            } else {
                mBinding.etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mBinding.ivEye.setImageResource(R.drawable.ic_eye_open);
            }
            mBinding.etPassword.setSelection(mBinding.etPassword.getText().length());
        });
        mBinding.tvBtn.setOnClickListener(v-> {
            String userName = mBinding.etName.getText().toString();
            if(TextUtils.isEmpty(userName)){
                ToastUtils.showCenterShort("请输入用户名");
                return;
            }
            String password = mBinding.etPassword.getText().toString();
            if(TextUtils.isEmpty(password)){
                ToastUtils.showCenterShort("请输入密码");
                return;
            }
            doLogin(userName, password);
        });
        mBinding.layoutRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_dev){
                    Url.BASE_URL = "http://192.168.8.95:8120/";
                    Url.reset();
                    mBinding.etName.setText("haojiangfeng");
                    mBinding.etPassword.setText("123456789");
                } else {
                    Url.BASE_URL = "https://launch.novelsbd.com/";
                    Url.reset();
                    mBinding.etName.setText("haojiangfeng");
                    mBinding.etPassword.setText("hjf@123");
                }
            }
        });
    }

    @Override
    protected void initData() {
        if(GlobalBuildConfig.DEBUG){
            mBinding.layoutRadio.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestSuccess(LoginBean loginBean) {
        gotoMain();
    }


    @Override
    public void onRequestFail(ApiException e) {
        toast("登录失败");
    }


    private void doLogin(String userName, String password) {
        mPresenter.doLogin(userName, password);
    }

    /**
     * 跳转到MainActivity
     */
    private void gotoMain() {
        Bundle bundle = new Bundle();
        startActivity(new Intent(this, HomeActivity.class), bundle);
        AnimationUtil.overridePendingTransition(this, R.anim.dialog_fade_in, R.anim.anim_none);
        App.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.this.finish();
            }
        }, 500);
    }

}
