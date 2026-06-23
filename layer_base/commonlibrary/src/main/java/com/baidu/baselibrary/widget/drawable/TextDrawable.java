package com.baidu.baselibrary.widget.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * Created by jeffrey on 2017/1/6.
 */

public class TextDrawable extends Drawable {

    public static final String CHAR_CN = "阅";
    public static final String CHAR_EN = "i";
    public static final String STR_MORE = "···";

    private static final int LAYOUT_EXACTLY = 1;
    private static final int LAYOUT_WRAP_CONTENT = 2;

    /**
     * 这个数组保存了每个字符的长度
     */
    private static float[] sCharWidths = new float[500];
    private static int sMoreCharWidth;

    /**
     * 背景 drawable
     */
    protected Drawable mBG;
    /**
     * 绘制的文本
     */
    private String mText = "";
    /**
     * 可绘制文字的范围
     */
    private Rect mTextBounds;
    /**
     * TextColor 状态表
     */
    private ColorStateList mTextColors;
    /**
     * 当前 TextColor
     */
    private int mCurTextColor;
    /**
     * Layout类型，默认为 wrap_content
     */
    private int mLayoutType = LAYOUT_WRAP_CONTENT;

    /**
     * 最后一行文字的宽度，不包括 "..."
     */
    private int mLastLineTextWidth;
    /**
     * 最大行数
     */
    private int mMaxLines = Integer.MAX_VALUE;
    /**
     * 绘制的最大高度
     */
    private int mMaxHeightLimit = Integer.MAX_VALUE;
    /**
     * 绘制的行数
     */
    private int mDrawLines = 0;
    /**
     * 行间距，默认为 1px
     */
    private int mLineSpacingExtra = 1;
    /**
     * 行间距倍数
     */
    private float mLineSpacingMultiplier = 1f;

    /**
     * 强制显示 "..."
     */
    private boolean mShowMoreForce = true;
    /**
     * "..."的宽度
     */
    private float mApostropheMoreWidth;
    /**
     * 首行的baseLine
     */
    private int mFirstBaseLine;
    /**
     * padding
     */
    private int mPaddingLeft, mPaddingRight, mPaddingTop, mPaddingBottom;

    /**
     * measure 是否 有效。
     * true，则不需要重新 measure；
     * false，则需要 measure；
     */
    private boolean mMeasureValid = false;

    /**
     * FontMetrics
     */
    private Paint.FontMetricsInt mFontMetrics;
    /**
     * 单个文字高度
     */
    private int mWordHeight;
    /**
     * 单个文字的 baseLine
     */
    private int mWordBaseLineY;

    /**
     * 绘制的最后一个字符的索引位置
     */
    private int mLastShowCharIndex = 0;

    /**
     * 测量数据
     */
    private MeasuredParam mMeasuredParam;


    protected Paint mPaint;
    protected boolean mIsBoundSetted = false;

    protected int mDrawHeight;
    protected int mDrawWidth;


    public TextDrawable(View view) {
        setCallback(view);
        init();
    }

    public TextDrawable(View view, Paint paint) {
        setCallback(view);
        init();
    }


    public Paint getPaint() {
        return mPaint;
    }


    /**
     * 设置边界。
     * 如果边界发生变化 或者 measure状态已失效，则重新计算。
     *
     * @param bounds
     */
    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        if (mBG != null) {
            mBG.setBounds(bounds);
        }

        // set draw rect
        mTextBounds.set(bounds.left + mPaddingLeft,
                bounds.top + mPaddingTop,
                bounds.right - mPaddingRight,
                bounds.bottom - mPaddingBottom);

        if (!mMeasureValid) {
            measure(bounds);
        }
    }

    /**
     * 设置边界。
     * 如果边界发生变化 或者 measure状态已失效，则重新计算。
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        if (mBG != null) {
            mBG.setBounds(left, top, right, bottom);
        }

        // set draw rect
        mTextBounds.set(left + mPaddingLeft,
                top + mPaddingTop,
                right - mPaddingRight,
                bottom - mPaddingBottom);

        if (!mMeasureValid) {
            measure(getBounds());
        }
    }

    public void setBoundsWidthoutMeasure(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);

        if (mBG != null) {
            mBG.setBounds(left, top, right, bottom);
        }

        // set draw rect
        mTextBounds.set(left + mPaddingLeft,
                top + mPaddingTop,
                right - mPaddingRight,
                bottom - mPaddingBottom);
    }

    /**
     * 获取绘制文字的高度
     *
     * @return
     */
    public int getDrawHeight() {
        return mMeasuredParam.textDrawHeight + mPaddingTop + mPaddingBottom;
    }

    /**
     * 获取绘制文字的宽度
     *
     * @return
     */
    public int getDrawWidth() {
        return mMeasuredParam.textDrawWidth + mPaddingLeft + mPaddingRight;
    }

    /**
     * 获取文字绘制宽度
     *
     * @return
     */
    public int getTextDrawWidth() {
        return mMeasuredParam.textDrawWidth;
    }

    /**
     * 获取文字绘制高度
     *
     * @return
     */
    public int getTextDrawHeight() {
        return mMeasuredParam.textDrawHeight;
    }

    /**
     * fixme 设置布局类型
     *
     * @param layoutType one of {@link #LAYOUT_EXACTLY}、{@link #LAYOUT_WRAP_CONTENT}
     */
    public void setLayoutType(int layoutType) {
        this.mLayoutType = layoutType;
    }

    /**
     * fixme 获取布局类型
     *
     * @return
     * @see #setLayoutType(int)
     */
    public int getLayoutType() {
        return mLayoutType;
    }


    /**
     * 设置阴影
     *
     * @param radius 阴影半径
     * @param dx     x方向上的阴影长度
     * @param dy     y方向上的阴影长度
     * @param color  阴影颜色
     */
    public void setShadowLayer(float radius, float dx, float dy, int color) {
        mPaint.setShadowLayer(radius, dx, dy, color);
        invalidateSelf();
    }


    /**
     * 设置背景
     *
     * @param resId
     */
    public void setBackgroundResource(int resId) {
        View view = (View) getCallback();
        if (view == null) {
            return;
        }
        mBG = view.getResources().getDrawable(resId);
        invalidateSelf();
    }

    /**
     * 设置背景
     *
     * @param drawable
     */
    public void setBackgroundDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        mBG = drawable;
        invalidateSelf();
    }

    /**
     * 获取背景 drawable
     *
     * @return
     */
    public Drawable getBackgroundDrawable() {
        return mBG;
    }

    /**
     * 设置绘制文本
     *
     * @param text
     */
    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        if (mText == null) {
            mText = "";
        }
        if (!mText.equals(text)) {
            mText = text;
            mMeasureValid = false;
            View view = (View) getCallback();
            view.requestLayout();
        }
    }

    /**
     * 将 measure 状态置为无效，会在下次 onMeasure 时候重新 measure
     */
    public void setMeasureInvalid() {
        mMeasureValid = false;
    }

    /**
     * 将 measure 状态置为有效，避免下次 onMeasure 时候重新 measure
     */
    public void setMeasureValid() {
        mMeasureValid = true;
    }

    /**
     * 设置强制使用 "..."
     *
     * @param showMore
     */
    public void setForceShowMore(boolean showMore) {
        if (mShowMoreForce == showMore) {
            return;
        }
        mShowMoreForce = showMore;
        mMeasureValid = false;
    }

    /**
     * 获取首行 baseline
     *
     * @return
     */
    public int getFirstBaseLine() {
        return mFirstBaseLine;
    }

    /**
     * 设置左边内边距
     *
     * @param paddingLeft
     */
    public void setPaddingLeft(int paddingLeft) {
        if (mPaddingLeft == paddingLeft) {
            return;
        }
        this.mPaddingLeft = paddingLeft;
        mMeasureValid = false;
    }

    /**
     * 设置右边内边距
     *
     * @param paddingRight
     */
    public void setPaddingRight(int paddingRight) {
        if (mPaddingRight == paddingRight) {
            return;
        }
        this.mPaddingRight = paddingRight;
    }

    /**
     * 设置顶部内边距
     *
     * @param paddingTop
     */
    public void setPaddingTop(int paddingTop) {
        if (mPaddingTop == paddingTop) {
            return;
        }
        this.mPaddingTop = paddingTop;
        mMeasureValid = false;
    }

    /**
     * 设置底部内边距
     *
     * @param paddingBottom
     */
    public void setPaddingBottom(int paddingBottom) {
        if (mPaddingBottom == paddingBottom) {
            return;
        }
        this.mPaddingBottom = paddingBottom;
        mMeasureValid = false;
    }

    /**
     * 获取左边内边距
     *
     * @return
     */
    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    /**
     * 获取顶部内边距
     *
     * @return
     */
    public int getPaddingTop() {
        return mPaddingTop;
    }

    /**
     * 获取右边内边距
     *
     * @return
     */
    public int getPaddingRight() {
        return mPaddingRight;
    }

    /**
     * 获取底部内边距
     *
     * @return
     */
    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    /**
     * 设置内边距。
     *
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        if (mPaddingLeft == paddingLeft
                && mPaddingTop == paddingTop
                && mPaddingRight == paddingRight
                && mPaddingBottom == paddingBottom) {
            return;
        }
        this.mPaddingLeft = paddingLeft;
        this.mPaddingTop = paddingTop;
        this.mPaddingRight = paddingRight;
        this.mPaddingBottom = paddingBottom;
        mMeasureValid = false;
    }

    /**
     * 设置内边距。
     *
     * @param padding
     */
    public void setPadding(int padding) {
        if (mPaddingLeft == padding
                && mPaddingTop == padding
                && mPaddingRight == padding
                && mPaddingBottom == padding) {
            return;
        }
        this.mPaddingLeft = padding;
        this.mPaddingTop = padding;
        this.mPaddingRight = padding;
        this.mPaddingBottom = padding;
        mMeasureValid = false;
    }

    /**
     * 设置行间距
     *
     * @param lineSpacing    行间距
     * @param lineSpaceMulti 行间距倍数
     */
    public void setLineSpacing(int lineSpacing, float lineSpaceMulti) {
        if (mLineSpacingExtra == lineSpacing && mLineSpacingMultiplier == lineSpaceMulti) {
            return;
        }
        this.mLineSpacingExtra = lineSpacing;
        this.mLineSpacingMultiplier = lineSpaceMulti;
        mMeasureValid = false;
    }

    /**
     * 设置行间距
     *
     * @param lineSpacing 行间距
     */
    public void setLineSpacing(int lineSpacing) {
        if (mLineSpacingExtra == lineSpacing) {
            return;
        }
        this.mLineSpacingExtra = lineSpacing;
        mMeasureValid = false;
    }

    /**
     * 设置画笔
     *
     * @param paint
     */
    public void setPaint(Paint paint) {
        if (mPaint == paint) {
            return;
        }
        this.mPaint.set(paint);
        mMeasureValid = false;
    }

    /**
     * 设置最大行数
     *
     * @param maxLines 最大行数
     */
    public void setMaxLines(int maxLines) {
        if (mMaxLines == maxLines) {
            return;
        }
        mMaxLines = maxLines;
        mMeasureValid = false;
    }

    /**
     * 获取最大行数
     *
     * @return
     */
    public int getMaxLines() {
        return mMaxLines;
    }

    /**
     * FIXME 设置最大高度
     *
     * @param maxHeight
     */
    public void setMaxHeight(int maxHeight) {
        if (mMaxHeightLimit == maxHeight) {
            return;
        }
        this.mMaxHeightLimit = maxHeight;
        mMeasureValid = false;
    }

    /**
     * FIXME  获取最大高度限制
     *
     * @return
     */
    public int getMaxHeight() {
        return mMaxHeightLimit;
    }

    /**
     * 获取绘制文字的行数
     *
     * @return
     */
    public int getDrawLines() {
        return mDrawLines;
    }

    /**
     * 设置排列方向
     *
     * @param align
     */
    public void setTextAlign(Paint.Align align) {
        if (mPaint.getTextAlign() == align) {
            return;
        }
        mPaint.setTextAlign(align);
        mMeasureValid = false;
    }

    /**
     * 获取字体大小
     *
     * @return
     */
    public float getTextSize() {
        return mPaint.getTextSize();
    }

    /**
     * 设置字体大小
     *
     * @param unit
     * @param size
     */
    public void setTextSize(int unit, float size) {
        Resources resources;
        View view = (View) getCallback();
        if (view == null) {
            return;
        }
        Context context = view.getContext();
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }

        float rawTextSize = TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
        if (rawTextSize != mPaint.getTextSize()) {
            mPaint.setTextSize(rawTextSize);
            mMeasureValid = false;
        }
    }

    /**
     * 设置字体大小
     *
     * @param sizeSp 单位 sp
     */
    public void setTextSizeSp(float sizeSp) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
    }

    /**
     * 设置字体大小
     * 注：除非特殊需求使用本方法，其他一般都用 {@link #setTextSizeSp(float)}
     *
     * @param sizeDp 单位 dp
     */
    public void setTextSizeDp(float sizeDp) {
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        mTextColors = ColorStateList.valueOf(color);
        updateTextColors();
    }

    /**
     * 设置字体颜色。
     * 注：使用该方法设置字体颜色，需要手动调用字体更新 {@link #updateTextColors()}
     *
     * @param colors 字体颜色状态表
     */
    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }
        mTextColors = colors;
        updateTextColors();
    }

    /**
     * 获取字体样式（加粗、斜体等）
     *
     * @return
     */
    public int getTypefaceStyle() {
        Typeface typeface = mPaint.getTypeface();
        return typeface != null ? typeface.getStyle() : Typeface.NORMAL;
    }

    /**
     * 设置字体样式（加粗、斜体等）
     *
     * @param tf
     */
    public void setTypeface(Typeface tf) {
        if (mPaint.getTypeface() != tf) {
            mPaint.setTypeface(tf);
            mMeasureValid = false;
            invalidateSelf();
        }
    }

    /**
     * 获取绘制参数列表
     *
     * @return
     */
    public List<LineIndex> getLineIndexList() {
        return mMeasuredParam.lineIndexList;
    }

    /**
     * 设置绘制参数
     *
     * @param list
     */
    public void setLineIndexList(List<LineIndex> list) {
        mMeasuredParam.lineIndexList.clear();
        if (list != null && list.size() > 0) {
            mMeasuredParam.lineIndexList.addAll(list);
        }
    }

    /**
     * 设置绘制参数
     *
     * @param param
     */
    public void setMeasureSize(MeasuredParam param) {
        mMeasuredParam.set(param);
    }

    public void setTextDrawSize(int textDrawWidth, int textDrawHeight) {
        mMeasuredParam.textDrawWidth = textDrawWidth;
        mMeasuredParam.textDrawHeight = textDrawHeight;
    }

    /**
     * 绘制的最后一个字符的索引位置
     *
     * @return
     */
    public int getLastShowCharIndex() {
        return mLastShowCharIndex;
    }

    /**
     * 更新字体颜色
     */
    public void updateTextColors() {
        boolean inval = false;
        View view = (View) getCallback();
        if (view == null) {
            return;
        }
        int color = mTextColors.getColorForState(view.getDrawableState(), 0);
        if (color != mCurTextColor) {
            mCurTextColor = color;
            inval = true;
        }
        mPaint.setColor(mCurTextColor);
        if (inval) {
            invalidateSelf();
        }
    }

    /**
     * 计算文字宽度
     *
     * @return
     */
    public int measureTextWidth() {
        return TextUtils.isEmpty(mText) ? 0 : (int) mPaint.measureText(mText);
    }

    /**
     * 计算字体高度
     *
     * @return
     */
    public int measureWordHeight() {
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        return fontMetrics.bottom - fontMetrics.top;
    }

    /**
     * 是否在 文本边界范围内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean inTextBounds(int x, int y) {
        return mTextBounds.contains(x, y);
    }

    /**
     * 是否在 文本边界范围内
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public boolean inTextBounds(int left, int top, int right, int bottom) {
        return mTextBounds.contains(left, top, right, bottom);
    }

    /**
     * 是否在边界范围内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean inBounds(int x, int y) {
        return getBounds().contains(x, y);
    }

    /**
     * 是否在边界范围内
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public boolean inBounds(int left, int top, int right, int bottom) {
        return getBounds().contains(left, top, right, bottom);
    }

    /**
     * 获取文本边界范围
     *
     * @return
     */
    public Rect getTextBounds() {
        return mTextBounds;
    }

    /**
     * 获取 text
     *
     * @return
     */
    public String getText() {
        return mText;
    }


    //************************  private   ****************************

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    @Override
    public boolean setState(int[] stateSet) {
        boolean bool = super.setState(stateSet);
        if (mBG != null) {
            mBG.setState(stateSet);
        }
        updateTextColors();
        return bool;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mMeasureValid = false;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTextBounds = new Rect();
        mMeasuredParam = new MeasuredParam();
    }

    public void measure(Rect bounds) {
        View view = (View) getCallback();
        if (view == null || TextUtils.isEmpty(mText)) {
            return;
        }
        if (!mMeasureValid) {
            measureText();
        }
    }

    private void measureWord() {
        mFontMetrics = mPaint.getFontMetricsInt();
        mWordHeight = mFontMetrics.bottom - mFontMetrics.top;
        mWordBaseLineY = Math.abs(mFontMetrics.top);
    }

    public int getWordHeight() {
        return mWordHeight;
    }

    public void resetMeasureDimen() {
        mDrawLines = 0;
        mMeasuredParam.clear();
    }

    public void measureText() {
        // reset
        resetMeasureDimen();

        if (TextUtils.isEmpty(mText)) {
            mDrawHeight = 0;
            mDrawWidth = 0;
            return;
        }

        // word
        measureWord();
        onMeasure();

        mMeasureValid = true;
    }

    private void onMeasure() {
        int lineSpace = (int) (mLineSpacingExtra * mLineSpacingMultiplier);

        int length = Math.min(mText.length(), sCharWidths.length);
        int boundWidth = mTextBounds.width();
        int textDrawWidth = mTextBounds.width();
        int textDrawHeight;

        mFirstBaseLine = mTextBounds.top + mWordBaseLineY; // 首行baselineY

        int minCharWidth = (int) mPaint.measureText(CHAR_EN.toCharArray(), 0, 1);

        int startIndex = 0;
        int lineCharCount = 0;
        int baseLineY = mFirstBaseLine;

        // 获取每个字符的绘制长度
        int totalNum = mPaint.getTextWidths(mText, 0, length, sCharWidths);

        //FixBug：低版本手机上最后一个特殊字符测量值为0
        if (sCharWidths[length - 1] == 0f) {
            sCharWidths[length - 1] = sCharWidths[0];
        }

        int width = 0;
        int currentLine = 1;  // 0, 1, 2, 3...
        boolean hasNextLine = false;
        boolean showMore = false;
        List<LineIndex> mIndexList = new ArrayList<>();

        int startX = getInitialStartX();

        for (int i = 0; i < length && currentLine <= mMaxLines; i++) {
            width += sCharWidths[i];

            // 超过 boundWidth 则开始下一行
            if (width > boundWidth) {

                if (mShowMoreForce && (currentLine == mMaxLines && i < length - 1)) {
                    /**
                     * 显示 “...”
                     * 从该行最后一个字符开始减，直到减去的长度 > “...” 的长度
                     */
                    width -= sCharWidths[i];
                    showMore = true;

                    if (mApostropheMoreWidth == 0) {
                        mApostropheMoreWidth = mPaint.measureText(STR_MORE); // "..."的宽度
                    }
                    if (mPaint.getTextAlign() == Paint.Align.RIGHT) {
                        startX = (int) (mTextBounds.right - mApostropheMoreWidth);
                    }

                    int subCharWidth = 0;
                    int subCount = 0;


                    // 最后一个是从 i -1 开始，因为 i 应为下一行的第1个
                    for (int j = i - 1; j > Math.max(0, i - 4); j--) {
                        width -= sCharWidths[j];
                        subCharWidth += sCharWidths[j];
                        subCount++;
                        if (subCharWidth >= mApostropheMoreWidth) {
                            mIndexList.add(new LineIndex(startIndex, lineCharCount - subCount, startX, baseLineY));
                            mLastLineTextWidth = width;
                            mLastShowCharIndex = i - 1 - subCount;
                            break;
                        }
                    }
                    lineCharCount = 0;
                    currentLine++;
                    startX = getInitialStartX();
                    boundWidth = mTextBounds.width();
                    width = (int) sCharWidths[i];
                    textDrawWidth = boundWidth;

                } else {
                    /**
                     *  不显示 “...”，第 i 个 超过本行 (width > boundWidth)
                     */
                    mIndexList.add(new LineIndex(startIndex, lineCharCount, startX, baseLineY));
                    mLastShowCharIndex = i - 1;

                    currentLine++;
                    startX = getInitialStartX();
                    boundWidth = mTextBounds.width();

                    // 下一行第1个 为 i
                    startIndex = i;
                    hasNextLine = startIndex < length && currentLine <= mMaxLines;
                    if (hasNextLine) {
                        baseLineY += mWordHeight + lineSpace;
                    }
                    lineCharCount = hasNextLine ? 1 : 0;

                    width = (int) sCharWidths[i];
                    textDrawWidth = boundWidth;
                }

            } else if (width == boundWidth) {
                lineCharCount++;

                if (mShowMoreForce && (currentLine == mMaxLines && i < length - 1)) {
                    /**
                     *  显示"..."，正好一行结束，第i个字符正好显示完本行
                     */
                    showMore = true;

                    if (mApostropheMoreWidth == 0) {
                        mApostropheMoreWidth = mPaint.measureText(STR_MORE); // "..."的宽度
                    }
                    if (mPaint.getTextAlign() == Paint.Align.RIGHT) {
                        startX = (int) (mTextBounds.right - mApostropheMoreWidth);
                    }

                    int subCharWidth = 0;
                    int subCount = 0;

                    // i 为本行的最后一个
                    for (int j = i; j > Math.max(0, i - 3); j--) {
                        width -= sCharWidths[j];
                        subCharWidth += sCharWidths[j];
                        subCount++;
                        if (subCharWidth >= mApostropheMoreWidth) {
                            mIndexList.add(new LineIndex(startIndex, lineCharCount - subCount, startX, baseLineY));
                            mLastLineTextWidth = width;
                            mLastShowCharIndex = i - subCount;
                            break;
                        }
                    }
                    currentLine++;
                    startX = getInitialStartX();
                    boundWidth = mTextBounds.width();

                } else {
                    /**
                     *  不显示 “...”，正好一行结束，第 i 个为本行最后一个 (width > boundWidth)
                     */
                    mIndexList.add(new LineIndex(startIndex, lineCharCount, startX, baseLineY));

                    currentLine++;
                    startX = getInitialStartX();
                    boundWidth = mTextBounds.width();

                    // 下一行第1个为 i+1
                    startIndex = i + 1;
                    hasNextLine = startIndex < length && currentLine <= mMaxLines;
                    if (hasNextLine) {
                        baseLineY += mWordHeight + lineSpace;
                    }
                }
                textDrawWidth = boundWidth;
                lineCharCount = 0;
                width = 0;


            } else {
                // 未超过该行，就继续累加下一个字符
                lineCharCount++;
            }
        }

        // 最后一行显示长度不足一行的
        if (lineCharCount > 0) {
            mIndexList.add(new LineIndex(startIndex, lineCharCount, startX, baseLineY));
            mLastShowCharIndex += lineCharCount;
            if (currentLine == 1) {
                textDrawWidth = width;
            }
        }

        textDrawHeight = baseLineY + mFontMetrics.bottom - mTextBounds.top;
        mDrawWidth = textDrawWidth;
        mDrawHeight = textDrawHeight + getPaddingBottom();

        int moreStartX = getMoreStartX();

        saveMeasuredSize(mIndexList, textDrawWidth, textDrawHeight, moreStartX, showMore);
    }


    private int getInitialStartX() {
        int startX = 0;
        Paint.Align align = mPaint.getTextAlign();
        switch (align) {
            case RIGHT:
                startX = mTextBounds.right;
                break;
            case CENTER:
                startX = mTextBounds.centerX();
                break;
            case LEFT:
            default:
                startX = mTextBounds.left;
                break;
        }
        return startX;
    }

    private int getMoreStartX(){
        int moreStartX;
        Paint.Align align = mPaint.getTextAlign();
        switch (align){
            case RIGHT:
                moreStartX = mTextBounds.right;
                break;

            case LEFT:
            case CENTER:
            default:
                moreStartX = mTextBounds.left + mLastLineTextWidth;
                break;
        }
        return moreStartX;
    }

    private int getMoreStartX(List<LineIndex> mIndexList) {
        int moreStartX;
        Paint.Align align = mPaint.getTextAlign();
        switch (align) {
            case RIGHT:
                moreStartX = mTextBounds.right;
                break;
            case CENTER:
                moreStartX = mTextBounds.left + mLastLineTextWidth;
                mPaint.setTextAlign(Paint.Align.LEFT);
                for (LineIndex lineIndex : mIndexList) {
                    lineIndex.startX = getInitialStartX();
                }
                break;
            case LEFT:
            default:
                moreStartX = mTextBounds.left + mLastLineTextWidth;
                break;
        }
        return moreStartX;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mBG != null) {
            mBG.draw(canvas);
        }
        if (TextUtils.isEmpty(mText)) {
            return;
        }

        char[] chars = mText.toCharArray();
        int lineCount = mMeasuredParam.lineIndexList.size();
        for (int i = 0; i < lineCount; i++) {
            LineIndex line = mMeasuredParam.lineIndexList.get(i);

            try {
                // draw text one line
                canvas.drawText(chars, line.textIndex, line.textCount, line.startX, line.baseLineY, mPaint);

            } catch (Throwable e) {
//                Log.e("LineIndex", line.toString() + "，text = " + (TextUtils.isEmpty(mText) ? "" : mText));
                e.printStackTrace();
            }

            // draw "..."
            if (mMeasuredParam.showMore && i == lineCount - 1) {
                canvas.drawText(STR_MORE, mMeasuredParam.moreStartX, line.baseLineY, mPaint);
            }
        }
    }

    @Override
    public void invalidateSelf() {
        super.invalidateSelf();
        if (mBG != null) {
            mBG.invalidateSelf();
        }
    }

    public MeasuredParam getMeasuredSize() {
        return mMeasuredParam;
    }

    /**
     * 保存测量数据
     *
     * @param lineIndices
     * @param drawWidth
     * @param drawHeight
     * @param moreStartX
     * @param showMore
     */
    public void saveMeasuredSize(List<LineIndex> lineIndices, int drawWidth, int drawHeight, int moreStartX, boolean showMore) {
        mMeasuredParam.clear();
        if (lineIndices != null && lineIndices.size() > 0) {
            mMeasuredParam.lineIndexList.addAll(lineIndices);
        }
        mMeasuredParam.textDrawWidth = drawWidth;
        mMeasuredParam.textDrawHeight = drawHeight;
        mMeasuredParam.moreStartX = moreStartX;
        mMeasuredParam.showMore = showMore;
        showMore = false;
    }

    public static class MeasuredParam {
        /**
         * 每一行的位置列表
         *
         * @see LineIndex
         */
        public List<LineIndex> lineIndexList;

        /**
         * 文字绘制宽度
         */
        public int textDrawWidth;
        /**
         * 文字绘制高度
         */
        public int textDrawHeight;

        /**
         * "..." 的起始 x坐标
         */
        public int moreStartX;

        /**
         * 是否显示 "..."
         */
        public boolean showMore;

        public MeasuredParam() {
            lineIndexList = new ArrayList<>();
            showMore = false;
        }

        public void clear() {
            lineIndexList.clear();
            showMore = false;
            moreStartX = 0;
            textDrawWidth = 0;
            textDrawHeight = 0;
        }

        public void set(MeasuredParam param) {
            if (param == null || param.lineIndexList == null || param.lineIndexList.size() == 0) {
                clear();
            } else {
                lineIndexList.clear();
                lineIndexList.addAll(param.lineIndexList);
                textDrawWidth = param.textDrawWidth;
                textDrawHeight = param.textDrawHeight;
                moreStartX = param.moreStartX;
                showMore = param.showMore;
            }
        }
    }

    /**
     * 此类保存了该行需要绘制文字的索引、字数、baseline
     */
    public static class LineIndex {

        /**
         * 该行要绘制文字的索引
         */
        int textIndex;
        /**
         * 该行要绘制的文字字数
         */
        int textCount;
        /**
         * 该行绘制文字的 startX
         */
        int startX;
        /**
         * 该行绘制文字的 baseline
         */
        int baseLineY;

        public LineIndex(int textIndex, int textCount, int startX, int baseLineY) {
            this.textIndex = textIndex;
            this.textCount = textCount;
            this.startX = startX;
            this.baseLineY = baseLineY;
        }

        @Override
        public String toString() {
            return "LineIndex{" +
                    "textIndex=" + textIndex +
                    ", textCount=" + textCount +
                    ", startX=" + startX +
                    ", baseLineY=" + baseLineY +
                    '}';
        }
    }
}
