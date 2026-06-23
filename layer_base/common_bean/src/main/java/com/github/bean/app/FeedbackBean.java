package com.github.bean.app;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FeedbackBean implements Parcelable {
    @SerializedName("id")
    public String id;
    @SerializedName("account_id")
    public String accountId;
    @SerializedName("answer_id")
    public String answerId;
    @SerializedName("parent_id")
    public String parentId;
    @SerializedName("is_top")
    public int isTop;
    @SerializedName("likes")
    public int likes;
    @SerializedName("type_id")
    public String typeId;
    @SerializedName("type_name")
    public String typeName;
    @SerializedName("content")
    public String content;
    @SerializedName("contacts")
    public String contacts;
    @SerializedName("answer_count")
    public int answerCount;
    @SerializedName("create_time")
    public String createTime;
    @SerializedName("update_time")
    public String updateTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.accountId);
        dest.writeString(this.answerId);
        dest.writeString(this.parentId);
        dest.writeInt(this.isTop);
        dest.writeInt(this.likes);
        dest.writeString(this.typeId);
        dest.writeString(this.typeName);
        dest.writeString(this.content);
        dest.writeString(this.contacts);
        dest.writeInt(this.answerCount);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.accountId = source.readString();
        this.answerId = source.readString();
        this.parentId = source.readString();
        this.isTop = source.readInt();
        this.likes = source.readInt();
        this.typeId = source.readString();
        this.typeName = source.readString();
        this.content = source.readString();
        this.contacts = source.readString();
        this.answerCount = source.readInt();
        this.createTime = source.readString();
        this.updateTime = source.readString();
    }

    public FeedbackBean() {
    }

    protected FeedbackBean(Parcel in) {
        this.id = in.readString();
        this.accountId = in.readString();
        this.answerId = in.readString();
        this.parentId = in.readString();
        this.isTop = in.readInt();
        this.likes = in.readInt();
        this.typeId = in.readString();
        this.typeName = in.readString();
        this.content = in.readString();
        this.contacts = in.readString();
        this.answerCount = in.readInt();
        this.createTime = in.readString();
        this.updateTime = in.readString();
    }

    public static final Creator<FeedbackBean> CREATOR = new Creator<FeedbackBean>() {
        @Override
        public FeedbackBean createFromParcel(Parcel source) {
            return new FeedbackBean(source);
        }

        @Override
        public FeedbackBean[] newArray(int size) {
            return new FeedbackBean[size];
        }
    };
}
