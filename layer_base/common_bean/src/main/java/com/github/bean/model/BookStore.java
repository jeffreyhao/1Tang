package com.github.bean.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baidu.baselibrary.util.sys.LogUtil;
import com.fold.recyclyerview.entity.MultiItemEntity;
import com.github.bean.operation.BannerDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 书库首页的model
 */
public class BookStore implements MultiItemEntity, Parcelable {

    public static final int DISPLAY_TEMPLATE_BANNER         = 1;    // Banner
    public static final int DISPLAY_TEMPLATE_3LINE          = 15;   // 横向三行可滑动 //83;
    public static final int DISPLAY_TEMPLATE_LINE_SMALL     = 4;    // 横向一行可滑动（小图） //82;
    public static final int DISPLAY_TEMPLATE_LINE_VIEW_NUM  = 5;    // 横向一行可滑动，大图带观看人数
    public static final int DISPLAY_TEMPLATE_LINE_VIEW_URI  = 20;
    public static final int ROW2_COLUMN_N                   = 7;    // 2行n列可滑动
    public static final int ROW2_COLUMN3_FIX                = 12;   // 2行3列固定
    public static final int ROW1_COLUMN3_FIX                = 18;   // 1行3列
    public static final int ROW_N_COLUMN1                   = 19;   // 一行n列加载等多
//    public static final int DISPLAY_TEMPLATE_LINE_VIEW_NUM = 13;//80;

    public static final int DISPLAY_TEMPLATE_LINE           = 81;   // 横向一行可滑动，大图不带观看人数
    public static final int ROW_N_COLUMN_SCROLL             = 6;    // n行1列可滑动
    public static final int ROW_N_COLUMN                    = 21;   // n行1列不可滑动
    public static final int ROW2_COLUMN4                    = 22;   // 2行4列
    public static final int ROW1_SCROLL_TRANSFORM           = 23;   // 1行滑动缩放
    public static final int RANKING                         = 24;   // 排行


    public static final int CUSTOM_INFINITY = Integer.MAX_VALUE - 1000;       // 自定义的无限加载模块



    public ModuleBean module;
    public List<DataBean> module_data;
    public List<DataBean> module_force_data;
    public List<BannerDataBean> banner_info;
    public String uri;

    public ModuleBean getModule() {
        return module;
    }

    public void setModule(ModuleBean module) {
        this.module = module;
    }

    public List<DataBean> getModuleData() {
        return module_data;
    }

    public void setData(List<DataBean> data) {
        this.module_data = data;
    }

    public List<DataBean> getModule_data() {
        return module_data;
    }

    public void setModule_data(List<DataBean> module_data) {
        this.module_data = module_data;
    }

    public List<DataBean> getModule_force_data() {
        return module_force_data;
    }

    public void setModule_force_data(List<DataBean> module_force_data) {
        this.module_force_data = module_force_data;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<BannerDataBean> getBanner_info() {
        return banner_info;
    }

    public void setBanner_info(List<BannerDataBean> banner_info) {
        this.banner_info = banner_info;
    }

    @Override
    public int getItemType() {
        return module.module_type_code;
    }

    public static class ModuleBean implements Parcelable {
        public String name;
        public String url;
        public String unfoldTip;
        public int templateId;
        public int type;
        public int id;
        public int module_id;
        public int module_type_code;
        public String description;
        public String icon;
        public int is_dynamic_data;
        public int dynamic_books_num;
        public int module_min_show_number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUnfoldTip() {
            return unfoldTip;
        }

        public void setUnfoldTip(String unfoldTip) {
            this.unfoldTip = unfoldTip;
        }

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getModule_id() {
            return module_id;
        }

        public void setModule_id(int module_id) {
            this.module_id = module_id;
        }

        public int getModule_type_code() {
            return module_type_code;
        }

        public void setModule_type_code(int module_type_code) {
            this.module_type_code = module_type_code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getDynamic_books_num() {
            return dynamic_books_num;
        }

        public void setDynamic_books_num(int dynamic_books_num) {
            this.dynamic_books_num = dynamic_books_num;
        }

        public int getIs_dynamic_data() {
            return is_dynamic_data;
        }

        public void setIs_dynamic_data(int is_dynamic_data) {
            this.is_dynamic_data = is_dynamic_data;
        }

        public int getModule_min_show_number() {
            return module_min_show_number;
        }

        public void setModule_min_show_number(int module_min_show_number) {
            this.module_min_show_number = module_min_show_number;
        }

        public ModuleBean() {
        }

        public ModuleBean(ModuleBean moduleBean) {
            this.name = moduleBean.name;
            this.url = moduleBean.url;
            this.unfoldTip = moduleBean.unfoldTip;
            this.templateId = moduleBean.templateId;
            this.type = moduleBean.type;
            this.id = moduleBean.id;
            this.module_id = moduleBean.module_id;
            this.module_type_code = moduleBean.module_type_code;
            this.description = moduleBean.description;
            this.icon = moduleBean.icon;
            this.dynamic_books_num = moduleBean.dynamic_books_num;
            this.is_dynamic_data = moduleBean.is_dynamic_data;
            this.module_min_show_number = moduleBean.module_min_show_number;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.url);
            dest.writeString(this.unfoldTip);
            dest.writeInt(this.templateId);
            dest.writeInt(this.type);
            dest.writeInt(this.id);
            dest.writeInt(this.module_id);
            dest.writeInt(this.module_type_code);
            dest.writeString(this.description);
            dest.writeString(this.icon);
            dest.writeInt(this.dynamic_books_num);
            dest.writeInt(this.is_dynamic_data);
            dest.writeInt(this.module_min_show_number);
        }

        public void readFromParcel(Parcel source) {
            this.name = source.readString();
            this.url = source.readString();
            this.unfoldTip = source.readString();
            this.templateId = source.readInt();
            this.type = source.readInt();
            this.id = source.readInt();
            this.module_id = source.readInt();
            this.module_type_code = source.readInt();
            this.description = source.readString();
            this.icon = source.readString();
            this.dynamic_books_num = source.readInt();
            this.is_dynamic_data = source.readInt();
            this.module_min_show_number = source.readInt();
        }

        protected ModuleBean(Parcel in) {
            this.name = in.readString();
            this.url = in.readString();
            this.unfoldTip = in.readString();
            this.templateId = in.readInt();
            this.type = in.readInt();
            this.id = in.readInt();
            this.module_id = in.readInt();
            this.module_type_code = in.readInt();
            this.description = in.readString();
            this.icon = in.readString();
            this.dynamic_books_num = in.readInt();
            this.is_dynamic_data = in.readInt();
            this.module_min_show_number = in.readInt();
        }

        public static final Creator<ModuleBean> CREATOR = new Creator<ModuleBean>() {
            @Override
            public ModuleBean createFromParcel(Parcel source) {
                return new ModuleBean(source);
            }

            @Override
            public ModuleBean[] newArray(int size) {
                return new ModuleBean[size];
            }
        };
    }

    public static class DataBean implements Parcelable {
        public String extra;
        public String url;
        public String link;
        public String poster;
        public String title;
        public String created;
        public String content;
        public String id;
        public int view_num;
        public String description;
        public String main_category;
        public int status;
        public String name;
        public String background;
        public String book_id;
        public String com_book_id;
        public String external_cp_book_id;
        public String cover;
        public String score;
        public String author_id;
        public String author_name;
        public String book_category_id;
        public String book_category;
        public String pre_book_updated;
        public String latest_book_updated;
        public String create_time;
        public String update_time;
        public String img;
        public int views_count;
        public String intro;
        public int finished;
        public int marketing_type;//营销类型 0无营销类型 1推荐书籍 2 独家推荐书籍 3 免费书籍 4 折扣书籍
        public int discount;
        public int force_index;
        public String category_id;
        public String category_name;
        public List<DataBean> book_list;
        public String rankings_type;
        public String rankings_title;
        public Extra extraHolder;
        public int banner_type;
        public String jump_page;
        public int is_vip;

        public String is_short_story;

        public String getIs_short_story() {
            return is_short_story;
        }

        public void setIs_short_story(String is_short_story) {
            this.is_short_story = is_short_story;
        }

        public boolean isShortBook(){
            if(TextUtils.isEmpty(is_short_story)) {
                return false;
            }
            int value = 0;
            try {
                value = Integer.parseInt(is_short_story);
            } catch (Throwable e) {
                LogUtil.e(e);
            }
            return value == 1;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getView_num() {
            return view_num;
        }

        public void setView_num(int view_num) {
            this.view_num = view_num;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMain_category() {
            return main_category;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setMain_category(String main_category) {
            this.main_category = main_category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getCom_book_id() {
            return com_book_id;
        }

        public void setCom_book_id(String com_book_id) {
            this.com_book_id = com_book_id;
        }

        public String getExternal_cp_book_id() {
            return external_cp_book_id;
        }

        public void setExternal_cp_book_id(String external_cp_book_id) {
            this.external_cp_book_id = external_cp_book_id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getBook_category_id() {
            return book_category_id;
        }

        public void setBook_category_id(String book_category_id) {
            this.book_category_id = book_category_id;
        }

        public String getBook_category() {
            return book_category;
        }

        public void setBook_category(String book_category) {
            this.book_category = book_category;
        }

        public String getPre_book_updated() {
            return pre_book_updated;
        }

        public void setPre_book_updated(String pre_book_updated) {
            this.pre_book_updated = pre_book_updated;
        }

        public String getLatest_book_updated() {
            return latest_book_updated;
        }

        public void setLatest_book_updated(String latest_book_updated) {
            this.latest_book_updated = latest_book_updated;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public Extra getExtraHolder() {
            return extraHolder;
        }

        public void setExtraHolder(Extra extraHolder) {
            this.extraHolder = extraHolder;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getViews_count() {
            return views_count;
        }

        public void setViews_count(int views_count) {
            this.views_count = views_count;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getFinished() {
            return finished;
        }

        public void setFinished(int finished) {
            this.finished = finished;
        }

        public int getMarketing_type() {
            return marketing_type;
        }

        public void setMarketing_type(int marketing_type) {
            this.marketing_type = marketing_type;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public int getForce_index() {
            return force_index;
        }

        public void setForce_index(int force_index) {
            this.force_index = force_index;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public List<DataBean> getBook_list() {
            return book_list;
        }

        public void setBook_list(List<DataBean> book_list) {
            this.book_list = book_list;
        }

        public String getRankings_type() {
            return rankings_type;
        }

        public void setRankings_type(String rankings_type) {
            this.rankings_type = rankings_type;
        }

        public String getRankings_title() {
            return rankings_title;
        }

        public void setRankings_title(String rankings_title) {
            this.rankings_title = rankings_title;
        }

        public DataBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.extra);
            dest.writeString(this.url);
            dest.writeString(this.link);
            dest.writeString(this.poster);
            dest.writeString(this.title);
            dest.writeString(this.created);
            dest.writeString(this.content);
            dest.writeString(this.id);
            dest.writeInt(this.view_num);
            dest.writeString(this.description);
            dest.writeString(this.main_category);
            dest.writeInt(this.status);
            dest.writeString(this.name);
            dest.writeString(this.background);
            dest.writeString(this.book_id);
            dest.writeString(this.com_book_id);
            dest.writeString(this.external_cp_book_id);
            dest.writeString(this.cover);
            dest.writeString(this.score);
            dest.writeString(this.author_id);
            dest.writeString(this.author_name);
            dest.writeString(this.book_category_id);
            dest.writeString(this.book_category);
            dest.writeString(this.pre_book_updated);
            dest.writeString(this.latest_book_updated);
            dest.writeString(this.create_time);
            dest.writeString(this.update_time);
            dest.writeString(this.img);
            dest.writeInt(this.views_count);
            dest.writeString(this.intro);
            dest.writeInt(this.finished);
            dest.writeInt(this.marketing_type);
            dest.writeInt(this.discount);
            dest.writeInt(this.force_index);
            dest.writeString(this.category_id);
            dest.writeString(this.category_name);
            dest.writeTypedList(this.book_list);
            dest.writeParcelable(this.extraHolder, flags);
            dest.writeInt(this.banner_type);
            dest.writeString(this.jump_page);
            dest.writeInt(this.is_vip);
            dest.writeString(this.is_short_story);
        }

        public void readFromParcel(Parcel source) {
            this.extra = source.readString();
            this.url = source.readString();
            this.link = source.readString();
            this.poster = source.readString();
            this.title = source.readString();
            this.created = source.readString();
            this.content = source.readString();
            this.id = source.readString();
            this.view_num = source.readInt();
            this.description = source.readString();
            this.main_category = source.readString();
            this.status = source.readInt();
            this.name = source.readString();
            this.background = source.readString();
            this.book_id = source.readString();
            this.com_book_id = source.readString();
            this.external_cp_book_id = source.readString();
            this.cover = source.readString();
            this.score = source.readString();
            this.author_id = source.readString();
            this.author_name = source.readString();
            this.book_category_id = source.readString();
            this.book_category = source.readString();
            this.pre_book_updated = source.readString();
            this.latest_book_updated = source.readString();
            this.create_time = source.readString();
            this.update_time = source.readString();
            this.img = source.readString();
            this.views_count = source.readInt();
            this.intro = source.readString();
            this.finished = source.readInt();
            this.marketing_type = source.readInt();
            this.discount = source.readInt();
            this.force_index = source.readInt();
            this.category_id = source.readString();
            this.category_name = source.readString();
            this.book_list = source.createTypedArrayList(DataBean.CREATOR);
            this.extraHolder = source.readParcelable(Extra.class.getClassLoader());
            this.banner_type = source.readInt();
            this.jump_page = source.readString();
            this.is_vip = source.readInt();
            this.is_short_story = source.readString();
        }

        protected DataBean(Parcel in) {
            this.extra = in.readString();
            this.url = in.readString();
            this.link = in.readString();
            this.poster = in.readString();
            this.title = in.readString();
            this.created = in.readString();
            this.content = in.readString();
            this.id = in.readString();
            this.view_num = in.readInt();
            this.description = in.readString();
            this.main_category = in.readString();
            this.status = in.readInt();
            this.name = in.readString();
            this.background = in.readString();
            this.book_id = in.readString();
            this.com_book_id = in.readString();
            this.external_cp_book_id = in.readString();
            this.cover = in.readString();
            this.score = in.readString();
            this.author_id = in.readString();
            this.author_name = in.readString();
            this.book_category_id = in.readString();
            this.book_category = in.readString();
            this.pre_book_updated = in.readString();
            this.latest_book_updated = in.readString();
            this.create_time = in.readString();
            this.update_time = in.readString();
            this.img = in.readString();
            this.views_count = in.readInt();
            this.intro = in.readString();
            this.finished = in.readInt();
            this.marketing_type = in.readInt();
            this.discount = in.readInt();
            this.force_index = in.readInt();
            this.category_id = in.readString();
            this.category_name = in.readString();
            this.book_list = in.createTypedArrayList(DataBean.CREATOR);
            this.extraHolder = in.readParcelable(Extra.class.getClassLoader());
            this.banner_type = in.readInt();
            this.jump_page = in.readString();
            this.is_vip = in.readInt();
            this.is_short_story = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    public BookStore() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.module, flags);
        dest.writeList(this.module_data);
        dest.writeList(this.module_force_data);
    }

    public void readFromParcel(Parcel source) {
        this.module = source.readParcelable(ModuleBean.class.getClassLoader());
        this.module_data = new ArrayList<DataBean>();
        this.module_force_data = new ArrayList<DataBean>();
        source.readList(this.module_data, DataBean.class.getClassLoader());
        source.readList(this.module_force_data, DataBean.class.getClassLoader());
    }

    protected BookStore(Parcel in) {
        this.module = in.readParcelable(ModuleBean.class.getClassLoader());
        this.module_data = new ArrayList<DataBean>();
        this.module_force_data = new ArrayList<DataBean>();
        in.readList(this.module_data, DataBean.class.getClassLoader());
        in.readList(this.module_force_data, DataBean.class.getClassLoader());
    }

    public static final Creator<BookStore> CREATOR = new Creator<BookStore>() {
        @Override
        public BookStore createFromParcel(Parcel source) {
            return new BookStore(source);
        }

        @Override
        public BookStore[] newArray(int size) {
            return new BookStore[size];
        }
    };
}
