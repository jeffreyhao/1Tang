package com.fold.dialog.listener;

/**
 * Created by adison on 2017/7/4.
 */

public abstract class DismissOnBtnClickL implements OnBtnClickL {

    @Override
    public boolean dismiss() {
        return true;
    }
}
