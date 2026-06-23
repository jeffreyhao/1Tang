package com.baidu.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;

import androidx.annotation.Nullable;

/**
 *@Author: lhc
 *@Date: 2018/10/8 14:00
 *@Desc: 空页面view
 */
public class EmptyView extends LinearLayout {
    private static final int HIDE = 0;
    public static final int NO_DATA = 1;

    private ImageView imageView;
    private TextView textView;
    private TextView tvRetry;

    private int imgId;
    private int textId;
    private boolean showRetry;

    public EmptyView(Context context) {
        this(context,null);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);

        imgId = ta.getResourceId(R.styleable.EmptyView_imgId, 0);
        textId = ta.getResourceId(R.styleable.EmptyView_textId,0);
        showRetry = ta.getBoolean(R.styleable.EmptyView_showRetry,true);
        ta.recycle();
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
//        setBackgroundColor(Color.parseColor("#f9f9f9"));
        imageView = new ImageView(context);
        LayoutParams imgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageView.setImageResource(imgId);
        addView(imageView,imgParams);
        imageView.setVisibility(imgId!=0?VISIBLE:GONE);

        textView = new TextView(context);
        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.topMargin = UIUtil.dip2px(context,20);
        textParams.leftMargin = UIUtil.dip2px(context,20);
        textParams.rightMargin = UIUtil.dip2px(context,20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#666666"));
        textView.getPaint().setTextSize(UIUtil.dip2px(context,16));
        textView.setText(textId!=0?textId:R.string.common_no_data);
        addView(textView,textParams);

        if(showRetry){
            tvRetry = new TextView(context);
            LayoutParams retryParams = new LayoutParams(LayoutParams.WRAP_CONTENT, UIUtil.dip2px(context,40));
            retryParams.topMargin = UIUtil.dip2px(context,20);
            tvRetry.setGravity(Gravity.CENTER);
            tvRetry.setPadding(UIUtil.dip2px(context, 10), 0, UIUtil.dip2px(context, 10), 0);
            tvRetry.setMinWidth(UIUtil.dip2px(context,120));
            tvRetry.setTextColor(Color.parseColor("#666666"));
            tvRetry.getPaint().setTextSize(UIUtil.dip2px(context,16));
            tvRetry.setText(context.getString(R.string.pp_retry));
            tvRetry.setBackgroundResource(R.drawable.bg_btn_empty_retry);
            addView(tvRetry,retryParams);
        }

    }

    public void setEmptyImg(int imgId) {
        imageView.setVisibility(VISIBLE);
        imageView.setImageResource(imgId);
    }

    public void setEmptyHint(int content) {
        textView.setText(content);
    }

    public void setEmptyHint(String content) {
        textView.setText(content);
    }

    public void setType(int type) {
        switch (type) {
            case HIDE:
                setVisibility(GONE);
                break;
            case NO_DATA:
                setEmptyHint(R.string.common_no_data);
                break;
        }
    }

    public void setContent(int textId,int resId) {
        setVisibility(View.VISIBLE);
        textView.setVisibility(textId!=0?VISIBLE:GONE);
        imageView.setVisibility(resId!=0?VISIBLE:GONE);
        if(textId!=0) textView.setText(getContext().getString(textId));
        if(resId!=0) imageView.setImageResource(resId);
        if(textId==R.string.common_no_data){
            if(tvRetry!=null){
                tvRetry.setVisibility(View.GONE);
            }
        }
    }

}
