package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.mvp.MvpView;

import java.util.List;

/**
 * Created by Eric on 2016/5/9.
 */
public interface DeliveryListView extends MvpView {
    void renderDeliveryList(List<OrderBean> orderList);
    void viewDelivery(OrderBean order);
    void complaintHandingDelivery(OrderBean order);
    void setRefreshState(boolean isShow);
}
