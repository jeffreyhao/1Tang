package com.xcyh.cheatsheet.pages.applink;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.baidu.baselibrary.base.BaseActivity;
import com.baidu.baselibrary.util.ui.ToastUtils;
import com.baidu.baselibrary.widget.dialog.BaseAlertDialog;
import com.baidu.baselibrary.widget.dialog.BaseDialog;
import com.benefit.novelverse.R;
import com.benefit.novelverse.databinding.ActivityAppLinkBinding;
import com.gyf.immersionbar.ImmersionBar;
import com.xcyh.cheatsheet.pages.scheme.SchemePresenter;


public class AppLinkTestActivity extends BaseActivity<ActivityAppLinkBinding, SchemePresenter> {


    private View.OnClickListener mAppLinkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button editText = (Button) v;
            String url = editText.getText().toString().toLowerCase();
            startUriIntent(url);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_link;
    }

    @Override
    protected void initTitleBar(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .navigationBarColor(R.color.color_navigation_main_tab)
                .autoNavigationBarDarkModeEnable(true)
                .init();

        mRootBinding.titleBar.setTitle("App Links 测试");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        mBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mBinding.editText.getText().toString();
                if(TextUtils.isEmpty(url)){
                    ToastUtils.showToastCenter("请输入scheme");
                } else {
                    startUriIntent(url);
                }
            }
        });

        mBinding.appLink0.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink1.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink2.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink3.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink4.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink5.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink6.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink7.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink8.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink9.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink10.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink11.setOnClickListener(mAppLinkClickListener);
        mBinding.appLink12.setOnClickListener(mAppLinkClickListener);
    }

    private void startUriIntent(String url){
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Throwable e){
            e.printStackTrace();
            showExceptionDialog(e);
        }
    }

    public void showExceptionDialog(Throwable e){
        BaseAlertDialog appTipDialog = BaseAlertDialog.newInstance(this, "提示", e.getMessage(), "知道了", false);
        appTipDialog.setOnDialogClickListener(new BaseAlertDialog.OnDialogClickListener() {
            @Override
            public void onConfirm(BaseDialog dialog) {
                dialog.dismiss();
            }
        });
        appTipDialog.show();
    }

}
