package com.base.net.request;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;

/**
 * Created by haojiangfeng on 2024/12/27.
 */
public class RequestConfig {


    public int maxIdleConnections;
    public long keepAliveDuration;
    public TimeUnit connectPoolUnit;


    public int connectTimeOutValue = 10;
    public TimeUnit connectTimeOutUnit;

    public int readTimeOutValue;
    public TimeUnit readTimeOutUnit;


    public int writeTimeOutValue;
    public TimeUnit writeTimeOutUnit;


    public static RequestConfig newDefault() {
        return new RequestConfig.Builder().build();
    }

    public static class Builder {

        public int maxIdleConnections = 10;
        public long keepAliveDuration = 5;
        public TimeUnit connectPoolUnit = TimeUnit.MINUTES;


        public int connectTimeOutValue = 10;
        public TimeUnit connectTimeOutUnit = TimeUnit.SECONDS;

        public int readTimeOutValue = 10;
        public TimeUnit readTimeOutUnit = TimeUnit.SECONDS;

        public int writeTimeOutValue = 10;
        public TimeUnit writeTimeOutUnit = TimeUnit.SECONDS;


        public Builder configConnectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit connectPoolUnit){
            this.maxIdleConnections = maxIdleConnections;
            this.keepAliveDuration = keepAliveDuration;
            this.connectPoolUnit = connectPoolUnit;
            return this;
        }

        public Builder configConnectTimeout(int connectTimeOutValue, TimeUnit connectTimeOutUnit){
            this.connectTimeOutValue = connectTimeOutValue;
            this.connectTimeOutUnit = connectTimeOutUnit;
            return this;
        }

        public Builder configReadTimeout(int readTimeOutValue, TimeUnit readTimeOutUnit){
            this.readTimeOutValue = readTimeOutValue;
            this.readTimeOutUnit = readTimeOutUnit;
            return this;
        }

        public Builder configWriteTimeout(int writeTimeOutValue, TimeUnit writeTimeOutUnit){
            this.writeTimeOutValue = writeTimeOutValue;
            this.writeTimeOutUnit = writeTimeOutUnit;
            return this;
        }

        public RequestConfig build(){
            RequestConfig config = new RequestConfig();
            config.maxIdleConnections = maxIdleConnections;
            config.keepAliveDuration = keepAliveDuration;
            config.connectPoolUnit = connectPoolUnit;
            config.connectTimeOutValue = connectTimeOutValue;
            config.connectTimeOutUnit = connectTimeOutUnit;
            config.readTimeOutValue = readTimeOutValue;
            config.readTimeOutUnit = readTimeOutUnit;
            config.writeTimeOutValue = writeTimeOutValue;
            config.writeTimeOutUnit = writeTimeOutUnit;
            return config;
        }
    }


}
