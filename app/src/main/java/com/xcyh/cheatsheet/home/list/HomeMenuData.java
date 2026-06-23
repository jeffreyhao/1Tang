package com.xcyh.cheatsheet.home.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 三级菜单占位数据。后续接入真实数据时，只改本类的 getDemoCourseData()。
 */
public final class HomeMenuData {

    private HomeMenuData() {
    }

    public static List<CourseLevel1> getDemoCourseData() {
        List<CourseLevel1> data = new ArrayList<>();

        data.add(new CourseLevel1("时间管理", Arrays.asList(
                new CourseLevel2("时间管理", Collections.singletonList(
                        "全员必修：时间管理必修课"))
        )));

        data.add(new CourseLevel1("不断提认知", Arrays.asList(
                new CourseLevel2("科学学习", Arrays.asList(
                        "IPO认知篇：重新理解「科学学习」",
                        "IPO实操篇：IPO落地武器库")),
                new CourseLevel2("深度复盘", Collections.singletonList(
                        "全员必修：深度复盘第一课")),
                new CourseLevel2("知识管理", Collections.singletonList(
                        "全员必修：知识管理必修课"))
        )));

        data.add(new CourseLevel1("不断练能力", Arrays.asList(
                new CourseLevel2("刻意练习", Collections.singletonList(
                        "刻意练习：重新理解「科学成长」")),
                new CourseLevel2("练记笔记", Collections.singletonList(
                        "一堂笔记法：清单式解题练习")),
                new CourseLevel2("灵感闪现", Arrays.asList(
                        "全员必修：灵感闪现认知篇",
                        "全员必修：灵感闪现工具篇")),
                new CourseLevel2("写逐字稿", Collections.singletonList(
                        "逐字稿实操：从入门到高手")),
                new CourseLevel2("卖点讲香", Collections.singletonList(
                        "讲香实操：一堂十指模型"))
        )));

        data.add(new CourseLevel1("形成竞争力", Arrays.asList(
                new CourseLevel2("表达力", Arrays.asList(
                        "表达力1：科学表达必修课",
                        "表达力2：开始练习公开演讲")),
                new CourseLevel2("设计力", Arrays.asList(
                        "全员必修：泛产品设计认知篇",
                        "全员必修：泛产品设计框架篇",
                        "泛产品实操1：需求篇",
                        "泛产品实操2：审美篇",
                        "泛产品实操3：落地篇")),
                new CourseLevel2("AI力", Arrays.asList(
                        "全员必修：重新理解「人工智能」",
                        "人工智能入门：提示词必修课",
                        "全员必修：AI上手第一课",
                        "全员必修：AI场景第一课"))
        )));

        data.add(new CourseLevel1("人生红点", Arrays.asList(
                new CourseLevel2("人生红点", Arrays.asList(
                        "全员必修：人生红点认知篇",
                        "红点实操1：笃定红点篇",
                        "红点实操2：路径规划篇"))
        )));

        return data;
    }
}
