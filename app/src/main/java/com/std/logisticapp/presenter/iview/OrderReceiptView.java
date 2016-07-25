package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.mvp.MvpView;

import java.util.List;

/**
 * Created by Maik on 2016/5/16.
 */
public interface OrderReceiptView extends MvpView {
    void renderOrderList(List<OrderBean> orderData);
    void setRefreshState(boolean isShow);
    void refreshOrderList();
}
