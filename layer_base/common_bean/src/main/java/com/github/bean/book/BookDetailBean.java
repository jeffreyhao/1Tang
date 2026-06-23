package com.github.bean.book;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookDetailBean {

    @SerializedName("book_id")
    private String bookId;
    @SerializedName("title")
    private String title;
    @SerializedName("intro")
    private String intro;
    @SerializedName("recommend")
    private String recommend;
    @SerializedName("cover")
    private String cover;
    @SerializedName("adds_count")
    private int addsCount;
    @SerializedName("views_count")
    private int viewsCount;
    @SerializedName("rating")
    private String rating;
    @SerializedName("score")
    private String score;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("book_category_id")
    private String bookCategoryId;
    @SerializedName("book_category")
    private String bookCategory;
    @SerializedName("words")
    private String words;
    @SerializedName("chapter_count")
    private int chapterCount;
    @SerializedName("language")
    private String language;
    @SerializedName("pre_book_updated")
    private String preBookUpdated;
    @SerializedName("latest_book_updated")
    private String latestBookUpdated;
    @SerializedName("sale_type")
    private int saleType;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("update_time")
    private String updateTime;
    @SerializedName("subscribe")
    private int subscribe;
    @SerializedName("tags")
    private List<String> tags;
    private int finished;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getAddsCount() {
        return addsCount;
    }

    public void setAddsCount(int addsCount) {
        this.addsCount = addsCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookCategoryId() {
        return bookCategoryId;
    }

    public void setBookCategoryId(String bookCategoryId) {
        this.bookCategoryId = bookCategoryId;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(int chapterCount) {
        this.chapterCount = chapterCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreBookUpdated() {
        return preBookUpdated;
    }

    public void setPreBookUpdated(String preBookUpdated) {
        this.preBookUpdated = preBookUpdated;
    }

    public String getLatestBookUpdated() {
        return latestBookUpdated;
    }

    public void setLatestBookUpdated(String latestBookUpdated) {
        this.latestBookUpdated = latestBookUpdated;
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
