package com.xcyh.cheatsheet.home.list;

import android.graphics.Typeface;
import android.view.View;

import com.benefit.novelverse.R;
import com.fold.recyclyerview.BaseMultiItemQuickAdapter;
import com.fold.recyclyerview.BaseViewHolder;

import java.util.List;

/**
 * 左栏适配器：L1 标题 + L2 项混排。
 * 通过 {@link #setSelected(int, int)} 切换 L2 选中态（背景高亮 + 文字主色加粗）。
 */
public class MenuLeftAdapter extends BaseMultiItemQuickAdapter<LeftMenuItem, BaseViewHolder> {

    /** 左栏 L2 被点击回调 */
    public interface OnLeftItemSelectedListener {
        void onL2Selected(int l1Index, int l2Index);
    }

    private OnLeftItemSelectedListener mListener;

    /** 当前选中 L2，初始 (0,0) */
    private int selectedL1 = 0;
    private int selectedL2 = 0;

    public MenuLeftAdapter(List<LeftMenuItem> data) {
        super(data);
        addItemType(LeftMenuItem.TYPE_L1_HEADER, R.layout.item_menu_l1_header);
        addItemType(LeftMenuItem.TYPE_L2_ITEM, R.layout.item_menu_l2_item);
    }

    public void setOnLeftItemSelectedListener(OnLeftItemSelectedListener listener) {
        this.mListener = listener;
    }

    /**
     * 更新选中态，局部刷新旧/新两项。
     */
    public void setSelected(int l1Index, int l2Index) {
        if (l1Index == selectedL1 && l2Index == selectedL2) {
            return;
        }
        int oldPos = findL2ItemPosition(selectedL1, selectedL2);
        selectedL1 = l1Index;
        selectedL2 = l2Index;
        int newPos = findL2ItemPosition(selectedL1, selectedL2);
        if (oldPos >= 0) {
            notifyItemChanged(oldPos);
        }
        if (newPos >= 0) {
            notifyItemChanged(newPos);
        }
    }

    /** 返回指定 L2 项在列表中的位置，找不到返回 -1。 */
    public int findL2ItemPosition(int l1Index, int l2Index) {
        if (mData == null) {
            return -1;
        }
        for (int i = 0; i < mData.size(); i++) {
            LeftMenuItem item = mData.get(i);
            if (item.type == LeftMenuItem.TYPE_L2_ITEM
                    && item.l1Index == l1Index
                    && item.l2Index == l2Index) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void convert(BaseViewHolder helper, LeftMenuItem item) {
        if (item.type == LeftMenuItem.TYPE_L1_HEADER) {
            helper.setText(R.id.tv_title, item.text);
            return;
        }
        // TYPE_L2_ITEM
        // 注意：BaseViewHolder 没有 getContext()，颜色/资源通过 adapter 自身的 mContext 取
        // （BaseQuickAdapter.onCreateViewHolder 中赋值，protected Context mContext）
        boolean selected = item.l1Index == selectedL1 && item.l2Index == selectedL2;
        View root = helper.getView(R.id.fl_root);
        root.setBackgroundColor(mContext.getResources().getColor(
                selected ? R.color.menu_selected_bg : android.R.color.transparent));
        android.widget.TextView tv = helper.getView(R.id.tv_title);
        tv.setText(item.text);
        tv.setTextColor(mContext.getResources().getColorStateList(
                selected ? R.color.colorAccent : R.color.darkColorAccent).getDefaultColor());
        tv.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
        root.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onL2Selected(item.l1Index, item.l2Index);
            }
        });
    }
}
