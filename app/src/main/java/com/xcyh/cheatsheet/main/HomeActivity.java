package com.xcyh.cheatsheet.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.baidu.baselibrary.annotation.PageConfig;
import com.baidu.baselibrary.base.BaseActivity;
import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.fragment.CustomFragmentUtil;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.ClickId;
import com.baidu.baselibrary.main.MainTabConfig;
import com.baidu.baselibrary.util.ui.StatusBarUtil;
import com.baidu.baselibrary.util.ui.ViewUtil;
import com.base.net.bean.ApiException;
import com.benefit.event.SelectTabEvent;
import com.benefit.novelverse.R;
import com.benefit.novelverse.databinding.ActivityMainBinding;
import com.benefit.novelverse.view.actvitity.MainTab;
import com.gyf.immersionbar.ImmersionBar;
import com.meta.ui.widget.BottomToolBarLayout.OnTabClickListener;
import com.meta.ui.widget.ToolBar;
import com.xcyh.cheatsheet.app.AppInit;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@PageConfig(needPaddingTop = true)
public class HomeActivity extends BaseActivity<ActivityMainBinding, MainPresenter> {

    private List<MainTab.ItemInfo> mItems = MainTab.buildItems(MainTabConfig.mItemConfig);

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initTitleBar(Bundle savedInstanceState) {
        ViewUtil.setVisible(mRootBinding.titleBar, false);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        mPresenter.setContext(this);
    }

    /**
     * 加载书架 View
     */
    @Override
    public void initView() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 添加以下代码有些手机中的书架界面会不断闪烁
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getDecorView().setBackgroundDrawable(null);

        ImmersionBar.with(this)
                .navigationBarColor(R.color.color_navigation_main_tab)
                .autoNavigationBarDarkModeEnable(true)
                .init();

        configTab();
    }

    private void configTab() {
        int num = mItems.size();
        List<MainTabConfig.ItemStyle> items = MainTabConfig.getConfig();
        for (int i = 0; i <= 3; i++) {
            ToolBar toolBar = mBinding.bottomNav.getToolBar(i);
            if (i < num) {
                toolBar.setVisibility(View.VISIBLE);
                MainTabConfig.ItemStyle item = items.get(i);
                toolBar.setTitle(item.tabNameRes);
                toolBar.setImg(item.getImageDrawable());
                toolBar.setSelectAnimator(item.animatorRes);
                toolBar.setTopLineBackground(item.getTopLineDrawable());
                toolBar.setBottomLineBackground(item.getBottomLineDrawable());
            } else {
                toolBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initData() {
        jumpToHome();

        CustomFragment fragment = makeNewFragment(MainTabConfig.sPosition, null);
        StatusBarUtil.setLightStatusBar(this, fragment != null && fragment.isStatusDarkStyle());

        AppInit.initOnMainTabCreate();
    }

    @Override
    public void initListeners() {
        mBinding.bottomNav.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabClick(View view, int position) {
                onNavigationBarSelected(position);
                switch (position) {
                    case MainTabConfig.TAB_0:
                        ALog.click("MainActivity", "initListeners", ClickId.MainTabBookShelf, "点击第1个tab");
                        break;
                    case MainTabConfig.TAB_1:
                        ALog.click("MainActivity", "initListeners", ClickId.MainTabBookStore, "点击第2个tab");
                        break;
                    case MainTabConfig.TAB_2:
                        ALog.click("MainActivity", "initListeners", ClickId.MainTabBenefit, "点击第3个tab");
                        break;
                    case MainTabConfig.TAB_3:
                        ALog.click("MainActivity", "initListeners", ClickId.MainTabProfile, "点击第4个tab");
                        break;
                    case MainTabConfig.TAB_4:
                        // none
                        break;
                }
            }
        });
    }

    public void jumpToHome() {
        onNavigationBarSelected(MainTabConfig.TAB_INDEX_BOOKSHELF);
    }

    private void jumpToMine() {
        onNavigationBarSelected(MainTabConfig.TAB_INDEX_BOOKSTORE);
    }

    /**
     * 执行点击回调
     */
    private void onNavigationBarSelected(int index) {
        switchTab(index);
        setNavigationBarChecked(index);
    }

    /**
     * 选中某一个 NavigationBar
     */
    private void setNavigationBarChecked(int selectedIndex) {
        for (int index = 0; index < mBinding.bottomNav.getChildCount(); index++) {
            View child = mBinding.bottomNav.getChildAt(index);
            if (child instanceof ToolBar) {
                ((ToolBar) child).setChecked(index == selectedIndex);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean registerEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectTabEvent(SelectTabEvent event) {
        if (event.getTab() == 1) {
            jumpToMine();
        } else if (event.getTab() == 0) {
            jumpToHome();
        }
    }

    private MainTab.ItemInfo getCurrentItem() {
        if (MainTabConfig.sPosition < 0 || MainTabConfig.sPosition > mItems.size()) {
            return null;
        }
        return mItems.get(MainTabConfig.sPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        MainTab.ItemInfo info = getCurrentItem();
        if (info != null && info.fragment != null) {
            info.fragment.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainTab.ItemInfo info = getCurrentItem();
        if (info != null && info.fragment != null) {
            info.fragment.onPause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainTab.ItemInfo info = getCurrentItem();
        if (info != null && info.fragment != null) {
            info.fragment.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainTab.ItemInfo info = getCurrentItem();
        if (info != null && info.fragment != null) {
            info.fragment.onStop();
        }
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainTabConfig.sPosition = MainTabConfig.INIT;
        AppInit.callOnMainTabDestroy();

        for (int i = 0; i < mItems.size(); i++) {
            MainTab.ItemInfo item = mItems.get(i);
            if (item != null && item.fragment != null) {
                item.fragment.onDestroy();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < mItems.size(); i++) {
            MainTab.ItemInfo item = mItems.get(i);
            if (item != null && item.saveInstanceStateBundle != null && !item.saveInstanceStateBundle.isEmpty()) {
                outState.putBundle(String.valueOf(i), item.saveInstanceStateBundle);
            }
        }
    }

    @Override
    public void onBackPressed() {
        MainTab.ItemInfo info = getCurrentItem();
        if (info == null || info.fragment == null || !info.fragment.onBackPress()) {
            if (mPresenter.onExitApp()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        for (int i = 0; i < mItems.size(); i++) {
            MainTab.ItemInfo item = mItems.get(i);
            if (item != null && item.fragment != null) {
                item.fragment.onMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (int i = 0; i < mItems.size(); i++) {
            MainTab.ItemInfo item = mItems.get(i);
            if (item != null && item.fragment != null) {
                item.fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 创建 fragment
     */
    private CustomFragment makeNewFragment(int position, Bundle savedInstanceState) {
        MainTab.ItemInfo itemInfo = mItems.get(position);
        CustomFragment fragment = null;
        if (position != MainTabConfig.INIT) {
            fragment = itemInfo.fragment;
        }

        if (fragment == null) {
            fragment = CustomFragmentUtil.assembleFragment(
                    this,
                    MainTabConfig.buildFragment(position),
                    mBinding.container,
                    savedInstanceState
            );
            itemInfo.fragment = fragment;
        }
        return fragment;
    }

    private void switchTab(int position) {
        if (position == MainTabConfig.sPosition) {
            return;
        }
        lastFragmentGone(MainTabConfig.sPosition);
        MainTabConfig.sPosition = position;

        CustomFragment fragment = makeNewFragment(position, null);
        View view = fragment.getView();
        if (view.getParent() != null) {
            view.setVisibility(View.VISIBLE);
        } else {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                view.setLayoutParams(layoutParams);
            }
            mBinding.content.addView(view, 0);
        }
        if (isShowing()) {
            fragment.onStart();
            fragment.onResume();
            StatusBarUtil.setLightStatusBar(this, fragment.isStatusDarkStyle());
        }
    }

    /**
     * 之前可见的 Fragment 执行消失的回调
     */
    private void lastFragmentGone(int position) {
        if (position != MainTabConfig.INIT) {
            MainTab.ItemInfo item = mItems.get(position);
            if (item != null) {
                CustomFragment historyFragment = item.fragment;
                if (isShowing()) {
                    historyFragment.onPause();
                    historyFragment.onStop();
                }
                Bundle bundle = item.saveInstanceStateBundle;
                if (bundle == null) {
                    bundle = new Bundle();
                } else {
                    bundle.clear();
                }
                historyFragment.onSaveInstanceState(bundle);
                item.saveInstanceStateBundle = bundle;
                if (historyFragment.getView() != null) {
                    historyFragment.getView().setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean isShowing() {
        return mBinding.container.isShown();
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onRequestFail(ApiException e) {
        // none
    }

    @Override
    public void finish() {
        ALog.textSingle("MainActivity.finish()");
        super.finish();
    }
}
