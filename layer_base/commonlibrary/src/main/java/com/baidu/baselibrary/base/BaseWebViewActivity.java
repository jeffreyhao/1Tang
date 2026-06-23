package com.baidu.baselibrary.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.jess.baselibrary.R;
import com.jess.baselibrary.databinding.ActivityWebviewContainerBinding;


public abstract class BaseWebViewActivity<P extends BasePresenter<?>>
        extends BaseActivity<ActivityWebviewContainerBinding, P> {

    protected WebView mWebView = null;
    private String mUrl = "";

    public static void start(Context context, Class<?> clazz, String url) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public String className() {
        return "BaseWebViewActivity";
    }

    @Override
    public void initBeforeSetContent() {
        super.initBeforeSetContent();
        Intent intent = getIntent();
        mUrl = intent != null ? intent.getStringExtra("url") : "";
        if (mUrl == null) {
            mUrl = "";
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview_container;
    }

    @Override
    public void initView() {
        super.initView();
        mWebView = new WebView(this);
        mBinding.flWebviewContainer.addView(mWebView);
    }

    @Override
    public void initData() {
        initWebSettings();
        if (!mUrl.isEmpty()) {
            if (mWebView != null) {
                mWebView.loadUrl(mUrl);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebSettings() {
        if (mWebView == null) return;
        WebSettings settings = mWebView.getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setAllowFileAccess(true);
            settings.setDefaultTextEncodingName("utf-8");
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setSupportZoom(true);
        }
    }

    @Override
    public void initTitleBar(@Nullable Bundle savedInstanceState) {
        // no-op
    }

    @Override
    public void initListeners() {
        // no-op
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView = null;
        }
    }
}
