package com.baidu.baselibrary.global;

import android.app.Activity;
import android.app.Application;

import com.baidu.baselibrary.base.fragment.BaseFragment;
import com.baidu.baselibrary.fragment.CustomFragment;
import com.base.layout.ILayout;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by haojiangfeng on 2023/9/5.
 */
public class Clazz {

    // application class

    public static Class<? extends Application> foApplicationClass;


    // activity class

    public static Class<? extends Activity> splashActivityClass;
    public static Class<? extends Activity> loginActivityClass;
    public static Class<? extends Activity> pushActivityClass;
    public static Class<? extends Activity> readActivityClass;
    public static Class<? extends Activity> webActivityClass;
    public static Class<? extends Activity> rewardActivityClass;
    public static Class<? extends Activity> payActivityClass;
    public static Class<? extends Activity> rechargeActivityClass;
    public static Class<? extends Activity> payPalWebViewActivityClass;
    public static Class<? extends Activity> sendFeedBackActivityClass;
    public static Class<? extends Activity> downloadOrPurchaseActivityClass;
    public static Class<? extends Activity> bookDetailActivityClass;
    public static Class<? extends Activity> searchActivityClass;
    public static Class<? extends Activity> readHistoryActivityClass;
    public static Class<? extends Activity> categoryBookListActivityClass;
    public static Class<? extends Activity> mainActivityClass;
    public static Class<? extends Activity> moreSettingActivityClass;
    public static Class<? extends Activity> delAccountNoticeActivityClass;
    public static Class<? extends Activity> accountManageActivityClass;
    public static Class<? extends Activity> feedBackActivityClass;
    public static Class<? extends Activity> premiumCenterActivityClass;
    public static Class<? extends Activity> shortPremiumCenterActivityClass;
    public static Class<? extends Activity> subscribeDetailActivityClass;
    public static Class<? extends Activity> routerActivityClass;
    public static Class<? extends Activity> appLinksActivityClass;


    // fragment class

    public static Class<? extends CustomFragment> classBookShelf2Fragment;
    public static Class<? extends CustomFragment> classDiscoverFragment;
    public static Class<? extends CustomFragment> classRewardFragment;
    public static Class<? extends CustomFragment> classProfileFragment;
    public static Class<? extends CustomFragment> classVipProfileFragment;
    public static Class<? extends CustomFragment> classFreeProfileFragment;


    public static Class<? extends CustomFragment> rankingFragmentClass;
    public static Class<? extends CustomFragment> genresFragmentClass;
    public static Class<? extends CustomFragment> bookShelfClassWithViewed;
    public static Class<? extends CustomFragment> premiumCenterFragmentClass;


    public static Class<? extends BaseFragment> loginFragmentClass;
    public static Class<? extends BaseFragment> bookCatalogFragment2Class;


    public static Class<? extends ILayout> profileFreeLayoutClass;





    public static void initApplicationClass(Class<? extends Application> classFoApplication) {

        foApplicationClass = classFoApplication;
    }


    public static void initActivityClass(
            Class<? extends Activity> splashClass,
            Class<? extends Activity> loginClass,
            Class<? extends Activity> classPush,
            Class<? extends Activity> classWeb,
            Class<? extends Activity> classRewardActivity,
            Class<? extends Activity> classPayActivity,
            Class<? extends Activity> classRechargeActivity,
            Class<? extends Activity> classPayPalWebViewActivity,
            Class<? extends Activity> classSendFeedBackActivity,
            Class<? extends Activity> classDownloadOrPurchaseActivity,
            Class<? extends Activity> classBookDetailActivity,
            Class<? extends Activity> classSearchActivity,
            Class<? extends Activity> classReadHistoryActivity,
            Class<? extends Activity> classCategoryBookListActivity,
            Class<? extends Activity> classMainActivity,
            Class<? extends Activity> classMoreSettingActivity,
            Class<? extends Activity> classDelAccountNoticeActivity,
            Class<? extends Activity> classAccountManageActivity,
            Class<? extends Activity> classFeedBackActivity,
            Class<? extends Activity> classSubscribeDetailActivity,
            Class<? extends Activity> classPremiumCenterActivity,
            Class<? extends Activity> classShortPremiumCenterActivity,
            Class<? extends Activity> classRouterActivity,
            Class<? extends Activity> classAppLinksActivity
    ) {

        splashActivityClass = splashClass;
        pushActivityClass = classPush;
        loginActivityClass = loginClass;
        webActivityClass = classWeb;
        rewardActivityClass = classRewardActivity;
        payActivityClass = classPayActivity;
        rechargeActivityClass = classRechargeActivity;
        payPalWebViewActivityClass = classPayPalWebViewActivity;
        sendFeedBackActivityClass = classSendFeedBackActivity;
        downloadOrPurchaseActivityClass = classDownloadOrPurchaseActivity;
        bookDetailActivityClass = classBookDetailActivity;
        searchActivityClass = classSearchActivity;
        readHistoryActivityClass = classReadHistoryActivity;
        categoryBookListActivityClass = classCategoryBookListActivity;
        mainActivityClass = classMainActivity;
        moreSettingActivityClass = classMoreSettingActivity;
        delAccountNoticeActivityClass = classDelAccountNoticeActivity;
        accountManageActivityClass = classAccountManageActivity;
        feedBackActivityClass = classFeedBackActivity;
        subscribeDetailActivityClass = classSubscribeDetailActivity;
        premiumCenterActivityClass = classPremiumCenterActivity;
        shortPremiumCenterActivityClass = classShortPremiumCenterActivity;
        routerActivityClass = classRouterActivity;
        appLinksActivityClass = classAppLinksActivity;
    }

    public static void initMainTabClass(
            Class<? extends CustomFragment> classBookShelf2,
            Class<? extends CustomFragment> classDiscover,
            Class<? extends CustomFragment> classRewardFrag,
            Class<? extends CustomFragment> classProfile,
            Class<? extends CustomFragment> classProfileVip,
            Class<? extends CustomFragment> classProfileFree
    ) {
        classBookShelf2Fragment = classBookShelf2;
        classDiscoverFragment = classDiscover;
        classRewardFragment = classRewardFrag;
        classProfileFragment = classProfile;
        classVipProfileFragment = classProfileVip;
        classFreeProfileFragment = classProfileFree;
    }

    public static void initCustomFragmentClass(
            Class<? extends CustomFragment> classBookRankingFragment,
            Class<? extends CustomFragment> classBookGenresFragment,
            Class<? extends CustomFragment> classBookShelfWithViewed,
            Class<? extends CustomFragment> premiumCenter
    ) {
        rankingFragmentClass = classBookRankingFragment;
        genresFragmentClass = classBookGenresFragment;
        bookShelfClassWithViewed = classBookShelfWithViewed;
        premiumCenterFragmentClass = premiumCenter;
    }

    public static void initBaseFragmentClass(
            Class<? extends BaseFragment> classLoginFragment,
            Class<? extends BaseFragment> classBookCatalogFragment2
    ){
        loginFragmentClass = classLoginFragment;
        bookCatalogFragment2Class = classBookCatalogFragment2;
    }

    /**
     * 初始化Layout实现类。必须在flavor-module中调用。
     */
    public static void initLayoutClass(
            Class<? extends ILayout> classProfileFreeLayout
    ){
        profileFreeLayoutClass = classProfileFreeLayout;
    }



    /**
     * 根据 Layout 接口查找 Clazz 中定义的对应实现类 Class
     *
     * @param layoutInterface Layout 接口类 (例如 IProfileFreeLayout.class)
     * @param <L>            实现了 ILayout 的泛型接口
     * @return 对应的实现类 Class，如果未找到则返回 null
     */
    @SuppressWarnings("unchecked")
    public static <L extends ILayout> Class<? extends L> findLayoutClass(Class<L> layoutInterface) {
        if (layoutInterface == null) {
            return null;
        }
        Field[] fields = Clazz.class.getDeclaredFields();
        for (Field field : fields) {
            // 检查是否为静态字段
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // 检查字段类型是否为 Class
            if (field.getType() == Class.class) {
                try {
                    Object value = field.get(null);
                    if (value instanceof Class) {
                        Class<?> clazzValue = (Class<?>) value;
                        // 1. 必须是 layoutInterface 的子类或实现类
                        // 2. 不能是接口本身 (排除 IProfileFreeLayout.class 等)
                        if (layoutInterface.isAssignableFrom(clazzValue) && !clazzValue.isInterface()) {
                            return (Class<? extends L>) clazzValue;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    public static boolean isSplashActivity(Class<? extends Activity> clz){
        if(clz == null || splashActivityClass == null){
            return false;
        }
        return splashActivityClass.getSimpleName().equals(clz.getSimpleName());
    }

    public static boolean isReadActivity(Class<? extends Activity> clz){
        if(clz == null || readActivityClass == null){
            return false;
        }
        return readActivityClass.getSimpleName().equals(clz.getSimpleName());
    }

    public static boolean isMainActivity(Class<? extends Activity> clz){
        if(clz == null || mainActivityClass == null){
            return false;
        }
        return mainActivityClass.getSimpleName().equals(clz.getSimpleName());
    }

    public static boolean isPayActivity(Class<? extends Activity> clz){
        if(clz == null){
            return false;
        }
        if(payActivityClass != null && payActivityClass.getSimpleName().equals(clz.getSimpleName())){
            return true;
        }
        return rechargeActivityClass != null && rechargeActivityClass.getSimpleName().equals(clz.getSimpleName());
    }

    public static boolean isSchemeOrAppLinksActivity(Class<? extends Activity> clz){
        if(clz == null){
            return false;
        }
        if(routerActivityClass != null && routerActivityClass.getSimpleName().equals(clz.getSimpleName())){
            return true;
        }
        if(appLinksActivityClass != null && appLinksActivityClass.getSimpleName().equals(clz.getSimpleName())){
            return true;
        }
        return false;
    }
}
