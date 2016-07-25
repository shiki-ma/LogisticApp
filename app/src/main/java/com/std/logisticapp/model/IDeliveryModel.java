package com.std.logisticapp.model;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;

import java.util.List;

import rx.Observable;

/**
 * Created by Eric on 2016/5/19.
 */
public interface IDeliveryModel {
    Observable<ResultBean<List<OrderBean>>> getDeliveryList(String expressId);
    Observable<ResultBean<List<OrderBean>>> getDeliveryListSearch(String expressId,String searchKey);
    Observable<ResultBean<OrderBean>> getDeliveryDetail(String deliveryCode);
    Observable<ResultBean> modifyAppointment(String deliveryCode,String appoTime,String remark);
    Observable<ResultBean<OrderBean>> getDeliveryComplaint(String deliveryCode);
    Observable<ResultBean> dealComplaint(String deliveryCode,String remark);
    Observable<ResultBean> contactOrder(String deliveryCode);
    Observable<ResultBean> backOrder(String deliveryCode,String backReason);
}
