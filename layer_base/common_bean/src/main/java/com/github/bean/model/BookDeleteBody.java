package com.github.bean.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 删除用户收藏，服务器请求bean
 * */
public class BookDeleteBody implements Parcelable {
    public String uid;
    public List<String> bids;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeStringList(this.bids);
    }

    public BookDeleteBody() {
    }

    protected BookDeleteBody(Parcel in) {
        this.uid = in.readString();
        this.bids = in.createStringArrayList();
    }

    public static final Creator<BookDeleteBody> CREATOR = new Creator<BookDeleteBody>() {
        @Override
        public BookDeleteBody createFromParcel(Parcel source) {
            return new BookDeleteBody(source);
        }

        @Override
        public BookDeleteBody[] newArray(int size) {
            return new BookDeleteBody[size];
        }
    };
}
