package com.baidu.baselibrary.util.ui;

import com.baidu.baselibrary.widget.CustomRefreshFooter;
import com.baidu.baselibrary.widget.RefreshNoMoreFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

/**
* @author lhc
* @date 2022/5/10 9:25
* @desc
*/
public class RefreshUtils {
    public static void setRefreshFooter(boolean isHasNext, SmartRefreshLayout refresh) {
        if (refresh != null) {
            refresh.finishLoadMore();
            refresh.finishRefresh();
            if (isHasNext) {
                refresh.setRefreshFooter(new CustomRefreshFooter(refresh.getContext()));
            } else {
                refresh.setRefreshFooter(new RefreshNoMoreFooter(refresh.getContext()));
            }
        }
    }
}
