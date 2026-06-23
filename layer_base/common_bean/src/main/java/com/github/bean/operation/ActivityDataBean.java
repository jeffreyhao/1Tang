package com.github.bean.operation;

import android.os.Parcel;
import android.os.Parcelable;

import com.base.global.GlobalBuildConfig;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDataBean implements Parcelable {
    @SerializedName("is_show")
    public int isShow;
    @SerializedName("book_infos")
    public List<ActivityBookBean> bookList;

    /**
     * 活动id
     */
    @SerializedName("marketing_id")
    public String marketingId;
    @SerializedName("trigger_opportunity_type")
    public int triggerOpportunityType;
    @SerializedName("count_down")
    public long countDown;

    @SerializedName("frequency_num")
    public int frequencyNum;

    @SerializedName("img_url")
    public String imgUrl;
    @SerializedName("uri")
    public String uri;

    @SerializedName("priority")
    public int priority;
    @SerializedName("copywriting_title")
    public String copywritingTitle;
    @SerializedName("copywriting")
    public String copywriting;

    /**
     * @see ActivityDataWindowType 弹窗｜浮窗
     */
    @SerializedName("tips_type")
    public int tipsType;

    /**
     * @see ActivityDataJumpType 跳转类型
     */
    @SerializedName("jump_type")
    public int jumpType;

    /**
     * @see ActivityDataShowPosition
     */
    @SerializedName("position")
    public List<Integer> position;

    @SerializedName("recharge_template_mapping")
    public Map<String, Integer> rechargeTemplateMap;


    public boolean isClosed;


    public ActivityDataBean() {
    }

    public int getRechargeTemplateId(){
        if(rechargeTemplateMap == null || rechargeTemplateMap.isEmpty()) {
            return -1;
        }
        Integer value = rechargeTemplateMap.get(GlobalBuildConfig.PRODUCT_ID);
        return value == null ? -1 : value;
    }

    public boolean isReadBenefit() {
        if(position != null && position.contains(ActivityDataShowPosition.RAED)) {
            return jumpType == ActivityDataJumpType.JUMP_EXIT_RECOMMEND || jumpType == ActivityDataJumpType.JUMP_ADV_SUBSCRIBE;
        }
        return false;
    }

    public boolean isExitBenefit() {
        if(position != null && position.contains(ActivityDataShowPosition.RAED)) {
            return jumpType == ActivityDataJumpType.JUMP_EXIT_RECOMMEND;
        }
        return false;
    }

    public boolean isAdvUnlockBenefit() {
        if(position != null && position.contains(ActivityDataShowPosition.RAED)) {
            return jumpType == ActivityDataJumpType.JUMP_ADV_SUBSCRIBE;
        }
        return false;
    }


    public ActivityDataBean(Parcel in) {
        this.isShow = in.readInt();
        this.bookList = in.createTypedArrayList(ActivityBookBean.CREATOR);
        this.marketingId = in.readString();
        this.triggerOpportunityType = in.readInt();
        this.countDown = in.readLong();
        this.frequencyNum = in.readInt();
        this.imgUrl = in.readString();
        this.uri = in.readString();
        this.priority = in.readInt();
        this.copywritingTitle = in.readString();
        this.copywriting = in.readString();
        this.tipsType = in.readInt();
        this.jumpType = in.readInt();
        this.position = new ArrayList<>();
        in.readList(this.position, Integer.class.getClassLoader());
        int mapSize = in.readInt();
        if (mapSize >= 0) {
            this.rechargeTemplateMap = new HashMap<>(mapSize);
            for (int i = 0; i < mapSize; i++) {
                this.rechargeTemplateMap.put(in.readString(), in.readInt());
            }
        }
        this.isClosed = in.readInt() == 1;
    }

    public void readFromParcel(Parcel source) {
        this.isShow = source.readInt();
        this.bookList = source.createTypedArrayList(ActivityBookBean.CREATOR);
        this.marketingId = source.readString();
        this.triggerOpportunityType = source.readInt();
        this.countDown = source.readLong();
        this.frequencyNum = source.readInt();
        this.imgUrl = source.readString();
        this.uri = source.readString();
        this.priority = source.readInt();
        this.copywritingTitle = source.readString();
        this.copywriting = source.readString();
        this.tipsType = source.readInt();
        this.jumpType = source.readInt();
        this.position = new ArrayList<>();
        source.readList(this.position, Integer.class.getClassLoader());
        int mapSize = source.readInt();
        if (mapSize >= 0) {
            this.rechargeTemplateMap = new HashMap<>(mapSize);
            for (int i = 0; i < mapSize; i++) {
                this.rechargeTemplateMap.put(source.readString(), source.readInt());
            }
        }
        this.isClosed = source.readInt() == 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isShow);
        dest.writeTypedList(this.bookList);
        dest.writeString(this.marketingId);
        dest.writeInt(this.triggerOpportunityType);
        dest.writeLong(this.countDown);
        dest.writeInt(this.frequencyNum);
        dest.writeString(this.imgUrl);
        dest.writeString(this.uri);
        dest.writeInt(this.priority);
        dest.writeString(this.copywritingTitle);
        dest.writeString(this.copywriting);
        dest.writeInt(this.tipsType);
        dest.writeInt(this.jumpType);
        dest.writeList(this.position);
        if (this.rechargeTemplateMap != null) {
            dest.writeInt(this.rechargeTemplateMap.size());
            for (Map.Entry<String, Integer> entry : this.rechargeTemplateMap.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeInt(entry.getValue());
            }
        } else {
            dest.writeInt(-1);
        }
        dest.writeInt(this.isClosed ? 1 : 0);
    }

    public static final Creator<ActivityDataBean> CREATOR = new Creator<ActivityDataBean>() {
        @Override
        public ActivityDataBean createFromParcel(Parcel source) {
            return new ActivityDataBean(source);
        }

        @Override
        public ActivityDataBean[] newArray(int size) {
            return new ActivityDataBean[size];
        }
    };

    public static class ActivityBookBean implements Parcelable {
        @SerializedName("book_id")
        public String bookId;
        @SerializedName("com_book_id")
        public String comBookId;
        @SerializedName("external_cp_book_id")
        public String externalCpBookId;
        @SerializedName("book_title")
        public String bookTitle;
        @SerializedName("book_cover")
        public String bookCover;
        @SerializedName("first_chapter_title")
        public String firstChapterTitle;
        @SerializedName("first_chapter_content")
        public String firstChapterContent;
        @SerializedName("marketing_type")
        public int marketingType;
        @SerializedName("discount")
        public int discount;
        @SerializedName("is_vip")
        public int isVip;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.bookId);
            dest.writeString(this.comBookId);
            dest.writeString(this.externalCpBookId);
            dest.writeString(this.bookTitle);
            dest.writeString(this.bookCover);
            dest.writeString(this.firstChapterTitle);
            dest.writeString(this.firstChapterContent);
            dest.writeInt(this.marketingType);
            dest.writeInt(this.discount);
            dest.writeInt(this.isVip);
        }

        public void readFromParcel(Parcel source) {
            this.bookId = source.readString();
            this.comBookId = source.readString();
            this.externalCpBookId = source.readString();
            this.bookTitle = source.readString();
            this.bookCover = source.readString();
            this.firstChapterTitle = source.readString();
            this.firstChapterContent = source.readString();
            this.marketingType = source.readInt();
            this.discount = source.readInt();
            this.isVip = source.readInt();
        }

        public ActivityBookBean() {
        }

        protected ActivityBookBean(Parcel in) {
            this.bookId = in.readString();
            this.comBookId = in.readString();
            this.externalCpBookId = in.readString();
            this.bookTitle = in.readString();
            this.bookCover = in.readString();
            this.firstChapterTitle = in.readString();
            this.firstChapterContent = in.readString();
            this.marketingType = in.readInt();
            this.discount = in.readInt();
            this.isVip = in.readInt();
        }

        public static final Creator<ActivityBookBean> CREATOR = new Creator<ActivityBookBean>() {
            @Override
            public ActivityBookBean createFromParcel(Parcel source) {
                return new ActivityBookBean(source);
            }

            @Override
            public ActivityBookBean[] newArray(int size) {
                return new ActivityBookBean[size];
            }
        };
    }


}
