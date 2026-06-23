package com.xcyh.cheatsheet.home.list;

import com.fold.recyclyerview.entity.MultiItemEntity;

/**
 * 右栏扁平化后的列表项。
 * - L1 标题 / L2 标题：不可点击
 * - L3 叶子：可点击
 * 仅 L1 标题记录 l1Index（用于映射）。
 */
public class RightMenuItem implements MultiItemEntity {

    public static final int TYPE_L1_HEADER = 1;
    public static final int TYPE_L2_HEADER = 2;
    public static final int TYPE_L3_ITEM = 3;

    public final int type;
    public final String text;
    public final int l1Index;

    private RightMenuItem(int type, String text, int l1Index) {
        this.type = type;
        this.text = text;
        this.l1Index = l1Index;
    }

    public static RightMenuItem ofL1Header(String text) {
        return new RightMenuItem(TYPE_L1_HEADER, text, -1);
    }

    public static RightMenuItem ofL2Header(String text) {
        return new RightMenuItem(TYPE_L2_HEADER, text, -1);
    }

    public static RightMenuItem ofL3Item(String text) {
        return new RightMenuItem(TYPE_L3_ITEM, text, -1);
    }

    @Override
    public int getItemType() {
        return type;
    }
}
