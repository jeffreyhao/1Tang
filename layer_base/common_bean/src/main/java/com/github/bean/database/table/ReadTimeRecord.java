package com.github.bean.database.table;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.crypto.CipherHelper;
import com.baidu.baselibrary.util.UserManager;
import com.base.util.thread.ExecutorsUtils;
import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tencent.common.util.TimeUtils;

import java.util.List;

@Table(database = AppDatabase.class)
public class ReadTimeRecord extends BaseModel {

    @TableFieldV20
    @PrimaryKey(autoincrement = true)
    @Column(name = "_id")
    public long id = 0;

    @TableFieldV20
    @Column
    public String uid = "";

    @TableFieldV20
    @Column
    public String bid = null;

    @TableFieldV20
    @Column
    public String startTime = "";

    @TableFieldV20
    @Column
    public int duration = 0; // min

    @TableFieldV20
    @Column
    public String content = ""; // 上报真实的内容


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean save() {
        content = CipherHelper.encryptWithIv(startTime + "," + duration);
        return super.save();
    }

    // 静态方法相当于Kotlin的companion object
    public static List<ReadTimeRecord> getAllRecords() {
        return SQLite.select()
                .from(ReadTimeRecord.class)
                .where(ReadTimeRecord_Table.uid.eq(UserManager.getUserId()))
                .queryList();
    }

    public static void saveReadTimeRecord(final String bId, final long startTime) {
        ExecutorsUtils.getInstance().getAppExecutors().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (bId == null || bId.isEmpty()) {
                    return;
                }
                long time = System.currentTimeMillis() - startTime;
                if (time < 10 * 1000) {
                    ALog.textSingle("ReadTimeRecord", "saveReadTimeRecord", "saveReadTimeRecord, return --> time: " + (int) (time / 1000));
                    return;
                }
                if (time > 480 * 1000) {
                    time = 480 * 1000;
                }
                ReadTimeRecord record = new ReadTimeRecord();
                record.startTime = TimeUtils.millis2String(startTime);
                record.duration = (int) (time / 1000);
                record.uid = UserManager.getUserId();
                record.bid = bId;
                record.save();
                ALog.textSingle("ReadTimeRecord", "saveReadTimeRecord", "saveReadTimeRecord, duration: " + record.duration);
            }
        });
    }

    public static void del(Long id) {
        if (id == null) return;
        SQLite.delete()
                .from(ReadTimeRecord.class)
                .where(ReadTimeRecord_Table._id.eq(id))
                .and(ReadTimeRecord_Table.uid.eq(UserManager.getUserId()))
                .async()
                .execute();
    }

    public static void delByMaxId(long maxId) {
        SQLite.delete()
                .from(ReadTimeRecord.class)
                .where(ReadTimeRecord_Table._id.lessThanOrEq(maxId))
                .async()
                .execute();
    }

    public static void delByBookId(String bookId) {
        if (bookId == null || bookId.isEmpty()) return;
        SQLite.delete()
                .from(ReadTimeRecord.class)
                .where(ReadTimeRecord_Table.bid.eq(bookId))
                .async()
                .execute();
    }
}
