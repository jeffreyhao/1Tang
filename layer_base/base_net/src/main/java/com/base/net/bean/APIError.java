package com.base.net.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/6/12.
 */
public class APIError extends Throwable implements Parcelable {

    public static final int ERROR_DEFAULT = 1;
    public static final int ERROR_BODY_EMPTY = 2;
    public static final int ERROR_NO_DATA_CODE = 3; //没有数据
    public static final int ERROR_NO_NET = 4;       //meiyou
    public static final int ERROR_NO_AUTHORIZED = 5; //未授权
    public static final int ERROR_LIST_EMPTY = 6;
    public static final String ERROR_DEFAULT_USER_MESSAGE = "获取数据失败";
    public static final String ERROR_DEFAULT_MESSAGE = "get data fail";
    /**
     * 业务错误码
     */
    @SerializedName("code")
    public int errorCode;
    /**
     * http 状态码
     */
    public int statusCode;
    /**
     * 中文信息，提供给用户
     */
    @SerializedName("localized_message")
    public String errorUserMsg;
    /**
     * 错误信息，提供给开发者
     */
    @SerializedName("message")
    public String errorMessage;


    public APIError() {
        this.errorUserMsg = ERROR_DEFAULT_USER_MESSAGE;
        this.errorMessage = ERROR_DEFAULT_MESSAGE;
    }


    public APIError(int errorCode) {
        super();
        this.errorCode = errorCode;
        this.errorUserMsg = ERROR_DEFAULT_USER_MESSAGE;
        this.errorMessage = ERROR_DEFAULT_MESSAGE;
    }

    public APIError(int errorCode, int statusCode) {
        super();
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.errorUserMsg = ERROR_DEFAULT_USER_MESSAGE;
        this.errorMessage = ERROR_DEFAULT_MESSAGE;
    }

    public APIError(int errorCode, int statusCode, String errorUserMsg, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.errorUserMsg = errorUserMsg;
        this.errorMessage = errorMessage;
    }

    public APIError(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errorCode);
        dest.writeInt(this.statusCode);
        dest.writeString(this.errorUserMsg);
        dest.writeString(this.errorMessage);
    }

    protected APIError(Parcel in) {
        this.errorCode = in.readInt();
        this.statusCode = in.readInt();
        this.errorUserMsg = in.readString();
        this.errorMessage = in.readString();
    }

    public static final Creator<APIError> CREATOR = new Creator<APIError>() {
        @Override
        public APIError createFromParcel(Parcel source) {
            return new APIError(source);
        }

        @Override
        public APIError[] newArray(int size) {
            return new APIError[size];
        }
    };

    @Override
    public String toString() {
        return "APIError{" +
                "errorCode=" + errorCode +
                ", statusCode=" + statusCode +
                ", errorUserMsg='" + errorUserMsg + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                "} " + super.toString();
    }
}
