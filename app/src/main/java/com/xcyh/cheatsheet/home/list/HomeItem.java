package com.xcyh.cheatsheet.home.list;

/**
 * Created by haojiangfeng on 2025/6/30.
 */
public class HomeItem {

    public static final int ITEM_TEST           = 100;
    public static final int ITEM_SCHEME         = 101;
    public static final int ITEM_APP_LINKS      = 102;


    public int type;
    public String title;

    public HomeItem(int type, String title){
        this.type = type;
        this.title = title;
    }
}
