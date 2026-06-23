package com.github.bean.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2018/10/28 4:55 PM.
 * 
 * Description: 首页书库有额为信息
 */
public class Extra implements Parcelable {
    private String desc;
    private String status;
    private int cateId;
    private String cate;
    private String bid;
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public Extra() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.desc);
        dest.writeString(this.status);
        dest.writeInt(this.cateId);
        dest.writeString(this.cate);
        dest.writeString(this.bid);
        dest.writeStringList(this.tags);
    }

    protected Extra(Parcel in) {
        this.desc = in.readString();
        this.status = in.readString();
        this.cateId = in.readInt();
        this.cate = in.readString();
        this.bid = in.readString();
        this.tags = in.createStringArrayList();
    }

    public static final Creator<Extra> CREATOR = new Creator<Extra>() {
        @Override
        public Extra createFromParcel(Parcel source) {
            return new Extra(source);
        }

        @Override
        public Extra[] newArray(int size) {
            return new Extra[size];
        }
    };
}
