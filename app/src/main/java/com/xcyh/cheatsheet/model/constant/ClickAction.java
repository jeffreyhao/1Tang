package com.xcyh.cheatsheet.model.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 点击事件
 *
 * Created by haojiangfeng on 2024/11/22.
 */
@IntDef({
        ClickAction.ITEM_DETAIL,
        ClickAction.ITEM_SWITCH_TIMER,
        ClickAction.ITEM_BUDGET,
        ClickAction.ITEM_SWITCH,
        ClickAction.ITEM_ADV_NAME,
        ClickAction.ITEM_ADV_MULTI,
        ClickAction.ITEM_TASK_CANCEL,
        ClickAction.ITEM_TASK_CREATE,
        ClickAction.ITEM_TASK_MODIFY,
        ClickAction.ITEM_BUDGET_TIMER
})
@Retention(RetentionPolicy.SOURCE)
public @interface ClickAction {

    int ITEM_DETAIL         = 1;
    int ITEM_SWITCH_TIMER   = 2;
    int ITEM_BUDGET         = 3;
    int ITEM_SWITCH         = 4;
    int ITEM_ADV_NAME       = 5;


    /** 取消定时器 **/
    int ITEM_TASK_CANCEL    = 6;

    /** 创建定时器 **/
    int ITEM_TASK_CREATE    = 7;

    /** 修改定时器 **/
    int ITEM_TASK_MODIFY    = 8;

    /** 多选 **/
    int ITEM_ADV_MULTI      = 9;

    /** 预算定时器 **/
    int ITEM_BUDGET_TIMER   = 10;

}
