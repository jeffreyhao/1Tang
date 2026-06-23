# HomeListFragment 三级联动菜单改造设计

日期：2026-06-23
模块：`CheatSheet/app/src/main/java/com/xcyh/cheatsheet/home/list/`

## 1. 背景与目标

当前 `HomeListFragment` 继承 `BaseListCustomFragment<HomeItem, HomeListPresenter>`，是一个「单 RecyclerView + 下拉刷新」的列表，承载 3 个测试入口（test / Scheme / App Links），点击通过 `HomeListPresenter` 跳转。

目标：改造成「左右双栏三级联动菜单」，参考一份 Compose 实现（`PersonalRequiredPage`）的交互与视觉：
- 左栏：L1 标题 + L2 项 混排（L1 是不可点击小标题，L2 可点击）
- 右栏：L1 标题 + L2 标题 + L3 项 混排
- 联动：点击左栏 L2 → 右栏滚动到对应 L2；滚动右栏 → 左栏高亮当前可见 L2

数据：先用占位（演示课程）数据搭好结构，方便后续接入真实数据。

## 2. 技术选型

- 语言：Java（项目以 Java 为主，尽量少用 Kotlin）
- UI：DataBinding + RecyclerView + `BaseMultiItemQuickAdapter`（来自 `layer_base:RecyclerViewHelper`）
- 基类：`BindingCustomFragment<VB, P>`（自定义布局），替代原来的 `BaseListCustomFragment`
- 模块：`CheatSheet/app`（HomeListFragment 所在模块）

## 3. 数据模型（新增，`home/list/` 下）

```
CourseLevel1
  - String name
  - List<CourseLevel2> level2Items

CourseLevel2
  - String name
  - List<String> level3Items
```

占位数据：新建 `HomeMenuData.java`，提供静态 `getDemoCourseData()`，返回参考代码中的课程结构：
- 时间管理
- 不断提认知（科学学习 / 深度复盘 / 知识管理）
- 不断练能力（刻意练习 / 练记笔记 / 灵感闪现 / 写逐字稿 / 卖点讲香）
- 形成竞争力（表达力 / 设计力 / AI力）
- 人生红点

后续接入真实数据时，只改 `HomeMenuData`。

## 4. 左栏适配器 `MenuLeftAdapter`

继承 `BaseMultiItemQuickAdapter<Object, BaseViewHolder>`，两种 viewtype 混排：

| viewtype | 内容 | 行为 |
|---|---|---|
| `TYPE_L1_HEADER` | L1 标题 | 不可点击，主色加粗小标题 |
| `TYPE_L2_ITEM` | L2 项 | 可点击，记录 `(l1Index, l2Index)`；选中时背景高亮 + 文字加粗变主色 |

构造时把 `List<CourseLevel1>` 扁平化为 `List<Object>`（L1 标题 + 其下各 L2 项 依次排列）。

对外方法：
- `setSelected(int l1Index, int l2Index)` — 更新选中态，`notifyItemChanged` 局部刷新（旧选中项 + 新选中项）
- 回调 `OnLeftItemSelectedListener.onL2Selected(l1Index, l2Index)` — 通知 Fragment

## 5. 右栏适配器 `MenuRightAdapter`

继承 `BaseMultiItemQuickAdapter<Object, BaseViewHolder>`，三种 viewtype 混排：

| viewtype | 内容 | 行为 |
|---|---|---|
| `TYPE_L1_HEADER` | L1 标题 | 不可点击，主色加粗 |
| `TYPE_L2_HEADER` | L2 标题 | 不可点击，主色半粗 |
| `TYPE_L3_ITEM` | L3 叶子 | 可点击，触发回调（占位 Toast，后续接 presenter 跳转） |

构造时把 `List<CourseLevel1>` 扁平化为 `List<Object>`（L1 标题 → 各 L2 标题 → 该 L2 下各 L3 项）。

对外方法：
- 回调 `OnRightItemSelectedListener.onL3Selected(text)` — 通知 Fragment（占位 Toast）

## 6. 联动逻辑（Fragment 内协调）

初始化时遍历数据，预建两张映射表（与参考代码 `rightIndexToL2` / `l2ToRightIndex` 对应）：

- `rightIndexToL2`：`SparseArray<int[]>` — 右栏每个位置 → `[l1Index, l2Index]`
- `l2ToRightIndex`：`SparseIntegerArray` 或 `Map<Integer,Integer>`，key = `l1Index*1000 + l2Index`，value = 右栏 L2 标题所在位置

### a) 左 → 右（点击联动）
左栏 L2 被点击 → `onL2Selected(l1,l2)` → Fragment 用 `l2ToRightIndex` 查右栏位置 → `rightRecyclerView.smoothScrollToPosition(pos)`。

### b) 右 → 左（滚动联动）
右栏 `RecyclerView.OnScrollListener.onScrolled` → 取 `linearLayoutManager.findFirstVisibleItemPosition()` → 用 `rightIndexToL2` 查出当前首项所属 L2 → 若与当前选中不同：
1. 更新左栏 `setSelected(l1,l2)`
2. 计算该 L2 项在左栏列表中的位置 → `leftRecyclerView.smoothScrollToPosition(pos)` 让选中项可见

防抖：用「比较当前选中态是否变化」决定是否更新，避免滚动回调风暴（等效参考代码的 `distinctUntilChanged`）。

## 7. 布局文件

- `fragment_home_list.xml`（DataBinding 根布局）：横向 `LinearLayout`：
  - 左 `RecyclerView`（`weight=3`，约 30%）
  - 1dp 宽竖分隔线（`View`，`outlineVariant` 色）
  - 右 `RecyclerView`（`weight=7`）
- `item_menu_l1_header.xml` — L1/L2 标题共用（文字样式在 adapter 里按 type 区分）
- `item_menu_l2_item.xml` — 左栏可点击 L2 项（含选中背景容器）
- `item_menu_l3_item.xml` — 右栏 L3 叶子项

颜色/字号沿用参考代码语义：L1 标题 `titleMedium`+主色+加粗，L2 标题 `titleSmall`+主色+半粗，L2 项 `bodyMedium`（选中加粗+主色+`primaryContainer` 背景），L3 项 `bodyMedium`。映射到项目已有 `dimens.xml` / `colors.xml`（如无对应色，用就近值并注释）。

## 8. HomeListFragment 改造

- 基类：`BindingCustomFragment<FragHomeListBinding, HomeListPresenter>`
- `getLayoutId()` → `R.layout.fragment_home_list`
- `initView()`：左/右 RecyclerView 各设 `LinearLayoutManager`、创建并 `setAdapter`
- `initData()`：从 `HomeMenuData.getDemoCourseData()` 取数 → 构建两适配器数据 + 预建两张映射表
- `initListener()`：左栏适配器选中回调（左→右联动）；右栏 RecyclerView 滚动监听（右→左联动）；右栏 L3 点击回调（占位 Toast）
- 保留 `mPresenter`，供后续 L3 跳转复用

注意：`BindingCustomFragment` 的 `setData()` 流程会调用 `initView()/initData()/initListener()`，且需要实现 `initListener()`（抽象方法）。需保留 `getPresenterClass()` 反射推导 presenter（基类已支持）。

## 9. 改动清单

**修改：**
- `HomeListFragment.java`（重写：换基类、布局、双栏 + 联动）

**新建：**
- `CourseLevel1.java`
- `CourseLevel2.java`
- `HomeMenuData.java`（占位数据）
- `MenuLeftAdapter.java`
- `MenuRightAdapter.java`
- `fragment_home_list.xml`
- `item_menu_l1_header.xml`
- `item_menu_l2_item.xml`
- `item_menu_l3_item.xml`

**保留（不动）：**
- `HomeListAdapter.java` / `HomeItem.java`（暂不删，真实数据接入后再清理；确认无其它引用后可删）
- `HomeListPresenter.java`（L3 跳转复用，暂不动）
- `IHomeListView.java`（暂不动）

## 10. 风险与限制

- **CheatSheet/app 模块当前编译不过**：缺 vest 依赖、包路径不匹配（`com.baidu.baselibrary.base.module.*` 等）、缺 zhifu bean。改造完成后无法直接编译验证。本次仅做静态检查（import 完整、引用一致、viewtype/资源名一致）。要跑起来需先解决该模块依赖问题。
- 联动用 `smoothScrollToPosition` + `onScrolled` + 选中态比较防抖，不做额外手势/惯量处理。
- 真实数据与 L3 点击行为待后续接入，当前为占位。
