package com.baidu.baselibrary.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.global.Clazz;
import com.baidu.baselibrary.util.clz.ReflectUtil;
import com.base.layout.ILayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;


/**
 * 与ILayout绑定的 Fragment基类。
 * <br>
 * <br> 这里有套彻底解决app层与布局资源解耦的方案：
 * <br>     1. 页面View层的抽象要用ILayout的子接口
 * <br>     2. 各个flavor-module模块，实现各自的 ILayout子接口，并通过{@link Clazz}指定相关的子接口实现类
 * <br>     3. 本类内部通过反射，实例化对应的 ILayout子接口实现类
 * <br>
 * @param <L> ILayout的子接口
 * @param <P> BasePresenter的子类
 */
public abstract class FlavorCustomFragment<L extends ILayout, P extends BasePresenter> extends BaseCustomFragment<P> {

    protected L mLayout;

    @Override
    protected ViewGroup createRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayout = buildLayout(inflater);
        if (mLayout == null) {
            return null;
        }
        return (ViewGroup) mLayout.getRoot();
    }

    protected L buildLayout(LayoutInflater inflater){
        L layout = null;
        // 获取泛型 L 的类型
        Class<L> layoutInterface = (Class<L>) ReflectUtil.getClassType(this, 0);
        if (layoutInterface != null) {
            // 从 Clazz 中查找对应的实现类
            Class<? extends L> layoutImplClass = Clazz.findLayoutClass(layoutInterface);
            if (layoutImplClass != null) {
                try {
                    // 实例化 Layout 对象
                    Constructor<? extends L> constructor = layoutImplClass.getConstructor(LayoutInflater.class);
                    layout = constructor.newInstance(inflater);
                } catch (Exception e) {
                    ALog.crash(className(), "buildLayout", e);
                }
            }
        }
        return layout;
    }
}