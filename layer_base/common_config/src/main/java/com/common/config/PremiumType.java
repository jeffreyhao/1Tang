package com.common.config;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        PremiumType.NONE,
        PremiumType.PURE_PREMIUM,
        PremiumType.BOTH_PREMIUM,
        PremiumType.FREE
})
@Retention(RetentionPolicy.SOURCE)
public @interface PremiumType {

    /**
     * 非会员： 普通支付包
     */
    int NONE            = 0;

    /**
     * 纯会员包：NovelDaily
     */
    int PURE_PREMIUM    = 1;

    /**
     * 普通支付 + 会员支付： NovelHive
     */
    int BOTH_PREMIUM    = 2;

    /**
     * 免费包
     */
    int FREE            = 3;

}
