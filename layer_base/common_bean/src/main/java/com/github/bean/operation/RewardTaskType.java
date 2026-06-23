package com.github.bean.operation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2025/3/12.
 */
@IntDef({
        RewardTaskType.CommonTask,
        RewardTaskType.DailyAdvTask
})
@Retention(RetentionPolicy.SOURCE)
public @interface RewardTaskType {

    int CommonTask      = 1;

    int DailyAdvTask    = 2;
}
