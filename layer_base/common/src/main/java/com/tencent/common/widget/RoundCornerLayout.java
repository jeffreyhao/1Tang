package com.tencent.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.fold.common.R;
/**
* 通用圆角控件
* @author adison
* @date 2017/6/25
* @time 下午8:29
*/
public class RoundCornerLayout extends FrameLayout {
    private final static float CORNER_RADIUS_DEFAULT = 10.0f;
    private boolean mTopLeftEnabled = true;
    private boolean mTopRightEnabled = true;
    private boolean mBottomLeftEnabled = true;
    private boolean mBottomRightEnabled = true;
    private float topLeftRadius = CORNER_RADIUS_DEFAULT;
    private float topRightRadius = CORNER_RADIUS_DEFAULT;
    private float bottomLeftRadius = CORNER_RADIUS_DEFAULT;
    private float bottomRightRadius = CORNER_RADIUS_DEFAULT;

    private Paint roundPaint;
    private Paint imagePaint;

    private boolean enable=true;


    private float mCornerRadius = CORNER_RADIUS_DEFAULT;

    private void setupAttributes(AttributeSet attrs) {
        final float radius = getPixelValue(CORNER_RADIUS_DEFAULT);
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerLayout);

        mCornerRadius = a.getDimension(R.styleable.RoundCornerLayout_cornerRadius, radius);

        if (a.hasValue(R.styleable.RoundCornerLayout_topEnabled)) {
            mTopLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_topEnabled, true);
            mTopRightEnabled = mTopLeftEnabled;
        } else {
            mTopLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_topLeftEnabled, true);
            mTopRightEnabled = a.getBoolean(R.styleable.RoundCornerLayout_topRightEnabled, true);
        }

        if (a.hasValue(R.styleable.RoundCornerLayout_bottomEnabled)) {
            mBottomLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_bottomEnabled, true);
            mBottomRightEnabled = mBottomLeftEnabled;
        } else {
            mBottomLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_bottomLeftEnabled, true);
            mBottomRightEnabled = a.getBoolean(R.styleable.RoundCornerLayout_bottomRightEnabled, true);
        }

        topLeftRadius = a.getDimension(R.styleable.RoundCornerLayout_topLeftRadius, mCornerRadius);
        topRightRadius = a.getDimension(R.styleable.RoundCornerLayout_topRightRadius, mCornerRadius);
        bottomLeftRadius = a.getDimension(R.styleable.RoundCornerLayout_bottomLeftRadius, mCornerRadius);
        bottomRightRadius = a.getDimension(R.styleable.RoundCornerLayout_bottomRightRadius, mCornerRadius);

        a.recycle();
    }

    //
    private float getPixelValue(float dip) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
    }


    public RoundCornerLayout(Context context) {
        this(context, null);
    }

    public RoundCornerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode())
            return;
        setupAttributes(attrs);
        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        imagePaint = new Paint();
        imagePaint.setXfermode(null);
    }
    ////实现1
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        int width = getWidth();
//        int height = getHeight();
//        Path path = new Path();
//        path.moveTo(0, topLeftRadius);
//        path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2), -180, 90);
//        path.lineTo(width - topRightRadius, 0);
//        path.arcTo(new RectF(width - 2 * topRightRadius, 0, width, topRightRadius * 2), -90, 90);
//        path.lineTo(width, height - bottomRightRadius);
//        path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2 * bottomRightRadius, width, height), 0, 90);
//        path.lineTo(bottomLeftRadius, height);
//        path.arcTo(new RectF(0, height - 2 * bottomLeftRadius, bottomLeftRadius * 2, height), 90, 90);
//        path.close();
//        canvas.clipPath(path);
//        super.dispatchDraw(canvas);
//    }
    ////实现2
    //    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        drawTopLeft(canvas);//用PorterDuffXfermode
//        drawTopRight(canvas);//用PorterDuffXfermode
//        drawBottomLeft(canvas);//用PorterDuffXfermode
//        drawBottomRight(canvas);//用PorterDuffXfermode
//    }
    ////实现3
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas newCanvas = new Canvas(bitmap);
//        super.dispatchDraw(newCanvas);
//        drawTopLeft(newCanvas);
//        drawTopRight(newCanvas);
//        drawBottomLeft(newCanvas);
//        drawBottomRight(newCanvas);
//        canvas.drawBitmap(bitmap, 0, 0, imagePaint);
////        invalidate();
//    }

    //实现4
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(!enable){
            super.dispatchDraw(canvas);
        } else{
            canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), imagePaint, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            if (mTopLeftEnabled) {
                drawTopLeft(canvas);
            }
            if (mTopRightEnabled) {
                drawTopRight(canvas);
            }
            if (mBottomLeftEnabled) {
                drawBottomLeft(canvas);
            }
            if (mBottomRightEnabled) {
                drawBottomRight(canvas);
            }
            canvas.restore();
        }

    }

    private void drawTopLeft(Canvas canvas) {
        if (topLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(0, topLeftRadius);
            path.lineTo(0, 0);
            path.lineTo(topLeftRadius, 0);
            path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if (topRightRadius > 0) {
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - topRightRadius, 0);
            path.lineTo(width, 0);
            path.lineTo(width, topRightRadius);
            path.arcTo(new RectF(width - 2 * topRightRadius, 0, width,
                    topRightRadius * 2), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if (bottomLeftRadius > 0) {
            int height = getHeight();
            Path path = new Path();
            path.moveTo(0, height - bottomLeftRadius);
            path.lineTo(0, height);
            path.lineTo(bottomLeftRadius, height);
            path.arcTo(new RectF(0, height - 2 * bottomLeftRadius,
                    bottomLeftRadius * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomRight(Canvas canvas) {
        if (bottomRightRadius > 0) {
            int height = getHeight();
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - bottomRightRadius, height);
            path.lineTo(width, height);
            path.lineTo(width, height - bottomRightRadius);
            path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2
                    * bottomRightRadius, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    public void setRounedEnable(boolean enable){
        this.enable=enable;
    }

}
