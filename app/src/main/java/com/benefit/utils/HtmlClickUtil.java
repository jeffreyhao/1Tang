package com.benefit.utils;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.benefit.listener.HtmlLinkClickListener;
import com.benefit.novelverse.R;

import java.net.URL;

/**
 * Time: 2024/1/5
 * Author: lhc
 * Desc:
 */
public class HtmlClickUtil {
    public static CharSequence getClickableHtml(Activity activity, String htmlContent){
        if(TextUtils.isEmpty(htmlContent)) return "";
        Spanned spanned = Html.fromHtml(htmlContent);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);
        URLSpan[] spans = spannableStringBuilder.getSpans(0, spanned.length(), URLSpan.class);
        if(spans != null){
            for(URLSpan item: spans){
                setLinkClickable(activity, spannableStringBuilder, item);
            }
        }
        return spannableStringBuilder;
    }

    private static void setLinkClickable(Activity activity, SpannableStringBuilder stringBuilder, URLSpan urlSpan) {
        if(activity==null
                ||stringBuilder==null
                ||TextUtils.isEmpty(stringBuilder.toString())
                ||urlSpan==null
        ) return;
        int spanStart = stringBuilder.getSpanStart(urlSpan);
        int spanEnd = stringBuilder.getSpanEnd(urlSpan);
        int flags = stringBuilder.getSpanFlags(urlSpan);
        stringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = urlSpan.getURL();
                if(!TextUtils.isEmpty(url)&&url.contains("novelverse")){
//                    SendFeedBackActivity.start(activity, 4);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.linkColor = ContextCompat.getColor(activity, R.color.colorAccent);
                ds.setUnderlineText(true);
            }
        }, spanStart, spanEnd, flags);
    }

    public static CharSequence getClickableHtml(Activity activity, String htmlContent, HtmlLinkClickListener listener){
        if(TextUtils.isEmpty(htmlContent)) return "";
        Spanned spanned = Html.fromHtml(htmlContent);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);
        URLSpan[] spans = spannableStringBuilder.getSpans(0, spanned.length(), URLSpan.class);
        if(spans != null){
            for(URLSpan item: spans){
                setLinkClickable(activity, spannableStringBuilder, item, listener);
            }
        }
        return spannableStringBuilder;
    }

    private static void setLinkClickable(Activity activity, SpannableStringBuilder stringBuilder, URLSpan urlSpan, HtmlLinkClickListener listener) {
        if(activity==null
                ||stringBuilder==null
                ||TextUtils.isEmpty(stringBuilder.toString())
                ||urlSpan==null
        ) return;
        int spanStart = stringBuilder.getSpanStart(urlSpan);
        int spanEnd = stringBuilder.getSpanEnd(urlSpan);
        int flags = stringBuilder.getSpanFlags(urlSpan);
        stringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = urlSpan.getURL();
                if(!TextUtils.isEmpty(url)&&url.contains("novelverse")){
//                    SendFeedBackActivity.start(activity, 4);
                }
                if(listener!=null) {
                    listener.onLinkClick();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.linkColor = ContextCompat.getColor(activity, R.color.Cr_FFAC3B);
                ds.setUnderlineText(true);
            }
        }, spanStart, spanEnd, flags);
    }
}
