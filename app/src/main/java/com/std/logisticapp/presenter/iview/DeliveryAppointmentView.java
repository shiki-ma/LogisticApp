package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.mvp.MvpView;

/**
 * Created by Eric on 2016/5/12.
 */
public interface DeliveryAppointmentView extends MvpView {
    void loadAppointment(OrderBean order);
    String getAppointmentReason();
    String getAppointmentTime();
    void submitDone();
    void showSubmitProgress();
    void closeSubmitProgress();
}
