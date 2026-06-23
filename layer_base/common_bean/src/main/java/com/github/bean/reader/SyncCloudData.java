package com.github.bean.reader;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.bean.database.table.BookInfo;

import java.util.List;

public class SyncCloudData implements Parcelable {


    public static final Creator<SyncCloudData> CREATOR = new Creator<SyncCloudData>() {
        @Override
        public SyncCloudData createFromParcel(Parcel in) {
            return new SyncCloudData(in);
        }

        @Override
        public SyncCloudData[] newArray(int size) {
            return new SyncCloudData[size];
        }
    };

    public List<BookInfo> body;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.body);
    }

    public SyncCloudData() {
    }

    protected SyncCloudData(Parcel in) {
        this.body = in.createTypedArrayList(BookInfo.CREATOR);
    }


}
