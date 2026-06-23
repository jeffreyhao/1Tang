//package com.benefit.novelverse.utils;
//
//import com.baidu.baselibrary.ALog;
//import com.baidu.baselibrary.request.ApiException;
//import com.baidu.baselibrary.request.HttpResult;
//import com.baidu.baselibrary.request.IDataCallback;
//import com.baidu.baselibrary.util.App;
//import com.base.util.thread.ExecutorsUtils;
//import com.baidu.baselibrary.util.ListUtil;
//import com.baidu.baselibrary.util.storage.PreferencesUtil;
//import com.benefit.pay.pattern.GooglePay;
//import com.benefit.pv.presenter.DeepLinkPresenter;
//import com.benefit.pv.presenter.FBTrackPresenter;
//import com.benefit.pv.presenter.PayPresenter;
//import com.benefit.pv.presenter.UploadErrorPresenter;
//import com.benefit.pv.presenter.UploadRecordPresenter;
//import com.github.bean.database.table.RechargeFailOrder;
//import com.github.bean.zhifu.RechargeQueryBean;
//import com.vest.annotation.MappingField;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//public class PageViewRecordTimer {
//
//    @MappingField(flavors = {
//            "novelhive=disposable",
//            "novelclub=disposableAA", "lycannovel=disposableAA",
//            "novelplant=disposableAAcc", "wolffiction=disposableAAcc",
//            "novelhome=disposableAAccD", "novelworm=disposableAAccD", "alphafiction=disposableAAccD",
//            "romanfic=disposableAbc", "novelnook=disposableAbc",
//            "lycanfiction=disposableAbcDe"
//    })
//    private Disposable disposable;
//    private UploadRecordPresenter presenter;
//
//    private static PageViewRecordTimer timer;
//
//    private static int queryUnConsumeCount = 0;
//    //上报异常的事件最多检查再次上报的次数
//    private static final int MAX_CHECK_NUM = 10;
//
//    //上报异常的事件已经检查的次数
//    private static int reportCountNum = 0;
//
//    public static PageViewRecordTimer getInstance() {
//        if(timer==null){
//            timer = new PageViewRecordTimer();
//        }
//        return timer;
//    }
//
//    public void startTimer() {
//        if(disposable==null) {
//            if(presenter == null) {
//                presenter = new UploadRecordPresenter();
//            }
//            disposable = Observable.interval(5, TimeUnit.MINUTES)
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(aLong -> {
//                        ALog.d("startTimer----->" + aLong);
//                        uploadPageViewRecord();
//                    });
//        }
//
//    }
//
//    private void uploadPageViewRecord() {
//        ExecutorsUtils.Companion.getInstance().getAppExecutors().diskIO().execute(()->{
//            List<RechargeFailOrder> failOrders = RechargeFailOrder.getFailOrders();
//            if(ListUtil.isNotEmpty(failOrders)) {
//                for(RechargeFailOrder failOrder: failOrders) {
//                    rechargeFailOrder(failOrder);
//                }
//            }
//            if(queryUnConsumeCount<5){
//                GooglePay.getInstance().queryPurchases();
//                queryUnConsumeCount++;
//            }
//            if(reportCountNum < MAX_CHECK_NUM) {
//                FBTrackPresenter fbTrackPresenter = new FBTrackPresenter();
//                fbTrackPresenter.checkFBTrackData();
//                fbTrackPresenter.trackFailReportUserInfoBackTraceData();
//                new DeepLinkPresenter().checkReportAdSource();
//                reportCountNum++;
//            }
//            if(PreferencesUtil.get(Constant.SP_CONSTANT.NEED_UPLOAD_ALOG, false)
//                    || (!App.isReading() && PreferencesUtil.get(Constant.SP_CONSTANT.IS_READING, false))){ //不在阅读页但有持久化记录，说明没有正常退出阅读页
//                // 上报本地日志
//                AWSUploadUtil.postUploadAlogFiles();
//            }
//        });
//    }
//
//    private void rechargeFailOrder(RechargeFailOrder order) {
//        new PayPresenter().rechargeFailOrder(order.orderNo, order.tradeNo, order.token, order.countryCode, order.productId, order.sign, 1,
//                order.itemId, order.topUpSource, order.bookModule, order.bookModuleId, order.bookPosition, order.source,0,
//                new IDataCallback<RechargeQueryBean>() {
//                    @Override
//                    public void onSuccess(HttpResult result, RechargeQueryBean rechargeQueryEntity) {
//                        RechargeFailOrder.delByOrderId(order.orderNo);
//                    }
//
//                    @Override
//                    public void onFail(ApiException e) {
//                        super.onFail(e);
//                        if(!ApiException.needSaveFailOrder(e.getCode())){
//                            RechargeFailOrder.delByOrderId(order.orderNo);
//                        }
//                        new UploadErrorPresenter().uploadErrorMsg("recharger","server api error: " + e.getMsg(), order.orderNo);
//                        ALog.e("PageViewRecordTimer-->rechargeFailOrder-->onFail-->code:" + e.getCode() + ", msg:" + e.getMsg());
//                    }
//                });
//    }
//
//    public void stopTimer() {
//        if (null != disposable) {
//            disposable.dispose();
//            disposable = null;
//            timer = null;
//        }
//    }
//}
