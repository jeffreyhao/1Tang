package com.meta.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Created by haojiangfeng on 2025/1/13.
 */
public class LimitLinearLayout extends LinearLayout {

    public int mMaxHeight = 0;

    public LimitLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public LimitLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LimitLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LimitLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            int[] attrsArray = new int[]{android.R.attr.maxHeight};
            TypedArray typedArray = context.obtainStyledAttributes(attrs, attrsArray);
            mMaxHeight = typedArray.getDimensionPixelSize(0, -1);
            typedArray.recycle();
        }
    }

    public void setMaxHeight(int maxHeight){
        mMaxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (heightMode == MeasureSpec.UNSPECIFIED || heightSize > mMaxHeight) {
                heightSize = mMaxHeight;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
