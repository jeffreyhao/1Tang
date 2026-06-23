package com.baidu.baselibrary.widget.drawable;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;


/**
 * 垂直居中的 TextDrawable
 * Created by jeffrey on 2017/1/23.
 */

public class CenterVerticalTextDrawable extends TextDrawable {

    public CenterVerticalTextDrawable(View view) {
        super(view);
    }

    @Override
    public void measure(@NonNull Rect bounds) {
        super.measure(bounds);

        int restHeight = bounds.height() - getTextDrawHeight();
        int offsetY = restHeight / 2;

        int lineCount = getLineIndexList().size();
        if(lineCount > 0){
            for (int i = 0; i < lineCount; i ++){
                LineIndex lineIndex = getLineIndexList().get(i);
                lineIndex.baseLineY += offsetY;
            }
        }

        // 绘制的边界
        int textDrawWidth = measureTextWidth();
        int textDrawHeight = bounds.height();
        setTextDrawSize(textDrawWidth, textDrawHeight);
        mDrawWidth = textDrawWidth;
        mDrawHeight = textDrawHeight;
    }
}
