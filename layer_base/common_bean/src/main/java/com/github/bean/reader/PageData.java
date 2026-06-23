package com.github.bean.reader;

import com.github.bean.database.table.BookChapter;
import com.github.bean.enumclz.PageType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 页面数据
 */
public class PageData {


////////////////////////////////////        页面信息         /////////////////////////////////////////


    /** 章节信息 **/
    @Nullable
    public BookChapter bookChapter;

    /** 书籍名称 **/
    public String bookTitle;

    /** 章节名称 **/
    public String chapterTitle;

    /** 页面类型 **/
    public PageType pageType = PageType.NORMAL;

    /** 页面index **/
    public int pageIndex;

    /** 页面加载状态 **/
    public int status;

    /** 广告data **/
    @Nullable
    public Object nativeAdv;

////////////////////////////////////         行信息         /////////////////////////////////////////


    /** 当前 lines 中为 title 的行数 **/
    public int titleLines;

    /** 起止段落 ID **/
    public int startParaId, endParaId;

    /** 起止元素 **/
    public int startElement, endElement;


    public List<LineInfo> lines = new ArrayList<>();





    public PageData (){

    }

    public static boolean isHeadPage(PageData page) {
        return page!=null && page.pageType == PageType.HEAD_PAGE;
    }

    public static PageData buildPageData(int pageIndex, String bookTitle, String chapterTitle, List<LineInfo> lines, int titleLines, BookChapter bookChapter, int status){
        PageData page = new PageData();
        page.pageIndex = pageIndex;             // 当前页索引
        page.bookTitle = bookTitle;             // 顶部显示的书名
        page.chapterTitle = chapterTitle;       // 顶部显示的章节标题
        page.lines = new ArrayList<>(lines);    // 当前页的所有行
        page.titleLines = titleLines;           // 标题行数
        page.bookChapter = bookChapter;         // 所属章节
        page.status = status;                   // 页面状态
        return page;
    }

    public int getChapterIndex(){
        return bookChapter == null ? 0 : bookChapter.getIndex();
    }
}