package com.baidu.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.jess.baselibrary.R;

public class GradientTextView extends AppCompatTextView {

    private static final int COLOR_UNSET = 0x00000001;

    private int startColor;
    private int centerColor;
    private int endColor;
    private int orientation; // 0: horizontal, 1: vertical

    public GradientTextView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            startColor = getCurrentTextColor();
            centerColor = COLOR_UNSET;
            endColor = getCurrentTextColor();
            orientation = 0;
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        startColor = ta.getColor(R.styleable.GradientTextView_startColor, getCurrentTextColor());
        centerColor = ta.getColor(R.styleable.GradientTextView_centerColor, COLOR_UNSET);
        endColor = ta.getColor(R.styleable.GradientTextView_endColor, getCurrentTextColor());
        orientation = ta.getInt(R.styleable.GradientTextView_orientation, 0);
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            applyGradient(w, h);
        }
    }

    private void applyGradient(int width, int height) {
        float xEnd = orientation == 0 ? (float) width : 0f;
        float yEnd = orientation == 1 ? (float) height : 0f;

        LinearGradient shader;
        if (centerColor != COLOR_UNSET) {
            shader = new LinearGradient(0f, 0f, xEnd, yEnd,
                    new int[]{startColor, centerColor, endColor},
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.CLAMP);
        } else {
            shader = new LinearGradient(0f, 0f, xEnd, yEnd,
                    startColor, endColor, Shader.TileMode.CLAMP);
        }

        getPaint().setShader(shader);
        invalidate();
    }

    public void setGradientColors(int startColor, int endColor) {
        this.startColor = startColor;
        this.centerColor = COLOR_UNSET;
        this.endColor = endColor;
        if (getWidth() > 0 && getHeight() > 0) {
            applyGradient(getWidth(), getHeight());
        }
    }

    public void setGradientColors(int startColor, int endColor, int orientation) {
        this.startColor = startColor;
        this.centerColor = COLOR_UNSET;
        this.endColor = endColor;
        this.orientation = orientation;
        if (getWidth() > 0 && getHeight() > 0) {
            applyGradient(getWidth(), getHeight());
        }
    }

    public void setGradientColors(int startColor, int centerColor, int endColor, int orientation) {
        this.startColor = startColor;
        this.centerColor = centerColor;
        this.endColor = endColor;
        this.orientation = orientation;
        if (getWidth() > 0 && getHeight() > 0) {
            applyGradient(getWidth(), getHeight());
        }
    }
}
