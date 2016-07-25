package com.std.logisticapp.model.impl;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.logistic.LogisticMain;
import com.std.logisticapp.model.IDeliveryModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Eric on 2016/5/19.
 */
public class DeliveryModel implements IDeliveryModel {
    private static final DeliveryModel instance = new DeliveryModel();

    public static DeliveryModel getInstance() {
        return instance;
    }

    private DeliveryModel() {
    }

    @Override
    public Observable<ResultBean<List<OrderBean>>> getDeliveryList(String expressId) {
        return LogisticMain.getInstance().getLogisticService().getDeliveryList(expressId,"");
    }

    @Override
    public Observable<ResultBean<List<OrderBean>>> getDeliveryListSearch(String expressId, String searchKey) {
        return LogisticMain.getInstance().getLogisticService().getDeliveryList(expressId,searchKey);
    }

    @Override
    public Observable<ResultBean<OrderBean>> getDeliveryDetail(String deliveryCode) {
        return LogisticMain.getInstance().getLogisticService().getDeliveryDetail(deliveryCode);
    }

    @Override
    public Observable<ResultBean> modifyAppointment(String deliveryCode,String appoTime,String remark) {
        return LogisticMain.getInstance().getLogisticService().modifyAppointment(deliveryCode,appoTime,remark);
    }

    @Override
    public Observable<ResultBean<OrderBean>> getDeliveryComplaint(String deliveryCode) {
        return LogisticMain.getInstance().getLogisticService().getDeliveryComplaint(deliveryCode);
    }

    @Override
    public Observable<ResultBean> dealComplaint(String deliveryCode,String remark) {
        return LogisticMain.getInstance().getLogisticService().dealComplaint(deliveryCode,remark);
    }

    @Override
    public Observable<ResultBean> contactOrder(String deliveryCode) {
        return LogisticMain.getInstance().getLogisticService().contactOrder(deliveryCode);
    }

    @Override
    public Observable<ResultBean> backOrder(String deliveryCode, String backReason) {
        return LogisticMain.getInstance().getLogisticService().backOrder(deliveryCode,backReason);
    }
}
