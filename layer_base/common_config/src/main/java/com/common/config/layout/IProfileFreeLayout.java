package com.common.config.layout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.baselibrary.widget.CommonRefreshLayout;
import com.base.layout.ILayout;

/**
 * ILayout接口。
 *
 * <br>
 * <br> 实现类，需要有这样的构造器：
 * <br>
 * <br>    public ILayout(LayoutInflater inflater){
 * <br>         mBinding = LayoutProfileFreeBinding.inflate(inflater);
 * <br>    }
 * <br>
 * <br>
 * Created by niepan on 28/01/26.
 */
public interface IProfileFreeLayout extends ILayout {


    TextView getUserIdView();

    View getUserMottoView();

    View getUserAccountGroup();

    ImageView getUserAvatarView();

    View getProfileViews();


    View getRatingView();

    View getFeedbackView();

    View getSettingView();


    CommonRefreshLayout getRefreshLayout();

    View getDotFeedback();


    View getTvSubscribe();

    View getPremiumInfoLayout();

    TextView getTvPremiumExpireDate();

    TextView getProfileUserNameView();



    View tvPremiumActive();

    TextView tvPremiumExpireDate();

    View ivFullVip();

    View ivVip();

    View ivPremium();

    View tvSubscribe();

    View tvPremiumTipLeft();

    View tvPremiumTipRight();

}
