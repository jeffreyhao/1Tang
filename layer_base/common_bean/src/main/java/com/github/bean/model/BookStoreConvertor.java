package com.github.bean.model;

import com.base.util.collection.ListUtil;
import com.github.bean.operation.BannerDataBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 数据转换器，转换成 BookStore
 *
 * Created by haojiangfeng on 2024/12/6.
 */
public class BookStoreConvertor {


    /**
     * 将 「banner-list」 转换为 「BookStore」
     */
    public static BookStore convertByBannerList(List<BannerDataBean> bannerDataList){
        BookStore bookStore = new BookStore();
        BookStore.ModuleBean moduleBean = new BookStore.ModuleBean();
        moduleBean.module_id = 0;
        moduleBean.module_type_code = 1;
        moduleBean.module_min_show_number = 1;
        bookStore.module = moduleBean;
        bookStore.module_force_data = new ArrayList<>();
        List<BookStore.DataBean> bookDataList = new ArrayList<>();
        for(BannerDataBean item : bannerDataList) {
            BookStore.DataBean bookDataBean = new BookStore.DataBean();
            bookDataBean.title = item.getBook_title();
            bookDataBean.book_id = item.getBook_id();
            bookDataBean.com_book_id = item.getCom_book_id();
            bookDataBean.img = item.getImg();
            bookDataBean.cover = item.getBook_cover();
            bookDataBean.book_category = item.getBook_category();
            bookDataBean.create_time = item.getCreate_time();
            bookDataBean.update_time = item.getUpdate_time();
            bookDataBean.banner_type = item.getBanner_type();
            bookDataBean.jump_page = item.getJump_page();
            bookDataBean.book_category_id = item.getBook_category();
            bookDataBean.book_category = item.getBook_category_name();
            bookDataList.add(bookDataBean);
        }
        bookStore.module_data = bookDataList;
        return bookStore;
    }


    /**
     * 加载更多模块，将每一条 「BookStore.DataBean」 都转换为 单独的一条 「BookStore」
     *
     * @param page          页码
     * @param module        模块。用来显示更多模块的title等
     * @param bookList      书籍list
     */
    public static List<BookStore> convertByBooks(int page, BookStore.ModuleBean module, List<BookStore.DataBean> bookList){
        List<BookStore> list = new ArrayList<>();
        if(module == null || ListUtil.isEmpty(bookList)){
            return list;
        }

        for(int i = 0; i < bookList.size(); i++){
            BookStore.DataBean dataBean = bookList.get(i);
            BookStore bookStore = new BookStore();
            bookStore.module = new BookStore.ModuleBean(module);
            bookStore.module.module_type_code = BookStore.CUSTOM_INFINITY;
            if(page == 1 && i == 0){
                bookStore.module.name = module.name;
            } else {
                bookStore.module.name = "";
            }
            bookStore.module_data = new ArrayList<>();
            bookStore.module_data.add(dataBean);
            list.add(bookStore);
        }

        return list;
    }



    public static List<BookStore> generateShuffledList(List<BookStore> list)  {
        if (ListUtil.isNotEmpty(list)) {
            List<BookStore> tempList = new ArrayList<>(list);
            for (BookStore it: tempList){
                if (it.module != null) {
                    if (!ListUtil.isNullOrEmpty(it.getModuleData())) {
                        if (it.module.module_type_code != 24) {
                            Collections.shuffle(it.module_data);
                        } else {
                            for (BookStore.DataBean dataBean : it.module_data) {
                                if (!ListUtil.isNullOrEmpty(dataBean.book_list)) {
                                    Collections.shuffle(dataBean.book_list);
                                }
                            }
                        }
                    }
                }
                if (it.module != null && !ListUtil.isNullOrEmpty(it.module_force_data)) {
                    if (it.module.module_type_code == 24){
                        for (BookStore.DataBean forceDataBean : it.module_force_data) {
                            if (!ListUtil.isNullOrEmpty(it.module_data)) {
                                for (BookStore.DataBean item : it.module_data) {
                                    if (item != null) {
                                        if (!ListUtil.isNullOrEmpty(item.book_list)) {
                                            if (forceDataBean.force_index < item.book_list.size()) {
                                                item.book_list.set(forceDataBean.force_index, forceDataBean);
                                            } else {
                                                item.book_list.add(forceDataBean);
                                            }
                                        } else {
                                            item.book_list = new ArrayList<>();
                                            item.book_list.add(forceDataBean);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        for(BookStore.DataBean dataBean : it.module_force_data){
                            if (!ListUtil.isNullOrEmpty(it.module_data)) {
                                if (dataBean.force_index < it.module_data.size()) {
                                    it.module_data.set(dataBean.force_index, dataBean);
                                } else {
                                    it.module_data.add(dataBean);
                                }
                            } else {
                                it.module_data = new ArrayList<>();
                                it.module_data.add(dataBean);
                            }
                        }
                    }
                }
            }
            return tempList;
        } else {
            return new ArrayList<>();
        }
    }
}
