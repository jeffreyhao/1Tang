package com.benefit.novelverse.utils;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;

public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int bgColor;

    private boolean isGradient;
    private int textColor;
    private int startColor;
    private int endColor;

    private LinearGradient linearGradient;

    private int mPaddingTB;
    private int mPaddingLR;

    private int mRadius;
    private int mMeasuredTextWidth;

    public RoundBackgroundColorSpan(int bgColor, int textColor) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.isGradient = false;
        init();
    }

    public RoundBackgroundColorSpan(int startColor, int endColor, int textColor) {
        super();
        init();
        this.startColor = startColor;
        this.endColor = endColor;
        this.textColor = textColor;
        this.isGradient = true;
        init();
    }

    private void init(){
        mPaddingTB = 3;
        mPaddingLR = 20;
        mRadius = 15;
    }


    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if(mMeasuredTextWidth == 0){
            mMeasuredTextWidth = (int) paint.measureText(text, start, end);
        }
        return (mMeasuredTextWidth + 60);
    }


    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int originPaintColor = paint.getColor();


        if(isGradient) {
            if(linearGradient == null){
                linearGradient = new LinearGradient(0,0,0,0, startColor, endColor, Shader.TileMode.CLAMP);
            }
            paint.setShader(linearGradient);
        }else{
            paint.setColor(this.bgColor);
        }
        if(mMeasuredTextWidth == 0){
            mMeasuredTextWidth = (int) paint.measureText(text, start, end);
        }
        canvas.drawRoundRect(x,
                top + mPaddingTB,
                x + mMeasuredTextWidth + (mPaddingLR << 1),
                bottom + mPaddingTB,
                mRadius, mRadius, paint);


        paint.setShader(null);
        paint.setColor(this.textColor);
        canvas.drawText(text, start, end, x + mPaddingLR, y, paint);


        paint.setColor(originPaintColor);
    }
}