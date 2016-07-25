package com.std.logisticapp.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiki.utils.DateUtils;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.DeliveryComponent;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.DeliveryComplaintPresenter;
import com.std.logisticapp.presenter.DeliveryDetailPresenter;
import com.std.logisticapp.presenter.iview.DeliveryComplaintView;
import com.std.logisticapp.presenter.iview.DeliveryDetailView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eric on 2016/5/24.
 */
public class DeliveryComplaintFragment extends BaseFragment implements DeliveryComplaintView {
    @Inject
    DeliveryComplaintPresenter mDeliveryComplaintPresenter;
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

    @Bind(R.id.tv_dc_complaint_content)
    TextView mTvDcComplaintContent;
    @Bind(R.id.et_dc_complaintBack)
    EditText mEtDcComplaintBack;
    @Bind(R.id.btn_dc_submit)
    Button mBtnDcSubmit;

    ProgressDialog mProgressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(DeliveryComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDeliveryComplaintPresenter.attachView(this);
        if (savedInstanceState == null)
            this.mDeliveryComplaintPresenter.loadComplaintData();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_delivery_complaint;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void renderOrderComplaint(OrderBean order) {
        if (order != null) {
            setOrdCodeWithFormat(order.getOrdCode());
            setDeliveryCodeWithFormat(order.getDeliveryCode());
            setLogisticCodeWithFormat(order.getLogisticCode());
            setDeliveryAddrWithFormat(order.getDeliveryAddr());
            setRecipientWithFormat(order.getRecipient());
            setOrdTelWithFormat(order.getOrdTel());
            setExprItemWithFormat(order.getExprItem());
            setCallVisibility(order.getOrdTel());
            setComplaintWithFormat(order.getOrdRemark());
            setComplaintBackWithFormat(order.getComplaintBack());
            setSubmitBtnVisibility(order.getComplaintBack());
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
    public void setComplaintWithFormat(String complaint) {
        mTvDcComplaintContent.setText(complaint);
    }

    @Override
    public void setComplaintBackWithFormat(String complaintBack) {
        if(StringUtils.isEmpty(complaintBack)){
           mEtDcComplaintBack.setEnabled(true);
        }else {
            mEtDcComplaintBack.setEnabled(false);
            mEtDcComplaintBack.setText(complaintBack);
        }
    }

    @Override
    public void setSubmitBtnVisibility(String complaintBack) {
        if(StringUtils.isEmpty(complaintBack)){
            mBtnDcSubmit.setVisibility(View.VISIBLE);
        }else {
            mBtnDcSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public void submitDone(String msg, OrderBean order) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        Intent outcallingIntent = new Intent();
        outcallingIntent.putExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY, order);
        getActivity().setResult(LogisticApi.RESULT_OK, outcallingIntent);
        getActivity().finish();
    }

    @Override
    public String getComplaintBack() {
        return mEtDcComplaintBack.getText().toString();
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
    public void onFailure(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mDeliveryComplaintPresenter.detachView();
    }

    @OnClick(R.id.iv_call)
    public void onClickCall() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.msg_prompt));
        builder.setMessage(getActivity().getString(R.string.dl_call_prompt_msg)+":"+mDeliveryComplaintPresenter.getOrder().getOrdTel());
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDeliveryComplaintPresenter.contactOrder();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mDeliveryComplaintPresenter.getOrder().getOrdTel()));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }

    @OnClick(R.id.btn_dc_submit)
    public void onClickSubmit() {
        if(StringUtils.isEmpty(getComplaintBack())){
            Toast.makeText(getActivity(),"请填写处理反馈",Toast.LENGTH_SHORT).show();
        }else {
            this.mDeliveryComplaintPresenter.submitHanding();
        }

    }
}
