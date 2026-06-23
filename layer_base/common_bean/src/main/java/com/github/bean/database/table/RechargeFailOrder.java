package com.github.bean.database.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.UserManager;
import com.baidu.baselibrary.virtual.UniqueKey;
import com.github.bean.database.AppDatabase;
import com.github.bean.database.annotation.TableFieldV20;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
* @author lhc
* @date 2022/8/15 9:36
* @desc google 支付成功 充值失败记录表
*/
@Table(database = AppDatabase.class)
public class RechargeFailOrder extends BaseModel implements Parcelable, UniqueKey {

    @TableFieldV20
    @PrimaryKey(autoincrement = true)
    public Long id;

    @TableFieldV20(name = "uId")
    @Column(name = "uId")
    public String uId;

    @TableFieldV20(name = "orderNo")
    @Column(name = "orderNo")
    public String orderNo;

    @TableFieldV20(name = "tradeNo")
    @Column(name = "tradeNo")
    public String tradeNo;

    @TableFieldV20(name = "token")
    @Column(name = "token")
    public String token;

    @TableFieldV20(name = "fee")
    @Column(name = "fee")
    public String fee;

    @TableFieldV20(name = "cCode")
    @Column(name = "cCode")
    public String countryCode;

    @TableFieldV20(name = "billNo")
    @Column(name = "billNo")
    public String billNo;

    @TableFieldV20(name = "pId")
    @Column(name = "pId")
    public String productId;

    @TableFieldV20(name = "sign")
    @Column(name = "sign")
    public String sign;

    @TableFieldV20(name = "reason")
    @Column(name = "reason")
    public String reason;

    @TableFieldV20(name = "itemId")
    @Column(name = "itemId")
    public String itemId;

    @TableFieldV20(name = "topUpSource")
    @Column(name = "topUpSource")
    public String topUpSource;

    @TableFieldV20(name = "bookModule")
    @Column(name = "bookModule")
    public String bookModule;

    @TableFieldV20(name = "bookModuleId")
    @Column(name = "bookModuleId")
    public String bookModuleId;

    @TableFieldV20(name = "bookPosition")
    @Column(name = "bookPosition")
    public int bookPosition;

    @TableFieldV20(name = "source")
    @Column(name = "source")
    public String source;

    @Column(name = "price")
    public double price;

    /**
     * 获取所有失败订单
     * @return 所有失败订单
     */
    public static List<RechargeFailOrder> getFailOrders() {
        return SQLite.select().from(RechargeFailOrder.class).where(RechargeFailOrder_Table.uId.eq(UserManager.getUserId())).queryList();
    }

    public static void saveFailOrder(RechargeFailOrder order) {
        if(null==order) return;
        RechargeFailOrder rechargeFailOrder = SQLite.select().from(RechargeFailOrder.class).where(RechargeFailOrder_Table.orderNo.eq(order.orderNo)).querySingle();
        if(rechargeFailOrder!=null) return;
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(databaseWrapper -> {
            order.save();
        });
    }


    public static void delByOrderId(String orderNo) {
        SQLite.delete(RechargeFailOrder.class)
                .where(RechargeFailOrder_Table.orderNo.eq(orderNo))
                .and(RechargeFailOrder_Table.uId.eq(UserManager.getUserId()))
                .async()
                .execute();
        ALog.textSingle("RechargeFailOrder-->delByOrderId-->orderNo: " + orderNo);
    }

    public static RechargeFailOrder getFailOrderByOrderNo(String orderNo) {
        return SQLite.select().from(RechargeFailOrder.class).where(RechargeFailOrder_Table.uId.eq(UserManager.getUserId())).and(RechargeFailOrder_Table.orderNo.eq(orderNo)).querySingle();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.uId);
        dest.writeString(this.orderNo);
        dest.writeString(this.tradeNo);
        dest.writeString(this.token);
        dest.writeString(this.fee);
        dest.writeString(this.countryCode);
        dest.writeString(this.billNo);
        dest.writeString(this.productId);
        dest.writeString(this.sign);
        dest.writeString(this.reason);
        dest.writeString(this.itemId);
        dest.writeString(this.topUpSource);
        dest.writeString(this.bookModule);
        dest.writeString(this.bookModuleId);
        dest.writeInt(this.bookPosition);
        dest.writeString(this.source);
        dest.writeDouble(this.price);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Long) source.readValue(Long.class.getClassLoader());
        this.uId = source.readString();
        this.orderNo = source.readString();
        this.tradeNo = source.readString();
        this.token = source.readString();
        this.fee = source.readString();
        this.countryCode = source.readString();
        this.billNo = source.readString();
        this.productId = source.readString();
        this.sign = source.readString();
        this.reason = source.readString();
        this.itemId = source.readString();
        this.topUpSource = source.readString();
        this.bookModule = source.readString();
        this.bookModuleId = source.readString();
        this.bookPosition = source.readInt();
        this.source = source.readString();
        this.price = source.readDouble();
    }

    public RechargeFailOrder() {
    }

    protected RechargeFailOrder(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.uId = in.readString();
        this.orderNo = in.readString();
        this.tradeNo = in.readString();
        this.token = in.readString();
        this.fee = in.readString();
        this.countryCode = in.readString();
        this.billNo = in.readString();
        this.productId = in.readString();
        this.sign = in.readString();
        this.reason = in.readString();
        this.itemId = in.readString();
        this.topUpSource = in.readString();
        this.bookModule = in.readString();
        this.bookModuleId = in.readString();
        this.bookPosition = in.readInt();
        this.source = in.readString();
        this.price = in.readDouble();
    }

    public static final Creator<RechargeFailOrder> CREATOR = new Creator<RechargeFailOrder>() {
        @Override
        public RechargeFailOrder createFromParcel(Parcel source) {
            return new RechargeFailOrder(source);
        }

        @Override
        public RechargeFailOrder[] newArray(int size) {
            return new RechargeFailOrder[size];
        }
    };

    @Override
    public String getUniqueKey() {
        return orderNo;
    }
}
