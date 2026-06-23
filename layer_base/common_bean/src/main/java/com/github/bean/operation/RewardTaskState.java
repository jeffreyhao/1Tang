package com.github.bean.operation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2025/3/13.
 */
@IntDef({
        RewardTaskState.UNFINISHED,
        RewardTaskState.CLAIM,
        RewardTaskState.DONE
})
@Retention(RetentionPolicy.SOURCE)
public @interface RewardTaskState {


    // 0 未完成 1 完成(未领取) 2 完成(已领取)

    int UNFINISHED      = 0;

    int CLAIM           = 1;

    int DONE            = 2;

}
