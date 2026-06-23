package com.base.api;

import android.app.Activity;

import com.base.abs.IFacebookTrack;
import com.base.abs.ILanguage;

public class FactbookTrackApi {

    public static IFacebookTrack iFacebookTrack;


    public static void startPay(Activity activity, double money) {
        if(iFacebookTrack != null) {
            iFacebookTrack.startPay(activity, money);
        }
    }


}
