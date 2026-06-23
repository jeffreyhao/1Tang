package com.github.bean.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Time: 2024/4/22
 * Author: lhc
 * Desc:
 */
public class PreferenceTagBean implements Parcelable {
    @SerializedName("user_tags")
    private UserTagBean userTags;
    @SerializedName("hot_tags")
    private List<HotTagBean> hotTags;

    public UserTagBean getUserTags() {
        return userTags;
    }

    public void setUserTags(UserTagBean userTags) {
        this.userTags = userTags;
    }

    public List<HotTagBean> getHotTags() {
        return hotTags;
    }

    public void setHotTags(List<HotTagBean> hotTags) {
        this.hotTags = hotTags;
    }


    public static class UserTagBean implements Parcelable {
        private String account_id;
        private List<TagBean> tags;

        public String getAccount_id() {
            return account_id;
        }

        public void setAccount_id(String account_id) {
            this.account_id = account_id;
        }

        public List<TagBean> getTags() {
            return tags;
        }

        public void setTags(List<TagBean> tags) {
            this.tags = tags;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.account_id);
            dest.writeTypedList(this.tags);
        }

        public void readFromParcel(Parcel source) {
            this.account_id = source.readString();
            this.tags = source.createTypedArrayList(TagBean.CREATOR);
        }

        public UserTagBean() {
        }

        protected UserTagBean(Parcel in) {
            this.account_id = in.readString();
            this.tags = in.createTypedArrayList(TagBean.CREATOR);
        }

        public static final Creator<UserTagBean> CREATOR = new Creator<UserTagBean>() {
            @Override
            public UserTagBean createFromParcel(Parcel source) {
                return new UserTagBean(source);
            }

            @Override
            public UserTagBean[] newArray(int size) {
                return new UserTagBean[size];
            }
        };
    }

    public static class TagBean implements Parcelable {
        private int tag_id;
        private String tag_name;

        public int getTag_id() {
            return tag_id;
        }

        public void setTag_id(int tag_id) {
            this.tag_id = tag_id;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.tag_id);
            dest.writeString(this.tag_name);
        }

        public void readFromParcel(Parcel source) {
            this.tag_id = source.readInt();
            this.tag_name = source.readString();
        }

        public TagBean() {
        }

        protected TagBean(Parcel in) {
            this.tag_id = in.readInt();
            this.tag_name = in.readString();
        }

        public static final Creator<TagBean> CREATOR = new Creator<TagBean>() {
            @Override
            public TagBean createFromParcel(Parcel source) {
                return new TagBean(source);
            }

            @Override
            public TagBean[] newArray(int size) {
                return new TagBean[size];
            }
        };
    }

    public static class HotTagBean implements Parcelable {
        private String id;
        private int tag_id;
        private String tag_name;
        private String tag_language;

        private boolean isSelected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTag_id() {
            return tag_id;
        }

        public void setTag_id(int tag_id) {
            this.tag_id = tag_id;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public String getTag_language() {
            return tag_language;
        }

        public void setTag_language(String tag_language) {
            this.tag_language = tag_language;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeInt(this.tag_id);
            dest.writeString(this.tag_name);
            dest.writeString(this.tag_language);
            dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        }

        public void readFromParcel(Parcel source) {
            this.id = source.readString();
            this.tag_id = source.readInt();
            this.tag_name = source.readString();
            this.tag_language = source.readString();
            this.isSelected = source.readByte() != 0;
        }

        public HotTagBean() {
        }

        protected HotTagBean(Parcel in) {
            this.id = in.readString();
            this.tag_id = in.readInt();
            this.tag_name = in.readString();
            this.tag_language = in.readString();
            this.isSelected = in.readByte() != 0;
        }

        public static final Creator<HotTagBean> CREATOR = new Creator<HotTagBean>() {
            @Override
            public HotTagBean createFromParcel(Parcel source) {
                return new HotTagBean(source);
            }

            @Override
            public HotTagBean[] newArray(int size) {
                return new HotTagBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userTags, flags);
        dest.writeTypedList(this.hotTags);
    }

    public void readFromParcel(Parcel source) {
        this.userTags = source.readParcelable(UserTagBean.class.getClassLoader());
        this.hotTags = source.createTypedArrayList(HotTagBean.CREATOR);
    }

    public PreferenceTagBean() {
    }

    protected PreferenceTagBean(Parcel in) {
        this.userTags = in.readParcelable(UserTagBean.class.getClassLoader());
        this.hotTags = in.createTypedArrayList(HotTagBean.CREATOR);
    }

    public static final Creator<PreferenceTagBean> CREATOR = new Creator<PreferenceTagBean>() {
        @Override
        public PreferenceTagBean createFromParcel(Parcel source) {
            return new PreferenceTagBean(source);
        }

        @Override
        public PreferenceTagBean[] newArray(int size) {
            return new PreferenceTagBean[size];
        }
    };
}
