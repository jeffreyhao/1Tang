package com.github.bean.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2018/7/9 下午6:06.
 * 
 * Description:
 */
public class Category implements Parcelable {
    private int id;
    private String name;
    private List<Category> children;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public boolean hasChildren() {
        return !CollectionUtils.isEmpty(children);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.children);
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.children = new ArrayList<Category>();
        in.readList(this.children, Category.class.getClassLoader());
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
