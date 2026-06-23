package com.github.bean.database.table;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baidu.baselibrary.util.json.GsonUtils;
import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 阅读历史
 */
@Table(database = AppDatabase.class)
public class ReadHistory extends BaseModel implements Parcelable {

    //书籍bookId
    @TableFieldV20
    @PrimaryKey
    public String bookId;

    @TableFieldV20(name = "comBookId")
    @Column(name = "comBookId")
    public String comBookId;

    //书籍
    @TableFieldV20(name = "bookInfoJson")
    @Column(name = "bookInfoJson")
    public String bookInfoJson;

    //上次阅读，收藏时间 用于书架排序
    @TableFieldV20(name = "readTime")
    @Column(name = "readTime")
    public long readTime;

    //上次阅读，收藏时间 用于书架排序
    @TableFieldV20(name = "chapterName")
    @Column(name = "chapterName")
    public String chapterName;

    @TableFieldV20(name = "chapterPos")
    @Column(name = "chapterPos")
    public int chapterPos;

    @Column(name = "is_vip")
    public int is_vip;

    //最后更新的章节信息
    public BookInfo bookInfo;

    public ReadHistory() {

    }

    @Override
    public boolean save() {
        if (bookInfo != null) {
            bookInfoJson = GsonUtils.toJson(bookInfo);
        }
        return super.save();
    }

    @Override
    public boolean update() {
        if (bookInfo != null) {
            bookInfoJson = GsonUtils.toJson(bookInfo);
        }
        return super.update();
    }

    public BookInfo getBookInfo() {
        BookInfo bookInfo = null;
        if(!TextUtils.isEmpty(bookInfoJson)) {
            bookInfo = GsonUtils.fromJson(bookInfoJson, BookInfo.class);
        }
        return bookInfo;
    }

    protected ReadHistory(Parcel in) {
        this.bookId = in.readString();
        this.comBookId = in.readString();
        this.bookInfoJson = in.readString();
        this.bookInfo = in.readParcelable(BookInfo.class.getClassLoader());
        this.readTime = in.readLong();
        this.chapterName = in.readString();
        this.is_vip = in.readInt();
    }

    public static final Creator<ReadHistory> CREATOR = new Creator<ReadHistory>() {
        @Override
        public ReadHistory createFromParcel(Parcel in) {
            return new ReadHistory(in);
        }

        @Override
        public ReadHistory[] newArray(int size) {
            return new ReadHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookId);
        dest.writeString(this.comBookId);
        dest.writeString(this.bookInfoJson);
        dest.writeParcelable(this.bookInfo, flags);
        dest.writeLong(this.readTime);
        dest.writeString(this.chapterName);
        dest.writeInt(this.is_vip);
    }
}