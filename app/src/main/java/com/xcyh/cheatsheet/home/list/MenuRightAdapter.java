package com.xcyh.cheatsheet.home.list;

import android.view.View;

import com.benefit.novelverse.R;
import com.fold.recyclyerview.BaseMultiItemQuickAdapter;
import com.fold.recyclyerview.BaseViewHolder;

import java.util.List;

/**
 * 右栏适配器：L1 标题 + L2 标题 + L3 叶子混排。
 * L3 点击回调（占位）。
 */
public class MenuRightAdapter extends BaseMultiItemQuickAdapter<RightMenuItem, BaseViewHolder> {

    /** L3 叶子被点击回调 */
    public interface OnRightItemSelectedListener {
        void onL3Selected(String text);
    }

    private OnRightItemSelectedListener mListener;

    public MenuRightAdapter(List<RightMenuItem> data) {
        super(data);
        addItemType(RightMenuItem.TYPE_L1_HEADER, R.layout.item_menu_l1_header);
        addItemType(RightMenuItem.TYPE_L2_HEADER, R.layout.item_menu_l2_header);
        addItemType(RightMenuItem.TYPE_L3_ITEM, R.layout.item_menu_l3_item);
    }

    public void setOnRightItemSelectedListener(OnRightItemSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, RightMenuItem item) {
        helper.setText(R.id.tv_title, item.text);
        if (item.type == RightMenuItem.TYPE_L3_ITEM) {
            View itemView = helper.itemView;
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onL3Selected(item.text);
                }
            });
        }
    }
}
