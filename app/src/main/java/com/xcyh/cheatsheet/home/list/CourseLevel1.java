package com.xcyh.cheatsheet.home.list;

import java.util.List;

/**
 * 三级菜单的一级节点。
 */
public class CourseLevel1 {

    public String name;
    public List<CourseLevel2> level2Items;

    public CourseLevel1(String name, List<CourseLevel2> level2Items) {
        this.name = name;
        this.level2Items = level2Items;
    }
}
