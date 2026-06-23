package com.xcyh.cheatsheet.app.utils;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.view.View;

import androidx.annotation.AnimatorRes;


/**
 * Created by haojiangfeng on 2024/12/27.
 */
public class AnimatorUtil {


    public static void startAnimator(View view, @AnimatorRes int animatorRes){
        if(view == null || animatorRes == 0){
            return;
        }

        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(view.getContext(), animatorRes);

        animatorSet.setTarget(view);
        animatorSet.start();
    }


}
