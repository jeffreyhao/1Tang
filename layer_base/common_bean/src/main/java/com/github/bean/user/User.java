package com.github.bean.user;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author adison
 * @date 2017/6/23
 * @time 下午4:10
 */

public class User implements Parcelable, Serializable {

    /**
     * avatar : http://scimg.jb51.net/touxiang/201709/2017091018241769.jpg
     * balance : 3.56
     * reward : 100
     * created :
     * description : description
     * id : 1
     * name : test
     */
    public String account_id;
    public String nickname;
    public String avatar;
    public String created;
    public String description;
    public int id;
    public String name;
    public String mobile;
    public String phone;
    public String region;
    public String country;
    public String sub_type;
    public int is_author;
    public int is_paying;//是否显示三方支付
    public int sex;
    public String birthday;
    public boolean is_bind_weixin;
    public boolean is_bind_qq;
    public String weixin_nickname;
    public String qq_nickname;
    public int unreadMsgCount;

    public String email;
    public boolean is_bind_google;
    public boolean is_bind_facebook;

    public int coins;
    public int bonus;
    public int diamonds;
    public String create_time;
    public boolean is_first_visit = true;
    public boolean is_premium;
    public String premium_started_at;
    public String premium_expired_at;
    public int activity_cycle;

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.account_id);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.created);
        dest.writeString(this.description);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.phone);
        dest.writeString(this.region);
        dest.writeString(this.country);
        dest.writeString(this.sub_type);
        dest.writeInt(this.is_author);
        dest.writeInt(this.sex);
        dest.writeString(this.birthday);
        dest.writeByte(this.is_bind_weixin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_bind_qq ? (byte) 1 : (byte) 0);
        dest.writeString(this.weixin_nickname);
        dest.writeString(this.qq_nickname);
        dest.writeInt(this.unreadMsgCount);
        dest.writeString(this.email);
        dest.writeByte(this.is_bind_google ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_bind_facebook ? (byte) 1 : (byte) 0);
        dest.writeInt(this.coins);
        dest.writeInt(this.bonus);
        dest.writeInt(this.diamonds);
        dest.writeString(this.create_time);
        dest.writeByte(this.is_first_visit ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_premium ? (byte) 1 : (byte) 0);
        dest.writeString(this.premium_started_at);
        dest.writeString(this.premium_expired_at);
    }

    public void readFromParcel(Parcel source) {
        this.account_id = source.readString();
        this.nickname = source.readString();
        this.avatar = source.readString();
        this.created = source.readString();
        this.description = source.readString();
        this.id = source.readInt();
        this.name = source.readString();
        this.mobile = source.readString();
        this.phone = source.readString();
        this.region = source.readString();
        this.country = source.readString();
        this.sub_type = source.readString();
        this.is_author = source.readInt();
        this.sex = source.readInt();
        this.birthday = source.readString();
        this.is_bind_weixin = source.readByte() != 0;
        this.is_bind_qq = source.readByte() != 0;
        this.weixin_nickname = source.readString();
        this.qq_nickname = source.readString();
        this.unreadMsgCount = source.readInt();
        this.email = source.readString();
        this.is_bind_google = source.readByte() != 0;
        this.is_bind_facebook = source.readByte() != 0;
        this.coins = source.readInt();
        this.bonus = source.readInt();
        this.diamonds = source.readInt();
        this.create_time = source.readString();
        this.is_first_visit = source.readByte() != 0;
        this.is_premium = source.readByte() != 0;
        this.premium_started_at = source.readString();
        this.premium_expired_at = source.readString();
        this.activity_cycle = source.readInt();
    }

    protected User(Parcel in) {
        this.account_id = in.readString();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.created = in.readString();
        this.description = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.mobile = in.readString();
        this.phone = in.readString();
        this.region = in.readString();
        this.country = in.readString();
        this.sub_type = in.readString();
        this.is_author = in.readInt();
        this.sex = in.readInt();
        this.birthday = in.readString();
        this.is_bind_weixin = in.readByte() != 0;
        this.is_bind_qq = in.readByte() != 0;
        this.weixin_nickname = in.readString();
        this.qq_nickname = in.readString();
        this.unreadMsgCount = in.readInt();
        this.email = in.readString();
        this.is_bind_google = in.readByte() != 0;
        this.is_bind_facebook = in.readByte() != 0;
        this.coins = in.readInt();
        this.bonus = in.readInt();
        this.diamonds = in.readInt();
        this.create_time = in.readString();
        this.is_first_visit = in.readByte() != 0;
        this.is_premium = in.readByte() != 0;
        this.premium_started_at = in.readString();
        this.premium_expired_at = in.readString();
        this.activity_cycle = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
