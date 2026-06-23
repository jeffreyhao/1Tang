package com.common.config.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.common.config.R;

public class PremiumMemberBenefitsView extends LinearLayout {
    private TextView tvMemberBenefits;
    private TextView tvBenefit1;
    private ImageView ivBenefit1;
    private TextView tvBenefit2;
    private ImageView ivBenefit2;
    private TextView tvBenefit3;
    private ImageView ivBenefit3;
    private TextView tvBenefit4;
    private ImageView ivBenefit4;
    private LinearLayout llPremiumBenefitsTitle;
    private RelativeLayout rlPremium;
    public PremiumMemberBenefitsView(Context context) {
        this(context, null);
    }

    public PremiumMemberBenefitsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_premium_member_benefits, this);
        tvMemberBenefits = findViewById(R.id.tv_member_benefits);
        tvBenefit1 = findViewById(R.id.tv_benefit1);
        ivBenefit1 = findViewById(R.id.iv_benefit1);
        tvBenefit2 = findViewById(R.id.tv_benefit2);
        ivBenefit2 = findViewById(R.id.iv_benefit2);
        tvBenefit3 = findViewById(R.id.tv_benefit3);
        ivBenefit3 = findViewById(R.id.iv_benefit3);
        tvBenefit4 = findViewById(R.id.tv_benefit4);
        ivBenefit4 = findViewById(R.id.iv_benefit4);

        llPremiumBenefitsTitle = findViewById(R.id.ll_premium_benefits_title);
        rlPremium = findViewById(R.id.rl_premium);

        rlPremium.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.toPremiumCenter();
                }
            }
        });
    }

    public void changeNightMode(boolean isNightMode) {

    }

    public void setProfileMode() {
        rlPremium.setVisibility(VISIBLE);
        llPremiumBenefitsTitle.setVisibility(GONE);

        tvBenefit1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_profile_premium_benefit));
        tvBenefit2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_profile_premium_benefit));
        tvBenefit3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_profile_premium_benefit));
        tvBenefit4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_profile_premium_benefit));

        ivBenefit1.setImageResource(R.drawable.ic_profile_premium_books);
        ivBenefit2.setImageResource(R.drawable.ic_profile_weekly_exclusive_books);
        ivBenefit3.setImageResource(R.drawable.ic_profile_ad_free);
        ivBenefit4.setImageResource(R.drawable.ic_profile_premium_identity);
    }


    private OnViewClickListener listener;
    public interface OnViewClickListener {
        void toPremiumCenter();
    }
    public void setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
    }
}
