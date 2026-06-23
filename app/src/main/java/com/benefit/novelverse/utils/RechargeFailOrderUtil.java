//package com.benefit.novelverse.utils;
//
//import android.os.Handler;
//import android.os.Looper;
//
//import com.baidu.baselibrary.log.ALog;
//import com.baidu.baselibrary.request.ApiException;
//import com.baidu.baselibrary.request.HttpResult;
//import com.baidu.baselibrary.request.IDataCallback;
//import com.baidu.baselibrary.util.App;
//import com.benefit.pv.presenter.PayPresenter;
//import com.benefit.pv.presenter.UploadErrorPresenter;
//import com.github.bean.database.AppDatabase;
//import com.github.bean.database.table.RechargeFailOrder;
//import com.github.bean.database.table.RechargeFailOrder_Table;
//import com.github.bean.zhifu.RechargeQueryBean;
//import com.raizlabs.android.dbflow.config.FlowManager;
//import com.raizlabs.android.dbflow.sql.language.SQLite;
//
//public class RechargeFailOrderUtil {
//
//    private int retryTime = 1;
//
//    private void rechargeFailOrder(RechargeFailOrder order) {
//        new PayPresenter().rechargeFailOrder(order.orderNo, order.tradeNo, order.token, order.countryCode, order.productId, order.sign, 1,
//                order.itemId, order.topUpSource, order.bookModule, order.bookModuleId, order.bookPosition, order.source, 0,
//                new IDataCallback<RechargeQueryBean>() {
//            @Override
//            public void onSuccess(HttpResult result, RechargeQueryBean rechargeQueryEntity) {
//                RechargeFailOrder.delByOrderId(order.orderNo);
//                App.getHandler().removeCallbacksAndMessages(null);
//            }
//
//            @Override
//            public void onFail(ApiException e) {
//                super.onFail(e);
//                ALog.exception("RechargeFailOrderUtil", "rechargeFailOrder", e);
//                if(ApiException.needSaveFailOrder(e.getCode())){
//                    retryTime++;
//                    if(retryTime<=3) {
//                        retry(order);
//                    }else{
//                        new UploadErrorPresenter().uploadErrorMsg("recharger","server api error: " + e.getMsg(), order.orderNo);
//                        App.getHandler().removeCallbacksAndMessages(null);
//                    }
//                }else{
//                    RechargeFailOrder.delByOrderId(order.orderNo);
//                    App.getHandler().removeCallbacksAndMessages(null);
//                    new UploadErrorPresenter().uploadErrorMsg("recharger","server api error: " + e.getMsg(), order.orderNo);
//                }
//            }
//        });
//    }
//
//    public void retry(RechargeFailOrder order) {
//        App.getHandler().postDelayed(() -> {
//            rechargeFailOrder(order);
//        },retryTime*20*1000L);
//    }
//
//
//    public static void updateFailOrder(String orderNo, String failReason){
//        RechargeFailOrder rechargeFailOrder = SQLite.select().from(RechargeFailOrder.class).where(RechargeFailOrder_Table.orderNo.eq(orderNo)).querySingle();
//        if(rechargeFailOrder!=null){
//            rechargeFailOrder.reason = failReason;
//            FlowManager.getDatabase(AppDatabase.class).executeTransaction(databaseWrapper -> {
//                rechargeFailOrder.update();
//            });
//            new RechargeFailOrderUtil().retry(rechargeFailOrder);
//        }
//    }
//}
