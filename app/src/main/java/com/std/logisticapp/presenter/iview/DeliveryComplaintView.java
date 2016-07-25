package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.mvp.MvpView;

/**
 * Created by Eric on 2016/5/12.
 */
public interface DeliveryComplaintView extends MvpView {
    void renderOrderComplaint(OrderBean order);
    void setOrdCodeWithFormat(String ordCode);
    void setDeliveryCodeWithFormat(String deliveryCode);
    void setLogisticCodeWithFormat(String logisticCode);
    void setDeliveryAddrWithFormat(String deliveryAddr);
    void setRecipientWithFormat(String recipient);
    void setOrdTelWithFormat(String ordTel);
    void setExprItemWithFormat(String exprItem);
    void setCallVisibility(String ordTel);
    void makeCall(String ordTel);

    void setComplaintWithFormat(String complaint);
    void setComplaintBackWithFormat(String complaintBack);
    void setSubmitBtnVisibility(String complaintBack);
    void submitDone(String msg,OrderBean order);
    String getComplaintBack();
    void showSubmitProgress();
    void closeSubmitProgress();
}
