package com.github.bean.database.table;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baidu.baselibrary.log.ALog;
import com.base.api.GlobalContext;
import com.base.net.cache.ACache;
import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2018/8/6 下午1:39.
 * 
 * Description: 章节列表 目录
 */
@Table(database = AppDatabase.class)
public class CatalogList extends BaseModel implements Parcelable {
    private List<BookChapter> chapterList;

    @TableFieldV20
    //章节列表
    @PrimaryKey
    private String bid;

    @Column(name = "comBookId")
    private String comBookId;

    @TableFieldV20(name = "chapters")
    @Column(name = "chapters")
    private String chapters;

    /**
     * 保存章节列表
     *
     * @param catalogList
     */
    public static void saveAsync(CatalogList catalogList) {
        if (catalogList != null && catalogList.chapterList != null && catalogList.chapterList.size() > 0) {
            try {
                catalogList.chapters = new Gson().toJson(catalogList.chapterList, new TypeToken<List<BookChapter>>() {
                }.getType());
            } catch (Exception e) {
                ALog.exception(e);
            }
            FlowManager.getDatabase(AppDatabase.class).executeTransaction(databaseWrapper -> {
                boolean saved = catalogList.save();
                if(!saved) {
                    ALog.text("catalogList未保存成功");
                }
            });
        }

    }

    /**
     * 查询章节目录
     *
     * @param bid
     * @return
     */
    public List<BookChapter> queryCatalogChapters(String bid) {
        CatalogList catalogList = SQLite.select()
                .from(CatalogList.class)
                .where(CatalogList_Table.bid.eq(bid))
                .querySingle();
        Type type = new TypeToken<List<BookChapter>>() {
        }.getType();
        catalogList.chapterList = new Gson().fromJson(chapters, type);
        return catalogList.chapterList;
    }

    /**
     * 查询是否有CatalogList
     *
     * @param bookId
     * @param callback
     */
    public static void hasRecord(String bookId,  QueryTransaction.QueryResultSingleCallback<CatalogList> callback) {
        hasRecord(bookId, null, callback);
    }

    public static void hasRecord(String bookId, String comBookId, QueryTransaction.QueryResultSingleCallback<CatalogList> callback) {
        if (TextUtils.isEmpty(comBookId)) {
            SQLite.select()
                    .from(CatalogList.class)
                    .where(CatalogList_Table.bid.eq(bookId))
                    .or(CatalogList_Table.comBookId.eq(bookId))
                    .async()
                    .querySingleResultCallback(callback)
                    .execute();
        } else {
            SQLite.select()
                    .from(CatalogList.class)
                    .where(CatalogList_Table.bid.eq(bookId))
                    .or(CatalogList_Table.comBookId.eq(bookId))
                    .or(CatalogList_Table.bid.eq(comBookId))
                    .or(CatalogList_Table.comBookId.eq(comBookId))
                    .async()
                    .querySingleResultCallback(callback)
                    .execute();
        }
    }

    /**
     * 清除缓存列表
     */
    public static void clear(){
        Delete.table(CatalogList.class);
    }

    public static void checkClear(){
        try{
            if(GlobalContext.getContext()!=null){
                String json = ACache.get(GlobalContext.getContext()).getAsString("bookChapter_dir_version");
                if(TextUtils.isEmpty(json)) {
                    clear();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public List<BookChapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<BookChapter> chapterList) {
        this.chapterList = chapterList;
    }

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getComBookId() {
        return comBookId;
    }

    public void setComBookId(String comBookId) {
        this.comBookId = comBookId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.chapterList);
        dest.writeString(this.bid);
        dest.writeString(this.comBookId);
        dest.writeString(this.chapters);
    }

    public CatalogList() {
    }

    protected CatalogList(Parcel in) {
        this.chapterList = in.createTypedArrayList(BookChapter.CREATOR);
        this.bid = in.readString();
        this.comBookId = in.readString();
        this.chapters = in.readString();
    }

    public static final Creator<CatalogList> CREATOR = new Creator<CatalogList>() {
        @Override
        public CatalogList createFromParcel(Parcel source) {
            return new CatalogList(source);
        }

        @Override
        public CatalogList[] newArray(int size) {
            return new CatalogList[size];
        }
    };
}
