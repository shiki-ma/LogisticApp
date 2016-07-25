package com.std.logisticapp.model;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;

import java.util.List;

import rx.Observable;

/**
 * Created by Maik on 2016/5/16.
 */
public interface IOrderModel {
    Observable<ResultBean<List<OrderBean>>> receiptOrderList(String expressId, String searchKey);
    Observable<ResultBean> receiptOrders(String deliveryCodes);
}
