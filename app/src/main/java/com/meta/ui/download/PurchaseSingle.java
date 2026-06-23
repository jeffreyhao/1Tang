package com.meta.ui.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benefit.novelverse.R;
import com.github.bean.zhifu.PurchaseInfoBean;

public class PurchaseSingle extends RelativeLayout {
    private final TextView titleView;
    private final TextView desView;
    private final TextView offView;

    public PurchaseSingle(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.module_view_purchase_single, this);

        titleView = findViewById(R.id.purchase_single_title);
        desView = findViewById(R.id.purchase_single_des);
        offView = findViewById(R.id.purchase_single_off);
    }

    @SuppressLint("SetTextI18n")
    public void initSingle(PurchaseInfoBean.ChapterShowsBean obj) {
        if (obj.chapterNum >= 99999999) {
            titleView.setText(getContext().getString(R.string.remaining_chapters));
            desView.setText(getContext().getString(R.string.coins_all_chapters,obj.presentPrice));
        } else {
            titleView.setText(getContext().getString(R.string.next_num_chapters,obj.chapterNum));
            desView.setText(getContext().getString(R.string.coins_for_num_chapters,obj.presentPrice,obj.chapterNum));
        }
        if (obj.discount <= 0) {
            offView.setVisibility(GONE);
        } else {
            offView.setVisibility(VISIBLE);
            offView.setText((int) (obj.discount * 100) + "% " + getContext().getString(R.string.pp_off));
        }
    }

    public void checkSingle(boolean check) {
        this.setSelected(check);
        titleView.setTextAppearance(check ? R.style.style_download_item_checked : R.style.style_download_item_uncheck);
    }
}