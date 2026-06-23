package com.github.bean.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.bean.database.table.BookInfo;
import com.github.bean.reader.CommentItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookDetail implements Parcelable {
    public BookInfo book;
    public List<CommentItem> comments;
    public String latestChapterTip;
    public String copyright_statement;
    public List<BookInfo> recommends;
    @SerializedName("comments_total")
    public int commentsTotal;
    @SerializedName("first_chapter_cid")
    public String firstChapterId;

    public String firstChapterCid;
    public String priceFormated;
    // 查看人数
    public String readUvFormated;

    public int pvCount;
    public int favCount;
    public int wordCount;

    public BookDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.book, flags);
        dest.writeTypedList(this.comments);
        dest.writeString(this.latestChapterTip);
        dest.writeString(this.copyright_statement);
        dest.writeTypedList(this.recommends);
        dest.writeInt(this.commentsTotal);
        dest.writeString(this.firstChapterCid);
        dest.writeString(this.priceFormated);
        dest.writeString(this.readUvFormated);
        dest.writeInt(this.pvCount);
        dest.writeInt(this.favCount);
        dest.writeInt(this.wordCount);
    }

    protected BookDetail(Parcel in) {
        this.book = in.readParcelable(BookInfo.class.getClassLoader());
        this.comments = in.createTypedArrayList(CommentItem.CREATOR);
        this.latestChapterTip = in.readString();
        this.copyright_statement = in.readString();
        this.recommends = in.createTypedArrayList(BookInfo.CREATOR);
        this.commentsTotal = in.readInt();
        this.firstChapterCid = in.readString();
        this.priceFormated = in.readString();
        this.readUvFormated = in.readString();
        this.pvCount = in.readInt();
        this.favCount = in.readInt();
        this.wordCount = in.readInt();
    }

    public static final Creator<BookDetail> CREATOR = new Creator<BookDetail>() {
        @Override
        public BookDetail createFromParcel(Parcel source) {
            return new BookDetail(source);
        }

        @Override
        public BookDetail[] newArray(int size) {
            return new BookDetail[size];
        }
    };
}
