package com.github.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
* 帐号信息
* @author adison
* @date 2017/7/7 
* @time 下午4:17
*/

public class AccountInfo implements Parcelable, Serializable {
    // 0 不更新   1 非强制更新   2 强制更新。
    @SerializedName("isupgrade")
    public int isupgrade;
    @SerializedName("upgrade_url")
    public String upgrade_url;

    @SerializedName("token_type")
    public String tokenType;

    public User user;

    @SerializedName("token")
    public String accessToken;
    public String scope;
    @SerializedName("expires_in")
    public int expiresIn;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("account_id")
    public int accountId;
    public String user_type;

    public String openId;

    public String caId;

    public AccountInfo() {
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "tokenType='" + tokenType + '\'' +
                ", user=" + user +
                ", accessToken='" + accessToken + '\'' +
                ", user_type='" + user_type + '\'' +
                ", openId='" + openId + '\'' +
                ", caId='" + caId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isupgrade);
        dest.writeString(this.upgrade_url);
        dest.writeString(this.tokenType);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.accessToken);
        dest.writeString(this.scope);
        dest.writeInt(this.expiresIn);
        dest.writeString(this.refreshToken);
        dest.writeInt(this.accountId);
        dest.writeString(this.user_type);
        dest.writeString(this.openId);
        dest.writeString(this.caId);
    }

    public void readFromParcel(Parcel source) {
        this.isupgrade = source.readInt();
        this.upgrade_url = source.readString();
        this.tokenType = source.readString();
        this.user = source.readParcelable(User.class.getClassLoader());
        this.accessToken = source.readString();
        this.scope = source.readString();
        this.expiresIn = source.readInt();
        this.refreshToken = source.readString();
        this.accountId = source.readInt();
        this.user_type = source.readString();
        this.openId = source.readString();
        this.caId = source.readString();
    }

    protected AccountInfo(Parcel in) {
        this.isupgrade = in.readInt();
        this.upgrade_url = in.readString();
        this.tokenType = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.accessToken = in.readString();
        this.scope = in.readString();
        this.expiresIn = in.readInt();
        this.refreshToken = in.readString();
        this.accountId = in.readInt();
        this.user_type = in.readString();
        this.openId = in.readString();
        this.caId = in.readString();
    }

    public static final Creator<AccountInfo> CREATOR = new Creator<AccountInfo>() {
        @Override
        public AccountInfo createFromParcel(Parcel source) {
            return new AccountInfo(source);
        }

        @Override
        public AccountInfo[] newArray(int size) {
            return new AccountInfo[size];
        }
    };
}
