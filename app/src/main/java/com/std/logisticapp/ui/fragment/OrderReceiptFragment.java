package com.std.logisticapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shiki.recyclerview.FGORecyclerView;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.OrderReceiptComponent;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.OrderReceiptPresenter;
import com.std.logisticapp.presenter.iview.OrderReceiptView;
import com.std.logisticapp.ui.activity.OrderScanActivity;
import com.std.logisticapp.ui.adapter.OrderReceiptAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Maik on 2016/5/16.
 */
public class OrderReceiptFragment extends BaseFragment implements OrderReceiptView {
    @Bind(R.id.fgo_recycler_view)
    FGORecyclerView rvReceipt;
    @Bind(R.id.chk_allcheck)
    CheckBox chkAllCheck;
    @Bind(R.id.et_receipt)
    EditText etReceipt;
    @Inject
    OrderReceiptAdapter orderReceiptAdapter;
    @Inject
    OrderReceiptPresenter orderReceiptPresenter;

    @OnClick(R.id.iv_receipt)
    public void rcpSearch() {
        this.orderReceiptPresenter.loadOrderData(etReceipt.getText().toString());
    }

    @OnEditorAction(R.id.et_receipt)
    public boolean hideKeyboard(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                rcpSearch();
            }
            return true;
        }
        return false;
    }

    @OnClick(R.id.iv_device_scan)
    public void scanDelivery() {
        Intent intentToLaunch = OrderScanActivity.getCallingIntent(getActivity());
        startActivityForResult(intentToLaunch, LogisticApi.INTENT_REQUEST_ORDER_SCAN_CODE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        this.rvReceipt.setAdapter(this.orderReceiptAdapter);
        this.rvReceipt.setEmptyView(R.layout.empty_view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(OrderReceiptComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.orderReceiptPresenter.attachView(this);
        if (savedInstanceState == null)
            this.orderReceiptPresenter.loadOrderData(etReceipt.getText().toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_receipt;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        rvReceipt.setHasFixedSize(true);
        rvReceipt.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initListeners() {
        this.rvReceipt.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                orderReceiptPresenter.loadOrderData(etReceipt.getText().toString());
            }
        });
        this.orderReceiptAdapter.setOnItemCheckListener(new OrderReceiptAdapter.OnItemCheckListener() {
            @Override
            public void onOrderItemChecked(CheckBox cbItem) {
                OrderBean order = (OrderBean) cbItem.getTag();
                order.setIsSelected(cbItem.isChecked());
                if (cbItem.isChecked()) {
                    chkAllCheck.setChecked(orderReceiptAdapter.isAllSelected());
                } else {
                    chkAllCheck.setChecked(false);
                }
            }
        });
        this.etReceipt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    rcpSearch();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.chk_allcheck)
    public void changeOrderData() {
        this.orderReceiptAdapter.setOrderDataChecked(chkAllCheck.isChecked());
    }

    @OnClick(R.id.btn_receipt)
    public void receipt() {
        System.out.println(this.orderReceiptAdapter.getDeliverys());
        this.orderReceiptPresenter.receiptSelected(this.orderReceiptAdapter.getDeliverys());
    }

    @Override
    public void onFailure(String msg) {
        if (StringUtils.isNumeric(msg))
            Toast.makeText(getActivity(), getString(Integer.parseInt(msg)), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.orderReceiptPresenter.detachView();
    }

    @Override
    public void renderOrderList(List<OrderBean> orderData) {
        if (orderData != null) {
            this.orderReceiptAdapter.setOrderData(orderData);
        }
    }

    @Override
    public void setRefreshState(boolean isShow) {
        this.rvReceipt.setRefreshing(isShow);
    }

    @Override
    public void refreshOrderList() {
        this.orderReceiptAdapter.updateAfterSubmit();
        chkAllCheck.setChecked(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data !=  null) {
            this.orderReceiptPresenter.loadOrderData(data.getExtras().getString("ordId"));
        }
    }
}
