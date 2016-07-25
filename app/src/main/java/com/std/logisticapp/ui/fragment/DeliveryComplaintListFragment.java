package com.std.logisticapp.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiki.recyclerview.FGORecyclerView;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.DeliveryComponent;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.DeliveryComplaintListPresenter;
import com.std.logisticapp.presenter.iview.DeliveryComplaintListView;
import com.std.logisticapp.ui.activity.DeliveryComplaintActivity;
import com.std.logisticapp.ui.activity.DeliveryDetailActivity;
import com.std.logisticapp.ui.adapter.DeliveryAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eric on 2016/5/30.
 */
public class DeliveryComplaintListFragment extends BaseFragment implements DeliveryComplaintListView {
    @Bind(R.id.fgo_recycler_view)
    FGORecyclerView mRVDelivery;
    //@Inject
    DeliveryAdapter mDeliveryAdapter;
    @Inject
    DeliveryComplaintListPresenter mDeliveryComplaintListPresenter;
    AlertDialog.Builder mBuilder;
    @Bind(R.id.et_receipt)
    EditText mEtReceipt;
    @Bind(R.id.iv_receipt)
    ImageView mIvReceipt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(DeliveryComponent.class).inject(this);
        mDeliveryAdapter = new DeliveryAdapter(getActivity(),true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDeliveryComplaintListPresenter.attachView(this);
        if (savedInstanceState == null)
            this.mDeliveryComplaintListPresenter.loadMessageData(getSearch());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mDeliveryComplaintListPresenter.detachView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        this.mRVDelivery.setAdapter(this.mDeliveryAdapter);
        this.mRVDelivery.setEmptyView(R.layout.empty_view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_delivery_complaint_list;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mRVDelivery.setHasFixedSize(true);
        mRVDelivery.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initListeners() {
        mRVDelivery.setOnLoadMoreListener(new FGORecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {

            }
        });
        mRVDelivery.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDeliveryComplaintListPresenter.loadMessageData(getSearch());
            }
        });
        /*mDeliveryAdapter.setOnItemClickListener(new DeliveryAdapter.OnItemClickListener() {
            @Override
            public void onDeliveryItemClicked(OrderBean order) {
                if (mDeliveryComplaintListPresenter != null && order != null) {
                    mDeliveryComplaintListPresenter.onDeliveryClicked(order);
                }
            }
        });*/
        mDeliveryAdapter.setOnComplaintClickListener(new DeliveryAdapter.OnComplaintClickListener() {
            @Override
            public void onDeliveryComplaintClicked(OrderBean order) {
                if (mDeliveryComplaintListPresenter != null && order != null) {
                    mDeliveryComplaintListPresenter.OnComplaintClicked(order);
                }
            }
        });
        /*mDeliveryAdapter.setOnCallClickListener(new DeliveryAdapter.OnCallClickListener() {
            @Override
            public void onDeliveryCallClicked(final OrderBean order) {
                if (mBuilder == null) {
                    mBuilder = new AlertDialog.Builder(getActivity());
                }
                mBuilder.setTitle(getActivity().getString(R.string.msg_prompt));
                mBuilder.setMessage(getActivity().getString(R.string.dl_call_prompt_msg) + ":" + order.getOrdTel());
                mBuilder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeliveryComplaintListPresenter.contactOrder(order);
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getOrdTel()));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                mBuilder.setNegativeButton(R.string.button_cancel, null);
                mBuilder.show();
            }
        });*/
        mEtReceipt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    // 先隐藏键盘
                    ((InputMethodManager) mEtReceipt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    searchData();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void renderDeliveryList(List<OrderBean> orderList) {
        if (orderList != null) {
            this.mDeliveryAdapter.setOrderList(orderList);
        }
    }

    @Override
    public void viewDelivery(OrderBean order) {
        Intent intentToLaunch = DeliveryDetailActivity.getCallingIntent(getActivity(), order);
        startActivityForResult(intentToLaunch, LogisticApi.INTENT_REQUEST_ORDER_DETAIL_CODE);
    }

    @Override
    public void complaintHandingDelivery(OrderBean order) {
        Intent intentToLaunch = DeliveryComplaintActivity.getCallingIntent(getActivity(), order);
        startActivityForResult(intentToLaunch, LogisticApi.INTENT_REQUEST_ORDER_COMPLAINT_CODE);
    }

    @Override
    public void setRefreshState(boolean isShow) {
        this.mRVDelivery.setRefreshing(isShow);
    }

    @Override
    public String getSearch() {
        return mEtReceipt.getText().toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case LogisticApi.RESULT_OK:
                switch (requestCode) {
                    case LogisticApi.INTENT_REQUEST_ORDER_COMPLAINT_CODE:
                        OrderBean order = data.getParcelableExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY);
                        this.mDeliveryAdapter.removeOrder(order);
                        break;
                }
                break;
        }
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getActivity(), getString(R.string.load_error), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_receipt)
    public void onClick() {
        searchData();
    }

    private void searchData(){
        mDeliveryComplaintListPresenter.loadMessageData(getSearch());
    }
}
