package com.base.res.style;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;


public class ClickCallbackSpan extends ClickableSpan {

    private boolean showUnderline = false;

    private View.OnClickListener mListener;

    public ClickCallbackSpan(View.OnClickListener listener, boolean showUnderlineText) {
        this.mListener = listener;
        this.showUnderline = showUnderlineText;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(showUnderline);
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            mListener.onClick(widget);
        }
    }
}