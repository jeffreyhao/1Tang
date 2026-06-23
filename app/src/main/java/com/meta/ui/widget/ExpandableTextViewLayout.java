package com.meta.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.benefit.novelverse.R;


/**
 * 可以收起或者展开的layout
 * <p>
 * 顶部固定为TextView
 * 底部为一个FlowLayout
 * TODO:底部还有bug,暂时不设置底部
 * </p>
 *
 * @author adison
 * @date 16/4/5
 * @time 下午3:35
 */
public class ExpandableTextViewLayout extends LinearLayout implements View.OnClickListener {
    /**
     * 默认行数
     */
    private static final int MAX_COLLAPSED_LINES = 8;

    /**
     * 默认动画时长
     */
    private static final int DEFAULT_ANIM_DURATION = 100;

    /**
     * 动画开始时默认透明度
     */
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;

    /**
     * 收起时，默认的展开图标ID（-1表示不使用图标，而显示...）
     */
    private static final int DEFAULT_COLLAPSED_ICON = -1;
    /**
     * 收起时，默认的展开图标宽度/高度（-1表示不处理）
     */
    private static final int DEFAULT_COLLAPSED_ICON_DIMEN = -1;

    protected TextView mTv;
    /**
     * FlowLayout
     */
    protected ExpandableFlowLayout mMoreLayout;
    /**
     * 重新layout标志
     */
    private boolean mRelayout;
    /**
     * 默认收起
     */
    private boolean mCollapsed = true;
    /**
     * 收起时ViewGroup的高度
     */
    private int mCollapsedHeight;
    /**
     * 文字完全展开时高度
     */
    private int mTextHeightWithMaxLines;
    /**
     * 收起时最大行数
     */
    private int mMaxCollapsedLines;
    /**
     * ViewGroup的底部和TextView的底部的间隔
     */
    private int mMarginBetweenTxtAndBottom;
    /**
     * 动画时长
     */
    private int mAnimationDuration;
    /**
     * 动画开始时透明度
     */
    private float mAnimAlphaStart;

    private boolean mAnimating;

    private int mMoreLayoutHeight;

    /**
     * 收起时显示的展开图标ID（-1表示不使用图标，而显示...）
     */
    private int mCollapsedIcon;
    /**
     * 收起时显示的展开图标宽度（单位：dp）
     */
    private int mCollapsedIconWidth;
    /**
     * 收起时显示的展开图标高度（单位：dp）
     */
    private int mCollapsedIconHeight;
    /**
     * 收起时显示的富文本
     */
    private SpannableStringBuilder mCollapsedSpannable;
    /**
     * 原始简介文字
     */
    private String mTvText;

    /**
     * 关闭展开监听
     */
    private OnExpandStateChangeListener mListener;


    public ExpandableTextViewLayout(Context context) {
        this(context, null);
    }

    public ExpandableTextViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView 仅仅支持垂直布局.");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onClick(View view) {
        mCollapsed = !mCollapsed;
        boolean isLowerKitKat = false;
        if (isLowerKitKat) {
            if (mListener != null) {
                mListener.onExpandStateStartChanged(mTv, !mCollapsed);
            }
            int newHeight;
            if (mCollapsed) {
                newHeight = mCollapsedHeight;
            } else {
                newHeight = getHeight() +
                        mTextHeightWithMaxLines - mTv.getHeight() + mMoreLayoutHeight;
            }
            //重新测量一下，为了恢复Ellipsize状态
            mRelayout = true;
            mTv.setMaxHeight(newHeight - mMoreLayoutHeight - mMarginBetweenTxtAndBottom);
            getLayoutParams().height = newHeight;
            requestLayout();
            // 通知
            if (mListener != null) {
                mListener.onExpandStateChanged(mTv, !mCollapsed);
            }
        } else {
            // 标示动画进行中
            mAnimating = true;

            Animation animation;
            if (mCollapsed) {
                animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
            } else {
                animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() +
                        mTextHeightWithMaxLines - mTv.getHeight() + mMoreLayoutHeight);
            }

            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // 通知
                    if (mListener != null) {
                        mListener.onExpandStateStartChanged(mTv, !mCollapsed);
                    }
                    applyAlphaAnimationForTextView(mAnimAlphaStart);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //清除动画，避免applyTransformation方法重复调用
                    clearAnimation();
                    mAnimating = false;
                    //重新测量一下，为了恢复Ellipsize状态
                    mRelayout = true;
                    // 通知
                    if (mListener != null) {
                        mListener.onExpandStateChanged(mTv, !mCollapsed);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            clearAnimation();
            startAnimation(animation);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //当动画进行中时，拦截子view所有手势操作
        return mAnimating;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //如果布局没有变化。不需要重新测量
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // 若是展开状态则重新设置显示文字
        if (!mCollapsed) {
            mTv.setText(mTvText);
        }

        //理想状态:文字行数等于或小于设置的最大收起行数
        mTv.setMaxLines(Integer.MAX_VALUE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mTv.getLineCount() <= mMaxCollapsedLines) {
            if (mMoreLayout != null) {
                mMoreLayout.setVisibility(View.VISIBLE);
            }
            if (mListener != null) {
                mListener.onNOExpandState();
            }
            mTv.setClickable(false);
            return;
        }
        mTv.setClickable(true);
        //保存文字真实高度
        mTextHeightWithMaxLines = getRealTextViewHeight(mTv);

        //非理想状态，需要展开
        if (mCollapsed && !mAnimating) {
            mTv.setMaxLines(mMaxCollapsedLines);
            // 展开图标（显示...或使用图标）
            if (DEFAULT_COLLAPSED_ICON == mCollapsedIcon) {
                mTv.setEllipsize(TextUtils.TruncateAt.END);
            } else {
                if (mCollapsedSpannable == null) {
                    // 设置收起后显示的富文本
                    Drawable drawableImageSpan = getResources().getDrawable(mCollapsedIcon);
                    // 收起的最后一行文字的baseline Y坐标
                    int collapsedBaseLineY = mTv.getLineBounds(mMaxCollapsedLines - 1, null);
                    // 收起后显示的文本，并将最后一个字替换为图标显示
                    int indexImageSpan = mTv.getLayout().getLineEnd(mMaxCollapsedLines - 1);
                    String strCollpsed = mTv.getText().subSequence(0, indexImageSpan).toString();
                    // 若收起的文字最后都是空格，则需要去除，否则可能影响绘制图标（图标替换最后一个空格，可能导致超出显示区域)
                    strCollpsed = strCollpsed.trim();
                    // 设置收起的文字，以便测量偏移量更精准
                    mTv.setText(strCollpsed);
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    // 展开图标的X坐标
                    int iconOffset = getCollapsedIconOffset();
                    float iconWidth = mTv.getPaint().measureText("阅");  // 一个文字的宽度，也是展开图标的宽度
                    if (iconWidth < iconOffset) {
                        // 当偏移量大于一个文字的宽度时，添加一个字符，使图标替换该字符，避免原文字被替换
                        iconOffset -= iconWidth;
                        strCollpsed += "阅";
                    }else{
                        // 当偏移量小于一个文字的宽度时，要保证最后一个字符宽度+偏移量>=iconWidth, jdxu 20160818
                        int lenStrCollpsed = strCollpsed.length();
                        int lastStrIndex = lenStrCollpsed - 1;  // 最后一个有效字符的下标
                        float curIndexWidth;
                        for(; lastStrIndex>=0; --lastStrIndex){
                            curIndexWidth = mTv.getPaint().measureText(strCollpsed.substring(lastStrIndex, lastStrIndex+1));
                            iconOffset += curIndexWidth;
                            if(iconOffset >= iconWidth){
                                // 宽度足够显示展开图标
                                iconOffset -= iconWidth;
                                break;
                            }else{
                                // 当前字符+偏移量<展开图标宽度，需要继续测量前一个字符
                            }
                        }
                        // 截取有效字符
                        if(lastStrIndex < (lenStrCollpsed - 1)) {
                            strCollpsed = strCollpsed.substring(0, lastStrIndex + 1);
                        }
                    }
                    ImageSpanBaseLine imageSpan = new ImageSpanBaseLine(drawableImageSpan, collapsedBaseLineY, iconOffset);
                    mCollapsedSpannable = new SpannableStringBuilder(strCollpsed);
                    if (TextUtils.isEmpty(strCollpsed)) return;
                    mCollapsedSpannable.setSpan(imageSpan, strCollpsed.length() - 1, strCollpsed.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                mTv.setText(mCollapsedSpannable);
            }
        }

        //重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (mCollapsed) {
            //获取ViewGroup的底部和TextView的底部的间隔
            mTv.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight();
                    mMoreLayoutHeight = 0;
                    if (mMoreLayout != null) {
                        int w = MeasureSpec.makeMeasureSpec(0,
                                MeasureSpec.EXACTLY);
                        int h = MeasureSpec.makeMeasureSpec(0,
                                MeasureSpec.UNSPECIFIED);
                        mMoreLayout.measure(w, h);
                        mMoreLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mMoreLayoutHeight = mMoreLayout.getMeasuredHeight();
                    }

                }
            });
            //收起时ViewGroup的高度
            mCollapsedHeight = getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //布局文件被加载完成后回调
        findViews();
    }

    public void setOnExpandStateChangeListener(OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public void setText(CharSequence text) {
        mRelayout = true;
        mTvText = text.toString();
        mTv.setText(text);
        requestLayout();    // 收起状态时，未登录进入详情页，登录成功后，会显示...（添加该行解决该问题）
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public CharSequence getText() {
        if (mTv == null) {
            return "";
        }
        return mTv.getText();
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
        if (typedArray != null) {
            mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
            mAnimationDuration = typedArray.getInt(R.styleable.ExpandTextView_animDuration, DEFAULT_ANIM_DURATION);
            mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
            mCollapsedIcon = typedArray.getResourceId(R.styleable.ExpandTextView_collapsedIcon, DEFAULT_COLLAPSED_ICON);
            mCollapsedIconWidth = typedArray.getDimensionPixelSize(R.styleable.ExpandTextView_collapsedIconWidth, DEFAULT_COLLAPSED_ICON_DIMEN);
            mCollapsedIconHeight = typedArray.getDimensionPixelSize(R.styleable.ExpandTextView_collapsedIconHeight, DEFAULT_COLLAPSED_ICON_DIMEN);
            typedArray.recycle();
        }
        //强制垂直
        setOrientation(LinearLayout.VERTICAL);
        setVisibility(GONE);
    }

    private void findViews() {
        mTv = (TextView) findViewById(R.id.expandable_text);
        mTv.setOnClickListener(this);
        mMoreLayout = (ExpandableFlowLayout) findViewById(R.id.flow_layout);
        if (mMoreLayout != null) {
            mMoreLayout.setVisibility(View.GONE);
        }
    }


    private void applyAlphaAnimationForTextView(float alpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mTv.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            // 立即执行
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            mTv.startAnimation(alphaAnimation);
        }
    }

    private void applyAlphaAnimationForMoreLayout(int textMaxHeight, float alpha) {
        if (mMoreLayout == null) return;
        if (mCollapsed && textMaxHeight <= mTextHeightWithMaxLines) {
            int initialHeight = mMoreLayout.getMeasuredHeight();
            if (alpha == 1) {
                mMoreLayout.setVisibility(View.GONE);
            } else {
                mMoreLayout.getLayoutParams().height = initialHeight - (int) (initialHeight * alpha);
                mMoreLayout.requestLayout();
            }
        } else if (!mCollapsed && textMaxHeight >= mTextHeightWithMaxLines) {
            mMoreLayout.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final int targetHeight = mMoreLayout.getMeasuredHeight();
            mMoreLayout.getLayoutParams().height = 0;
            mMoreLayout.setVisibility(VISIBLE);
            mMoreLayout.getLayoutParams().height = (alpha == 1) ? RelativeLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * alpha);
            mMoreLayout.requestLayout();
        }
    }


    /**
     * 获取TextView 真实高度
     *
     * @param textView
     * @return
     */
    private static int getRealTextViewHeight(TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTv.setMaxHeight(newHeight - mMoreLayoutHeight - mMarginBetweenTxtAndBottom);
            float alpha = mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart);
            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimationForTextView(alpha);
            }
            if (mMoreLayout != null) {
                applyAlphaAnimationForMoreLayout(newHeight, alpha);
            }
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }


    }


    public interface OnExpandStateChangeListener {
        /**
         * 当展开／关闭动画结束时调用
         *
         * @param textView
         * @param isExpanded 展开为true
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);

        /**
         * 当展开／关闭动画开始时调用
         *
         * @param textView
         * @param isExpanded 展开为true
         */
        void onExpandStateStartChanged(TextView textView, boolean isExpanded);

        /**
         * 当没有展开状态时调用
         */
        void onNOExpandState();
    }

    /**
     * 调整ImageSpan的位置与文字平齐（以baseline为基准，不支持bottom）
     */
    private class ImageSpanBaseLine extends ImageSpan {
        private int mBaseLineY = 0;
        private int mIconOffset = 0;

        /**
         * @param d
         * @param lineSpace  图标绘制时的 baseline Y坐标
         * @param iconOffset 图标绘制时，x坐标向右的便宜量（保证图标始终靠右显示）
         */
        public ImageSpanBaseLine(Drawable d, int lineSpace, int iconOffset) {
            super(d);
            mBaseLineY = lineSpace;
            mIconOffset = iconOffset;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            // 设置图片边界与一个文字边界相同
            Rect rectImageSpan = new Rect();
            paint.getTextBounds("阅", 0, 1, rectImageSpan);
            // 若设置了展开图标的宽高且小于字体宽高：则根据指定的宽高居中显示
            int txtWidth = rectImageSpan.right - rectImageSpan.left;
            if (mCollapsedIconWidth != DEFAULT_COLLAPSED_ICON_DIMEN && mCollapsedIconWidth < txtWidth) {
                rectImageSpan.left += (txtWidth - mCollapsedIconWidth) / 2;
                rectImageSpan.right -= (txtWidth - mCollapsedIconWidth) / 2;
            }
            int txtHeight = rectImageSpan.bottom - rectImageSpan.top;
            if (mCollapsedIconHeight != DEFAULT_COLLAPSED_ICON_DIMEN && mCollapsedIconHeight < txtHeight) {
                rectImageSpan.top += (txtHeight - mCollapsedIconHeight) / 2;
                rectImageSpan.bottom -= (txtHeight - mCollapsedIconHeight) / 2;
            }
            b.setBounds(rectImageSpan);
            int transY = mBaseLineY;

            canvas.save();
            canvas.translate(x + mIconOffset, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 获取展开图标与最右边的文字的偏移量（展开图标会与最右边的文字对齐）
     *
     * @return
     */
    private int getCollapsedIconOffset() {
        int maxLineWidth = 0;
        int curLineWidth = 0;
        try {
            int txtMaxWidth = mTv.getMeasuredWidth();
            for (int i = 0; i < mMaxCollapsedLines; ++i) {
                curLineWidth = (int) mTv.getLayout().getLineWidth(i);
                if (txtMaxWidth > 0 && curLineWidth > txtMaxWidth) {
                    // 每行文字的宽度不能大于textview的宽度，修复bug：64345
                    curLineWidth = txtMaxWidth;
                }
                if (curLineWidth > maxLineWidth) {
                    maxLineWidth = curLineWidth;
                }
            }
        } catch (Throwable e) {
            if(e!=null){
                e.printStackTrace();
            }
        }
        return maxLineWidth - curLineWidth;
    }
}
