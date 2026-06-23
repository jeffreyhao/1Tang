package com.base.abs;

import androidx.fragment.app.FragmentActivity;

public interface IRecharge {

    boolean checkShowPremiumActiveDialog(String productId, String orderNo);

    void checkShowPremiumSubscribeDialog(FragmentActivity activity);

}
