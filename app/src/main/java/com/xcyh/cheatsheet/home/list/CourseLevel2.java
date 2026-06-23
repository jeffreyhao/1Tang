package com.xcyh.cheatsheet.home.list;

import java.util.List;

/**
 * 三级菜单的二级节点。
 */
public class CourseLevel2 {

    public String name;
    public List<String> level3Items;

    public CourseLevel2(String name, List<String> level3Items) {
        this.name = name;
        this.level3Items = level3Items;
    }
}
