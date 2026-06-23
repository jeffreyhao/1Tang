package com.github.bean.database.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.bean.database.annotation.TableFieldV20;
import com.github.bean.model.ChapterIntroBean;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 书籍的每个章节model
 */
public class BookChapter extends BaseModel implements Parcelable, Serializable {


    public String book_id;
    private String com_book_id;

    @TableFieldV20
    @Column
    private String title;

    private String url;

    @TableFieldV20
    @Column
    private int need_pay;

    // 付费false  免费true
    @TableFieldV20
    @Column
    private boolean free;

    /**
     * cid是个动态的章节唯一值。
     *
     *  <br>    1. 该字段可能会在后台换掉，这里只用来记录或者打印。
     *  <br>    2. 业务用的章节id请使用 {@link #chapter_id}
     */
    @TableFieldV20
    @Column
    private String cid;

    /**
     * 章节id
     */
    @TableFieldV20
    @Column
    private String chapter_id;

    @TableFieldV20
    @Column
    private int total;//总数量

    @TableFieldV20
    @Column
    private String create_time;

    @TableFieldV20
    @Column
    private String update_time;

    @TableFieldV20
    @Column
    private String updatedFormated;

    private int words;

    private String txt;
    private String price;

    // 记录数据库
    public ChapterIntroBean introBean;
    private int index;

    public boolean isLastChapter;


    public BookChapter() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNeed_pay() {
        return need_pay;
    }

    public void setNeed_pay(int need_pay) {
        this.need_pay = need_pay;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ChapterIntroBean getIntroBean() {
        return introBean;
    }

    public void setIntroBean(ChapterIntroBean introBean) {
        this.introBean = introBean;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.book_id);
        dest.writeString(this.com_book_id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.need_pay);
        dest.writeByte(this.free ? (byte) 1 : (byte) 0);
        dest.writeString(this.cid);
        dest.writeString(this.chapter_id);
        dest.writeInt(this.total);
        dest.writeString(this.create_time);
        dest.writeString(this.update_time);
        dest.writeInt(this.words);
        dest.writeString(this.txt);
        dest.writeInt(this.index);
        dest.writeString(this.price);
    }

    public void readFromParcel(Parcel source) {
        this.book_id = source.readString();
        this.com_book_id = source.readString();
        this.title = source.readString();
        this.url = source.readString();
        this.need_pay = source.readInt();
        this.free = source.readByte() != 0;
        this.cid = source.readString();
        this.chapter_id = source.readString();
        this.total = source.readInt();
        this.create_time = source.readString();
        this.update_time = source.readString();
        this.words = source.readInt();
        this.txt = source.readString();
        this.index = source.readInt();
        this.price = source.readString();
    }

    protected BookChapter(Parcel in) {
        this.book_id = in.readString();
        this.com_book_id = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.need_pay = in.readInt();
        this.free = in.readByte() != 0;
        this.cid = in.readString();
        this.chapter_id = in.readString();
        this.total = in.readInt();
        this.create_time = in.readString();
        this.update_time = in.readString();
        this.words = in.readInt();
        this.txt = in.readString();
        this.index = in.readInt();
        this.price = in.readString();
    }

    public static final Creator<BookChapter> CREATOR = new Creator<BookChapter>() {
        @Override
        public BookChapter createFromParcel(Parcel source) {
            return new BookChapter(source);
        }

        @Override
        public BookChapter[] newArray(int size) {
            return new BookChapter[size];
        }
    };


    public boolean isLoading = false;
    public BookInfo bookInfo;
    public List<BookInfo> recommendBooks;
    public boolean isHeadPage;
    public boolean isRecommendPage;//是否是书尾推荐
    public boolean needRefresh;//是否需要刷新数据

    public boolean loadHeadPageError;//是否加载扉页失败

    public boolean loadError;//章节加载失败

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public boolean needPay(){
        return introBean != null
                && introBean.needPay
                && introBean.notEnough;
    }

    public boolean isEmpty(){
        return chapter_id == null;
    }

}