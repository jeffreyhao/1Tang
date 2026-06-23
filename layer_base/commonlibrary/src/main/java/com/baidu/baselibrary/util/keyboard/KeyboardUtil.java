package com.baidu.baselibrary.util.keyboard;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class KeyboardUtil {


    public static void showKeyboard(Context context, EditText view) {
        if(context == null || view == null) {
            return;
        }
        view.setFocusable(true);
        view.requestFocus();

        InputMethodManager manager = ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    public static void hideKeyboard(Activity context) {
        if(context == null) {
            return;
        }
        InputMethodManager manager = ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
