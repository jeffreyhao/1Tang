package com.baidu.baselibrary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.baidu.baselibrary.fragment.view.NoSaveStateFrameLayout;
import com.baidu.baselibrary.log.ALog;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by haojiangfeng on 2023/9/23.
 */
public class CustomFragmentUtil {




    public static CustomFragment assembleFragment(CustomFragmentActivity activity, CustomFragment fragment, ViewGroup rootView, Bundle savedInstanceState){
        fragment.setCustomFragmentManager(activity.getCustomFragmentManager());
        fragment.onAttach(activity);
        fragment.onCreate(savedInstanceState);
        ViewGroup view = fragment.onCreateView(fragment.getLayoutInflater(), rootView, savedInstanceState);
        if(view.getParent() == null){
            view = NoSaveStateFrameLayout.wrap(view);
        }
        fragment.setView(view);
        fragment.onViewCreated(view, savedInstanceState);
        fragment.onActivityCreated(savedInstanceState);
        return fragment;
    }



    public static void switchFragment(CustomFragment oldFragment, CustomFragment newFragment) {
        if(oldFragment == null || newFragment == null){
            return;
        }
        if (oldFragment != newFragment) {
            oldFragment.onPause();
            oldFragment.onStop();
//            var bundle: Bundle? = item.saveInstanceStateBundle
//            if (bundle == null) {
//                bundle = Bundle()
//            } else {
//                bundle.clear()
//            }
//            oldFragment.onSaveInstanceState(bundle)
//            item.saveInstanceStateBundle = bundle

            ViewGroup viewParent = null;
            if (oldFragment.mView != null) {
                viewParent = (ViewGroup) oldFragment.mView.getParent();
                if(viewParent == null){
                    oldFragment.mView.setVisibility(View.GONE);
                } else {
                    viewParent.removeView(oldFragment.mView);
                }
            }
            if(viewParent == null) {
                viewParent = (ViewGroup) oldFragment.getActivity().getWindow().getDecorView();
            }

            newFragment.setCustomFragmentManager(oldFragment.getCustomFragmentManager());
            newFragment.setParentFragment(oldFragment.getParentFragment());

            if(newFragment.mView == null){
                newFragment.onAttach(oldFragment.getActivity());
                newFragment.onCreate(null);
                ViewGroup view = newFragment.onCreateView(LayoutInflater.from(oldFragment.getContext()), viewParent, null);
                viewParent.addView(view);
                newFragment.mView = view;
                newFragment.onViewCreated(view, null);
                newFragment.onActivityCreated(null);
            } else {
                viewParent.addView(newFragment.mView);
            }
            newFragment.onStart();
            newFragment.onResume();
        }
    }


    public static void restoreFragment(ViewPager viewPager, CustomFragment fragment, Bundle savedInstance) {
        if (savedInstance == null) {
            return;
        }
        try {
            int width = viewPager.getMeasuredWidth();
            int height = viewPager.getMeasuredHeight();
            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
            fragment.getView().setLayoutParams(lp);
            fragment.getView().measure(widthSpec, heightSpec);
            fragment.getView().layout(0, 0, fragment.getView().getMeasuredWidth(), fragment.getView().getMeasuredHeight());
            fragment.onViewStateRestored(savedInstance);
        } catch (Throwable e) {
            ALog.exception("CustomFragmentUtil", "restoreFragment", e);
        }
    }


    public static void showFragment(CustomFragment fragment){
        if(fragment == null){
            return;
        }
        fragment.onStart();
        fragment.onResume();
    }

    public static void hideFragment(CustomFragment fragment){
        if(fragment == null){
            return;
        }
        fragment.onPause();
        fragment.onStop();
    }


    public static <T extends CustomFragment> T newInstance(Class<T> c, Bundle extras) {
        T instance = null;
        try {
            instance = c.newInstance();
            Bundle bundle = new Bundle();
            if (extras != null) {
                bundle.putAll(extras);
            }
            instance.setArguments(bundle);
        } catch (Throwable e) {
            ALog.exception("CustomFragmentUtil", "newInstance", e);
        }
        return instance;
    }
}
