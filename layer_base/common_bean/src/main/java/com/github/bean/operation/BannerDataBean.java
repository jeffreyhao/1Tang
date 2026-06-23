package com.github.bean.operation;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/**
 * Banner数据Bean
 */
public class BannerDataBean implements Parcelable {

    // --- 字段 (Fields) ---
    private final String title;
    private final String book_title;
    private final String img;
    private final String book_cover;
    private final int banner_type;
    private final String com_book_id;
    private final String book_id;
    private final String external_cp_book_id;
    private final String jump_page;
    private final String book_des;
    private final String book_category;
    private final String book_category_name;
    private final String id;
    private final int show_type;
    private final int position;
    private final String create_time;
    private final String update_time;

    // --- 构造函数 (Constructors) ---

    /**
     * 全参数构造函数，用于创建新实例。
     */
    public BannerDataBean(String title, String book_title, String img, String book_cover,
                          int banner_type, String com_book_id, String book_id, String external_cp_book_id,
                          String jump_page, String book_des, String book_category,
                          String book_category_name, String id, int show_type,
                          int position, String create_time, String update_time) {
        this.title = title;
        this.book_title = book_title;
        this.img = img;
        this.book_cover = book_cover;
        this.banner_type = banner_type;
        this.com_book_id = com_book_id;
        this.book_id = book_id;
        this.external_cp_book_id = external_cp_book_id;
        this.jump_page = jump_page;
        this.book_des = book_des;
        this.book_category = book_category;
        this.book_category_name = book_category_name;
        this.id = id;
        this.show_type = show_type;
        this.position = position;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    /**
     * 从Parcel中重建对象的私有构造函数。
     * @param in 包含序列化对象数据的Parcel。
     */
    private BannerDataBean(Parcel in) {
        // 读取顺序必须与 writeToParcel 中的写入顺序完全一致
        title = in.readString();
        book_title = in.readString();
        img = in.readString();
        book_cover = in.readString();
        banner_type = in.readInt();
        com_book_id = in.readString();
        book_id = in.readString();
        external_cp_book_id = in.readString();
        jump_page = in.readString();
        book_des = in.readString();
        book_category = in.readString();
        book_category_name = in.readString();
        id = in.readString();
        show_type = in.readInt();
        position = in.readInt();
        create_time = in.readString();
        update_time = in.readString();
    }

    // --- Parcelable 接口实现 ---

    @Override
    public int describeContents() {
        // 通常返回0，除非有文件描述符需要处理。
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 将对象数据写入Parcel。顺序非常重要！
        dest.writeString(title);
        dest.writeString(book_title);
        dest.writeString(img);
        dest.writeString(book_cover);
        dest.writeInt(banner_type);
        dest.writeString(com_book_id);
        dest.writeString(book_id);
        dest.writeString(external_cp_book_id);
        dest.writeString(jump_page);
        dest.writeString(book_des);
        dest.writeString(book_category);
        dest.writeString(book_category_name);
        dest.writeString(id);
        dest.writeInt(show_type);
        dest.writeInt(position);
        dest.writeString(create_time);
        dest.writeString(update_time);
    }

    /**
     * Parcelable接口要求的CREATOR字段，用于从Parcel创建对象实例。
     */
    public static final Parcelable.Creator<BannerDataBean> CREATOR = new Parcelable.Creator<BannerDataBean>() {
        @Override
        public BannerDataBean createFromParcel(Parcel in) {
            return new BannerDataBean(in);
        }

        @Override
        public BannerDataBean[] newArray(int size) {
            return new BannerDataBean[size];
        }
    };

    // --- Getter 方法 (Getters) ---
    // 为每个final字段提供getter方法

    public String getTitle() {
        return title;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getImg() {
        return img;
    }

    public String getBook_cover() {
        return book_cover;
    }

    public int getBanner_type() {
        return banner_type;
    }

    public String getCom_book_id() {
        return com_book_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getExternal_cp_book_id() {
        return external_cp_book_id;
    }

    public String getJump_page() {
        return jump_page;
    }

    public String getBook_des() {
        return book_des;
    }

    public String getBook_category() {
        return book_category;
    }

    public String getBook_category_name() {
        return book_category_name;
    }

    public String getId() {
        return id;
    }

    public int getShow_type() {
        return show_type;
    }

    public int getPosition() {
        return position;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }


    // --- data class 行为模拟 (equals, hashCode, toString) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BannerDataBean that = (BannerDataBean) o;
        return banner_type == that.banner_type &&
                show_type == that.show_type &&
                position == that.position &&
                Objects.equals(title, that.title) &&
                Objects.equals(book_title, that.book_title) &&
                Objects.equals(img, that.img) &&
                Objects.equals(book_cover, that.book_cover) &&
                Objects.equals(com_book_id, that.com_book_id) &&
                Objects.equals(book_id, that.book_id) &&
                Objects.equals(external_cp_book_id, that.external_cp_book_id) &&
                Objects.equals(jump_page, that.jump_page) &&
                Objects.equals(book_des, that.book_des) &&
                Objects.equals(book_category, that.book_category) &&
                Objects.equals(book_category_name, that.book_category_name) &&
                Objects.equals(id, that.id) &&
                Objects.equals(create_time, that.create_time) &&
                Objects.equals(update_time, that.update_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, book_title, img, book_cover, banner_type, com_book_id, book_id, external_cp_book_id, jump_page, book_des, book_category, book_category_name, id, show_type, position, create_time, update_time);
    }

    @Override
    public String toString() {
        return "BannerDataBean{" +
                "title='" + title + '\'' +
                ", book_title='" + book_title + '\'' +
                ", img='" + img + '\'' +
                ", book_cover='" + book_cover + '\'' +
                ", banner_type=" + banner_type +
                ", com_book_id='" + com_book_id + '\'' +
                ", book_id='" + book_id + '\'' +
                ", external_cp_book_id='" + external_cp_book_id + '\'' +
                ", jump_page='" + jump_page + '\'' +
                ", book_des='" + book_des + '\'' +
                ", book_category='" + book_category + '\'' +
                ", book_category_name='" + book_category_name + '\'' +
                ", id='" + id + '\'' +
                ", show_type=" + show_type +
                ", position=" + position +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
