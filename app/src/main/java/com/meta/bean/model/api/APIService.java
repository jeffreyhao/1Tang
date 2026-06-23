package com.meta.bean.model.api;

import com.github.bean.database.table.BookChapter;
import com.github.bean.model.BookDeleteBody;
import com.github.bean.model.BookDetail;
import com.github.bean.database.table.BookInfo;
import com.github.bean.model.BookShelfHeaderMeta;
import com.github.bean.model.BookStore;
import com.github.bean.model.CategoryItem;
import com.github.bean.model.ChapterIntroBean;
import com.github.bean.model.HotSearch;
import com.github.bean.model.ReadTimeSyncResult;
import com.github.bean.user.AccountInfo;
import com.github.bean.user.User;
import com.github.bean.zhifu.RechargeOrderBean;
import com.github.bean.zhifu.RechargeQueryBean;
import com.github.bean.zhifu.PurchaseInfoBean;
import com.github.bean.app.ProblemBean;
import com.github.bean.zhifu.AutoPayResultBean;
import com.github.bean.zhifu.PayResultBean;
import com.github.bean.qianbao.QianbaoBean;
import com.github.bean.zhifu.AutoZhifuBean;
import com.github.bean.qianbao.BonusBean;
import com.github.bean.qianbao.ConsumptionBean;
import com.github.bean.qianbao.TopUpBean;
import com.github.bean.reader.SyncCloudData;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIService {

    @FormUrlEncoded
    @POST("accounts/app_login2")
    Call<AccountInfo> login2(@Field("grant_type") String grant_type,
                             @Field("username") String username,
                             @Field("password") String password,
                             @Field("client_id") String client_id,
                             @Field("oauth_type") String oauthType,
                             @Field("id_token") String id_token,
                             @Field("server_auth_code") String server_auth_code,
                             @Field("user_id") String user_id,
                             @Field("display_name") String display_name,
                             @Field("given_name") String given_name,
                             @Field("family_name") String family_name,
                             @Field("email") String email,
                             @Field("photo_url") String photo_url);

    @POST("accounts/guest_login2")
    Call<AccountInfo> guestLogin();

    @GET("library/category")
    Call<List<CategoryItem>> getLibraryCategory(@Query("type") String type);

    @GET("bookstatuslist/{book_status}/{offset}/{limit}")
    Call<List<BookInfo>> bookStatusList(@Path("book_status") int bookStatus,
                                        @Path("offset") int offset,
                                        @Path("limit") int limit);

    @FormUrlEncoded
    @POST("app/api/subscribe/books")
    Call<SyncCloudData> getAllFavBooks(@Field("offset") int offset,
                                       @Field("limit") int limit,
                                       @Field("account_id") String userId);

    @FormUrlEncoded
    @POST("books/favs/sync")
    Call<SyncCloudData> getBookUpdateNotices(@Field("data") String data);

    @Streaming
    @GET
    Call<ResponseBody> getChapterContent(@Url String url);

    @GET("books/{bid}/chapters_content/{cid}")
    Call<ChapterIntroBean> bookChapterContent(@Path("bid") String bid,
                                              @Path("cid") String cid,
                                              @Query("user_id") String userId);

    @GET("books/{bid}/chapters_content/{cid}")
    Call<ChapterIntroBean> UpdateBookChapterInfo(@Path("bid") String bid,
                                                 @Path("cid") String cid,
                                                 @Query("user_id") String userId);

    @FormUrlEncoded
    @POST("accounts/revoke_token")
    Call<ResponseBody> logout(@Field("token") String refresh_token);

    @GET("users/{id}")
    Call<User> getUserInfo(@Path("id") int id);

    @POST("users/{id}")
    Call<User> changeUserInfo(@Path("id") int id,
                              @Body RequestBody body,
                              @Query("user_id") String userId);

    @GET("books/{book_id}/detail")
    Call<BookDetail> getBookDetail(@Path("book_id") String bookId);

    @FormUrlEncoded
    @POST("books/{book_id}/detail")
    Call<BookInfo> getBookDetailAsync(@Field("account_id") String userId,
                                      @Field("book_id") String bookId);

    @GET("pages/app_new_bookstore/{page_id}")
    Call<List<BookStore>> getBookStoreDataCall(@Path("page_id") String pageId,
                                               @Query("offset") int offset,
                                               @Query("limit") int limit);

    @GET("books/{book_id}/chapters")
    Call<List<BookChapter>> getBookCatalog(@Path("book_id") String bookId,
                                           @Query("user_id") String userId);

    @FormUrlEncoded
    @POST("books/favs")
    Call<ResponseBody> addFavBook(@Field("bid") String bid,
                                  @Field("user_id") String userId);

    @HTTP(method = "DELETE", path = "books/favs", hasBody = true)
    Call<ResponseBody> deleteFavBook(@Body BookDeleteBody data,
                                     @Query("user_id") String userId);

    @GET("books/search/{bname}")
    Call<List<BookInfo>> getSearchesult(@Path("bname") String bname,
                                        @Query("offset") int offset,
                                        @Query("limit") int limit);

    @GET("books/hotsearch")
    Call<HotSearch> getHotSearch();

    @GET("books/hot_recomment_new/{bid}")
    Call<List<BookInfo>> getHotBooks(@Path("bid") String bid);

    @GET("books/newcomers/default/new")
    Call<List<BookInfo>> getNewCommerBook(@Query("user_id") String userId);

    @DELETE("del_note/{note_id}")
    Call<ResponseBody> deleteNote(@Path("note_id") String note_id);

    @FormUrlEncoded
    @POST("app/api/bookstore/user_read_total_time")
    Call<ReadTimeSyncResult> getReadTime(@Field("account_id") String userId);

    @FormUrlEncoded
    @POST("app/api/bookstore/send_mult_read_time")
    Call<ReadTimeSyncResult> uploadReadTimeCallable(@Field("report") String data,
                                                    @Field("account_id") String userId);

    @GET("sys_time")
    Call<ResponseBody> getSeverTime();

    @FormUrlEncoded
    @POST("app/api/bookstore/send_cur_book_chapter")
    Call<ResponseBody> uploadUserPrivilegeBookAsync(@Field("account_id") String userId,
                                                    @Field("book_id") String bookId,
                                                    @Field("book_chapter_index") int bookChapterIndex,
                                                    @Field("book_chapter_page") int bookChapterPage,
                                                    @Field("finished") int finished);

    @FormUrlEncoded
    @POST("recharge/order")
    Call<RechargeOrderBean> rechargeOrderAsync(@Query("user_id") String userId,
                                               @Query("pay_method") String payMethod,
                                               @Query("recharge_id") String rechargeId,
                                               @Field("user_id") String user_id,
                                               @Field("pay_method") String pay_method,
                                               @Field("recharge_id") String recharge_id);

    @FormUrlEncoded
    @POST("recharge_GC")
    Call<RechargeQueryBean> rechargeQueryAsync(@Field("order_no") String orderNo,
                                               @Field("out_trade_no") String tradeNo,
                                               @Field("purchase_token") String token,
                                               @Field("total_fee") String totalFee,
                                               @Field("country_code") String countryCode,
                                               @Field("bill_NO") String billNO,
                                               @Field("production_id") String productId,
                                               @Field("sign") String sign);

    @GET("users/feedbacktype")
    Call<List<ProblemBean>> problems();

    @FormUrlEncoded
    @POST("users/sendfeedback/{user_id}")
    Call<ResponseBody> submitFeedback(@Path("user_id") String userId,
                                      @Field("problem_type_id") int problemId,
                                      @Field("problem_description") String problemDes,
                                      @Field("contacts") String contacts);

    @FormUrlEncoded
    @POST("chapter/show")
    Call<PurchaseInfoBean> chapterShow(@Field("user_id") String userId,
                                       @Field("bid") String bid,
                                       @Field("cid") String cid);


    @FormUrlEncoded
    @POST("purchase/chapter")
    Call<PayResultBean> purchaseChapter(@Field("user_id") String userId,
                                        @Field("bid") String bid,
                                        @Field("cid") String cid,
                                        @Field("chapter_show_id") int chapterShowId,
                                        @Field("need_pay") int needPay);

    @FormUrlEncoded
    @POST("wallet/setup")
    Call<ResponseBody> walletSetup(@Field("user_id") String userId,
                                   @Field("bid") String bid,
                                   @Field("status") int status);

    @FormUrlEncoded
    @POST("chapter/checkurl")
    Call<AutoPayResultBean> chapterCheckUrl(@Field("user_id") String userId,
                                            @Field("bid") String bid,
                                            @Field("cid") String cid);

    @GET("recharge/google_product_id")
    Call<List<String>> rechargeGoogleProduct();

    @FormUrlEncoded
    @POST("wallet/balance")
    Call<QianbaoBean> walletBalance(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("app/api/wallet/query_autobuy")
    Call<List<AutoZhifuBean>> walletQuery(@Query("account_id") String userId,
                                          @Query("offset") int offset,
                                          @Query("limit") int limit);

    @FormUrlEncoded
    @POST("app/api/wallet/recharge_record")
    Call<List<TopUpBean>> walletRecord(@Field("account_id") String userId,
                                       @Field("offset") int offset,
                                       @Field("limit") int limit);

    @FormUrlEncoded
    @POST("app/api/wallet/bonus_record")
    Call<List<BonusBean>> bonusRecord(@Field("account_id") String userId,
                                      @Field("offset") int offset,
                                      @Field("limit") int limit);

    @FormUrlEncoded
    @POST("app/api/wallet/purchase_record")
    Call<List<ConsumptionBean>> consumptionRecord(@Query("account_id") String userId,
                                                  @Query("offset") int offset,
                                                  @Query("limit") int limit);
}
