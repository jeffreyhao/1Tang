package com.benefit.novelverse.view.actvitity;

import android.os.Bundle;


import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.main.MainTabConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haojiangfeng on 2023/11/14.
 */
public class MainTab {


    public static class ItemInfo {
        public CustomFragment fragment;
        public Bundle saveInstanceStateBundle;

        // 这几个，是神策统计用
        public String pageName;
        public String pageElementName;
        public String pageElementId;
    }


    public static List<ItemInfo> buildItems(List<MainTabConfig.ItemStyle> mainConfigs) {
        List<ItemInfo> list = new ArrayList<>();
        for(MainTabConfig.ItemStyle itemStyle : mainConfigs){
            ItemInfo item = new ItemInfo();
            item.pageName = itemStyle.pageName;
            item.pageElementName = itemStyle.pageElementName;
            item.pageElementId = itemStyle.pageElementId;
            list.add(item);
        }
        return list;
    }

}
