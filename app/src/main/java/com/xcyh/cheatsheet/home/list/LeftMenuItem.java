package com.xcyh.cheatsheet.home.list;

import com.fold.recyclyerview.entity.MultiItemEntity;

/**
 * 左栏扁平化后的列表项。
 * - L1 标题：不可点击
 * - L2 项：可点击，记录所属 (l1Index, l2Index)
 */
public class LeftMenuItem implements MultiItemEntity {

    public static final int TYPE_L1_HEADER = 1;
    public static final int TYPE_L2_ITEM = 2;

    public final int type;
    public final String text;
    public final int l1Index;   // 仅 TYPE_L2_ITEM 有效
    public final int l2Index;   // 仅 TYPE_L2_ITEM 有效

    private LeftMenuItem(int type, String text, int l1Index, int l2Index) {
        this.type = type;
        this.text = text;
        this.l1Index = l1Index;
        this.l2Index = l2Index;
    }

    public static LeftMenuItem ofL1Header(String text) {
        return new LeftMenuItem(TYPE_L1_HEADER, text, -1, -1);
    }

    public static LeftMenuItem ofL2Item(String text, int l1Index, int l2Index) {
        return new LeftMenuItem(TYPE_L2_ITEM, text, l1Index, l2Index);
    }

    @Override
    public int getItemType() {
        return type;
    }
}
