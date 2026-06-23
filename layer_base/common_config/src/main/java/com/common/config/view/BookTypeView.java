package com.common.config.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.baidu.baselibrary.util.ui.ViewUtil;
import com.common.config.FeatureConfig;
import com.common.config.R;
import com.github.bean.database.table.BookInfo;

import androidx.core.content.ContextCompat;

/**
 * 书籍营销类型 0无营销类型 1推荐书籍 2 独家推荐书籍 3 免费书籍 4 折扣书籍
 * @author lhc
 * @date 2022/11/21 10:18
 */
public class BookTypeView extends RelativeLayout {


    private TextView tvStatus;
    private TextView tvOff;
    private TextView tvShort;


    public BookTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_book_type, this);
        tvStatus = findViewById(R.id.tv_status);
        tvOff = findViewById(R.id.tv_off);
        tvShort = findViewById(R.id.tv_short_corner);
    }

    /**
     *
     * @param isVip
     * @param marketingType  {@link BookInfo#getMarketing_type()}
     * @param discount
     * @param textSizeMode
     */
    public void setBookType(int isVip, int marketingType, int discount, int textSizeMode) {
        if (FeatureConfig.isPremiumApp) {
            if(isVip == 1){
                setVisibility(View.VISIBLE);
                ViewUtil.setVisible(tvStatus, false);
                ViewUtil.setVisible(tvOff, false);
            } else {
                setVisibility(View.GONE);
            }
            return;
        }

        ViewUtil.setVisible(tvStatus, marketingType != BookInfo.MARKET_TYPE_NONE && marketingType != BookInfo.MARKET_TYPE_DISCOUNT);
        ViewUtil.setVisible(tvOff, marketingType == BookInfo.MARKET_TYPE_DISCOUNT && discount > 0);

        if (marketingType == BookInfo.MARKET_TYPE_NONE || (marketingType == BookInfo.MARKET_TYPE_DISCOUNT && discount == 0)) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        tvStatus.setTextSize(textSizeMode == 0 ? 8f : 7f);

        int drawable = -1;
        int textRes = -1;
        switch (marketingType) {
            case BookInfo.MARKET_TYPE_RECOMMEND:
                drawable = R.drawable.ic_recommend_bg;
                textRes = R.string.pp_recommend;
                tvStatus.setText(getContext().getString(textRes));
                tvStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_book_shelf_recommend));
                break;
            case BookInfo.MARKET_TYPE_EXCLUSIVE:
                drawable = R.drawable.ic_exclusive_bg;
                textRes = R.string.pp_exclusive;
                tvStatus.setText(getContext().getString(textRes));
                break;
            case BookInfo.MARKET_TYPE_FREE:
                drawable = R.drawable.ic_free_bg;
                textRes = R.string.pp_free;
                tvStatus.setText(getContext().getString(textRes));
                break;
            case BookInfo.MARKET_TYPE_DISCOUNT:
                drawable = R.drawable.ic_off_bg;
                textRes = R.string.pp_off;
                tvOff.setText(discount + "%\n" + getContext().getString(textRes));
                break;
        }

        if (drawable != -1) {
            if (marketingType == BookInfo.MARKET_TYPE_DISCOUNT) {
                tvOff.setBackgroundResource(drawable);
            } else {
                tvStatus.setBackgroundResource(drawable);
            }
        }
    }

    public void setBookType(int isVip, int marketingType, int discount) {
        setBookType(isVip, marketingType, discount, 0);
    }

    public void setBookType(int marketingType, int discount) {
        setBookType(0, marketingType, discount, 0);
    }

    public void setShort(boolean isShort){
        if(isShort) {
            setVisibility(View.VISIBLE);
            tvShort.setVisibility(View.VISIBLE);
        } else {
            tvShort.setVisibility(View.GONE);
        }
    }
}
