# HomeListFragment 三级联动菜单改造 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 把 `HomeListFragment` 从单列表改造成左右双栏三级联动菜单（左：L1标题+L2项；右：L1标题+L2标题+L3项），点击左栏 L2 联动右栏滚动，滚动右栏联动左栏高亮，先用占位课程数据。

**Architecture:** Fragment 改继承 `BindingCustomFragment`，持有左右两个 RecyclerView + 两个 `BaseMultiItemQuickAdapter`。初始化时把 `List<CourseLevel1>` 扁平化为两个适配器的 item 列表，并预建「右栏位置↔(L1,L2)」双向映射表驱动联动。

**Tech Stack:** Java、DataBinding、RecyclerView、`com.fold.recyclyerview.BaseMultiItemQuickAdapter` / `MultiItemEntity` / `BaseViewHolder`（来自 `layer_base:RecyclerViewHelper`）、`BindingCustomFragment`（来自 `layer_base:commonlibrary`）。

**模块/包：** `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/`，资源在 `CheatSheet/app/src/main/res/`，R 类为 `com.benefit.novelverse.R`（见现有 `HomeListAdapter`）。

**参考：** 设计文档 `docs/superpowers/specs/2026-06-23-home-list-three-level-menu-design.md`。

**已知限制：** CheatSheet/app 模块当前编译不过（缺 vest 依赖 + 包路径不匹配 + 缺 zhifu bean），与本次改造无关。本计划末尾只做静态自检（import 完整、资源名一致、viewtype 常量一致），不跑 gradle。

---

## 关键约定（后续 Task 必须严格遵守）

- **资源 R 类前缀**：布局/控件 id 引用统一用 `com.benefit.novelverse.R`（与现有 `HomeListAdapter` 一致）。
- **颜色资源**（已确认存在于 `CheatSheet/app/src/main/res/values/colors.xml`）：
  - `@color/colorAccent` = `#559AF0`（L1/L2 标题主色、选中 L2 项文字主色）
  - `@color/divider_line_color_vertical` = `#ECECEC`（中间分隔线）
  - `@color/darkColorAccent` = `#666666`（未选中 L2 项文字色）
  - `@color/text_color_primary` = `@color/black`（L3 项文字色）
  - 新增 `@color/menu_selected_bg` = `#1A559AF0`（选中 L2 项背景，10% 蓝）
- **字号**：直接用 sp 字面量（项目 `dimens.xml` 无字号资源）：L1 标题 `18sp`，L2 标题 `16sp`，L2/L3 项 `14sp`。
- **viewtype 常量**：左栏用 `TYPE_L1_HEADER=1`、`TYPE_L2_ITEM=2`；右栏用 `TYPE_L1_HEADER=1`、`TYPE_L2_HEADER=2`、`TYPE_L3_ITEM=3`。

---

## 文件结构

**新建（`home/list/`）：**
- `CourseLevel1.java` — L1 数据模型
- `CourseLevel2.java` — L2 数据模型
- `HomeMenuData.java` — 占位课程数据
- `LeftMenuItem.java` — 左栏扁平 item（实现 `MultiItemEntity`，L1标题/L2项两态）
- `RightMenuItem.java` — 右栏扁平 item（实现 `MultiItemEntity`，L1/L2/L3 三态）
- `MenuLeftAdapter.java` — 左栏适配器
- `MenuRightAdapter.java` — 右栏适配器

**新建（`res/layout/`）：**
- `fragment_home_list.xml` — 双栏根布局（DataBinding）
- `item_menu_l1_header.xml` — L1 标题（左/右复用）
- `item_menu_l2_item.xml` — 左栏 L2 项
- `item_menu_l2_header.xml` — 右栏 L2 标题
- `item_menu_l3_item.xml` — 右栏 L3 叶子项

**修改：**
- `CheatSheet/app/src/main/res/values/colors.xml` — 新增 `menu_selected_bg`
- `HomeListFragment.java` — 换基类 + 双栏 + 联动（重写）

**不动：** `HomeListAdapter.java`、`HomeItem.java`、`HomeListPresenter.java`、`IHomeListView.java`。

---

## Task 1: 新增颜色资源

**Files:**
- Modify: `CheatSheet/app/src/main/res/values/colors.xml`

- [ ] **Step 1: 新增 `menu_selected_bg` 颜色**

在 `CheatSheet/app/src/main/res/values/colors.xml` 的 `<resources>` 内（`color_ripple` 那行之后）插入：

```xml
    <color name="menu_selected_bg">#1A559AF0</color>   <!-- 三级菜单选中项背景（10% 蓝）-->
```

- [ ] **Step 2: Commit**

```bash
git add CheatSheet/app/src/main/res/values/colors.xml
git commit -m "feat: 新增三级菜单选中项背景色 menu_selected_bg"
```

---

## Task 2: 数据模型 + 占位数据

**Files:**
- Create: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/CourseLevel1.java`
- Create: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/CourseLevel2.java`
- Create: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/HomeMenuData.java`

- [ ] **Step 1: 创建 `CourseLevel2.java`**

```java
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
```

- [ ] **Step 2: 创建 `CourseLevel1.java`**

```java
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
```

- [ ] **Step 3: 创建 `HomeMenuData.java`（占位课程数据，照参考代码）**

```java
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
```

- [ ] **Step 4: Commit**

```bash
git add CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/CourseLevel1.java \
        CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/CourseLevel2.java \
        CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/HomeMenuData.java
git commit -m "feat: 新增三级菜单数据模型与占位课程数据"
```

---

## Task 3: 左栏扁平 item 与适配器

**Files:**
- Create: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/LeftMenuItem.java`
- Create: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/MenuLeftAdapter.java`
- Create: `CheatSheet/app/src/main/res/layout/item_menu_l1_header.xml`
- Create: `CheatSheet/app/src/main/res/layout/item_menu_l2_item.xml`

- [ ] **Step 1: 创建 `LeftMenuItem.java`（实现 MultiItemEntity，两态：L1标题 / L2项）**

```java
package com.xcyh.cheatsheet.home.list;

import com.fold.recyclyerview.entity.MultiItemEntity;

/**
 * 左栏扁平化后的列表项。
 * - L1 标题：不可点击
 * - L2 项：可点击，记录所属 (l1Index, l2Index)
 */
public class LeftMenuItem implements MultiItemEntity {

    public static final int TYPE_L1_HEADER = 1;
    public static final int TYPE_L2_ITEM = 2;

    public final int type;
    public final String text;
    public final int l1Index;   // 仅 TYPE_L2_ITEM 有效
    public final int l2Index;   // 仅 TYPE_L2_ITEM 有效

    private LeftMenuItem(int type, String text, int l1Index, int l2Index) {
        this.type = type;
        this.text = text;
        this.l1Index = l1Index;
        this.l2Index = l2Index;
    }

    public static LeftMenuItem ofL1Header(String text) {
        return new LeftMenuItem(TYPE_L1_HEADER, text, -1, -1);
    }

    public static LeftMenuItem ofL2Item(String text, int l1Index, int l2Index) {
        return new LeftMenuItem(TYPE_L2_ITEM, text, l1Index, l2Index);
    }

    @Override
    public int getItemType() {
        return type;
    }
}
```

- [ ] **Step 2: 创建 `item_menu_l1_header.xml`（L1 标题，左/右复用）**

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/tv_title"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:textColor="@color/colorAccent"
    android:textSize="18sp"
    android:textStyle="bold"
    android:paddingHorizontal="12dp"
    android:paddingTop="12dp"
    android:paddingBottom="4dp"
    tools:text="时间管理" />
```

- [ ] **Step 3: 创建 `item_menu_l2_item.xml`（左栏 L2 可点击项，根容器带背景）**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/fl_root"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/tv_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:paddingHorizontal="12dp"
        android:paddingVertical="10dp"
        android:textColor="@color/darkColorAccent"
        android:textSize="14sp"
        tools:text="科学学习" />
</FrameLayout>
```

- [ ] **Step 4: 创建 `MenuLeftAdapter.java`**

```java
package com.xcyh.cheatsheet.home.list;

import android.graphics.Typeface;
import android.view.View;

import com.benefit.novelverse.R;
import com.fold.recyclyerview.BaseMultiItemQuickAdapter;
import com.fold.recyclyerview.BaseViewHolder;

import java.util.List;

/**
 * 左栏适配器：L1 标题 + L2 项混排。
 * 通过 {@link #setSelected(int, int)} 切换 L2 选中态（背景高亮 + 文字主色加粗）。
 */
public class MenuLeftAdapter extends BaseMultiItemQuickAdapter<LeftMenuItem, BaseViewHolder> {

    /** 左栏 L2 被点击回调 */
    public interface OnLeftItemSelectedListener {
        void onL2Selected(int l1Index, int l2Index);
    }

    private OnLeftItemSelectedListener mListener;

    /** 当前选中 L2，初始 (0,0) */
    private int selectedL1 = 0;
    private int selectedL2 = 0;

    public MenuLeftAdapter(List<LeftMenuItem> data) {
        super(data);
        addItemType(LeftMenuItem.TYPE_L1_HEADER, R.layout.item_menu_l1_header);
        addItemType(LeftMenuItem.TYPE_L2_ITEM, R.layout.item_menu_l2_item);
    }

    public void setOnLeftItemSelectedListener(OnLeftItemSelectedListener listener) {
        this.mListener = listener;
    }

    /**
     * 更新选中态，局部刷新旧/新两项。
     */
    public void setSelected(int l1Index, int l2Index) {
        if (l1Index == selectedL1 && l2Index == selectedL2) {
            return;
        }
        int oldPos = findL2ItemPosition(selectedL1, selectedL2);
        selectedL1 = l1Index;
        selectedL2 = l2Index;
        int newPos = findL2ItemPosition(selectedL1, selectedL2);
        if (oldPos >= 0) {
            notifyItemChanged(oldPos);
        }
        if (newPos >= 0) {
            notifyItemChanged(newPos);
        }
    }

    /** 返回指定 L2 项在列表中的位置，找不到返回 -1。 */
    public int findL2ItemPosition(int l1Index, int l2Index) {
        if (mData == null) {
            return -1;
        }
        for (int i = 0; i < mData.size(); i++) {
            LeftMenuItem item = mData.get(i);
            if (item.type == LeftMenuItem.TYPE_L2_ITEM
                    && item.l1Index == l1Index
                    && item.l2Index == l2Index) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void convert(BaseViewHolder helper, LeftMenuItem item) {
        if (item.type == LeftMenuItem.TYPE_L1_HEADER) {
            helper.setText(R.id.tv_title, item.text);
            return;
        }
        // TYPE_L2_ITEM
        // 注意：BaseViewHolder 没有 getContext()，颜色/资源通过 adapter 自身的 mContext 取
        // （BaseQuickAdapter.onCreateViewHolder 中赋值，protected Context mContext）
        boolean selected = item.l1Index == selectedL1 && item.l2Index == selectedL2;
        View root = helper.getView(R.id.fl_root);
        root.setBackgroundColor(mContext.getResources().getColor(
                selected ? R.color.menu_selected_bg : android.R.color.transparent));
        android.widget.TextView tv = helper.getView(R.id.tv_title);
        tv.setText(item.text);
        tv.setTextColor(mContext.getResources().getColorStateList(
                selected ? R.color.colorAccent : R.color.darkColorAccent).getDefaultColor());
        tv.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
        root.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onL2Selected(item.l1Index, item.l2Index);
            }
        });
    }
}
```

- [ ] **Step 5: Commit**

```bash
git add CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/LeftMenuItem.java \
        CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/MenuLeftAdapter.java \
        CheatSheet/app/src/main/res/layout/item_menu_l1_header.xml \
        CheatSheet/app/src/main/res/layout/item_menu_l2_item.xml
git commit -m "feat: 新增左栏适配器与布局（L1标题+L2项混排，选中态）"
```

---

## Task 4: 右栏扁平 item 与适配器

**Files:**
- Create: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/RightMenuItem.java`
- Create: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/MenuRightAdapter.java`
- Create: `CheatSheet/app/src/main/res/layout/item_menu_l2_header.xml`
- Create: `CheatSheet/app/src/main/res/layout/item_menu_l3_item.xml`

- [ ] **Step 1: 创建 `RightMenuItem.java`（实现 MultiItemEntity，三态：L1/L2/L3）**

```java
package com.xcyh.cheatsheet.home.list;

import com.fold.recyclyerview.entity.MultiItemEntity;

/**
 * 右栏扁平化后的列表项。
 * - L1 标题 / L2 标题：不可点击
 * - L3 叶子：可点击
 * 仅 L1 标题记录 l1Index（用于映射）。
 */
public class RightMenuItem implements MultiItemEntity {

    public static final int TYPE_L1_HEADER = 1;
    public static final int TYPE_L2_HEADER = 2;
    public static final int TYPE_L3_ITEM = 3;

    public final int type;
    public final String text;
    public final int l1Index;

    private RightMenuItem(int type, String text, int l1Index) {
        this.type = type;
        this.text = text;
        this.l1Index = l1Index;
    }

    public static RightMenuItem ofL1Header(String text) {
        return new RightMenuItem(TYPE_L1_HEADER, text, -1);
    }

    public static RightMenuItem ofL2Header(String text) {
        return new RightMenuItem(TYPE_L2_HEADER, text, -1);
    }

    public static RightMenuItem ofL3Item(String text) {
        return new RightMenuItem(TYPE_L3_ITEM, text, -1);
    }

    @Override
    public int getItemType() {
        return type;
    }
}
```

- [ ] **Step 2: 创建 `item_menu_l2_header.xml`（右栏 L2 标题）**

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/tv_title"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:textColor="@color/colorAccent"
    android:textSize="16sp"
    android:textStyle="bold"
    android:paddingHorizontal="12dp"
    android:paddingTop="8dp"
    android:paddingBottom="4dp"
    tools:text="科学学习" />
```

- [ ] **Step 3: 创建 `item_menu_l3_item.xml`（右栏 L3 叶子项，可点击）**

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/tv_title"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:textColor="@color/text_color_primary"
    android:textSize="14sp"
    android:paddingHorizontal="12dp"
    android:paddingVertical="6dp"
    android:background="?android:attr/selectableItemBackground"
    android:clickable="true"
    android:focusable="true"
    tools:text="IPO认知篇：重新理解「科学学习」" />
```

- [ ] **Step 4: 创建 `MenuRightAdapter.java`**

```java
package com.xcyh.cheatsheet.home.list;

import android.view.View;

import com.benefit.novelverse.R;
import com.fold.recyclyerview.BaseMultiItemQuickAdapter;
import com.fold.recyclyerview.BaseViewHolder;

import java.util.List;

/**
 * 右栏适配器：L1 标题 + L2 标题 + L3 叶子混排。
 * L3 点击回调（占位）。
 */
public class MenuRightAdapter extends BaseMultiItemQuickAdapter<RightMenuItem, BaseViewHolder> {

    /** L3 叶子被点击回调 */
    public interface OnRightItemSelectedListener {
        void onL3Selected(String text);
    }

    private OnRightItemSelectedListener mListener;

    public MenuRightAdapter(List<RightMenuItem> data) {
        super(data);
        addItemType(RightMenuItem.TYPE_L1_HEADER, R.layout.item_menu_l1_header);
        addItemType(RightMenuItem.TYPE_L2_HEADER, R.layout.item_menu_l2_header);
        addItemType(RightMenuItem.TYPE_L3_ITEM, R.layout.item_menu_l3_item);
    }

    public void setOnRightItemSelectedListener(OnRightItemSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, RightMenuItem item) {
        helper.setText(R.id.tv_title, item.text);
        if (item.type == RightMenuItem.TYPE_L3_ITEM) {
            View itemView = helper.itemView;
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onL3Selected(item.text);
                }
            });
        }
    }
}
```

- [ ] **Step 5: Commit**

```bash
git add CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/RightMenuItem.java \
        CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/MenuRightAdapter.java \
        CheatSheet/app/src/main/res/layout/item_menu_l2_header.xml \
        CheatSheet/app/src/main/res/layout/item_menu_l3_item.xml
git commit -m "feat: 新增右栏适配器与布局（L1/L2/L3 三态）"
```

---

## Task 5: 双栏根布局

**Files:**
- Create: `CheatSheet/app/src/main/res/layout/fragment_home_list.xml`

- [ ] **Step 1: 创建 `fragment_home_list.xml`（横向 LinearLayout：左 weight=3 + 1dp 分隔线 + 右 weight=7）**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="horizontal">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rv_left"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="3"
            android:overScrollMode="never" />

        <View
            android:layout_width="1dp"
            android:layout_height="match_parent"
            android:background="@color/divider_line_color_vertical" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rv_right"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="7"
            android:overScrollMode="never" />
    </LinearLayout>
</layout>
```

- [ ] **Step 2: Commit**

```bash
git add CheatSheet/app/src/main/res/layout/fragment_home_list.xml
git commit -m "feat: 新增三级菜单双栏根布局 fragment_home_list"
```

---

## Task 6: 改造 HomeListFragment（双栏 + 联动）

**Files:**
- Modify: `CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/HomeListFragment.java`

- [ ] **Step 1: 用下面内容整体替换 `HomeListFragment.java`**

```java
package com.xcyh.cheatsheet.home.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.fragment.BindingCustomFragment;
import com.benefit.novelverse.R;
import com.benefit.novelverse.databinding.FragmentHomeListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 三级联动菜单：左栏（L1 标题 + L2 项），右栏（L1 标题 + L2 标题 + L3 叶子）。
 * 点击左栏 L2 -> 右栏滚动到对应 L2；滚动右栏 -> 左栏高亮当前可见 L2。
 */
public class HomeListFragment extends BindingCustomFragment<FragmentHomeListBinding, HomeListPresenter>
        implements MenuLeftAdapter.OnLeftItemSelectedListener,
        MenuRightAdapter.OnRightItemSelectedListener {

    private MenuLeftAdapter mLeftAdapter;
    private MenuRightAdapter mRightAdapter;

    private LinearLayoutManager mRightLayoutManager;

    /** 右栏每个位置 -> l1Index*1000 + l2Index（该位置归属的 L2） */
    private Map<Integer, Integer> mRightIndexToL2Key = new HashMap<>();
    /** L2 key (l1Index*1000+l2Index) -> 右栏中该 L2 标题所在位置 */
    private Map<Integer, Integer> mL2KeyToRightIndex = new HashMap<>();

    /** 当前选中的 L2 key（防抖用） */
    private int mSelectedL2Key = 0;
    /** 标记右栏->左栏联动是否正在进行，避免回调风暴期间重复刷新 */
    private boolean mIsLinking = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_list;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding.rvLeft.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvLeft.setNestedScrollingEnabled(false);

        mRightLayoutManager = new LinearLayoutManager(mContext);
        mBinding.rvRight.setLayoutManager(mRightLayoutManager);
    }

    @Override
    public void initData() {
        List<CourseLevel1> courseData = HomeMenuData.getDemoCourseData();

        mLeftAdapter = new MenuLeftAdapter(buildLeftItems(courseData));
        mLeftAdapter.setOnLeftItemSelectedListener(this);
        mBinding.rvLeft.setAdapter(mLeftAdapter);

        List<RightMenuItem> rightItems = buildRightItems(courseData);
        buildMappings(rightItems);
        mRightAdapter = new MenuRightAdapter(rightItems);
        mRightAdapter.setOnRightItemSelectedListener(this);
        mBinding.rvRight.setAdapter(mRightAdapter);
    }

    @Override
    protected void initListener() {
        mBinding.rvRight.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (mIsLinking || mRightLayoutManager == null) {
                    return;
                }
                int firstPos = mRightLayoutManager.findFirstVisibleItemPosition();
                Integer l2Key = mRightIndexToL2Key.get(firstPos);
                if (l2Key == null || l2Key == mSelectedL2Key) {
                    return;
                }
                mSelectedL2Key = l2Key;
                int l1 = l2Key / 1000;
                int l2 = l2Key % 1000;
                mLeftAdapter.setSelected(l1, l2);
                int leftPos = mLeftAdapter.findL2ItemPosition(l1, l2);
                if (leftPos >= 0) {
                    mBinding.rvLeft.smoothScrollToPosition(leftPos);
                }
            }
        });
    }

    // ===== 左栏 L2 被点击：右栏滚动联动 =====
    @Override
    public void onL2Selected(int l1Index, int l2Index) {
        int key = l1Index * 1000 + l2Index;
        if (key == mSelectedL2Key) {
            return;
        }
        mSelectedL2Key = key;
        mLeftAdapter.setSelected(l1Index, l2Index);
        Integer rightPos = mL2KeyToRightIndex.get(key);
        if (rightPos != null) {
            mIsLinking = true;
            mBinding.rvRight.smoothScrollToPosition(rightPos);
            mIsLinking = false;
        }
    }

    // ===== 右栏 L3 被点击：占位 Toast（后续接 presenter 跳转）=====
    @Override
    public void onL3Selected(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    // ===== 数据扁平化 =====

    private List<LeftMenuItem> buildLeftItems(List<CourseLevel1> data) {
        List<LeftMenuItem> items = new ArrayList<>();
        for (int l1 = 0; l1 < data.size(); l1++) {
            CourseLevel1 level1 = data.get(l1);
            items.add(LeftMenuItem.ofL1Header(level1.name));
            for (int l2 = 0; l2 < level1.level2Items.size(); l2++) {
                items.add(LeftMenuItem.ofL2Item(level1.level2Items.get(l2).name, l1, l2));
            }
        }
        return items;
    }

    private List<RightMenuItem> buildRightItems(List<CourseLevel1> data) {
        List<RightMenuItem> items = new ArrayList<>();
        for (CourseLevel1 level1 : data) {
            items.add(RightMenuItem.ofL1Header(level1.name));
            for (CourseLevel2 level2 : level1.level2Items) {
                items.add(RightMenuItem.ofL2Header(level2.name));
                for (String l3 : level2.level3Items) {
                    items.add(RightMenuItem.ofL3Item(l3));
                }
            }
        }
        return items;
    }

    /**
     * 预建右栏位置 <-> L2 key 映射。
     * 遍历顺序与 buildRightItems 一致：遇 L1 标题则 currentL1++ 并重置 currentL2；
     * 遇 L2 标题则 currentL2++，记录该 L2 锚点位置；L3 叶子归属当前 L2。
     */
    private void buildMappings(List<RightMenuItem> rightItems) {
        mRightIndexToL2Key.clear();
        mL2KeyToRightIndex.clear();
        int currentL1 = -1;
        int currentL2 = -1;
        for (int i = 0; i < rightItems.size(); i++) {
            RightMenuItem item = rightItems.get(i);
            if (item.type == RightMenuItem.TYPE_L1_HEADER) {
                currentL1++;
                currentL2 = -1;
                // 进入新 L1 后还没有 L2，L1 标题位置归 currentL1*1000（即该 L1 下第一个 L2 的 key）
                mRightIndexToL2Key.put(i, currentL2 < 0 ? currentL1 * 1000 : currentL1 * 1000 + currentL2);
            } else if (item.type == RightMenuItem.TYPE_L2_HEADER) {
                currentL2++;
                int key = currentL1 * 1000 + currentL2;
                mL2KeyToRightIndex.put(key, i);
                mRightIndexToL2Key.put(i, key);
            } else {
                // L3 叶子归属当前 L2
                mRightIndexToL2Key.put(i, currentL1 * 1000 + currentL2);
            }
        }
    }
}
```

> 注意点（实现时务必遵守）：
> 1. `getPresenterClass()` 由基类 `BaseCustomFragment` 通过反射泛型推导，无需手写。
> 2. `BindingCustomFragment` 要求实现抽象方法 `initListener()`；`initView()`/`initData()` 为可重写钩子，调用 `super.initView()` 不是必须（基类为空实现），这里保留 `super` 调用以防未来基类扩展。
> 3. `mIsLinking` 配合 `smoothScrollToItem` 在 `onL2Selected` 里同步置位——因为 `smoothScrollToPosition` 是异步滚动，但 `onScrolled` 在滚动过程中多次回调；用 `mIsLinking` 包裹「主动触发的滚动」起点。由于置位在同步代码块内立即复位，**实际防抖主要靠 `l2Key == mSelectedL2Key` 的相等判断**。这是预期行为，符合设计文档「选中态比较防抖」。
> 4. 移除原 `createAdapter()`/`getData()`/`onGetDataSuccess()`（旧 `BaseListCustomFragment` 的抽象方法，新基类无此要求）。

- [ ] **Step 2: 静态自检——import 与资源名一致性**

逐项核对（人工 + 可选 grep）：
1. `R.layout.fragment_home_list`、`R.layout.item_menu_l1_header`、`R.layout.item_menu_l2_item`、`R.layout.item_menu_l2_header`、`R.layout.item_menu_l3_item` 均已在 Task 3/4/5 创建。
2. `R.color.menu_selected_bg` 已在 Task 1 创建；`R.color.colorAccent`、`R.color.darkColorAccent`、`R.color.divider_line_color_vertical`、`R.color.text_color_primary` 已存在。
3. 控件 id `rv_left`/`rv_right`（布局）、`tv_title`/`fl_root`（item 布局）一一对应。
4. DataBinding 生成的 `FragmentHomeListBinding` 名称 = `fragment_home_list` + `Binding`，字段 `rvLeft`/`rvRight` 对应 id。
5. `HomeListPresenter extends BasePresenter`（已确认），满足 `BindingCustomFragment<VB, P extends BasePresenter>` 约束。

可执行（可选，验证资源名拼写）：
```bash
grep -rn "menu_selected_bg\|item_menu_\|fragment_home_list" CheatSheet/app/src/main/res/layout CheatSheet/app/src/main/res/values/colors.xml CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list
```

- [ ] **Step 3: Commit**

```bash
git add CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/HomeListFragment.java
git commit -m "feat: HomeListFragment 改造为双栏三级联动菜单"
```

---

## Task 7: 收尾自检（不跑 gradle，因模块编译不过）

**Files:** 无（仅校验）

- [ ] **Step 1: 文件清单核对**

确认以下文件均已创建/修改：
```
CheatSheet/app/src/main/res/values/colors.xml                       (改: +menu_selected_bg)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/CourseLevel1.java      (新)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/CourseLevel2.java      (新)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/HomeMenuData.java      (新)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/LeftMenuItem.java      (新)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/MenuLeftAdapter.java   (新)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/RightMenuItem.java     (新)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/MenuRightAdapter.java  (新)
CheatSheet/app/src/main/res/layout/fragment_home_list.xml            (新)
CheatSheet/app/src/main/res/layout/item_menu_l1_header.xml           (新)
CheatSheet/app/src/main/res/layout/item_menu_l2_item.xml             (新)
CheatSheet/app/src/main/res/layout/item_menu_l2_header.xml           (新)
CheatSheet/app/src/main/res/layout/item_menu_l3_item.xml             (新)
CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/HomeListFragment.java  (改)
```

- [ ] **Step 2: 交叉引用一致性 grep**

```bash
# viewtype 常量
grep -rn "TYPE_L1_HEADER\|TYPE_L2_HEADER\|TYPE_L2_ITEM\|TYPE_L3_ITEM" CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list
# 资源名
grep -rn "R.layout.item_menu\|R.layout.fragment_home_list\|R.color.menu_selected_bg" CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list
# 控件 id
grep -rn "@+id/tv_title\|@+id/fl_root\|@+id/rv_left\|@+id/rv_right" CheatSheet/app/src/main/res/layout
```
预期：每个常量/资源名定义处与使用处匹配。

- [ ] **Step 3: 确认未误删旧文件**

```bash
git status --short
ls CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/
```
预期：`HomeListAdapter.java`、`HomeItem.java`、`HomeListPresenter.java`、`IHomeListView.java` 仍在（未删）。

- [ ] **Step 4: 记录编译限制**

向用户说明：本次仅做静态自检，CheatSheet/app 模块因预先存在的依赖问题（vest 依赖、`com.baidu.baselibrary.base.module.*` 包路径不匹配、缺 zhifu bean）无法 gradle 编译验证，与本次改造无关。要运行验证需先解决该模块依赖问题。

---

## Self-Review

**1. Spec coverage：**
- 数据模型 CourseLevel1/2 + 占位数据 → Task 2 ✓
- 左栏 L1标题+L2项 + 选中态 → Task 3 ✓
- 右栏 L1/L2/L3 → Task 4 ✓
- 左→右点击联动 → Task 6 `onL2Selected` ✓
- 右→左滚动联动 + 防抖 → Task 6 `onScrolled` + `mSelectedL2Key` 比较 ✓
- 映射表预建 → Task 6 `buildMappings` ✓
- 双栏布局（左30%+分隔线）→ Task 5 ✓
- Fragment 改继承 BindingCustomFragment → Task 6 ✓
- 保留 presenter / 不删旧文件 → Task 6 说明 + Task 7 核对 ✓

**2. Placeholder scan：** 无 TODO/TBD/「适当处理」。Task 6 的 `buildMappings` 为单一清晰实现。✓

**3. Type consistency：**
- `LeftMenuItem.TYPE_L1_HEADER=1/TYPE_L2_ITEM=2`、`RightMenuItem.TYPE_L1_HEADER=1/TYPE_L2_HEADER=2/TYPE_L3_ITEM=3` 与各 adapter `addItemType` 一致 ✓
- `MenuLeftAdapter.OnLeftItemSelectedListener.onL2Selected(int,int)` 与 Fragment `onL2Selected(int,int)` 一致 ✓
- `MenuRightAdapter.OnRightItemSelectedListener.onL3Selected(String)` 与 Fragment `onL3Selected(String)` 一致 ✓
- `findL2ItemPosition(int,int)` 定义于 adapter、调用于 Fragment ✓
- `HomeListPresenter extends BasePresenter` 满足泛型约束 ✓
