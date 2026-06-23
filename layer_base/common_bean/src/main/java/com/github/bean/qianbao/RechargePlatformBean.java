package com.github.bean.qianbao;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.virtual.UniqueKey;
import com.base.util.collection.ListUtil;
import com.base.util.collection.Replicable;
import com.github.bean.zhifu.PayRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RechargePlatformBean implements Parcelable, Serializable, Replicable<RechargePlatformBean>, UniqueKey {

    public static final Creator<RechargePlatformBean> CREATOR = new Creator<RechargePlatformBean>() {
        @Override
        public RechargePlatformBean createFromParcel(Parcel in) {
            return new RechargePlatformBean(in);
        }

        @Override
        public RechargePlatformBean[] newArray(int size) {
            return new RechargePlatformBean[size];
        }
    };


    public static final String PAYMENT_GOOGLE       = "sdk-googlepay";
    public static final String PAYMENT_TYPE_SDK     = "SDK";



    private int code;
    private String sub_code;
    private int usage_mode;
    private String platform;
    private int favourable_percent;
    private int is_show;
    private int checked;
    private String image_url;
    private String tips;
    private String payment;
    private String payment_type;

    private List<PayRule> rules_info;

    private List<PayRule> activity_info;


    public RechargePlatformBean() {

    }

    protected RechargePlatformBean(Parcel in) {
        code = in.readInt();
        sub_code = in.readString();
        usage_mode = in.readInt();
        platform = in.readString();
        favourable_percent = in.readInt();
        is_show = in.readInt();
        checked = in.readInt();
        image_url = in.readString();
        tips = in.readString();
        payment = in.readString();
        payment_type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(sub_code);
        dest.writeInt(usage_mode);
        dest.writeString(platform);
        dest.writeInt(favourable_percent);
        dest.writeInt(is_show);
        dest.writeInt(checked);
        dest.writeString(image_url);
        dest.writeString(tips);
        dest.writeString(payment);
        dest.writeString(payment_type);
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public int getUsage_mode() {
        return usage_mode;
    }

    public void setUsage_mode(int usage_mode) {
        this.usage_mode = usage_mode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getFavourable_percent() {
        return favourable_percent;
    }

    public void setFavourable_percent(int favourable_percent) {
        this.favourable_percent = favourable_percent;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public List<PayRule> getRules_info() {
        return rules_info == null ? new ArrayList<>() : rules_info;
    }

    public void setRules_info(List<PayRule> rules_info) {
        this.rules_info = rules_info;
    }


    public List<PayRule> getActivity_info() {
        return activity_info;
    }

    public void setActivity_info(List<PayRule> activity_info) {
        this.activity_info = activity_info;
    }

    public static RechargePlatformBean createGooglePlatform() {
        RechargePlatformBean rechargePlatform = new RechargePlatformBean();
        rechargePlatform.setCode(1);
        rechargePlatform.setSub_code("");
        rechargePlatform.setPayment(PAYMENT_GOOGLE);
        rechargePlatform.setPayment_type(PAYMENT_TYPE_SDK);
        return rechargePlatform;
    }

    public static RechargePlatformBean createPlatform(int platformCode, String payment, String paymentType) {
        RechargePlatformBean rechargePlatform = new RechargePlatformBean();
        rechargePlatform.setCode(platformCode);
        rechargePlatform.setPayment(payment);
        rechargePlatform.setPayment_type(paymentType);
        return rechargePlatform;
    }

    public static boolean useSdk(RechargePlatformBean rechargePlatform) {
        return rechargePlatform!=null
                && TextUtils.equals(rechargePlatform.getPayment(), RechargePlatformBean.PAYMENT_GOOGLE)
                && TextUtils.equals(rechargePlatform.getPayment_type(), RechargePlatformBean.PAYMENT_TYPE_SDK);
    }

    @Override
    public RechargePlatformBean copy() {
        try {
            RechargePlatformBean newBean = (RechargePlatformBean) this.clone();
            newBean.activity_info = new ArrayList<>();
            if (!ListUtil.isEmpty(activity_info)) {
                for(PayRule subscribeBean : activity_info){
                    newBean.activity_info.add(subscribeBean.copy());
                }
            }
            return newBean;
        } catch (Throwable e) {
            LogUtil.e(e);
            return new RechargePlatformBean();
        }
    }

    public void uncheckSubscribeList(){
        if(!ListUtil.isEmpty(activity_info)){
            for(PayRule subscribeBean : activity_info){
                subscribeBean.selected = false;
            }
        }
    }


    public String platformInfo(){
        return getPlatform() + "&" + platformCode() + "&" + getPayment();
    }

    private String platformCode(){
        return getCode() + "-" + getSub_code();
    }

    @Override
    public String getUniqueKey() {
        return getCode() + "-" + getSub_code();
    }
}
