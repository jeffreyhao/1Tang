package com.xcyh.cheatsheet.pages.scheme;


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
import com.benefit.novelverse.databinding.ActivitySchemeBinding;
import com.gyf.immersionbar.ImmersionBar;


public class SchemeTestActivity extends BaseActivity<ActivitySchemeBinding, SchemePresenter> {


    private View.OnClickListener mSchemeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button editText = (Button) v;
            String url = editText.getText().toString();
            startUriIntent(url);
        }
    };



    @Override
    protected int getLayoutId() {
        return R.layout.activity_scheme;
    }

    @Override
    protected void initTitleBar(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .navigationBarColor(R.color.color_navigation_main_tab)
                .autoNavigationBarDarkModeEnable(true)
                .init();
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

        mBinding.scheme1.setOnClickListener(mSchemeClickListener);
        mBinding.scheme2.setOnClickListener(mSchemeClickListener);
        mBinding.scheme3.setOnClickListener(mSchemeClickListener);
        mBinding.scheme4.setOnClickListener(mSchemeClickListener);
        mBinding.scheme5.setOnClickListener(mSchemeClickListener);
        mBinding.scheme6.setOnClickListener(mSchemeClickListener);
        mBinding.scheme7.setOnClickListener(mSchemeClickListener);
        mBinding.scheme8.setOnClickListener(mSchemeClickListener);
        mBinding.scheme9.setOnClickListener(mSchemeClickListener);
        mBinding.scheme10.setOnClickListener(mSchemeClickListener);
        mBinding.scheme11.setOnClickListener(mSchemeClickListener);
        mBinding.scheme12.setOnClickListener(mSchemeClickListener);
        mBinding.scheme13.setOnClickListener(mSchemeClickListener);
        mBinding.scheme14.setOnClickListener(mSchemeClickListener);
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
