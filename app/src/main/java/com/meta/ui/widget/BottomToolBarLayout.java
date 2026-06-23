package com.meta.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.benefit.novelverse.R;

public class BottomToolBarLayout extends LinearLayout {


    public interface OnTabClickListener {
        void onTabClick(View view, int position);
    }


    private ToolBar mToolBar0, mToolBar1, mToolBar2, mToolBar3, mToolBar4;
    private OnTabClickListener mOnTabClickListener;


    public BottomToolBarLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BottomToolBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BottomToolBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BottomToolBarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setOnTabClickListener(OnTabClickListener listener){
        mOnTabClickListener = listener;
    }

    public ToolBar getToolBar(int position) {
        switch (position){
            case 0: return mToolBar0;
            case 1: return mToolBar1;
            case 2: return mToolBar2;
            case 3: return mToolBar3;
            case 4: return mToolBar4;
            default: return null;
        }
    }


    private void init(Context context, AttributeSet attrs){
        inflate(context, R.layout.layout_bottom_tool_bar, this);
        mToolBar0 = findViewById(R.id.main_tab_0);
        mToolBar1 = findViewById(R.id.main_tab_1);
        mToolBar2 = findViewById(R.id.main_tab_2);
        mToolBar3 = findViewById(R.id.main_tab_3);
        mToolBar4 = findViewById(R.id.main_tab_4);
        initListeners();
    }


    private void initListeners() {
        mToolBar0.setOnClickListener(v -> {
            if(mOnTabClickListener != null){
                mOnTabClickListener.onTabClick(v, 0);
            }
        });
        mToolBar1.setOnClickListener(v -> {
            if(mOnTabClickListener != null){
                mOnTabClickListener.onTabClick(v, 1);
            }
        });
        mToolBar2.setOnClickListener(v -> {
            if(mOnTabClickListener != null){
                mOnTabClickListener.onTabClick(v, 2);
            }
        });
        mToolBar3.setOnClickListener(v -> {
            if(mOnTabClickListener != null){
                mOnTabClickListener.onTabClick(v, 3);
            }
        });
        mToolBar4.setOnClickListener(v -> {
            if(mOnTabClickListener != null){
                mOnTabClickListener.onTabClick(v, 4);
            }
        });
    }


}
