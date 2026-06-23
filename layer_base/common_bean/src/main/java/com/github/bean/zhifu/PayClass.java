package com.github.bean.zhifu;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 支付类别：普通支付、订阅金币、订阅会员
 */
@IntDef({
        PayClass.NONE,
        PayClass.COMMON_COINS,
        PayClass.SUBSCRIBE_COINS,
        PayClass.SUBSCRIBE_PREMIUM
})
@Retention(RetentionPolicy.SOURCE)
public @interface PayClass {

    /** 啥也不是 **/
    int NONE                    = -1;

    /** 普通支付 **/
    int COMMON_COINS            = 0;

    /** 订阅支付 **/
    int SUBSCRIBE_COINS         = 3;

    /** 会员支付 **/
    int SUBSCRIBE_PREMIUM       = 4;

}
