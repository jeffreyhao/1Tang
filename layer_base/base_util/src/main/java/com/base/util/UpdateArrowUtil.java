package com.base.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.List;

public class UpdateArrowUtil {
    public static void updateAllArrows(View view, String tag) {
        try{
            List<ImageView> arrows = new ArrayList<>();
            findImageViewsByTag(view, tag, arrows);
            for (ImageView arrow : arrows) {
                // RTL 翻转（VectorDrawable autoMirrored 可省略）
                if (ViewCompat.getLayoutDirection(arrow) == ViewCompat.LAYOUT_DIRECTION_RTL || AppUtil.isRTLMode()) {
                    arrow.setScaleX(-1f);
                } else {
                    arrow.setScaleX(1f);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void findImageViewsByTag(View view, String tag, List<ImageView> arrows) {
        if (view instanceof ImageView && tag.equals(view.getTag())) {
            arrows.add((ImageView) view);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findImageViewsByTag(group.getChildAt(i), tag, arrows);
            }
        }
    }
}
