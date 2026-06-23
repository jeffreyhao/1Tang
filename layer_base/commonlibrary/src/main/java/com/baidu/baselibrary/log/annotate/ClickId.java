package com.baidu.baselibrary.log.annotate;

/**
 * Created by haojiangfeng on 2024/6/14.
 */
public enum ClickId {

    Download_Purchase       (10000),


    Reward_Daily_Item       (10001),
    Reward_CheckIn_Item     (10002),
    Reward_Notice_Item      (10003),

    Reward_Back             (10004),
    Reward_Rules            (10005),
    Reward_Empty            (10006),
    Reward_CheckLogin       (10007),
    StoreRankSeeMore        (10008),
    StoreRankCatalogItem    (10009),
    ActivityEmptyView       (10010),
    DialogKeyBack           (10011),


    BkShelfSelectAll        (10012),
    BkShelfBanner           (10013),
    BkShelfDelete           (10014),
    BkShelfEdit             (10015),
    BkShelfEditDone         (10016),
    BkShelfSearch           (10017),
    BkShelfErrorView        (10018),
    BkShelfReward           (10019),
    BkShelfHistory          (10020),
    BkShelfCloseBanner      (10021),
    BkShelfEmptyView        (10022),
    BkShelfItemBook         (10023),
    BkShelfAddBook          (10024),


    ReadUserGuide           (11000),
    ReadNavBack             (11001),

    ReadFontSub             (11002),
    ReadFontAdd             (11003),
    ReadRowSpacing1         (11004),
    ReadRowSpacing2         (11005),
    ReadRowSpacing3         (11006),
    ReadRowFontType1        (11007),
    ReadRowFontType2        (11008),
    ReadRowFontType3        (11009),
    ReadChangeBg1           (11010),
    ReadChangeBg2           (11011),
    ReadChangeBg3           (11012),
    ReadChangeBg4           (11013),
    ReadChangeBg5           (11014),
    ReadPageFlipType1       (11015),
    ReadPageFlipType2       (11016),
    ReadPageFlipType3       (11017),

    ReadEmptyView           (11018),
    ReadCategory            (11019),
    ReadMoreMenu            (11020),
    ReadDownload            (11021),
    ReadSetting             (11022),
    ReadNightMode           (11023),
    ReadCenter              (11024),

    PendingViewRestore      (11025),
    PendingViewClose        (11026),
    SubscribeDetailFaq      (11027),


    PayActivityMethodMore   (11122),
    PayViewMethodMore       (11123),

    ReadPayAutoGroup        (11125),

    ReadPayFirstPay         (11126),

    ReadPayMoreAmount       (11127),
    ReadPayPurchaseNow      (11128),
    ReadPayFreeCoins        (11129),
    ReadPay2AutoGroup       (11130),
    ReadPay2FirstPay        (11131),
    ReadPay2MoreAmount      (11132),
    ReadPayTopUp            (11133),
    ReadPurchaseItem        (11134),

    PayActivityRestore      (11136),
    PayActivityFAQ          (11137),
    PayActivityPayNow       (11138),
    PayDialogEmptyView      (11139),
    PayDialogFAQ            (11140),
    PayDialogPayNow         (11141),
    ReadCancelOrderBtn      (11142),
    Read2CancelOrderBtn     (11143),
    ProfileCancelOrderBtn   (11144),
    PayActivityCancelOrder  (11145),
    PremiumPayNow           (11146),


    MainTabBookShelf        (11146),
    MainTabBookStore        (11147),
    MainTabBenefit          (11148),
    MainTabProfile          (11149),
    ReadPay2UnlockAdv       (11150),
    ReadPayUnlockAdv        (11151),



    HOOK                   (Integer.MAX_VALUE);

    private final int index;

    ClickId(int index) {
        this.index = index;
    }

    public int toInt(){
        return index;
    }
}
