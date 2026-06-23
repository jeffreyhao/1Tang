package com.github.bean.operation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2025/3/17.
 */


@IntDef({
        ActivityDataJumpType.JUMP_BOOK_RECOMMEND,
        ActivityDataJumpType.JUMP_RECHARGE,
        ActivityDataJumpType.JUMP_OTHER,
        ActivityDataJumpType.JUMP_ADV,
        ActivityDataJumpType.JUMP_EXIT_RECOMMEND,
        ActivityDataJumpType.JUMP_ADV_SUBSCRIBE
})
@Retention(RetentionPolicy.SOURCE)
public @interface ActivityDataJumpType {

    int JUMP_BOOK_RECOMMEND     = 0;    // 书籍推荐

    int JUMP_RECHARGE           = 1;    // 充值

    int JUMP_OTHER              = 2;    // 其他
    int JUMP_ADV                = 3;    // 广告

    int JUMP_EXIT_RECOMMEND     = 4;    // 退出阅读推荐
    int JUMP_ADV_SUBSCRIBE      = 5;    // 点击广告订阅



}
