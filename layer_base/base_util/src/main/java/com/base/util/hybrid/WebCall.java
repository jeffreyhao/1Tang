package com.base.util.hybrid;

import androidx.annotation.Keep;

import com.base.api.Logger;

@Keep
public class WebCall {

    public static WebCallListener sHybrid = new WebCallListener() {
        @Override
        public void hybrid(String message) {
            Logger.textSingle("WebCall", "hybrid","[SEC_NATIVE] " + message);
        }
    };


}
