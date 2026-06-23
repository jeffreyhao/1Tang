
package com.github.bean.operation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by haojiangfeng on 2025/3/17.
 */
@IntDef({
        ActivityDataWindowType.DIALOG_WINDOW,
        ActivityDataWindowType.FLOAT_WINDOW
})
@Retention(RetentionPolicy.SOURCE)
public @interface ActivityDataWindowType {

    int DIALOG_WINDOW       = 0;
    int FLOAT_WINDOW        = 1;

}
