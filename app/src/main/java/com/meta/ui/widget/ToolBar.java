package com.meta.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benefit.novelverse.R;

import androidx.annotation.Nullable;


public class ToolBar extends LinearLayout {


    private ImageView mImg;
    private TextView mTitle;
    private View mTopLine;
    private View mBottomLine;

    private int mAnimatorResource;



    public ToolBar(Context context) {
        super(context);
        init(context, null);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        inflate(context, R.layout.bottom_navigation_bar, this);
        mImg = findViewById(R.id.tool_bar_img);
        mTitle = findViewById(R.id.tool_bar_title);
        mTopLine = findViewById(R.id.tool_bar_top_line);
        mBottomLine = findViewById(R.id.tool_bar_bottom_line);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        if(attrs != null){
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ToolBar);
            int toolBarImg = attributes.getResourceId(R.styleable.ToolBar_img, 0);
            String toolBarTitle = attributes.getString(R.styleable.ToolBar_title);
            mImg.setBackgroundResource(toolBarImg);
            mTitle.setText(toolBarTitle);
            attributes.recycle();
        }
    }

    public void setChecked(final boolean isChecked) {
        boolean needAnim = isChecked && !isSelected();
        setSelected(isChecked);
        mImg.setSelected(isChecked);
        mTitle.setSelected(isChecked);
        if(needAnim){
            startIconAnim(mImg);
        }
    }

    private void startIconAnim(View view) {
        if(mAnimatorResource != 0){
            Animator animator = AnimatorInflater.loadAnimator(getContext(), mAnimatorResource);
            animator.setTarget(view);
            animator.start();
        }
    }

    public void setTitle(int title){
        mTitle.setText(title);
    }

    public void setTitleColor(ColorStateList color){
        mTitle.setTextColor(color);
    }

    public void setImg(Drawable drawable){
        mImg.setBackground(drawable);
    }

    public void setSelectAnimator(int animatorResource){
        mAnimatorResource = animatorResource;
    }

    public void setTopLineBackground(Drawable drawable){
        if(drawable != null){
            mTopLine.setBackground(drawable);
        }
    }

    public void setBottomLineBackground(Drawable drawable){
        if(drawable != null){
            mBottomLine.setBackground(drawable);
        }
    }

}
