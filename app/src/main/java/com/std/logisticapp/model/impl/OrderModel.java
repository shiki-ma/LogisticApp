package com.std.logisticapp.model.impl;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.logistic.LogisticMain;
import com.std.logisticapp.model.IOrderModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Maik on 2016/5/16.
 */
public class OrderModel implements IOrderModel {
    private static final OrderModel instance = new OrderModel();

    public static OrderModel getInstance() {
        return instance;
    }

    private OrderModel() {
    }

    @Override
    public Observable<ResultBean<List<OrderBean>>> receiptOrderList(String expressId, String searchKey) {
        return LogisticMain.getInstance().getLogisticService().receiptOrderList(expressId, searchKey);
    }

    @Override
    public Observable<ResultBean> receiptOrders(String deliveryCodes) {
        return LogisticMain.getInstance().getLogisticService().receiptOrders(deliveryCodes);
    }
}
