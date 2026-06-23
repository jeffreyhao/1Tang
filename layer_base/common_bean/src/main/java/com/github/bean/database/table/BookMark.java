package com.github.bean.database.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.REPLACE)})
public class BookMark extends BaseModel implements Parcelable {

    @TableFieldV20(name = "_id")
    @PrimaryKey(autoincrement = true)
    @Column(name = "_id")
    public long id;

    @TableFieldV20
    @Column
    @Unique(uniqueGroups = 1,unique = false)
    public String bookId = "";

    @TableFieldV20
    @Column
    public int parapghId = 0;

    @TableFieldV20
    @Column
    public String chapterName = "";

    @TableFieldV20
    @Column
    @Unique(uniqueGroups = 1,unique = false)
    public String chapterId = "";

    @TableFieldV20
    @Column
    @Unique(uniqueGroups = 1,unique = false)
    public int startElement = 0;

    @TableFieldV20
    @Column
    public int endElement = 0;


    public static final Creator<BookMark> CREATOR = new Creator<BookMark>() {
        @Override
        public BookMark createFromParcel(Parcel in) {
            return new BookMark(in);
        }

        @Override
        public BookMark[] newArray(int size) {
            return new BookMark[size];
        }
    };

    public void saveAsync() {
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(databaseWrapper -> {
            BookMark.this.save();

        });
    }

    public BookMark() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.bookId);
        dest.writeInt(this.parapghId);
        dest.writeString(this.chapterName);
        dest.writeString(this.chapterId);
        dest.writeInt(this.startElement);
        dest.writeInt(this.endElement);
    }



    protected BookMark(Parcel in) {
        this.id = in.readLong();
        this.bookId = in.readString();
        this.parapghId = in.readInt();
        this.chapterName = in.readString();
        this.chapterId = in.readString();
        this.startElement = in.readInt();
        this.endElement = in.readInt();
    }


}
