package com.std.logisticapp.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.DeliveryComponent;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.DeliveryDetailPresenter;
import com.std.logisticapp.presenter.iview.DeliveryDetailView;
import com.std.logisticapp.ui.activity.DeliveryAppointmentActivity;
import com.std.logisticapp.ui.activity.SignActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eric on 2016/5/24.
 */
public class DeliveryDetailFragment extends BaseFragment implements DeliveryDetailView {
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

    EditText mEtBackReason;

    ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        if (StringUtils.isEmpty(ordTel)) {
            mIvCall.setVisibility(View.GONE);
        } else {
            mIvCall.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void makeCall(final String ordTel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.msg_prompt));
        builder.setMessage(getActivity().getString(R.string.dl_call_prompt_msg) + ":" + ordTel);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ordTel));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }

    @Override
    public void changeAppointment(OrderBean order) {
        Intent intentToLaunch = DeliveryAppointmentActivity.getCallingIntent(getActivity(), order);
        startActivityForResult(intentToLaunch, LogisticApi.INTENT_REQUEST_MESSAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case LogisticApi.RESULT_OK:
                switch (requestCode) {
                    case LogisticApi.INTENT_REQUEST_MESSAGE_CODE:
                        String newAppointment = data.getStringExtra("Appointment");
                        String remark = data.getStringExtra("Remark");
                        setAppointmentWithFormat(newAppointment);
                        setOrdRemarkWithFormat(remark);
                        this.mDeliveryDetailPresenter.changeOrderAppointment(newAppointment);
                        break;
                    case LogisticApi.INTENT_REQUEST_ORDER_SIGN_CODE:
                        getActivity().setResult(LogisticApi.RESULT_DEL, data);
                        getActivity().finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void sign(OrderBean order) {
        Intent intentToLaunch = SignActivity.getCallingIntent(getActivity(), order);
        startActivityForResult(intentToLaunch, LogisticApi.INTENT_REQUEST_ORDER_SIGN_CODE);
    }

    @Override
    public String getBackReason() {
        return mEtBackReason.getText().toString();
    }

    @Override
    public void showSubmitProgress() {
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage("提交中,请稍候...");
        mProgressDialog.show();
    }

    @Override
    public void closeSubmitProgress() {
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void submitDone(OrderBean order) {
        Toast.makeText(getActivity(), "退单成功", Toast.LENGTH_SHORT).show();
        Intent outcallingIntent = new Intent();
        outcallingIntent.putExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY, order);
        getActivity().setResult(LogisticApi.RESULT_DEL, outcallingIntent);
        getActivity().finish();
    }

    @Override
    public void onFailure(String msg) {
        if (msg == null)
            Toast.makeText(getActivity(), getString(R.string.load_error), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.msg_prompt));
        builder.setMessage(getActivity().getString(R.string.dl_call_prompt_msg) + ":" + mDeliveryDetailPresenter.getOrder().getOrdTel());
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDeliveryDetailPresenter.contactOrder();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mDeliveryDetailPresenter.getOrder().getOrdTel()));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delivery_sign, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delivery_sign:
                this.mDeliveryDetailPresenter.sign();
                break;
            case R.id.action_delivery_back:
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_back, null);
                mEtBackReason = (EditText) view.findViewById(R.id.et_back_reason);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("退单原因");
                builder.setView(view);
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(StringUtils.isEmpty(getBackReason())){
                            Toast.makeText(getActivity(),"请填写退单原因",Toast.LENGTH_SHORT).show();
                        }else {
                            mDeliveryDetailPresenter.backOrder();
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton(R.string.button_cancel, null);
                builder.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
