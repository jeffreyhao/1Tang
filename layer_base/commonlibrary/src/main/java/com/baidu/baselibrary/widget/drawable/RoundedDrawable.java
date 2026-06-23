/*
* Copyright (C) 2015 Vincent Mi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.baidu.baselibrary.widget.drawable;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;

import com.baidu.baselibrary.util.sys.LogUtil;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


/**
 * 圆角图片 Drawable
 */

@SuppressWarnings("UnusedDeclaration")
public class RoundedDrawable extends Drawable {

    public static final String TAG = "RoundedDrawable";
    public static final int DEFAULT_BORDER_COLOR = 0xffe7e7e7;
    private Bitmap mBitmap;
    private final RectF mBounds = new RectF();
    private final RectF mDrawableRect = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final RectF mBorderRect = new RectF();
    private final RectF mSquareCornersRect = new RectF();

    private Paint mBitmapPaint;
    private Paint mBorderPaint;
    private Paint mTouchPaint;

    private Shader.TileMode mTileModeX = Shader.TileMode.CLAMP;
    private Shader.TileMode mTileModeY = Shader.TileMode.CLAMP;

    private int mBitmapWidth;
    private int mBitmapHeight;

    private boolean mRebuildShader = true;
    private boolean mOval = false;

    private final Matrix mShaderMatrix = new Matrix();
    private ScaleType mScaleType = ScaleType.FIT_XY;
    private ColorStateList mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
    private ColorStateList mTouchColors = ColorStateList.valueOf(Color.TRANSPARENT);

    private float mBorderWidth = 0;

    // [ topLeft, topRight, bottomLeft, bottomRight ]
    private float mCornerRadius = 0f;
    private final boolean[] mCornersRounded = new boolean[]{true, true, true, true};

    /**
     * 是否適配新ui系統
     */
    private boolean mFixNewUiSystem;

    public RoundedDrawable() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mTouchPaint = new Paint();
        mTouchPaint.setAntiAlias(true);
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mBitmapWidth = bitmap == null ? 0 : bitmap.getWidth();
        mBitmapHeight = bitmap == null ? 0 : bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);
        updateShaderMatrix();
        mRebuildShader = true;
    }

    /**
     * @return
     */
    public float getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * @param width
     * @return
     */
    public RoundedDrawable setBorderWidth(float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        return this;
    }

    /**
     * @return
     */
    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    /**
     * @param color
     * @return
     */
    public RoundedDrawable setBorderColor(@ColorInt int color) {
        return setBorderColor(ColorStateList.valueOf(color));
    }

    /**
     * @return
     */
    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public ColorStateList getTouchColors() {
        return mTouchColors;
    }

    /**
     * @param colors
     * @return
     */
    public RoundedDrawable setTouchColor(ColorStateList colors) {
        mTouchColors = (colors == null) ? mTouchColors : colors;
        mTouchPaint.setColor(mTouchColors.getColorForState(getState(), Color.TRANSPARENT));
        return this;
    }

    /**
     * @param colors
     * @return
     */
    public RoundedDrawable setBorderColor(ColorStateList colors) {
        mBorderColor = colors != null ? colors : ColorStateList.valueOf(0);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        return this;
    }

    /**
     * @return
     */
    public boolean isOval() {
        return mOval;
    }

    /**
     * @param oval
     * @return
     */
    public RoundedDrawable setOval(boolean oval) {
        mOval = oval;
        return this;
    }

    /**
     * @return
     */
    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * @param scaleType
     * @return
     */
    public RoundedDrawable setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_CENTER;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            updateShaderMatrix();
        }
        return this;
    }

    /**
     * @return the corner radius.
     */
    public float getCornerRadius() {
        return mCornerRadius;
    }

    /**
     * @param corner the specific corner to get radius of.
     * @return the corner radius of the specified corner.
     */
    public float getCornerRadius(@Corner int corner) {
        return mCornersRounded[corner] ? mCornerRadius : 0f;
    }


    /**
     * @return
     */
    public Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    /**
     * @param tileModeX
     * @return
     */
    public RoundedDrawable setTileModeX(Shader.TileMode tileModeX) {
        if (mTileModeX != tileModeX) {
            mTileModeX = tileModeX;
            mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    /**
     * @return
     */
    public Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    /**
     * @param tileModeY
     * @return
     */
    public RoundedDrawable setTileModeY(Shader.TileMode tileModeY) {
        if (mTileModeY != tileModeY) {
            mTileModeY = tileModeY;
            mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    /**
     * @return
     */
    public Bitmap toBitmap() {
        return drawableToBitmap(this);
    }

    /**
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Throwable e) {
            LogUtil.e(e);
            Log.w(TAG, "Failed to create bitmap from drawable!");
            bitmap = null;
        }

        return bitmap;
    }

    /**
     * @return
     */
    public Bitmap getSourceBitmap() {
        return mBitmap;
    }

    @Override
    public boolean isStateful() {
        return mBorderColor.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        int newColor = mBorderColor.getColorForState(state, 0);
        if (mBorderPaint.getColor() != newColor) {
            mBorderPaint.setColor(newColor);
            return true;
        } else {
            return super.onStateChange(state);
        }
    }

    private void updateShaderMatrix() {
        float scale;
        float dx;
        float dy;

        switch (mScaleType) {
            case CENTER:
                mBorderRect.set(mBounds);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);

                mShaderMatrix.reset();
                mShaderMatrix.setTranslate((int) ((mBorderRect.width() - mBitmapWidth) * 0.5f + 0.5f),
                        (int) ((mBorderRect.height() - mBitmapHeight) * 0.5f + 0.5f));
                break;

            case CENTER_CROP:
                mBorderRect.set(mBounds);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);

                mShaderMatrix.reset();

                dx = 0;
                dy = 0;

                if (mBitmapWidth * mBorderRect.height() > mBorderRect.width() * mBitmapHeight) {
                    scale = mBorderRect.height() / (float) mBitmapHeight;
                    dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = mBorderRect.width() / (float) mBitmapWidth;
                    dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth / 2,
                        (int) (dy + 0.5f) + mBorderWidth / 2);
                break;

            case CENTER_INSIDE:
                mShaderMatrix.reset();

                if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f;
                } else {
                    scale = Math.min(mBounds.width() / (float) mBitmapWidth,
                            mBounds.height() / (float) mBitmapHeight);
                }

                dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);

                mBorderRect.set(mBitmapRect);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            default:
            case FIT_CENTER:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_END:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_START:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_XY:
                mBorderRect.set(mBounds);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.reset();
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;
        }

        mDrawableRect.set(mBorderRect);
    }


    public void fixNewUiSystem(boolean fix) {
        mFixNewUiSystem = fix;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        if(!mFixNewUiSystem) {
            mBounds.set(bounds);
            updateShaderMatrix();
            // bounds change后如果不重绘shader 会导致图片边缘拉伸
            mRebuildShader = true;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mRebuildShader) {
            if (mBitmap != null) {
                BitmapShader bitmapShader = new BitmapShader(mBitmap, mTileModeX, mTileModeY);
                if (mTileModeX == Shader.TileMode.CLAMP && mTileModeY == Shader.TileMode.CLAMP) {
                    bitmapShader.setLocalMatrix(mShaderMatrix);
                }
                mBitmapPaint.setShader(bitmapShader);
            }
            mRebuildShader = false;
        }

        int color = mTouchColors.getColorForState(getState(), 0);

        if (mOval) {  // oval
            if (mBorderWidth > 0) {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
                canvas.drawOval(mBorderRect, mBorderPaint);
            } else {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
            }

        } else {    // rect
            if (any(mCornersRounded)) {  // 圆角
                float radius = mCornerRadius;
                if (mBorderWidth > 0) { // 有边框
                    canvas.drawRoundRect(mDrawableRect, radius, radius, mBitmapPaint);
                    canvas.drawRoundRect(mBorderRect, radius, radius, mBorderPaint);
                    redrawBitmapForSquareCorners(canvas);
                    redrawBorderForSquareCorners(canvas);
                } else {
                    //fixbug:
                    if (mBitmapPaint.getShader() == null) {
                        if (mBitmap != null) {
                            BitmapShader bitmapShader = new BitmapShader(mBitmap, mTileModeX, mTileModeY);
                            if (mTileModeX == Shader.TileMode.CLAMP && mTileModeY == Shader.TileMode.CLAMP) {
                                bitmapShader.setLocalMatrix(mShaderMatrix);
                            }
                            mBitmapPaint.setShader(bitmapShader);
                        }
                    }
                    canvas.drawRoundRect(mDrawableRect, radius, radius, mBitmapPaint);
                    redrawBitmapForSquareCorners(canvas);
                }
                if (color != 0) {
                    canvas.drawRoundRect(mBorderRect, radius, radius, mTouchPaint);
                }

            } else {  // 直角
                canvas.drawRect(mDrawableRect, mBitmapPaint);
                if (mBorderWidth > 0) {
                    canvas.drawRect(mBorderRect, mBorderPaint);
                }
                if (color != 0) {
                    canvas.drawRect(mBorderRect, mTouchPaint);
                }
            }
        }
    }

    private void redrawBitmapForSquareCorners(Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        float left = mDrawableRect.left;
        float top = mDrawableRect.top;
        float right = left + mDrawableRect.width();
        float bottom = top + mDrawableRect.height();
        float radius = mCornerRadius;

        if (!mCornersRounded[Corner.TOP_LEFT]) {
            mSquareCornersRect.set(left, top, left + radius, top + radius);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[Corner.TOP_RIGHT]) {
            mSquareCornersRect.set(right - radius, top, right, radius);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT]) {
            mSquareCornersRect.set(right - radius, bottom - radius, right, bottom);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT]) {
            mSquareCornersRect.set(left, bottom - radius, left + radius, bottom);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }
    }

    private void redrawBorderForSquareCorners(Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        float left = mDrawableRect.left;
        float top = mDrawableRect.top;
        float right = left + mDrawableRect.width();
        float bottom = top + mDrawableRect.height();
        float radius = mCornerRadius;
        float offset = mBorderWidth / 2;

        if (!mCornersRounded[Corner.TOP_LEFT]) {
            canvas.drawLine(left - offset, top, left + radius, top, mBorderPaint);
            canvas.drawLine(left, top - offset, left, top + radius, mBorderPaint);
        }

        if (!mCornersRounded[Corner.TOP_RIGHT]) {
            canvas.drawLine(right - radius - offset, top, right, top, mBorderPaint);
            canvas.drawLine(right, top - offset, right, top + radius, mBorderPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT]) {
            canvas.drawLine(right - radius - offset, bottom, right + offset, bottom, mBorderPaint);
            canvas.drawLine(right, bottom - radius, right, bottom, mBorderPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT]) {
            canvas.drawLine(left - offset, bottom, left + radius, bottom, mBorderPaint);
            canvas.drawLine(left, bottom - radius, left, bottom, mBorderPaint);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getAlpha() {
        return mBitmapPaint.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mBitmapPaint.getColorFilter();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mBitmapPaint.setDither(dither);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mBitmapPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    /**
     * Sets all corners to the specified radius.
     *
     * @param radius the radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
        return this;
    }

    /**
     * Sets the corner radius of one specific corner.
     *
     * @param corner the corner.
     * @param radius the radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(@Corner int corner, float radius) {
        if (radius != 0 && mCornerRadius != 0 && mCornerRadius != radius) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        }

        if (radius == 0) {
            if (only(corner, mCornersRounded)) {
                mCornerRadius = 0;
            }
            mCornersRounded[corner] = false;
        } else {
            if (mCornerRadius == 0) {
                mCornerRadius = radius;
            }
            mCornersRounded[corner] = true;
        }

        return this;
    }

    /**
     * Sets the corner radii of all the corners.
     *
     * @param topLeft     top left corner radius.
     * @param topRight    top right corner radius
     * @param bottomRight bototm right corner radius.
     * @param bottomLeft  bottom left corner radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(float topLeft, float topRight, float bottomRight,
                                           float bottomLeft) {
        Set<Float> radiusSet = new HashSet<>(4);
        radiusSet.add(topLeft);
        radiusSet.add(topRight);
        radiusSet.add(bottomRight);
        radiusSet.add(bottomLeft);

        radiusSet.remove(0f);

        if (radiusSet.size() > 1) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        }

        if (!radiusSet.isEmpty()) {
            float radius = radiusSet.iterator().next();
            if (Float.isInfinite(radius) || Float.isNaN(radius) || radius < 0) {
                throw new IllegalArgumentException("Invalid radius value: " + radius);
            }
            mCornerRadius = radius;
        } else {
            mCornerRadius = 0f;
        }

        mCornersRounded[Corner.TOP_LEFT] = topLeft > 0;
        mCornersRounded[Corner.TOP_RIGHT] = topRight > 0;
        mCornersRounded[Corner.BOTTOM_RIGHT] = bottomRight > 0;
        mCornersRounded[Corner.BOTTOM_LEFT] = bottomLeft > 0;
        return this;
    }

    private static boolean only(int index, boolean[] booleans) {
        for (int i = 0, len = booleans.length; i < len; i++) {
            if (booleans[i] != (i == index)) {
                return false;
            }
        }
        return true;
    }

    private static boolean any(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    private static boolean all(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean setState(int[] stateSet) {
        boolean bool = super.setState(stateSet);
        int color = mTouchColors.getColorForState(stateSet, 0);
        if (color != mTouchPaint.getColor()) {
            mTouchPaint.setColor(color);
        }
        return bool;
    }

}
