package com.base.net.bean;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by haojiangfeng on 2024/11/11.
 */
public class ResponseInfo {

    public String path;

    public Map<String, Object> paramMap;

    public String content;

    public Response<ResponseBody> response;

    public boolean isCache;


    public ResponseInfo(String path, Map<String, Object> paramMap, String content, Response<ResponseBody> response, boolean isCache){
        this.path = path;
        this.paramMap = paramMap;
        this.content = content;
        this.response = response;
        this.isCache = isCache;
    }



    public static class Builder {
        private String path;
        private Map<String, Object> param = null;
        private String content;
        private Response<ResponseBody> response = null;
        private boolean isCache = false;


        public ResponseInfo.Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public ResponseInfo.Builder setParam(Map<String, Object> param) {
            this.param = param;
            return this;
        }

        public ResponseInfo.Builder setIsCache(boolean isCache) {
            this.isCache = isCache;
            return this;
        }

        public ResponseInfo.Builder setResponse(Response<ResponseBody> response) {
            this.response = response;
            return this;
        }

        public ResponseInfo.Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public ResponseInfo create() {
            return new ResponseInfo(path, param, content, response, isCache);
        }
    }
}
