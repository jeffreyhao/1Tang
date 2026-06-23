package com.github.bean.database;

import android.text.TextUtils;

import com.base.api.GlobalContext;
import com.base.net.cache.ACache;
import com.base.util.collection.ListUtil;
import com.base.util.content.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haojiangfeng on 2024/2/18.
 */
class DbUtil {


    /**
     * 是否需要隐藏此书
     * @param bookId
     * @return
     */
    public static boolean shouldHideBook(String bookId) {
        if(TextUtils.isEmpty(bookId)) {
            return true;
        }
        List<String> hideBooks = getHideBooks();
        return !ListUtil.isEmpty(hideBooks) && hideBooks.contains(bookId);
    }

    public static List<String> getHideBooks() {
        List<String> idList = new ArrayList<>();
        if(GlobalContext.getContext()!=null) {
            try {
                String json = ACache.get(GlobalContext.getContext()).getAsString("hideBooks");
                if(!TextUtils.isEmpty(json)) {
                    idList =  GsonUtil.parseJsonToList(json);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return idList;
            }
        }
        return idList;
    }

}
