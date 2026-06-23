package com.github.bean.database.table;

import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = AppDatabase.class)
public class ReadingRecord extends BaseModel implements Serializable {


    /**
     *  书籍id
     */
    @TableFieldV20
    @PrimaryKey
    @Column
    public String bookId = "";

    /**
     *  1U书籍id
     */
    @TableFieldV20
    @Column
    public String comBookId = "";

    /**
     *  章节index
     */
    @TableFieldV20
    @Column
    public int chapterPos = 0;

    /**
     *  章节id。
     *  注意：数据库V25及之前，存的是cid
     */
    @TableFieldV20
    @Column
    public String chapterId = "";

    @TableFieldV20
    @Column
    public int startElement = 0;

    @TableFieldV20
    @Column
    public int endElement = 0;

    /**
     *  阅读进度
     */
    @TableFieldV20
    @Column
    public int progress = 0;

    /**
     *  距离顶部偏移
     */
    @TableFieldV20
    @Column
    public int topOffset = 0;

    /**
     *  章节阅读百分比
     */
    @TableFieldV20
    @Column
    public double chapterProgress = 0.0;

    /**
     *  计费开始的章节
     */
    @Column
    public int startFeeChapter = 0;

    /**
     *  动态唯一值：cid
     */
    @Column
    public String cid = "";

    @Column(name = "is_short_story")
    public int is_short_story;


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getComBookId() {
        return comBookId;
    }

    public void setComBookId(String comBookId) {
        this.comBookId = comBookId;
    }

    public int getChapterPos() {
        return chapterPos;
    }

    public void setChapterPos(int chapterPos) {
        this.chapterPos = chapterPos;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public int getStartElement() {
        return startElement;
    }

    public void setStartElement(int startElement) {
        this.startElement = startElement;
    }

    public int getEndElement() {
        return endElement;
    }

    public void setEndElement(int endElement) {
        this.endElement = endElement;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTopOffset() {
        return topOffset;
    }

    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }

    public double getChapterProgress() {
        return chapterProgress;
    }

    public void setChapterProgress(double chapterProgress) {
        this.chapterProgress = chapterProgress;
    }

    public int getStartFeeChapter() {
        return startFeeChapter;
    }

    public void setStartFeeChapter(int startFeeChapter) {
        this.startFeeChapter = startFeeChapter;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getIs_short_story() {
        return is_short_story;
    }

    public void setIs_short_story(int is_short_story) {
        this.is_short_story = is_short_story;
    }

    public boolean isShortBook(){
        return is_short_story == 1;
    }
}
