package com.std.logisticapp.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.DeliveryComponent;
import com.std.logisticapp.presenter.DeliveryDetailPresenter;
import com.std.logisticapp.presenter.iview.DeliveryDetailView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Eric on 2016/5/24.
 */
public class DeliverySignFragment extends BaseFragment implements DeliveryDetailView {
    @Inject
    DeliveryDetailPresenter mDeliveryDetailPresenter;
    @Bind(R.id.tv_ordCode_content)
    TextView mTvOrdCodeContent;
    @Bind(R.id.tv_deliveryCode_content)
    TextView mTvDeliveryCodeContent;
    @Bind(R.id.tv_logisticCode_content)
    TextView mTvLogisticCodeContent;
    @Bind(R.id.tv_deliveryAddr_content)
    TextView mTvDeliveryAddrContent;
    @Bind(R.id.tv_recipient_content)
    TextView mTvRecipientContent;
    @Bind(R.id.tv_ordTel_content)
    TextView mTvOrdTelContent;
    @Bind(R.id.iv_call)
    ImageView mIvCall;
    @Bind(R.id.tv_exprItem_content)
    TextView mTvExprItemContent;

    @Bind(R.id.tv_dd_appointment_content)
    TextView mTvDdAppointmentContent;
    @Bind(R.id.btn_dd_appointment)
    Button mBtnDdAppointment;
    @Bind(R.id.tv_dd_ordRemark_content)
    TextView mTvDdOrdRemarkContent;
    @Bind(R.id.btn_dd_sign)
    TextView mBtnDdSign;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(DeliveryComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDeliveryDetailPresenter.attachView(this);
        if (savedInstanceState == null)
            this.mDeliveryDetailPresenter.loadDetailData();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_delivery_detail;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void renderOrderDetail(OrderBean order) {
        if (order != null) {
            setOrdCodeWithFormat(order.getOrdCode());
            setDeliveryCodeWithFormat(order.getDeliveryCode());
            setLogisticCodeWithFormat(order.getLogisticCode());
            setDeliveryAddrWithFormat(order.getDeliveryAddr());
            setRecipientWithFormat(order.getRecipient());
            setOrdTelWithFormat(order.getOrdTel());
            setExprItemWithFormat(order.getExprItem());
            setAppointmentWithFormat(order.getAppointment());
            setOrdRemarkWithFormat(order.getOrdRemark());
            setCallVisibility(order.getOrdTel());
        }
    }

    @Override
    public void setOrdCodeWithFormat(String ordCode) {
        mTvOrdCodeContent.setText(ordCode);
    }

    @Override
    public void setDeliveryCodeWithFormat(String deliveryCode) {
        mTvDeliveryCodeContent.setText(deliveryCode);
    }

    @Override
    public void setLogisticCodeWithFormat(String logisticCode) {
        mTvLogisticCodeContent.setText(logisticCode);
    }

    @Override
    public void setDeliveryAddrWithFormat(String deliveryAddr) {
        mTvDeliveryAddrContent.setText(deliveryAddr);
    }

    @Override
    public void setRecipientWithFormat(String recipient) {
        mTvRecipientContent.setText(recipient);
    }

    @Override
    public void setOrdTelWithFormat(String ordTel) {
        mTvOrdTelContent.setText(ordTel);
    }

    @Override
    public void setExprItemWithFormat(String exprItem) {
        mTvExprItemContent.setText(exprItem);
    }

    @Override
    public void setAppointmentWithFormat(String appointment) {
        mTvDdAppointmentContent.setText(appointment);
    }

    @Override
    public void setOrdRemarkWithFormat(String ordRemark) {
        mTvDdOrdRemarkContent.setText(ordRemark);
    }

    @Override
    public void setCallVisibility(String ordTel) {
        if(StringUtils.isEmpty(ordTel)){
            mIvCall.setVisibility(View.GONE);
        }else{
            mIvCall.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void makeCall(final String ordTel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.msg_prompt));
        builder.setMessage(getActivity().getString(R.string.dl_call_prompt_msg)+":"+ordTel);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ordTel));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }

    @Override
    public void changeAppointment(OrderBean order) {

    }

    @Override
    public void sign(OrderBean order) {

    }

    @Override
    public String getBackReason() {
        return null;
    }

    @Override
    public void showSubmitProgress() {

    }

    @Override
    public void closeSubmitProgress() {

    }

    @Override
    public void submitDone(OrderBean order) {

    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mDeliveryDetailPresenter.detachView();
    }

    @OnClick(R.id.btn_dd_appointment)
    public void onClickChangeAppointment() {
        this.mDeliveryDetailPresenter.changeAppointment();
    }

    @OnClick(R.id.btn_dd_sign)
    public void onClickSign() {
        this.mDeliveryDetailPresenter.sign();
    }

    @OnClick(R.id.iv_call)
    public void onClickCall() {
        //this.mDeliveryDetailPresenter.call();
    }
}
