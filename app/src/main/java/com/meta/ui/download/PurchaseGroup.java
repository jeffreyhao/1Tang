package com.meta.ui.download;

import android.view.ViewGroup;

import com.github.bean.zhifu.PurchaseInfoBean;

import java.util.ArrayList;
import java.util.List;

public class PurchaseGroup {

    private final ViewGroup mViewGroup;

    public PurchaseGroup(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    private final List<PurchaseSingle> singles = new ArrayList<>();
    public void initGroup(List<PurchaseInfoBean.ChapterShowsBean> list, int selectId, PurchaseListener purchaseListener) {
        singles.clear();
        mViewGroup.removeAllViews();
        int index = 0;
        int selectIndex = 0;
        for (PurchaseInfoBean.ChapterShowsBean obj : list) {
            if (obj.isShow == 0) {
                continue;
            }
            if (selectId != 0 && selectId == obj.id) {
                selectIndex = index;
            }
            index++;
            PurchaseSingle single = new PurchaseSingle(mViewGroup.getContext(), null);
            single.initSingle(obj);
            single.setOnClickListener(v -> {
                resetCheck();
                single.checkSingle(true);
                purchaseListener.checkPurchase(obj);
            });
            mViewGroup.addView(single);
            singles.add(single);
        }
        if (singles.size() > selectIndex) {
            singles.get(selectIndex).performClick();
        }
    }

    /**
     * 重置选择状态
     */
    private void resetCheck() {
        for (PurchaseSingle single : singles) {
            single.checkSingle(false);
        }
    }

    public interface PurchaseListener {
        void checkPurchase(PurchaseInfoBean.ChapterShowsBean obj);
    }
}