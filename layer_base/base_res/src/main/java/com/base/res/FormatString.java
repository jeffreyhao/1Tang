package com.base.res;


import android.content.Context;
import android.view.View;

import com.base.res.style.ClickCallbackSpan;
import com.base.res.style.URLSpanNoUnderline;

import androidx.annotation.ColorRes;

/**
 * Created by haojiangfeng on 2023/9/8.
 */
public class FormatString {





    public static CharSequence getPpPayTip2(Context context, String userId, View.OnClickListener uidCopyListener, View.OnClickListener clickListener){
        String resText = context.getString(R.string.pp_pay_tip2, userId);
        ClickCallbackSpan[] spans = new ClickCallbackSpan[]{new ClickCallbackSpan(uidCopyListener, true), new ClickCallbackSpan(clickListener, true)};
        return Res.getAccentText(resText, spans);
    }

    public static CharSequence getPpPayTip3(Context context, View.OnClickListener listener){
        String resText = context.getString(R.string.pp_pay_tip3);
        return Res.getAccentText(resText, new ClickCallbackSpan(listener, true));
    }


    public static CharSequence getBookDetailReportMsg(Context context, View.OnClickListener listener){
        String resText = Res.FormatString(context, R.string.book_detail_report_msg);
        return Res.getAccentText(resText, new ClickCallbackSpan(listener, true));
    }

    public static CharSequence getLoginViewTip(Context context, @ColorRes int colorRes, boolean isBold, View.OnClickListener clickListener1, View.OnClickListener clickListener2){
        String resText = Res.FormatString(context, R.string.login_view_tip);
        ClickCallbackSpan[] spans = new ClickCallbackSpan[]{new ClickCallbackSpan(clickListener1, true), new ClickCallbackSpan(clickListener2, true)};
        return Res.getSpanText(resText, colorRes, isBold, spans);
    }

    public static CharSequence getLoginViewTip(Context context, @ColorRes int colorRes, View.OnClickListener clickListener1, View.OnClickListener clickListener2){
        String resText = Res.FormatString(context, R.string.login_view_tip);
        ClickCallbackSpan[] spans = new ClickCallbackSpan[]{new ClickCallbackSpan(clickListener1, true), new ClickCallbackSpan(clickListener2, true)};
        return Res.getSpanText(resText, colorRes, spans);
    }

    public static CharSequence getPpWriterBenefits(Context context){
        String resText = Res.FormatString(context, R.string.pp_writer_benefits);
        URLSpanNoUnderline span = new URLSpanNoUnderline(getPPWriterWeb(context));
        return Res.getAccentText(resText, span);
    }

    public static CharSequence getPpWriterEmail(Context context){
        String resText = Res.formatString(context, R.string.pp_writer_email);
        return Res.getAccentText(resText);
    }

    public static CharSequence getSendCode(Context context, String replaceText, int accentColorRes){
        String resText = Res.formatString(context, R.string.pp_code_send_hint, replaceText);
        return Res.getSpanText(resText, accentColorRes);
    }


    public static CharSequence getPpWriterAuthor(Context context){
        String resText = Res.formatString(context, R.string.pp_writer_author);
        return Res.getAccentText(resText);
    }

    public static String getPPWriterWebWriter(Context context){
        return Res.formatString(context, R.string.pp_writer_web_writer);
    }

    public static String getPPWriterWeb(Context context){
        return Res.formatString(context, R.string.pp_writer_web);
    }


    public static String getAboutApp(Context context){
        return Res.FormatString(context, R.string.about_app);
    }


    public static String getPPWriterAboutDes(Context context){
        return Res.formatArrayString(context, R.string.pp_writer_about_des, Res.AppSimpleName);
    }

    public static CharSequence getPPCopyrightNoticeHint(String resText, View.OnClickListener clickListener){
        ClickCallbackSpan[] spans = new ClickCallbackSpan[]{new ClickCallbackSpan(clickListener, true)};
        return Res.getAccentText(resText, spans);
    }

    public static CharSequence getPPCopyrightNoticeHint2(Context context, String appName, String copyRight){
        String resText = context.getString(R.string.pp_copyright_notice_hint_line2, appName, copyRight);
        return resText;
    }

    public static CharSequence getPpPendingRetryTip(Context context, View.OnClickListener clickListener){
        String resText = Res.formatString(context, R.string.pp_pending_retry_tip);
        ClickCallbackSpan[] spans = new ClickCallbackSpan[]{new ClickCallbackSpan(clickListener, true)};
        return Res.getAccentText(resText, spans);
    }

    public static CharSequence getPpRewardFeedbackHint(String resText, View.OnClickListener clickListener){
        ClickCallbackSpan[] spans = new ClickCallbackSpan[]{new ClickCallbackSpan(clickListener, true)};
        return Res.getAccentText(resText, spans);
    }

    public static CharSequence getAlreadyBindThirdPartyHint(String text){
        return Res.getAccentText(text, new ClickCallbackSpan(null, false));
    }

    public static CharSequence getLoginWelcome(Context context){
        return Res.FormatString(context, R.string.login_welcome);
    }

    public static CharSequence getSubscribeFaqTip(Context context, View.OnClickListener listener){
        String resText = context.getString(R.string.subscribe_tips7);
        return Res.getAccentText(resText, new ClickCallbackSpan(listener, true));
    }

    public static CharSequence getVIPFeedbackTip(Context context, View.OnClickListener listener){
        String resText = context.getString(R.string.vip_feedback_rule);
        return Res.getAccentText(resText, new ClickCallbackSpan(listener, false));
    }

    public static CharSequence getPayTermsTip(Context context, View.OnClickListener listener){
        String resText = Res.FormatString(context, R.string.premium_center_recharge_tip);
        return Res.getAccentText(resText, new ClickCallbackSpan(listener, true));
    }
}
