package com.tencent.common.animation.FadeExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.tencent.common.animation.BaseAnimatorSet;

public class FadeExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration));
	}
}
