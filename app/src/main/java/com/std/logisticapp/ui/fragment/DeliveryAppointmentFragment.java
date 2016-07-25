package com.std.logisticapp.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shiki.utils.DateUtils;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.DeliveryComponent;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.DeliveryAppointmentPresenter;
import com.std.logisticapp.presenter.iview.DeliveryAppointmentView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eric on 2016/5/24.
 */
public class DeliveryAppointmentFragment extends BaseFragment implements DeliveryAppointmentView {
    @Inject
    DeliveryAppointmentPresenter mDeliveryAppointmentPresenter;
    @Bind(R.id.dp_da)
    DatePicker mDpDa;
    @Bind(R.id.tp_da)
    TimePicker mTpDa;
    @Bind(R.id.et_da_reason)
    EditText mEtDaReason;
    @Bind(R.id.btn_da_submit)
    Button mBtnDaSubmit;

    Date mDate;
    ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(DeliveryComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDeliveryAppointmentPresenter.attachView(this);
        if (savedInstanceState == null)
            this.mDeliveryAppointmentPresenter.loadAppointment();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_delivery_appointment;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mDeliveryAppointmentPresenter.detachView();
    }

    @Override
    public void loadAppointment(OrderBean order) {
        try {
            if (!StringUtils.isEmpty(order.getAppointment())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                mDate = sdf.parse(order.getAppointment());
            }else{
                mDate = new Date();
            }
            mDpDa.init(DateUtils.getYear(mDate), DateUtils.getMonth(mDate), DateUtils.getDay(mDate), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mDate.setYear(year-1900);
                    mDate.setMonth(monthOfYear+1);
                    mDate.setDate(dayOfMonth);
                }
            });
            mTpDa.setIs24HourView(true);
            mTpDa.setCurrentHour(mDate.getHours());
            mTpDa.setCurrentMinute(mDate.getMinutes());
            mTpDa.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    mDate.setHours(hourOfDay);
                    mDate.setMinutes(minute);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getAppointmentReason() {
        return mEtDaReason.getText().toString();
    }

    @Override
    public String getAppointmentTime() {
        return DateUtils.format(mDate,"yyyy-MM-dd HH:mm");
    }

    @Override
    public void submitDone() {
        Intent outcallingIntent = new Intent();
        outcallingIntent.putExtra("Appointment",DateUtils.format(mDate,"yyyy-MM-dd HH:mm"));
        outcallingIntent.putExtra("Remark",getAppointmentReason());
        getActivity().setResult(LogisticApi.RESULT_OK, outcallingIntent);
        getActivity().finish();
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

    @OnClick(R.id.btn_da_submit)
    public void onClickSubmit() {
        this.mDeliveryAppointmentPresenter.submitdAppointment();
    }
}
