package com.baidu.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.res.BaseResource;
import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;


/**
 * @author: lhc
 * @date: 2020-03-20 20:21
 * @description 自定义TitleBar
 */

public class TitleBar extends RelativeLayout {


    public interface OnTitleBarClickListener {

        void leftClick(View v);

        void rightClick(View v);
    }


    private Button leftBtn;
    private Button rightBtn;
    private ImageView leftIv;
    private ImageView rightIv;
    private TextView tvTitle;
    private TextView rightTextView;
    private View lineView;
    private OnTitleBarClickListener listener;


    private int leftTextColor, rightTextColor, titleTextColor;
    private String leftText, rightText, title;
    private float leftTextSize, rightTextSize, titleTextSize;
    private boolean isClickFinish = true;
    private boolean showBackRipple = false;



    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        initArrts(context, attrs);
        addViews(context);
        initListeners();
    }

    private void initArrts(Context context, AttributeSet attrs) {
        if(attrs == null){
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor, 0);
        leftText = ta.getString(R.styleable.TitleBar_leftText);
        leftTextSize = ta.getDimension(R.styleable.TitleBar_leftTextSize, 0);

        rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor, Color.BLACK);
        rightTextSize = ta.getDimension(R.styleable.TitleBar_rightTextSize, UIUtil.dip2px(context, 14));
        rightText = ta.getString(R.styleable.TitleBar_rightText);

        titleTextSize = ta.getDimension(R.styleable.TitleBar_titleTextSize, UIUtil.sp2px(context, 17));
        titleTextColor = ta.getColor(R.styleable.TitleBar_titleTextcolor, Color.BLACK);
        title = ta.getString(R.styleable.TitleBar_Toptitle);
        showBackRipple = ta.getBoolean(R.styleable.TitleBar_showBackRipple, false);

        ta.recycle();
    }

    private void addViews(Context context) {
        leftBtn = new Button(context);
        rightBtn = new Button(context);
        leftIv = new ImageView(context);
        rightIv = new ImageView(context);
        tvTitle = new TextView(context);
        rightTextView = new TextView(context);
        lineView = new View(context);

        leftBtn.setTextColor(leftTextColor);
        leftBtn.setGravity(Gravity.CENTER);
        leftBtn.setTextSize(16f);
        leftBtn.setText(leftText);
        leftBtn.setVisibility(GONE);

        if (!TextUtils.isEmpty(rightText)) {
            setRightBtIsVisible(true);
            rightBtn.setTextColor(rightTextColor);
            rightBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent, getContext().getTheme()));
            rightBtn.setTextSize(16f);
            rightBtn.setGravity(Gravity.CENTER);
            rightBtn.setPadding(UIUtil.dip2px(getContext(), 10), UIUtil.dip2px(getContext(), 2)
                    , UIUtil.dip2px(getContext(), 10), UIUtil.dip2px(getContext(), 15));
            rightBtn.setText(rightText);
        } else {
            setRightBtIsVisible(false);
        }

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.getPaint().setTextSize(titleTextSize);
        tvTitle.getPaint().setFakeBoldText(true);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setEllipsize(TruncateAt.END);
        tvTitle.setSingleLine(true);
        tvTitle.setMaxEms(13);

        rightTextView.setText(rightText);
        rightTextView.setTextColor(rightTextColor);
        rightTextView.getPaint().setTextSize(rightTextSize);
        rightTextView.setGravity(Gravity.CENTER);
        rightTextView.setEllipsize(TruncateAt.MIDDLE);
        rightTextView.setSingleLine(true);
        rightTextView.setMaxEms(8);
        rightTextView.setPadding(UIUtil.dip2px(getContext(), 10), UIUtil.dip2px(getContext(), 2)
                , UIUtil.dip2px(getContext(), 15), UIUtil.dip2px(getContext(), 2));

        lineView.setBackgroundColor(context.getResources().getColor(R.color.social_line_gray, context.getTheme()));

        /**设置标题栏背景颜色*/
        setBackgroundColor(getResources().getColor(android.R.color.white, getContext().getTheme()));

        LayoutParams leftBtnLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        leftBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START,
                RelativeLayout.TRUE);
        addView(leftBtn, leftBtnLayoutParams);


        LayoutParams rightBtnLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rightBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END,
                RelativeLayout.TRUE);
        addView(rightBtn, rightBtnLayoutParams);

        LayoutParams titleLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        titleLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        addView(tvTitle, titleLayoutParams);

        LayoutParams rightTextViewLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rightTextViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END,
                RelativeLayout.TRUE);
        addView(rightTextView, rightTextViewLayoutParams);


        LayoutParams leftIvParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        leftIvParams.addRule(RelativeLayout.ALIGN_PARENT_START,
                RelativeLayout.TRUE);
        leftIvParams.addRule(RelativeLayout.CENTER_VERTICAL,
                RelativeLayout.TRUE);
        leftIvParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        leftIvParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        leftIv.setLayoutParams(leftIvParams);
        int paddingHoriz = UIUtil.dip2px(getContext(), 20);
        int paddingVertical = UIUtil.dip2px(getContext(), 10);
        leftIv.setPadding(paddingHoriz, paddingVertical, paddingHoriz, paddingVertical);
        leftIv.setImageResource(R.drawable.ic_back_common);
        addView(leftIv, leftIvParams);
        if(showBackRipple){
            leftIv.setBackgroundResource(BaseResource.rippleCircleResource);
        }

        //固定宽高

        LayoutParams rightIvParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(rightIv, rightIvParams);

        LayoutParams lineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.dip2px(context,0.3f));
        lineParams.addRule(ALIGN_PARENT_BOTTOM);
        addView(lineView,lineParams);
    }

    private void initListeners(){
        leftBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.leftClick(v);
                }
            }
        });
        leftIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(getContext() instanceof Activity&&isClickFinish) {
                    ((Activity)getContext()).finish();
                }
                if (listener != null){
                    listener.leftClick(v);
                }
            }
        });
        rightIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.rightClick(v);
                }
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.rightClick(v);
                }
            }
        });
        rightTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.rightClick(v);
                }
            }
        });
    }

    public void setOnToolbarClickListener(OnTitleBarClickListener listener) {
        this.listener = listener;
    }


    public void setIsClickFinish(boolean isClickFinish) {
        this.isClickFinish = isClickFinish;
    }


    public void setShowBackRipple(boolean showBackRipple){
        this.showBackRipple = showBackRipple;
        if(showBackRipple){
            leftIv.setBackgroundResource(BaseResource.rippleCircleResource);
        } else {
            leftIv.setBackground(null);
        }
    }


    /**
    * 是否隐藏标题的左边按钮  默认是不隐藏  需要隐藏的时候在实现该方法
    * @param flag  true不隐藏   false隐藏
    */
    public TitleBar setLeftBtIsVisible(boolean flag) {
        if (flag) {
            leftBtn.setVisibility(View.VISIBLE);
        } else {
            leftBtn.setVisibility(View.GONE);
        }
        return this;
    }

    /*
     * 是否隐藏标题的左边图标  默认是不隐藏  需要隐藏的时候在实现该方法
     * params flag  true不隐藏   false隐藏
     */
    public TitleBar setLeftImageIsVisible(boolean flag) {
        if (flag) {
            leftIv.setVisibility(View.VISIBLE);
        } else {
            leftIv.setVisibility(View.GONE);
        }
        return this;
    }

    /**
    * 是否隐藏标题的右边按钮  默认是不隐藏  需要隐藏的时候在实现该方法
    * @param flag  true不隐藏   false隐藏
    */
    public TitleBar setRightBtIsVisible(boolean flag) {
        rightBtn.setVisibility(flag? View.VISIBLE: View.GONE);
        return this;
    }

    /**
     * 方法功能描述:设置右侧text
     */
    public TitleBar setRightText(String text) {
        rightTextView.setText(TextUtils.isEmpty(text)?"":text);
        return this;
    }

    public TitleBar setRightTextColor(int color) {
        rightTextView.setTextColor(color);
        return this;
    }

    public TitleBar setRightText(int text) {
        rightTextView.setText(text);
        return this;
    }

    public TitleBar setRightTextBold(boolean isBold) {
        rightTextView.getPaint().setFakeBoldText(isBold);
        return this;
    }

    public String getRightText() {
        return rightTextView.getText().toString();
    }
    /**
     * 方法功能描述:设置右侧text是否可见
     */
    public TitleBar setRightTextVisible(boolean visiable) {
        rightTextView.setVisibility(visiable? View.VISIBLE: View.GONE);
        return this;
    }
    /*
    * 设置左边标题左边的图片
    *
    * */
    public TitleBar setLeftImage(int m) {
        LayoutParams params = new LayoutParams(LayoutParams
                .WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_START,
                RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL,
                RelativeLayout.TRUE);
        params.width = UIUtil.dip2px(getContext(), 41);
        params.height = UIUtil.dip2px(getContext(), 21);
        leftIv.setLayoutParams(params);
        leftIv.setPadding(UIUtil.dip2px(getContext(), 10), 0, UIUtil.dip2px(getContext(),
                10), UIUtil.dip2px(getContext(), 0));
        leftIv.setImageResource(m);
        return this;
    }

    /*
    * 设置左边标题右边的图片
    *
    * */
    public TitleBar setRightImage(int n) {
        rightIv.setImageResource(n);

        LayoutParams params = new LayoutParams(LayoutParams
                .WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END,
                RelativeLayout.TRUE);
        rightIv.setLayoutParams(params);
        rightIv.setPadding(UIUtil.dip2px(getContext(), 10), 0, UIUtil.dip2px(getContext(),
                20), 0);
        return this;
    }


    public TitleBar setTitle(String text) {
        tvTitle.setText(text);
        return this;
    }
    public TitleBar setTitle(int title) {
        tvTitle.setText(title);
        return this;
    }

    public ImageView getRightIv() {
        return rightIv;
    }

    public TextView getRightTextView(){
        return rightTextView;
    }

    public ImageView getLeftIv() {
        return leftIv;
    }

    public Button getLeftBtn() {
        return leftBtn;
    }

    public View getLineView(){
        return lineView;
    }
}
