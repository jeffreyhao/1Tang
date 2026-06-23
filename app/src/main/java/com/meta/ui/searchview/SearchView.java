package com.meta.ui.searchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.common.util.ConvertUtils;
import com.benefit.novelverse.R;

import java.util.ArrayList;
import java.util.List;

public class SearchView extends LinearLayout {
    private static final String TAG = "SearchView";
    /**
     * 初始化成员变量
     */
    private Context context;

    // 搜索框组件
    private EditText et_search; // 搜索按键
    private RelativeLayout search_block; // 搜索框布局
    private TextView searchBack; // 返回按键

    // 数据库变量
    // 用于存放历史搜索记录
    private RecordSQLiteOpenHelper helper;
    private SQLiteDatabase db;

    // 回调接口
    private ICallBack mCallBack;// 搜索按键回调接口
    private bCallBack bCallBack; // 返回按键回调接口
    private SearchEventCallBack bSearchEventCallBack; // 右边搜索按钮的点击事件

    // 自定义属性设置
    // 1. 搜索字体属性设置：大小、颜色 & 默认提示
    private Float textSizeSearch;
    private int textColorSearch;
    private String textHintSearch;

    // 2. 搜索框设置：高度 & 颜色
    private int searchBlockColor;
//    private ImageView btSearch;

    public EditText getEt_search() {
        return et_search;
    }

    /**
     * 构造函数
     * 作用：对搜索框进行初始化
     */
    public SearchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(context, attrs); // ->>关注a
        init();// ->>关注b
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(context, attrs);
        init();
    }

    /**
     * 关注a
     * 作用：初始化自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {

        // 控件资源名称
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Search_View);

        // 搜索框字体大小（dp）
        textSizeSearch = typedArray.getDimension(R.styleable.Search_View_textSizeSearch, 14);

        // 搜索框字体颜色（使用十六进制代码，如#333、#8e8e8e）
        int defaultColor = context.getResources().getColor(R.color.textColor, getContext().getTheme()); // 默认颜色 = 灰色
        textColorSearch = typedArray.getColor(R.styleable.Search_View_textColorSearch, defaultColor);

        // 搜索框提示内容（String）
        textHintSearch = typedArray.getString(R.styleable.Search_View_textHintSearch);
        // 搜索框颜色
        searchBlockColor = typedArray.getColor(R.styleable.Search_View_searchBlockColor, R.drawable.box_search);

        // 释放资源
        typedArray.recycle();
    }


    /**
     * 关注b
     * 作用：初始化搜索框
     */
    private void init() {

        // 1. 初始化UI组件->>关注c
        initView();

        // 2. 实例化数据库SQLiteOpenHelper子类对象
        helper = new RecordSQLiteOpenHelper(context);

        // 3. 第1次进入时查询所有的历史搜索记录
        queryData("");

        /**
         * 监听输入键盘更换后的搜索按键
         * 调用时刻：点击键盘上的搜索键时
         */
        et_search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    // 1. 点击搜索按键后，根据输入的搜索字段进行查询
                    // 注：由于此处需求会根据自身情况不同而不同，所以具体逻辑由开发者自己实现，此处仅留出接口
                    if (!(mCallBack == null)) {
                        mCallBack.SearchAciton(et_search.getText().toString());
                    }
                    isExists();
                }
                return false;
            }
        });

        /**
         * 搜索框的文本变化实时监听
         */
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            // 输入文本后调用该方法
            @Override
            public void afterTextChanged(Editable s) {
                // 每次输入后，模糊查询数据库 & 显示
                // 注：若搜索框为空,则模糊搜索空字符 = 显示所有的搜索历史
                String tempName = et_search.getText().toString();
                queryData(tempName); // ->>关注1

            }
        });

        /**
         * 点击返回按键后的事件
         */
        searchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注：由于返回需求会根据自身情况不同而不同，所以具体逻辑由开发者自己实现，此处仅留出接口
                if (!(bCallBack == null)) {
                    bCallBack.BackAciton();
                }
            }
        });

        //右边的搜索按钮的点击事件
//        btSearch.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!(bSearchEventCallBack == null)) {
//                    bSearchEventCallBack.SearchEvent();
//                    isExists();
//                }
//            }
//        });

    }


    /**
     * 关注c：绑定搜索框xml视图
     */
    private void initView() {
        //  绑定R.layout.search_layout作为搜索框的xml文件
        LayoutInflater.from(context).inflate(R.layout.search_head_layout, this);

        // 绑定搜索框EditText
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ConvertUtils.px2sp(textSizeSearch));
        et_search.setTextColor(textColorSearch);
        et_search.setHint(textHintSearch);
        et_search.setBackgroundResource(searchBlockColor);

        //返回按键
        searchBack = (TextView) findViewById(R.id.search_cancel);
        //右边的搜索
//        btSearch = ((ImageView) findViewById(R.id.iv_rightbutton));
    }

    /**
     * 关注1
     * 模糊查询数据
     */
    public Cursor queryData(String s) {
        String tempName = s.replaceAll("'", "''");
        // 1. 模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        System.out.println(cursor.getCount());
        return cursor;
    }

    /**
     * 关注2：清空数据库
     */
    public void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    /**
     * 通过query方法查询全部数据数据
     */
    public List<String> selectByQuery() {
        List<String> mListData = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("records", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                mListData.add(cursor.getString(cursor.getColumnIndex("name")));
                Log.i(TAG, "selectByQuery: name: " + cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mListData;
    }

    /**
     * 删除数据
     *
     * @param name
     * @return
     */
    public int delete(String name) {
        db = helper.getWritableDatabase();
        int delete = db.delete("records", " name=?", new String[]{name});
        db.close();
        return delete;
    }

    /**
     * 关注3
     * 检查数据库中是否已经有该搜索记录
     */
    private boolean hasData(String tempName) {
        // 从数据库中Record表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //  判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 关注4
     * 插入数据到数据库，即写入搜索字段到历史搜索记录
     */
    private void insertData(String s) {
        String tempName = s.replaceAll("'", "''");
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 判断数据库中是否存在并插入
     * 必须在set 之后调用
     */
    public void isExists() {
        // 2. 点击搜索键后，对该搜索字段在数据库是否存在进行检查（查询）->> 关注1
        boolean hasData = hasData(et_search.getText().toString().trim());
        // 3. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
        if (hasData) {
            //存在的话先删除
            delete(et_search.getText().toString().trim());
        }
        insertData(et_search.getText().toString().trim());
    }

    /**
     * 给搜索框上设置内容
     *
     * @param s
     */
    public void setText(String s) {
        et_search.setText(s);
    }

    /**
     * 给搜索框上设置hint
     *
     * @param hitStr
     */
    public void setTextHint(String hitStr) {
        et_search.setHint(hitStr);
    }

    /**
     * 获取hint
     */
    public String getTextHint() {
        if (et_search.getHint() != null) {
            return et_search.getHint().toString();
        }
        return "";
    }

    /**
     * 获取内容
     *
     * @return
     */
    public String getText() {
        if (et_search.getText()!=null) {
            return et_search.getText().toString();
        }
        return "";
    }


    /**
     * 点击键盘中搜索键后的操作，用于接口回调
     */
    public void setOnClickSearch(ICallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 点击返回后的操作，用于接口回调
     */
    public void setOnClickBack(bCallBack bCallBack) {
        this.bCallBack = bCallBack;
    }

    public interface bCallBack {
        void BackAciton();
    }

    public void setbSearchEventCallBack(SearchEventCallBack bSearchEventCallBack) {
        this.bSearchEventCallBack = bSearchEventCallBack;
    }

    public interface SearchEventCallBack {
        void SearchEvent();
    }
}
