package com.base.net.callback;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
/**
* @author lhc
* @date 2022/5/9 9:16
* @desc
*/
public interface ApiService {

    //POST请求
    @POST
    @FormUrlEncoded
    Observable<Response<ResponseBody>> post(@Url String url, @FieldMap Map<String, Object> maps);

    @POST
    Observable<Response<ResponseBody>> post(@Url String url);

//    @POST
//    Observable<Response<ResponseBody>> postBody(@Url String url, @Body Object object);
//
//    @POST
//    Observable<Response<ResponseBody>> postBody(@Url String url, @Body RequestBody body);

    @POST
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<Response<ResponseBody>> postJson(@Url String url, @Body RequestBody jsonBody);

    // GET请求
    //无参
    @GET
    Observable<Response<ResponseBody>> get(@Url String url);

    //有参
    @GET
    Observable<Response<ResponseBody>> get(@Url String url, @QueryMap Map<String, Object> maps);


    @GET
    Observable<Response<ResponseBody>> get(@Url String url, @Header("Authorization") String authToken);

    @GET
    Observable<Response<ResponseBody>> get(@Url String url, @Header("Authorization") String authToken, @QueryMap Map<String, Object> maps);



//    //DELETE请求
//    @DELETE
//    Observable<Response<ResponseBody>> delete(@Url String url, @QueryMap Map<String, Object> maps);
//
//    @DELETE
//    Observable<Response<ResponseBody>> delete(@Url String url);
//
//    @DELETE
//    Observable<Response<ResponseBody>> deleteBody(@Url String url, @Body Object object);
//
//    @DELETE
//    Observable<Response<ResponseBody>> deleteBody(@Url String url, @Body RequestBody body);
//
//    @HTTP(method = "DELETE", hasBody = true)
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    Observable<Response<ResponseBody>> deleteJson(@Url String url, @Body RequestBody jsonBody);

    // PUT请求

    @PUT
    Observable<Response<ResponseBody>> put(@Url String url, @QueryMap Map<String, Object> maps);

//    @PUT
//    Observable<Response<ResponseBody>> putBody(@Url String url, @Body Object object);
//
//    @PUT
//    Observable<Response<ResponseBody>> putBody(@Url String url, @Body RequestBody body);

    @PUT
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<Response<ResponseBody>> putJson(@Url String url, @Body RequestBody jsonBody);

    //文件上传下载
//    @Multipart
//    @POST
//    Observable<Response<ResponseBody>> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> maps);
//
//    @Multipart
//    @POST
//    Observable<Response<ResponseBody>> uploadFiles(@Url String url, @Part MultipartBody.Part part);
//
//
//    @Streaming
//    @GET
//    Observable<Response<ResponseBody>> downloadFile(@Url String fileUrl);

    @Streaming
    @GET
    Call<ResponseBody> downloadChapterFile(@Url String fileUrl);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> fetchByteStream(@Url String fileUrl, @FieldMap Map<String, Object> maps);

}
