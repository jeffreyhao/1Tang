package com.benefit.novelverse.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public class ValueAnimatorUtil {
    public static void heightChangeAnimator(View view, int height, boolean isIncrease) {
        if(isIncrease) {
            view.setVisibility(View.VISIBLE);
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(isIncrease?0:height,isIncrease?height:0);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams.height = (int) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }
}
