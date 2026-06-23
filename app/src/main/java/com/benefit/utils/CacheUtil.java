package com.benefit.utils;

import android.text.TextUtils;

import com.baidu.baselibrary.util.UserManager;
import com.baidu.baselibrary.util.glide.GlideUtils;
import com.base.api.GlobalContext;
import com.base.global.PreferencesUtil;
import com.base.net.ApiBase;
import com.base.net.cache.ACache;
import com.base.util.collection.ListUtil;
import com.base.util.content.GsonUtil;
import com.base.util.thread.ExecutorsUtils;
import com.benefit.novelverse.utils.Constant;
import com.github.bean.app.FBReportResponseBean;
import com.github.bean.app.LanguageItemBean;
import com.github.bean.book.UploadPageRecordBean;
import com.github.bean.database.table.BookInfo;
import com.github.bean.model.BookStore;
import com.github.bean.operation.RankListBean;
import com.github.bean.operation.RewardSignBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tencent.common.util.TimeUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheUtil {
    public static void addShelfDelRecords(List<String> delBookIds) {
        if(GlobalContext.getContext()!=null && delBookIds!=null) {
            List<String> shelfDelIdRecords = getShelfDelIdRecords();
            if(ListUtil.isNotEmpty(shelfDelIdRecords)) {
                shelfDelIdRecords.addAll(delBookIds);
            }else{
                shelfDelIdRecords = delBookIds;
            }
            ACache.get(GlobalContext.getContext()).put(UserManager.getUserId() + "_delShelf", GsonUtil.bean2json(shelfDelIdRecords));
        }
    }

    public static List<String> getShelfDelIdRecords() {
        List<String> ids = null;
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString(UserManager.getUserId() + "_delShelf");
            if(!TextUtils.isEmpty(json)) {
                ids = GsonUtil.parseJsonToList(json);
            }
        }
        return ids==null? new ArrayList<>() : ids;
    }

    public static void removeShelfDelRecord(String id) {
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString(UserManager.getUserId() + "_delShelf");
            List<String> list = GsonUtil.parseJsonToList(json);
            if(list!=null) {
                list.remove(id);
                ACache.get(GlobalContext.getContext()).put(UserManager.getUserId() + "_delShelf", GsonUtil.bean2json(list));
            }
        }
    }

    public static void addBookUpdateTime(String bookId, String updateTime) {
        if(TextUtils.isEmpty(bookId)||TextUtils.isEmpty(updateTime)) return;
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("bookUpdate");
            if(TextUtils.isEmpty(json)) {
                Map<String,String> map = new HashMap<>();
                map.put(bookId,updateTime);
                ACache.get(GlobalContext.getContext()).put("bookUpdate",GsonUtil.bean2json(map));
            }else {
                Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                if(map!=null) {
                    map.put(bookId,updateTime);
                    ACache.get(GlobalContext.getContext()).put("bookUpdate",GsonUtil.bean2json(map));
                }
            }
        }
    }

    public static boolean isBookUpdate(String bookId, String updateTime) {
        if(TextUtils.isEmpty(bookId)||TextUtils.isEmpty(updateTime)) {
            return true;
        }
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("bookUpdate");
            if(TextUtils.isEmpty(json)) {
                return true;
            }else {
                Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                if(map!=null) {
                    String time = map.get(bookId);
                    return !TextUtils.equals(updateTime,time);
                }
            }
        }
        return true;
    }

    public static void addPageView(String pageName) {
        ExecutorsUtils.getInstance().getAppExecutors().diskIO().execute(() -> {
            if(GlobalContext.getContext()!=null) {
                try{
                    String json = ACache.get(GlobalContext.getContext()).getAsString("pageView");
                    if(!TextUtils.isEmpty(json)) {
                        List<UploadPageRecordBean> list = new Gson().fromJson(json, new TypeToken<List<UploadPageRecordBean>>(){}.getType());
                        if(ListUtil.isNotEmpty(list)) {
                            boolean hasThePage = false;
                            for(int i=0; i<list.size(); i++) {
                                if(TextUtils.equals(list.get(i).getPageName(),pageName)) {
                                    list.get(i).setViewTime(list.get(i).getViewTime()+1);
                                    hasThePage = true;
                                    break;
                                }
                            }
                            if(!hasThePage) {
                                list.add(new UploadPageRecordBean(pageName,1));
                            }
                        } else {
                            list.add(new UploadPageRecordBean(pageName,1));
                        }
                        ACache.get(GlobalContext.getContext()).put("pageView",GsonUtil.bean2json(list));
                    }else {
                        List<UploadPageRecordBean> list = new ArrayList<>();
                        list.add(new UploadPageRecordBean(pageName,1));
                        ACache.get(GlobalContext.getContext()).put("pageView",GsonUtil.bean2json(list));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取流量分布记录list
     * @return 流量分布列表
     */
    public static List<UploadPageRecordBean> getPageView() {
        List<UploadPageRecordBean> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            try {
                String json = ACache.get(GlobalContext.getContext()).getAsString("pageView");
                if(!TextUtils.isEmpty(json)) {
                    list = new Gson().fromJson(json, new TypeToken<List<UploadPageRecordBean>>(){}.getType());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 清除流量分布记录
     */
    public static void clearPageViewRecord() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).remove("pageView");
        }
    }

    /**
     * 设置活动显示
     * @param activityId 活动id
     */
    public static void setActivityShow(String activityId) {
        if(GlobalContext.getContext()!=null) {
            String key = generateActivityShowTimeKey(activityId);
            String time = ACache.get(GlobalContext.getContext()).getAsString(key);
            int timeNum = 0;
            if(!TextUtils.isEmpty(time)&&TextUtils.isDigitsOnly(time)) {
                timeNum = Integer.parseInt(time);
            }
            timeNum += 1;
            ACache.get(GlobalContext.getContext()).put(generateActivityShowTimeKey(activityId), String.valueOf(timeNum));
        }
    }

    private static String generateActivityShowTimeKey(String activityId) {
        return activityId + "_activityShow_" + UserManager.getUserId() + "_" + TimeUtils.getTodayString();
    }

    /**
     * 是否需要显示活动弹窗
     * @param activityId 活动Id
     * @param frequencyNum 活动每日弹窗频次
     * @return true 已提示 false 未提示
     */
    public static boolean shouldShowActivity(String activityId, int frequencyNum) {
        if(frequencyNum==0) {
            return true;
        }
        if(GlobalContext.getContext()!=null) {
            String time = ACache.get(GlobalContext.getContext()).getAsString(generateActivityShowTimeKey(activityId));
            if(!TextUtils.isEmpty(time) && TextUtils.isDigitsOnly(time)) {
                long timeNum = Integer.parseInt(time);
                return timeNum<frequencyNum;
            }
        }
        return true;
    }

    public static void recordRechargeTime() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("lastRechargeTime", TimeUtils.getTodayString());
        }
    }

    public static boolean hasRechargeToday() {
        if(GlobalContext.getContext()!=null) {
            String lastRechargeTime = ACache.get(GlobalContext.getContext()).getAsString("lastRechargeTime");
            return TextUtils.equals(lastRechargeTime,TimeUtils.getTodayString());
        }
        return false;
    }

    /**
     * 记录个人中心福利红点点击时间
     */
    public static void recordClickRewardDotTime() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("rewardDotShowTime", TimeUtils.getTodayString());
        }
    }

    /**
     * 判断个人中心福利红点今天是否已显示
     * @return
     */
    public static boolean hasShowRewardDotToday() {
        if(GlobalContext.getContext()!=null) {
            String lastRechargeTime = ACache.get(GlobalContext.getContext()).getAsString("rewardDotShowTime");
            return TextUtils.equals(lastRechargeTime,TimeUtils.getTodayString());
        }
        return false;
    }

    public static void saveHideBooks(String json) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("hideBooks", json);
        }
    }

    public static List<String> getHideBooks() {
        List<String> idList = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            try {
                String json = ACache.get(GlobalContext.getContext()).getAsString("hideBooks");
                if(!TextUtils.isEmpty(json)) {
                    idList =  GsonUtil.parseJsonToList(json);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return idList;
            }
        }
        return idList;
    }

    /**
     * 是否需要隐藏此书
     * @param bookId
     * @return
     */
    public static boolean shouldHideBook(String bookId) {
        if(TextUtils.isEmpty(bookId)) {
            return true;
        }
        List<String> hideBooks = getHideBooks();
        return !ListUtil.isEmpty(hideBooks) && hideBooks.contains(bookId);
    }

    /**
     * 缓存书架推荐书籍
     * @param data
     */
    public static void saveBookShelfRecommendBooks(String data) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("bookShelfRecommendBooks",data);
        }
    }

    public static List<BookInfo> getBookShelfRecommendBooks() {
        List<BookInfo> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("bookShelfRecommendBooks");
            try{
                if(!TextUtils.isEmpty(json)) {
                    list = new Gson().fromJson(json, new TypeToken<List<BookInfo>>(){}.getType());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public static void saveRecommendBookClickId(String bookId) {
        if(GlobalContext.getContext()!=null) {
            String ids = ACache.get(GlobalContext.getContext()).getAsString("recommendBookClickIds");
            if(!TextUtils.isEmpty(ids)) {
                ids = ids + "," + bookId;
                ACache.get(GlobalContext.getContext()).put("recommendBookClickIds", ids);
            }else{
                ACache.get(GlobalContext.getContext()).put("recommendBookClickIds", bookId);
            }
        }
    }

    public static String getRecommendBookClickIds() {
        if(GlobalContext.getContext()!=null) {
            String ids = ACache.get(GlobalContext.getContext()).getAsString("recommendBookClickIds");
            return TextUtils.isEmpty(ids)?"":ids;
        }
        return "";
    }

    /**
     * 保存退出阅读器推荐书籍数据
     * @param json
     */
    public static void saveExitBookRecommendBooks(String json) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("exitBookRecommendBooks_" + UserManager.getUserId(), json);
        }
    }

    /**
     * 获取退出阅读器推荐书籍数据
     * @return
     */
    public static List<BookInfo> getExitBookRecommendBooks() {
        List<BookInfo> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("exitBookRecommendBooks_" + UserManager.getUserId());
            try{
                if(!TextUtils.isEmpty(json)) {
                    list = new Gson().fromJson(json, new TypeToken<List<BookInfo>>(){}.getType());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 清除退出阅读器推荐书籍数据
     */
    public static void removeExitBookRecommendBooks() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).remove("exitBookRecommendBooks_" + UserManager.getUserId());
        }
    }

    /**
     * 缓存Rank ongoing 第一页数据
     * @param json
     */
    public static void saveFirstPageOnGoingBooks(String json) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put(LanguageUtil.getLanguage() + "_firstPageOnGoingBooks" + UserManager.getUserId(), json);
        }
    }

    /**
     * 获取Rank ongoing 第一页数据
     * @return
     */
    public static List<BookStore.DataBean> getFirstPageOnGoingBooks() {
        List<BookStore.DataBean> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString(LanguageUtil.getLanguage() + "_firstPageOnGoingBooks" + UserManager.getUserId());
            try{
                if(!TextUtils.isEmpty(json)) {
                    list = new Gson().fromJson(json, new TypeToken<List<BookStore.DataBean>>(){}.getType());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 清除Rank ongoing 第一页数据缓存
     */
    public static void removeFirstPageOnGoingBooks() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).remove(LanguageUtil.getLanguage() + "_firstPageOnGoingBooks" + UserManager.getUserId());
        }
    }

    /**
     * 缓存Rank complete 第一页数据
     * @param json
     */
    public static void saveFirstPageCompleteBooks(String json) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put(LanguageUtil.getLanguage() + "_firstPageCompleteBooks" + UserManager.getUserId(), json);
        }
    }

    /**
     * 获取Rank complete 第一页数据
     * @return
     */
    public static List<BookStore.DataBean> getFirstPageCompleteBooks() {
        List<BookStore.DataBean> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString(LanguageUtil.getLanguage() + "_firstPageCompleteBooks" + UserManager.getUserId());
            try{
                if(!TextUtils.isEmpty(json)) {
                    list = new Gson().fromJson(json, new TypeToken<List<BookStore.DataBean>>(){}.getType());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 清除Rank complete 第一页数据缓存
     */
    public static void removeFirstPageCompleteBooks() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).remove(LanguageUtil.getLanguage() + "_firstPageCompleteBooks" + UserManager.getUserId());
        }
    }

    /**
     * 添加阅读完书籍
     */
    public static void addReadOverBook(String bookId) {
        ExecutorsUtils.getInstance().getAppExecutors().diskIO().execute(() -> {
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString("bookReadOver");
                if(TextUtils.isEmpty(json)) {
                    List<String> list = new ArrayList<>();
                    list.add(bookId);
                    ACache.get(GlobalContext.getContext()).put("bookReadOver",GsonUtil.bean2json(list));
                }else {
                    List<String> list = GsonUtil.json2Bean(json, new TypeToken<List<String>>(){}.getType());
                    if(list!=null) {
                        list.add(bookId);
                        ACache.get(GlobalContext.getContext()).put("bookReadOver",GsonUtil.bean2json(list));
                    }else{
                        List<String> tempList = new ArrayList<>();
                        tempList.add(bookId);
                        ACache.get(GlobalContext.getContext()).put("bookReadOver",GsonUtil.bean2json(tempList));
                    }
                }
            }
        });
    }

    /**
     * 获取已阅读完的书籍
     * @return
     */
    public static List<String> getReadOverBookList() {
        List<String> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("bookReadOver");
            if(!TextUtils.isEmpty(json)) {
                list = GsonUtil.json2Bean(json, new TypeToken<List<String>>(){}.getType());
            }
        }
        return list;
    }

    /**
     * 缓存启动推荐数据展示位置和图片
     */
    public static void cacheNextSplashRecommendData(int position, String imgUrl) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("splashRecommendData_" + UserManager.getUserId(), String.valueOf(position));
            GlideUtils.preload(GlobalContext.getContext(),imgUrl);
        }
    }

    public static int getNextSplashRecommendPosition() {
        if(GlobalContext.getContext()!=null) {
            String position = ACache.get(GlobalContext.getContext()).getAsString("splashRecommendData_" + UserManager.getUserId());
            if(TextUtils.isEmpty(position)) return 0;
            if(TextUtils.isDigitsOnly(position)) {
                return Integer.parseInt(position);
            }
        }
        return 0;
    }

    public static void saveSplashRecommendCachedFlag() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("splashRecommendCached_" + UserManager.getUserId(), "true");
        }
    }

    public static boolean isSplashRecommendCached() {
        if(GlobalContext.getContext()!=null) {
            String isCached = ACache.get(GlobalContext.getContext()).getAsString("splashRecommendCached_" + UserManager.getUserId());
            return !TextUtils.isEmpty(isCached);
        }
        return true;
    }

    /**
     * 保存语言列表
     * @param json
     */
    public static void saveLanguageList(String json) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("languageList", json);
        }
    }

    /**
     * 获取语言列表
     * @return
     */
    public static List<LanguageItemBean> getLanguageList() {
        List<LanguageItemBean> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("languageList");
            if(!TextUtils.isEmpty(json)) {
                list = GsonUtil.json2Bean(json, new TypeToken<List<LanguageItemBean>>(){}.getType());
            }
        }
        return list;
    }

    public static void removeLanguageList() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).remove("languageList");
        }
    }

    public static void setAutoPay(String bookId, boolean flag) {
        if(TextUtils.isEmpty(bookId)) return;
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put(UserManager.getUserId()+"_"+bookId,flag?"1":"0");
        }
    }

    public static boolean isAutoPay(String bookId) {
        String result = "1";
        if(GlobalContext.getContext()!=null) {
            result = ACache.get(GlobalContext.getContext()).getAsString(UserManager.getUserId()+"_"+bookId);
            if(TextUtils.isEmpty(result)) {
                return true;
            }
        }
        return TextUtils.equals("1",result);
    }

    public static void setSignDataCache(List<RewardSignBean.SignBean.SignListBean> signList) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put(UserManager.getUserId() + "_" + TimeUtils.getTodayString(),GsonUtil.bean2json(signList));
        }
    }

    public static List<RewardSignBean.SignBean.SignListBean> getSignData(){
        List<RewardSignBean.SignBean.SignListBean> list = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            String dataContent = ACache.get(GlobalContext.getContext()).getAsString(UserManager.getUserId()+"_" + TimeUtils.getTodayString());
            if(!TextUtils.isEmpty(dataContent)){
                list = new Gson().fromJson(dataContent, new TypeToken<List<RewardSignBean.SignBean.SignListBean>>(){}.getType());
            }
        }
        return list;
    }

    public static void cacheLastAdId(String adId){
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put(UserManager.getUserId(), adId);
        }
    }

    public static String getLastAdId() {
        if(GlobalContext.getContext()!=null) {
            String adId = ACache.get(GlobalContext.getContext()).getAsString(UserManager.getUserId());
            return TextUtils.isEmpty(adId)? "" : adId;
        }
        return "";
    }

    public static void cacheUserFromTypeData(String bookId) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("userFromType", bookId);
        }
    }

    public static String getUserFromTypeData(){
        if(GlobalContext.getContext()!=null) {
            return ACache.get(GlobalContext.getContext()).getAsString("userFromType");
        }
        return "";
    }

    public static void clearUserFromType() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("userFromType", "");
        }
    }

    public static boolean saveRefreshVersion(int version){
        if(version==0) return false;
        int preVersion = PreferencesUtil.get("refresh_version",0);
        if(preVersion<version){
            PreferencesUtil.put("refresh_version", version);
            return true;
        }
        return false;
    }

    /**
     * 存储是否完成截图
     * @param bookId
     */
    public static void saveBookScreenShot(String bookId) {
        if(GlobalContext.getContext()!=null) {
            String date = TimeUtils.getTodayString();
            ACache.get(GlobalContext.getContext()).put(date + "_screen_shot_" + bookId, "1");
        }
    }

    public static boolean getBookScreenShot(String bookId) {
        if(GlobalContext.getContext()!=null) {
            String date = TimeUtils.getTodayString();
            String result = ACache.get(GlobalContext.getContext()).getAsString(date + "_screen_shot_" + bookId);
            return TextUtils.equals(result, "1");
        }
        return false;
    }

    /**
     * 缓存章节文件版本
     * @param bookId
     * @param bookVersion
     */
    public static void saveBookChapterVersion(String bookId, String bookVersion) {
        if(TextUtils.isEmpty(bookId)) return;
        if(TextUtils.isEmpty(bookVersion)){
            bookVersion = "-1";
        }
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("bookChapter_dir_version");
            if(TextUtils.isEmpty(json)) {
                Map<String,String> map = new HashMap<>();
                map.put(bookId,bookVersion);
                ACache.get(GlobalContext.getContext()).put("bookChapter_dir_version",GsonUtil.bean2json(map));
            }else {
                Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                if(map!=null) {
                    map.put(bookId,bookVersion);
                    ACache.get(GlobalContext.getContext()).put("bookChapter_dir_version",GsonUtil.bean2json(map));
                }
            }
        }
    }

    /**
     * 获取书籍章节文件版本
     * @param bookId
     * @return
     */
    public static String getBookChapterVersion(String bookId){
        if(TextUtils.isEmpty(bookId)) return "-1";
        if(GlobalContext.getContext()!=null) {
            String json = ACache.get(GlobalContext.getContext()).getAsString("bookChapter_dir_version");
            if(!TextUtils.isEmpty(json)) {
                Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                if(map!=null&&map.containsKey(bookId)) {
                    return map.get(bookId);
                }else{
                    return "-1";
                }
            }
        }
        return "-1";
    }

    /**
     * 保存书籍id映射关系
     * @param comBookId
     * @param bookId
     */
    public static void saveBookIdMap(String comBookId, String bookId){
        try{
            if(TextUtils.isEmpty(comBookId)||!comBookId.startsWith("1U")) {
                return;
            }
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString("comBookId2BookId");
                if(TextUtils.isEmpty(json)) {
                    Map<String,String> map = new HashMap<>();
                    map.put(comBookId,bookId);
                    ACache.get(GlobalContext.getContext()).put("comBookId2BookId",GsonUtil.bean2json(map));
                }else {
                    Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                    if(map!=null) {
                        map.put(comBookId,bookId);
                        ACache.get(GlobalContext.getContext()).put("comBookId2BookId",GsonUtil.bean2json(map));
                    }else{
                        Map<String,String> tempMap = new HashMap<>();
                        tempMap.put(comBookId,bookId);
                        ACache.get(GlobalContext.getContext()).put("comBookId2BookId",GsonUtil.bean2json(tempMap));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 通过comBookId获取bookId
     * @param comBookId
     * @return
     */
    public static String getBookIdFromComBookId(String comBookId){
        try{
            if(TextUtils.isEmpty(comBookId)||!comBookId.startsWith("1U")) return "";
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString("comBookId2BookId");
                if(!TextUtils.isEmpty(json)) {
                    Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                    if(map!=null&&map.containsKey(comBookId)) {
                        return map.get(comBookId);
                    }else{
                        return "";
                    }
                }else{
                    return "";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void saveFBTrackEventData(JsonObject jsonObject) {
        String key = "fb_track_cache_batch";
        try{
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if(TextUtils.isEmpty(json)) {
                    ArrayList<JsonObject> initList = new ArrayList<>();
                    initList.add(jsonObject);
                    ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                }else {
                    ArrayList<JsonObject> list = new Gson().fromJson(json, new TypeToken<List<JsonObject>>(){}.getType());
                    if(list!=null){
                        list.add(jsonObject);
                        if(list.size()>=5){
//                            new FBTrackPresenter().trackEventBatch(list.toString());
                            list.clear();
                        }
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }else{
                        ArrayList<JsonObject> initList = new ArrayList<>();
                        initList.add(jsonObject);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void saveFBTrackFailEventData(String requestParamMap, boolean isBatch) {
        String key = isBatch? "fb_track_fail_cache_batch" : "fb_track_fail_cache";
        try{
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if(TextUtils.isEmpty(json)) {
                    List<String> initList = new ArrayList<>();
                    initList.add(requestParamMap);
                    ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                }else {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(list!=null) {
                        list.add(requestParamMap);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }else{
                        List<String> initList = new ArrayList<>();
                        initList.add(requestParamMap);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<String> getFBTrackFailEventData(boolean isBatch) {
        String key = isBatch? "fb_track_fail_cache_batch" : "fb_track_fail_cache";
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if (!TextUtils.isEmpty(json)) {
                    return GsonUtil.parseJsonToList(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeFbCacheData(String content, boolean isBatch) {
        String key = isBatch? "fb_track_fail_cache_batch" : "fb_track_fail_cache";
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if (!TextUtils.isEmpty(json)) {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(ListUtil.isNotEmpty(list)) {
                        list.remove(content);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFBUserInfoEventData(String requestParam) {
        String key = "fb_track_report_user_info";
        try{
            if(GlobalContext.getContext()!=null) {
                ACache.get(GlobalContext.getContext()).put(key, requestParam);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getFBReportFailUserInfoData() {
        String key = "fb_track_report_user_info";
        try{
            if(GlobalContext.getContext()!=null) {
                return ACache.get(GlobalContext.getContext()).getAsString(key);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void removeFBUserInfoEventData() {
        String key = "fb_track_report_user_info";
        try{
            if(GlobalContext.getContext()!=null) {
                ACache.get(GlobalContext.getContext()).remove(key);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void saveFBUserInfoBackTraceEventData(String requestParam) {
        String key = "fb_track_report_user_info_backtrace";
        try{
            if(GlobalContext.getContext()!=null) {
                ACache.get(GlobalContext.getContext()).put(key, requestParam);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getFBReportFailUserInfoBackTraceData() {
        String key = "fb_track_report_user_info_backtrace";
        try{
            if(GlobalContext.getContext()!=null) {
                return ACache.get(GlobalContext.getContext()).getAsString(key);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void removeFBUserInfoBackTraceEventData() {
        String key = "fb_track_report_user_info_backtrace";
        try{
            if(GlobalContext.getContext()!=null) {
                ACache.get(GlobalContext.getContext()).remove(key);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void saveFBReportInfoEventData(String requestParam) {
        String key = "fb_track_report_info";
        try{
            if(GlobalContext.getContext()!=null) {
                ACache.get(GlobalContext.getContext()).put(key, requestParam);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getFBReportFailInfoData() {
        String key = "fb_track_report_info";
        try{
            if(GlobalContext.getContext()!=null) {
                return ACache.get(GlobalContext.getContext()).getAsString(key);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 缓存游客登录后 上传设备信息的请求参数
     * @param requestParam
     */
    public static void savePostDeviceData(String requestParam) {
        String key = "mid_post_device_data";
        try{
            if(GlobalContext.getContext()!=null) {
                ACache.get(GlobalContext.getContext()).put(key, requestParam);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getPostDeviceData() {
        String key = "mid_post_device_data";
        try{
            if(GlobalContext.getContext()!=null) {
                return ACache.get(GlobalContext.getContext()).getAsString(key);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void removePostDeviceData() {
        String key = "mid_post_device_data";
        try{
            if(GlobalContext.getContext()!=null) {
                File file = ACache.get(GlobalContext.getContext()).file(key);
                if(file!=null) {
                    ACache.get(GlobalContext.getContext()).remove(key);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 缓存第三方登录成功后 上传的第三方账号信息
     * @param requestParam
     */
    public static void savePostThirdPartyData(String requestParam) {
        String key = "mid_post_third_party_data";
        try{
            if(GlobalContext.getContext()!=null) {
                ACache.get(GlobalContext.getContext()).put(key, requestParam);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getPostThirdPartyData() {
        String key = "mid_post_third_party_data";
        try{
            if(GlobalContext.getContext()!=null) {
                return ACache.get(GlobalContext.getContext()).getAsString(key);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void removePostThirdPartyData() {
        String key = "mid_post_third_party_data";
        try{
            if(GlobalContext.getContext()!=null) {
                File file = ACache.get(GlobalContext.getContext()).file(key);
                if(file!=null) {
                    ACache.get(GlobalContext.getContext()).remove(key);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isForceLogin() {
        return PreferencesUtil.get(Constant.SP_CONSTANT.NEED_RECHARGE_LOGIN, false);
    }

    public static String getDeviceId() {
        String deviceId = PreferencesUtil.get(Constant.DeviceData.DEVICE_ID, "");
        return TextUtils.isEmpty(deviceId)? ApiBase.newDeviceId : deviceId;
    }

    public static String getGaId() {
        return PreferencesUtil.get(Constant.DeviceData.GA_ID, "");
    }

    public static void saveAndroidId(String androidId) {
        PreferencesUtil.put(Constant.DeviceData.ANDROID_ID, androidId);
    }

    public static String getAndroidId() {
        return PreferencesUtil.get(Constant.DeviceData.ANDROID_ID, "");
    }

    public static void saveUserCaId(String caId) {
        String key = Constant.SP_CONSTANT.CAID_FROM_SERVER;
        PreferencesUtil.put(key, caId);
    }

    public static String getUserCacheCaId() {
        String key = Constant.SP_CONSTANT.CAID_FROM_SERVER;
        return PreferencesUtil.get(key, "");
    }

    public static void saveCaIdInfo(String caIdInfoJson) {
        String key = Constant.SP_CONSTANT.CAID_DATA;
        PreferencesUtil.put(key, caIdInfoJson);
    }
    public static String getCaIdInfo() {
        String key = Constant.SP_CONSTANT.CAID_DATA;
        return PreferencesUtil.get(key, "");
    }

    public static void saveCacheValue(String key, String value) {
        PreferencesUtil.put(key, value);
    }

    public static String getCacheValueByKey(String key) {
        return PreferencesUtil.get(key, "");
    }

    /**
     * @param loginType 登录方式
     */
    public static void saveThirdLoginType(String loginType) {
        try{
            if(GlobalContext.getContext()!=null) {
                String json = PreferencesUtil.get(Constant.SP_CONSTANT.THIRD_LOGIN_TYPES, "");
                if(TextUtils.isEmpty(json)) {
                    ArrayList<String> initList = new ArrayList<>();
                    initList.add(loginType);
                    PreferencesUtil.put(Constant.SP_CONSTANT.THIRD_LOGIN_TYPES, GsonUtil.bean2json(initList));
                }else {
                    ArrayList<String> list = new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
                    if(list!=null){
                        list.add(loginType);
                        PreferencesUtil.put(Constant.SP_CONSTANT.THIRD_LOGIN_TYPES, GsonUtil.bean2json(list));
                    }else{
                        ArrayList<String> initList = new ArrayList<>();
                        initList.add(loginType);
                        PreferencesUtil.put(Constant.SP_CONSTANT.THIRD_LOGIN_TYPES, GsonUtil.bean2json(initList));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 已经登录过返回false
     * @return
     */
    public static boolean checkCanLogin(String loginType) {
        String loginTypes = PreferencesUtil.get(Constant.SP_CONSTANT.THIRD_LOGIN_TYPES, "");
        if(TextUtils.isEmpty(loginTypes)) {
            return true;
        }
        ArrayList<String> list = new Gson().fromJson(loginTypes, new TypeToken<List<String>>(){}.getType());
        if(list!=null){
            return !list.contains(loginType);
        }
        return true;
    }

    public static void removeThirdLoginTypes() {
        PreferencesUtil.put(Constant.SP_CONSTANT.THIRD_LOGIN_TYPES, "");
    }

    public static boolean hasSelectPreferenceTag() {
        return PreferencesUtil.get(Constant.SP_CONSTANT.SELECT_PREFERENCE_TAG, false);
    }

    public static void saveSelectPreferenceTag() {
        PreferencesUtil.put(Constant.SP_CONSTANT.SELECT_PREFERENCE_TAG, true);
    }

    public static void saveLastRequestTime(String key) {
        if(TextUtils.isEmpty(key)) return;
        if(GlobalContext.getContext()!=null) {
            key = key + "-" + LanguageUtil.getLanguage();
            String json = ACache.get(GlobalContext.getContext()).getAsString("pageDataRequest");
            if(TextUtils.isEmpty(json)) {
                Map<String,String> map = new HashMap<>();
                map.put(key, String.valueOf(System.currentTimeMillis()));
                ACache.get(GlobalContext.getContext()).put("pageDataRequest",GsonUtil.bean2json(map));
            }else {
                Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                if(map!=null) {
                    map.put(key, String.valueOf(System.currentTimeMillis()));
                    ACache.get(GlobalContext.getContext()).put("pageDataRequest",GsonUtil.bean2json(map));
                }
            }
        }
    }

    public static long getLastRequestTime(String pageName) {
        if(TextUtils.isEmpty(pageName)) return 0;
        try{
            if(GlobalContext.getContext()!=null) {
                pageName = pageName + "-" + LanguageUtil.getLanguage();
                String json = ACache.get(GlobalContext.getContext()).getAsString("pageDataRequest");
                if(TextUtils.isEmpty(json)) {
                    return 0;
                }else {
                    Map<String,String> map = GsonUtil.json2Bean(json, Map.class);
                    if(map!=null) {
                        String time = map.get(pageName);
                        if(TextUtils.isEmpty(time)){
                            return 0;
                        }
                        if(time!=null && TextUtils.isDigitsOnly(time)) {
                            return Long.parseLong(time);
                        }
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static void saveRankingDynamicData(String data) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("rankingDynamicData-" + LanguageUtil.getLanguage(), data);
        }
    }

    public static RankListBean getRankingDynamicData() {
        if(GlobalContext.getContext()!=null) {
            String content = ACache.get(GlobalContext.getContext()).getAsString("rankingDynamicData-" + LanguageUtil.getLanguage());
            if (TextUtils.isEmpty(content)) {
                return null;
            }else{
                return GsonUtil.json2Bean(content, RankListBean.class);
            }
        }
        return null;
    }

    public static void saveBookStoreDynamicData(String pageCode, String data) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("bookStoreData-" + pageCode, data);
        }
    }

    public static List<BookStore> getBookStoreDynamicData(String pageCode) {
        if(GlobalContext.getContext()!=null) {
            String content = ACache.get(GlobalContext.getContext()).getAsString("bookStoreData-" + pageCode);
            if (TextUtils.isEmpty(content)) {
                return null;
            }else{
                return new Gson().fromJson(content, new TypeToken<List<BookStore>>(){}.getType());
            }
        }
        return null;
    }

    public static void saveUserReportResponse(String content) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("adReportResponse-" + UserManager.getUserId(), content);
        }
    }

    public static FBReportResponseBean.Response getUserReportResponse() {
        if(GlobalContext.getContext()!=null) {
            String content = ACache.get(GlobalContext.getContext()).getAsString("adReportResponse-" + UserManager.getUserId());
            if(!TextUtils.isEmpty(content)) {
                FBReportResponseBean.Response response = GsonUtil.json2Bean(content, FBReportResponseBean.Response.class);
                if(response!=null) {
                    return response;
                }
            }
        }
        return null;
    }

    public static void saveReadRecord(String bookId, String comBookId, int readPosition) {
        try{
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString("readRecord");
                if(TextUtils.isEmpty(json)) {
                    Map<String,Integer> map = new HashMap<>();
                    if(!TextUtils.isEmpty(bookId)) {
                        map.put(bookId,readPosition);
                    }
                    if(!TextUtils.isEmpty(comBookId)) {
                        map.put(comBookId,readPosition);
                    }
                    ACache.get(GlobalContext.getContext()).put("readRecord",GsonUtil.bean2json(map));
                }else {
                    Map<String,Integer> map = GsonUtil.json2Bean(json, Map.class);
                    if(map!=null) {
                        if(!TextUtils.isEmpty(bookId)) {
                            map.put(bookId,readPosition);
                        }
                        if(!TextUtils.isEmpty(comBookId)) {
                            map.put(comBookId,readPosition);
                        }
                        ACache.get(GlobalContext.getContext()).put("readRecord",GsonUtil.bean2json(map));
                    }else{
                        Map<String,Integer> tempMap = new HashMap<>();
                        if(!TextUtils.isEmpty(bookId)) {
                            tempMap.put(bookId,readPosition);
                        }
                        if(!TextUtils.isEmpty(comBookId)) {
                            tempMap.put(comBookId,readPosition);
                        }
                        ACache.get(GlobalContext.getContext()).put("readRecord",GsonUtil.bean2json(tempMap));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static int getReadPosition(String bookId, String comBookId) {
        try{
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString("readRecord");
                if(!TextUtils.isEmpty(json)) {
                    Type type = new TypeToken<Map<String, Integer>>(){}.getType();
                    Map<String, Integer> map = GsonUtil.json2Bean(json, type);
//                    Map<String, Integer> map = GsonUtil.json2Bean(json, Map.class);
                    if(map!=null) {
                        int tempPosition = 0;
                        if(!TextUtils.isEmpty(bookId)) {
                            Integer readPosition = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                readPosition = map.getOrDefault(bookId, 0);
                            } else {
                                readPosition = map.get(bookId);
                            }
                            if (readPosition != null) {
                                tempPosition = readPosition;
                            }
                        }
                        if(!TextUtils.isEmpty(comBookId)) {
                            Integer readPosition1 = map.get(comBookId);
                            if(readPosition1!=null && readPosition1 > tempPosition) {
                                tempPosition = readPosition1;
                            }
                        }
                        return tempPosition;
                    }
                }else{
                    return 0;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    public static void cacheWebAdSourceInfo(String paramContent) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("webAdSourceInfo", paramContent);
        }
    }

    public static String getWebAdSourceInfo() {
        if(GlobalContext.getContext()!=null) {
            String content = ACache.get(GlobalContext.getContext()).getAsString("webAdSourceInfo");
            if(!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    public static void removeWebAdSourceInfo() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).remove("webAdSourceInfo");
        }
    }

    public static void cacheAdSourceInfo(String paramContent) {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).put("adSourceInfo", paramContent);
        }
    }

    public static String getAdSourceInfo() {
        if(GlobalContext.getContext()!=null) {
            String content = ACache.get(GlobalContext.getContext()).getAsString("adSourceInfo");
            if(!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    public static void removeAdSourceInfo() {
        if(GlobalContext.getContext()!=null) {
            ACache.get(GlobalContext.getContext()).remove("adSourceInfo");
        }
    }

    //--------------------------------------------------------------------------//
    public static void saveSDKLogsData(String requestParamMap) {
        String key = "sdkLogsData";
        try{
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if(TextUtils.isEmpty(json)) {
                    List<String> initList = new ArrayList<>();
                    initList.add(requestParamMap);
                    ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                } else {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(list!=null) {
                        list.add(requestParamMap);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }else{
                        List<String> initList = new ArrayList<>();
                        initList.add(requestParamMap);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<String> getSDKLogsData() {
        String key = "sdkLogsData";
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if (!TextUtils.isEmpty(json)) {
                    return GsonUtil.parseJsonToList(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeSDKLogsData(String content) {
        String key = "sdkLogsData";
        try {
            if (GlobalContext.getContext() != null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if (!TextUtils.isEmpty(json)) {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(ListUtil.isNotEmpty(list)) {
                        list.remove(content);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean cacheUploadScreenShot(String pageName) {
        String key = "uploadScreenShot";
        String content =  TimeUtils.getTodayString() + "-" + pageName;
        try{
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if(TextUtils.isEmpty(json)) {
                    List<String> initList = new ArrayList<>();
                    initList.add(content);
                    ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                } else {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(list!=null) {
                        if(list.contains(content)) {
                            return false;
                        }
                        list.add(content);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }else{
                        List<String> initList = new ArrayList<>();
                        initList.add(content);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 本地记录已经上报过的本地订单编号
     * @param orderId
     */
    public static boolean canCacheRechargeOrder(String orderId) {
        String key = "rechargeSuccessOrderList";
        try{
            if(GlobalContext.getContext()!=null) {
                String json = ACache.get(GlobalContext.getContext()).getAsString(key);
                if(TextUtils.isEmpty(json)) {
                    List<String> initList = new ArrayList<>();
                    initList.add(orderId);
                    ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                } else {
                    List<String> list = GsonUtil.parseJsonToList(json);
                    if(list!=null) {
                        if(list.contains(orderId)) {
                            return false;
                        }
                        list.add(orderId);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(list));
                    }else{
                        List<String> initList = new ArrayList<>();
                        initList.add(orderId);
                        ACache.get(GlobalContext.getContext()).put(key, GsonUtil.bean2json(initList));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
        return true;
    }

}
