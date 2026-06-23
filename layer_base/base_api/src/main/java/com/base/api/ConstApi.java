package com.base.api;

import com.base.abs.IConst;

public class ConstApi {

    public static IConst iConst;


    public static int UPDATE_PREMIUM_STATUS() {
        if(iConst != null) {
            return iConst.UPDATE_PREMIUM_STATUS();
        }
        return -1;
    }

    public static int READ_ACTIVITY_FINISH() {
        if(iConst != null) {
            return iConst.READ_ACTIVITY_FINISH();
        }
        return -1;
    }

}
