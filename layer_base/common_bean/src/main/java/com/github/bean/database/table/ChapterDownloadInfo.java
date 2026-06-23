package com.github.bean.database.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.UserManager;
import com.baidu.baselibrary.log.ALog;
import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;

import androidx.annotation.NonNull;

/**
* @author lhc
* @date 2022/5/24 10:40
* @desc 下载信息表
*/
@Table(database = AppDatabase.class)
public class ChapterDownloadInfo extends BaseModel implements Parcelable {

    @TableFieldV20(name = "id")
    @PrimaryKey(autoincrement = true)
    public Long id;

    @TableFieldV20(name = "uId")
    @Column(name = "uId")
    public String userId;

    @TableFieldV20(name = "bId")
    @Column(name = "bId")
    public String bookId;

    @TableFieldV20(name = "cId")
    @Column(name = "cId")
    public String chapterId;

    @TableFieldV20(name = "chapterName")
    @Column(name = "chapterName")
    public String chapterName;

    @TableFieldV20(name = "url")
    @Column(name = "url")
    public String url;

    @TableFieldV20(name = "updateTime")
    @Column(name = "updateTime")
    public String updateTime;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.bookId);
        dest.writeString(this.chapterId);
        dest.writeString(this.chapterName);
        dest.writeString(this.url);
        dest.writeString(this.updateTime);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Long) source.readValue(Long.class.getClassLoader());
        this.userId = source.readString();
        this.bookId = source.readString();
        this.chapterId = source.readString();
        this.chapterName = source.readString();
        this.url = source.readString();
        this.updateTime = source.readString();
    }

    public ChapterDownloadInfo() {
    }

    protected ChapterDownloadInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.userId = in.readString();
        this.bookId = in.readString();
        this.chapterId = in.readString();
        this.chapterName = in.readString();
        this.url = in.readString();
        this.updateTime = in.readString();
    }

    public static final Creator<ChapterDownloadInfo> CREATOR = new Creator<ChapterDownloadInfo>() {
        @Override
        public ChapterDownloadInfo createFromParcel(Parcel source) {
            return new ChapterDownloadInfo(source);
        }

        @Override
        public ChapterDownloadInfo[] newArray(int size) {
            return new ChapterDownloadInfo[size];
        }
    };

    public static List<ChapterDownloadInfo> getUnDownloadList() {
        return SQLite.select().from(ChapterDownloadInfo.class).where(ChapterDownloadInfo_Table.uId.eq(UserManager.getUserId())).queryList();
    }

    public static void insert(List<ChapterDownloadInfo> list) {
        FlowManager.getDatabase(AppDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<ChapterDownloadInfo>(BaseModel::insert).addAll(list).build())
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(@NonNull Transaction transaction) {
                        ALog.textSingle("ChapterDownloadInfo", "insert.onSuccess","success");
                    }
                })
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        ALog.textSingle("ChapterDownloadInfo", "insert.onError","error: " + error.getMessage());
                    }
                })
                .build()
                .execute();
    }

    public static void del(String url) {
        SQLite.delete(ChapterDownloadInfo.class)
                .where(ChapterDownloadInfo_Table.url.eq(url))
                .and(ChapterDownloadInfo_Table.uId.eq(UserManager.getUserId()))
                .async()
                .execute();
    }
}
