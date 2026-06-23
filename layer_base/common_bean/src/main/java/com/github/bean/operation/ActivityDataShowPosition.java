package com.github.bean.operation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2025/3/17.
 */


@IntDef({
        ActivityDataShowPosition.BOOKSHELF,
        ActivityDataShowPosition.DISCOVER,
        ActivityDataShowPosition.MINE,
        ActivityDataShowPosition.REWARD,
        ActivityDataShowPosition.RAED
})
@Retention(RetentionPolicy.SOURCE)
public @interface ActivityDataShowPosition {

    int BOOKSHELF       = 1;
    int DISCOVER        = 2;
    int MINE            = 3;
    int REWARD          = 4;
    int RAED            = 5;
}
