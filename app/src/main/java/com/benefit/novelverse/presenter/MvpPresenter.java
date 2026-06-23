package com.benefit.novelverse.presenter;



import com.benefit.novelverse.view.base.MvpView;

import java.lang.ref.WeakReference;

/**
 * <Pre>
 * MVP Presenter层基类
 * 使用{@link #getView()#mView} 时应当调用检查方法{@link #isViewAttached()}检查view是否还依附着
 * </Pre>
 *
 * @author adison
 * @date 2017/3/23
 * @time 下午11:57
 */
public abstract class MvpPresenter<V extends MvpView> {

    protected WeakReference<V> viewRef;

    public MvpPresenter(V view) {
        if (view == null) {
            throw new NullPointerException("View cannot be null!");
        }
        viewRef = new WeakReference<V>(view);
    }


    /**
     * 销毁依附view
     */
    public final void destroyUI() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }
}
