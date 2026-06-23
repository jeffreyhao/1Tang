package com.baidu.baselibrary.fragment.toolbar;

import android.view.MenuItem;
import android.view.View;

/**
 * Activity/Fragment使用 toolbar 顶部栏需要实现的接口
 * 用来：
 *     1、子类配置需要个性化的顶部栏样式
 *     2、返回键点击回调
 *     3、菜单点击回调
 */

public interface IToolbar {

    /**
     * 组装 toolbar:自定义 view，设置标题，配置 menu
     */
    void assembleToolbar();

    /**
     * @param view NavigationView
     *  返回键点击回调
     */
    void onNavigationClick(View view);

    /**
     *
     * @param item 菜单项
     * @return 暂无用
     * toolbar 菜单menuitem 点击回调
     */
    boolean onToolMenuItemClick(MenuItem item);

}
