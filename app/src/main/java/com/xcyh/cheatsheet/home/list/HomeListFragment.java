package com.xcyh.cheatsheet.home.list;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
