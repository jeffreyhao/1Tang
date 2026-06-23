package com.github.bean.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2018/7/23 下午7:45.
 * 
 * Description:
 */
public class HotSearch implements Parcelable {

    @SerializedName("default")
    private String defaultX;
    private List<String> hot;

    public String getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(String defaultX) {
        this.defaultX = defaultX;
    }

    public List<String> getHot() {
        return hot;
    }

    public void setHot(List<String> hot) {
        this.hot = hot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.defaultX);
        dest.writeStringList(this.hot);
    }

    public HotSearch() {
    }

    protected HotSearch(Parcel in) {
        this.defaultX = in.readString();
        this.hot = in.createStringArrayList();
    }

    public static final Creator<HotSearch> CREATOR = new Creator<HotSearch>() {
        @Override
        public HotSearch createFromParcel(Parcel source) {
            return new HotSearch(source);
        }

        @Override
        public HotSearch[] newArray(int size) {
            return new HotSearch[size];
        }
    };
}
