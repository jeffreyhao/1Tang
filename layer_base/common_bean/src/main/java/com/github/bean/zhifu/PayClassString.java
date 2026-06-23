package com.github.bean.zhifu;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 支付类别：普通支付、订阅金币、订阅会员
 */
@StringDef({
        PayClassString.NONE,
        PayClassString.COMMON_COINS,
        PayClassString.SUBSCRIBE_COINS,
        PayClassString.SUBSCRIBE_PREMIUM
})
@Retention(RetentionPolicy.SOURCE)
public @interface PayClassString {

    /** 啥也不是 **/
    String NONE                 = "-1";

    /** 普通支付 **/
    String COMMON_COINS         = "0";

    /** 订阅支付 **/
    String SUBSCRIBE_COINS      = "3";

    /** 会员支付 **/
    String SUBSCRIBE_PREMIUM    = "4";

}
