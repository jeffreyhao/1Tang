package com.github.bean.database.table;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baidu.baselibrary.util.UserManager;
import com.baidu.baselibrary.util.json.GsonUtils;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.github.bean.model.SearchPrompt;
import com.github.bean.model.SearchPromptItem;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.io.Serializable;
import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2018/7/6 下午3:56.
 * 
 * Description: BookModel
 */
@Table(database = AppDatabase.class)
public class BookInfo extends BaseModel implements Parcelable, SearchPromptItem, Serializable {

    // 营销类型
    public static final int MARKET_TYPE_NONE                = 0;    // 0-无营销类型
    public static final int MARKET_TYPE_RECOMMEND           = 1;    // 1-推荐书籍
    public static final int MARKET_TYPE_EXCLUSIVE           = 2;    // 2-独家推荐书籍
    public static final int MARKET_TYPE_FREE                = 3;    // 3-免费书籍
    public static final int MARKET_TYPE_DISCOUNT            = 4;    // 4-折扣书籍


    public static int STATUS_SERIAL_DEAFULT = 0;
    public static int STATUS_SERIAL_COMPELETED = 1;

    //书籍bookId
    @TableFieldV20
    @PrimaryKey
    private String book_id;

    @TableFieldV20(name = "com_book_id")
    @Column(name = "com_book_id")
    private String com_book_id;//组合书籍id  1Uxxx

    @Column(name = "external_book_id")
    private String external_cp_book_id;

    @TableFieldV20(name = "user_id")
    @Column(name = "user_id")
    private String userId;

    @TableFieldV20(name = "copyright")
    @Column(name = "copyright")
    private String copyright;

    //创建时间
    @TableFieldV20(name = "created")
    @Column(name = "created")
    private String created;

    //描述
    @TableFieldV20(name = "description")
    @Column(name = "description")
    private String description;

    //主编说
    @TableFieldV20(name = "recDesc")
    @Column(name = "recDesc")
    private String recDesc;

    //bookCover
    @TableFieldV20(name = "cover")
    @Column(name = "cover")
    private String cover;

    //书籍状态
    @TableFieldV20(name = "status")
    @Column(name = "status")
    private int finished;

    //书名
    @TableFieldV20(name = "title")
    @Column(name = "title")
    private String title;

    //上次阅读，收藏时间 用于书架排序
    @TableFieldV20(name = "updated")
    @Column(name = "updated")
    private String updated;

    //字数(废弃)
    @TableFieldV20(name = "wordCount")
    @Column(name = "wordCount")
    private String wordCount;

    //字数
    @TableFieldV20(name = "words")
    @Column(name = "words")
    private int words;

    // 最新更新章节名
    @TableFieldV20(name = "latestChapterTip")
    @Column(name = "latestChapterTip")
    private String latestChapterTip;

    private String updatedFormated;
    //最后更新章节的title

    @TableFieldV20(name = "lastChapter")
    @Column(name = "lastChapter")
    private String lastChapterJson;

    //最后更新的章节信息
    private BookChapter latestChapter;


    @SerializedName("num")
    private int pv;

    @TableFieldV20(name = "isUpdate")
    @Column(name = "isUpdate")
    private int isUpdate;

    private int tipTotal;

    @TableFieldV20(name = "isAd")
    @Column(name = "isAd")
    public boolean isAd;

    @TableFieldV20(name = "sale_type")
    @Column(name = "sale_type")
    public int sale_type;

    @TableFieldV20(name = "adds_count")
    @Column(name = "adds_count")
    private int adds_count;

    @TableFieldV20(name = "chapter_count")
    @Column(name = "chapter_count")
    private int chapter_count;

    @TableFieldV20(name = "views_count")
    @Column(name = "views_count")
    private int views_count;

    @TableFieldV20(name = "rate")
    @Column(name = "rate")
    private int rating;

    @TableFieldV20(name = "rank")
    @Column(name = "rank")
    private int rank;

    @TableFieldV20(name = "category")
    @Column(name = "category")
    private String category;

    @TableFieldV20(name = "score")
    @Column(name = "score")
    private String score;

    @TableFieldV20(name = "author_id")
    @Column(name = "author_id")
    private String author_id;

    @TableFieldV20(name = "author_name")
    @Column(name = "author_name")
    private String author_name;

    @TableFieldV20(name = "category_id")
    @Column(name = "category_id")
    private String book_category_id;

    @TableFieldV20(name = "category_name")
    @Column(name = "category_name")
    private String book_category;

    private String pre_book_updated;
    //最后更新时间
    private String latest_book_updated;

    @TableFieldV20(name = "create_time")
    @Column(name = "create_time")
    private String create_time;

    @TableFieldV20(name = "update_time")
    @Column(name = "update_time")
    private String update_time;

    @TableFieldV20(name = "intro")
    @Column(name = "intro")
    private String intro;


    @Column(name = "billing_begin")
    private int billing_begin;


    private String first_chapter_id;
    private String first_chapter_title;
    private String first_chapter_content;

    private String first_chapter_url;
    private String first_chapter_update_time;
    private int show_author_name;
    private List<BookChapter> chapters;
    private int shelves_status; // 0 下线 1 已上线 2 强制下线
    private boolean isInit;//是否是内置书
    private int subscribe;//是否收藏

    @TableFieldV20(name = "marketing_type")
    @Column(name = "marketing_type")
    private int marketing_type;//营销类型 0无营销类型 1推荐书籍 2 独家推荐书籍 3 免费书籍 4 折扣书籍

    @TableFieldV20(name = "discount")
    @Column(name = "discount")
    private int discount;//折扣百分比
    @Column(name = "is_vip")
    private int is_vip;

    private boolean isRecommendBook;
    private String language;


    //作者
    private List<String> authors;
    private List<String> categories;
    private List<String> tags;

    @Column(name = "is_short_story")
    public int is_short_story;



    public static void hasRecord(String bookId, String uid, QueryTransaction.QueryResultSingleCallback<BookInfo> callback) {
        SQLite.select()
                .from(BookInfo.class)
                .where(OperatorGroup.clause()
                        .and(BookInfo_Table.book_id.eq(bookId))
                        .or(BookInfo_Table.com_book_id.eq(bookId)))
                .and(BookInfo_Table.user_id.eq(uid))
                .async()
                .querySingleResultCallback(callback)
                .execute();
    }

    /**
     * 取
     *
     * @param uid
     * @return
     */
    public static List<BookInfo> loadAllUserBookSync(String uid) {
        List<BookInfo> queryResult = SQLite.select().from(BookInfo.class).where(BookInfo_Table.user_id.eq(uid)).queryList();
        for (BookInfo info : queryResult) {
            if (!TextUtils.isEmpty(info.lastChapterJson)) {
                info.latestChapter = GsonUtils.fromJson(info.lastChapterJson, BookChapter.class);
            }
        }
        return queryResult;
    }

    public static BookInfo querySingle(String bookId) {
        return SQLite
                .select()
                .from(BookInfo.class)
                .where(OperatorGroup.clause()
                        .and(BookInfo_Table.book_id.eq(bookId))
                        .or(BookInfo_Table.com_book_id.eq(bookId)))
                .and(BookInfo_Table.user_id.eq(UserManager.getUserId()))
                .querySingle();
    }

    public static void saveAsync(BookInfo record) {
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(databaseWrapper -> {
            record.save();
        });
    }

    public int getIs_short_story() {
        return is_short_story;
    }

    public void setIs_short_story(int is_short_story) {
        this.is_short_story = is_short_story;
    }

    @Override
    public boolean save() {
        if (latestChapter != null) {
            lastChapterJson = GsonUtils.toJson(latestChapter);
        }
        return super.save();
    }

    @Override
    public boolean update() {
        if (latestChapter != null) {
            lastChapterJson = GsonUtils.toJson(latestChapter);
        }
        return super.update();
    }

    public static void deleteAsync(BookInfo record) {
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(databaseWrapper -> {
            record.delete();
        });
    }

    public BookInfo() {
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecDesc() {
        return recDesc;
    }

    public void setRecDesc(String recDesc) {
        this.recDesc = recDesc;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    public String getUpdatedFormated() {
        return updatedFormated;
    }

    public void setUpdatedFormated(String updatedFormated) {
        this.updatedFormated = updatedFormated;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getTipTotal() {
        return tipTotal;
    }

    public void setTipTotal(int tipTotal) {
        this.tipTotal = tipTotal;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    public int getSale_type() {
        return sale_type;
    }

    public void setSale_type(int sale_type) {
        this.sale_type = sale_type;
    }

    public int getAdds_count() {
        return adds_count;
    }

    public void setAdds_count(int adds_count) {
        this.adds_count = adds_count;
    }

    public int getChapter_count() {
        return chapter_count;
    }

    public void setChapter_count(int chapter_count) {
        this.chapter_count = chapter_count;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getCom_book_id() {
        return com_book_id;
    }

    public void setCom_book_id(String com_book_id) {
        this.com_book_id = com_book_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getBook_category_id() {
        return book_category_id;
    }

    public void setBook_category_id(String book_category_id) {
        this.book_category_id = book_category_id;
    }

    public String getBook_category() {
        return book_category;
    }

    public void setBook_category(String book_category) {
        this.book_category = book_category;
    }

    public String getPre_book_updated() {
        return pre_book_updated;
    }

    public void setPre_book_updated(String pre_book_updated) {
        this.pre_book_updated = pre_book_updated;
    }

    public String getLatest_book_updated() {
        return latest_book_updated;
    }

    public void setLatest_book_updated(String latest_book_updated) {
        this.latest_book_updated = latest_book_updated;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getLatestChapterTip() {
        return latestChapterTip;
    }

    public void setLatestChapterTip(String latestChapterTip) {
        this.latestChapterTip = latestChapterTip;
    }

    public String getAuthorNameStr() {
        StringBuilder sb = new StringBuilder();
        if (authors != null && authors.size() > 0) {
            for (String name : authors) {
                if (name.equals(authors.get(0)))
                    sb.append(name);
                else
                    sb.append(",").append(name);
            }
        }
        return sb.toString();
    }


    public boolean getBookIsUpdate() {
        if (isUpdate == 1) {
            return true;
        }
        return false;
    }

    public void setLastRead(String timestamp) {
        this.updated = timestamp;
    }

    public String getBookStatusStr() {
        if (finished == STATUS_SERIAL_COMPELETED) {
            return "完结";
        } else if (finished == STATUS_SERIAL_DEAFULT) {
            return "连载中";
        }
        return "";
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public String getLastChapterJson() {
        return lastChapterJson;
    }

    public BookInfo(BookChapter latestChapter) {
        this.latestChapter = latestChapter;
    }

    public void setLastChapterJson(String lastChapterJson) {
        this.lastChapterJson = lastChapterJson;
    }

    public BookChapter getLatestChapter() {
        return latestChapter;
    }

    public void setLatestChapter(BookChapter latestChapter) {
        this.latestChapter = latestChapter;
    }

    public String getFirst_chapter_id() {
        return first_chapter_id;
    }

    public void setFirst_chapter_id(String first_chapter_id) {
        this.first_chapter_id = first_chapter_id;
    }

    public String getFirst_chapter_title() {
        return first_chapter_title;
    }

    public void setFirst_chapter_title(String first_chapter_title) {
        this.first_chapter_title = first_chapter_title;
    }

    public String getFirst_chapter_content() {
        return first_chapter_content;
    }

    public void setFirst_chapter_content(String first_chapter_content) {
        this.first_chapter_content = first_chapter_content;
    }

    public String getFirst_chapter_url() {
        return first_chapter_url;
    }

    public void setFirst_chapter_url(String first_chapter_url) {
        this.first_chapter_url = first_chapter_url;
    }

    public String getFirst_chapter_update_time() {
        return first_chapter_update_time;
    }

    public void setFirst_chapter_update_time(String first_chapter_update_time) {
        this.first_chapter_update_time = first_chapter_update_time;
    }

    public int getShow_author_name() {
        return show_author_name;
    }

    public void setShow_author_name(int show_author_name) {
        this.show_author_name = show_author_name;
    }

    public List<BookChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<BookChapter> chapters) {
        this.chapters = chapters;
    }

    public int getShelves_status() {
        return shelves_status;
    }

    public void setShelves_status(int shelves_status) {
        this.shelves_status = shelves_status;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getBilling_begin() {
        return billing_begin;
    }

    public void setBilling_begin(int billing_begin) {
        this.billing_begin = billing_begin;
    }

    public int getMarketing_type() {
        return marketing_type;
    }

    public void setMarketing_type(int marketing_type) {
        this.marketing_type = marketing_type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isRecommendBook() {
        return isRecommendBook;
    }

    public void setRecommendBook(boolean recommendBook) {
        isRecommendBook = recommendBook;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getExternal_cp_book_id() {
        return external_cp_book_id;
    }

    public void setExternal_cp_book_id(String external_cp_book_id) {
        this.external_cp_book_id = external_cp_book_id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.book_id);
        dest.writeString(this.com_book_id);
        dest.writeString(this.external_cp_book_id);
        dest.writeString(this.userId);
        dest.writeString(this.copyright);
        dest.writeString(this.created);
        dest.writeString(this.description);
        dest.writeString(this.recDesc);
        dest.writeString(this.cover);
        dest.writeInt(this.finished);
        dest.writeString(this.title);
        dest.writeString(this.updated);
        dest.writeString(this.wordCount);
        dest.writeString(this.latestChapterTip);
        dest.writeString(this.updatedFormated);
        dest.writeString(this.lastChapterJson);
        dest.writeParcelable(this.latestChapter, flags);
        dest.writeInt(this.pv);
        dest.writeInt(this.isUpdate);
        dest.writeInt(this.tipTotal);
        dest.writeByte(this.isAd ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sale_type);
        dest.writeInt(this.adds_count);
        dest.writeInt(this.chapter_count);
        dest.writeInt(this.views_count);
        dest.writeInt(this.rating);
        dest.writeInt(this.rank);
        dest.writeString(this.category);
        dest.writeString(this.score);
        dest.writeString(this.author_id);
        dest.writeString(this.author_name);
        dest.writeString(this.book_category_id);
        dest.writeString(this.book_category);
        dest.writeString(this.pre_book_updated);
        dest.writeString(this.latest_book_updated);
        dest.writeString(this.create_time);
        dest.writeString(this.update_time);
        dest.writeString(this.intro);
        dest.writeString(this.first_chapter_id);
        dest.writeString(this.first_chapter_title);
        dest.writeString(this.first_chapter_content);
        dest.writeString(this.first_chapter_url);
        dest.writeString(this.first_chapter_update_time);
        dest.writeInt(this.show_author_name);
        dest.writeTypedList(this.chapters);
        dest.writeStringList(this.authors);
        dest.writeStringList(this.categories);
        dest.writeStringList(this.tags);
        dest.writeByte(this.isInit ? (byte) 1 : (byte) 0);
        dest.writeInt(this.subscribe);
        dest.writeInt(this.marketing_type);
        dest.writeInt(this.discount);
        dest.writeString(this.language);
        dest.writeInt(this.is_vip);
        dest.writeInt(this.is_short_story);
        dest.writeInt(this.billing_begin);
    }

    public void readFromParcel(Parcel source) {
        this.book_id = source.readString();
        this.com_book_id = source.readString();
        this.external_cp_book_id = source.readString();
        this.userId = source.readString();
        this.copyright = source.readString();
        this.created = source.readString();
        this.description = source.readString();
        this.recDesc = source.readString();
        this.cover = source.readString();
        this.finished = source.readInt();
        this.title = source.readString();
        this.updated = source.readString();
        this.wordCount = source.readString();
        this.latestChapterTip = source.readString();
        this.updatedFormated = source.readString();
        this.lastChapterJson = source.readString();
        this.latestChapter = source.readParcelable(BookChapter.class.getClassLoader());
        this.pv = source.readInt();
        this.isUpdate = source.readInt();
        this.tipTotal = source.readInt();
        this.isAd = source.readByte() != 0;
        this.sale_type = source.readInt();
        this.adds_count = source.readInt();
        this.chapter_count = source.readInt();
        this.views_count = source.readInt();
        this.rating = source.readInt();
        this.rank = source.readInt();
        this.category = source.readString();
        this.score = source.readString();
        this.author_id = source.readString();
        this.author_name = source.readString();
        this.book_category_id = source.readString();
        this.book_category = source.readString();
        this.pre_book_updated = source.readString();
        this.latest_book_updated = source.readString();
        this.create_time = source.readString();
        this.update_time = source.readString();
        this.intro = source.readString();
        this.first_chapter_id = source.readString();
        this.first_chapter_title = source.readString();
        this.first_chapter_content = source.readString();
        this.first_chapter_url = source.readString();
        this.first_chapter_update_time = source.readString();
        this.show_author_name = source.readInt();
        this.chapters = source.createTypedArrayList(BookChapter.CREATOR);
        this.authors = source.createStringArrayList();
        this.categories = source.createStringArrayList();
        this.tags = source.createStringArrayList();
        this.isInit = source.readByte() != 0;
        this.subscribe = source.readInt();
        this.marketing_type = source.readInt();
        this.discount = source.readInt();
        this.language = source.readString();
        this.is_vip = source.readInt();
        this.is_short_story = source.readInt();
        this.billing_begin = source.readInt();
    }

    protected BookInfo(Parcel in) {
        this.book_id = in.readString();
        this.com_book_id = in.readString();
        this.external_cp_book_id = in.readString();
        this.userId = in.readString();
        this.copyright = in.readString();
        this.created = in.readString();
        this.description = in.readString();
        this.recDesc = in.readString();
        this.cover = in.readString();
        this.finished = in.readInt();
        this.title = in.readString();
        this.updated = in.readString();
        this.wordCount = in.readString();
        this.latestChapterTip = in.readString();
        this.updatedFormated = in.readString();
        this.lastChapterJson = in.readString();
        this.latestChapter = in.readParcelable(BookChapter.class.getClassLoader());
        this.pv = in.readInt();
        this.isUpdate = in.readInt();
        this.tipTotal = in.readInt();
        this.isAd = in.readByte() != 0;
        this.sale_type = in.readInt();
        this.adds_count = in.readInt();
        this.chapter_count = in.readInt();
        this.views_count = in.readInt();
        this.rating = in.readInt();
        this.rank = in.readInt();
        this.category = in.readString();
        this.score = in.readString();
        this.author_id = in.readString();
        this.author_name = in.readString();
        this.book_category_id = in.readString();
        this.book_category = in.readString();
        this.pre_book_updated = in.readString();
        this.latest_book_updated = in.readString();
        this.create_time = in.readString();
        this.update_time = in.readString();
        this.intro = in.readString();
        this.first_chapter_id = in.readString();
        this.first_chapter_title = in.readString();
        this.first_chapter_content = in.readString();
        this.first_chapter_url = in.readString();
        this.first_chapter_update_time = in.readString();
        this.show_author_name = in.readInt();
        this.chapters = in.createTypedArrayList(BookChapter.CREATOR);
        this.authors = in.createStringArrayList();
        this.categories = in.createStringArrayList();
        this.tags = in.createStringArrayList();
        this.isInit = in.readByte() != 0;
        this.subscribe = in.readInt();
        this.marketing_type = in.readInt();
        this.discount = in.readInt();
        this.language = in.readString();
        this.is_vip = in.readInt();
        this.is_short_story = in.readInt();
        this.billing_begin = in.readInt();
    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };



    public String getNonNullBookId(){
        return book_id == null ? "" : book_id;
    }


    public String getNonNullChapterUrl() {
        return first_chapter_url == null ? "" : first_chapter_url;
    }



    @Override
    public int getItemType() {
        return SearchPrompt.TYPE_BOOK;
    }

    @Override
    public String getItemText() {
        return getTitle();
    }

    public String getBid() {
        return TextUtils.isEmpty(com_book_id) ? book_id : com_book_id;
    }


    public boolean isShortBook(){
        return is_short_story == 1;
    }
}
