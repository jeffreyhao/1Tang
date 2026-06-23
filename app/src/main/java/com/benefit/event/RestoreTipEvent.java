package com.benefit.event;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class RestoreTipEvent {

    public static final int TIP_TYPE_REMIND         = 0;
    public static final int TIP_TYPE_PAY_FAIL       = 1;
    public static final int TIP_TYPE_RECHARGE_ERR   = 2;
    public static final int TIP_TYPE_PENDING        = 4;


    public static final int ACTION_CLICK_CLOSE          = 1001;
    public static final int ACTION_CLICK_BUTTON         = 1002;
    public static final int ACTION_CLICK_BOTTOM_TIP     = 1003;


    @IntDef({RestoreTipEvent.TIP_TYPE_REMIND, RestoreTipEvent.TIP_TYPE_PAY_FAIL, RestoreTipEvent.TIP_TYPE_RECHARGE_ERR, RestoreTipEvent.TIP_TYPE_PENDING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RestoreTipType {}

    @IntDef({RestoreTipEvent.ACTION_CLICK_CLOSE, RestoreTipEvent.ACTION_CLICK_BUTTON, RestoreTipEvent.ACTION_CLICK_BOTTOM_TIP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RestoreTipAction {}



    //////////////////////////////////////////////////////////////////////////////////////////



    public @RestoreTipType int tipType;
    public @RestoreTipAction int action;


    public RestoreTipEvent(@RestoreTipType int tipType, @RestoreTipAction int action){
        this.tipType = tipType;
        this.action = action;
    }

}
