package com.github.bean.reader;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.bean.user.User;

import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2018/9/5 下午7:48.
 * 
 * Description: 评论的model
 */
public class CommentItem implements Parcelable {
    public String content;
    public int id;
    public String created;
    public String createdFormated;
    public User user;
    public int replies;
    public String replyName;
    public String bid;
    public List<CommentItem> children;
    public boolean isTop;       //1 置顶 0正常
    public boolean isAuthor;    //1 作者 0正常
    public String commentType; //"NORMAL" 正常评论 "REWARD" 打赏
    public boolean isDelete;

    public CommentItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeInt(this.id);
        dest.writeString(this.created);
        dest.writeString(this.createdFormated);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.replies);
        dest.writeString(this.replyName);
        dest.writeString(this.bid);
        dest.writeTypedList(this.children);
        dest.writeByte(this.isTop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAuthor ? (byte) 1 : (byte) 0);
        dest.writeString(this.commentType);
        dest.writeByte(this.isDelete ? (byte) 1 : (byte) 0);
    }

    protected CommentItem(Parcel in) {
        this.content = in.readString();
        this.id = in.readInt();
        this.created = in.readString();
        this.createdFormated = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.replies = in.readInt();
        this.replyName = in.readString();
        this.bid = in.readString();
        this.children = in.createTypedArrayList(CommentItem.CREATOR);
        this.isTop = in.readByte() != 0;
        this.isAuthor = in.readByte() != 0;
        this.commentType = in.readString();
        this.isDelete = in.readByte() != 0;
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel source) {
            return new CommentItem(source);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };
}
