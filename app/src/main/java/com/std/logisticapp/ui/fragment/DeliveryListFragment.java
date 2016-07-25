package com.std.logisticapp.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.shiki.recyclerview.FGORecyclerView;
import com.shiki.utils.ReservoirUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.MainComponent;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.DeliveryListPresenter;
import com.std.logisticapp.presenter.iview.DeliveryListView;
import com.std.logisticapp.ui.activity.DeliveryComplaintActivity;
import com.std.logisticapp.ui.activity.DeliveryDetailActivity;
import com.std.logisticapp.ui.adapter.DeliveryAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eric on 2016/5/12.
 */
public class DeliveryListFragment extends BaseFragment implements DeliveryListView {
    @Bind(R.id.fgo_recycler_view)
    FGORecyclerView mRVDelivery;
    @Inject
    DeliveryAdapter mDeliveryAdapter;
    @Inject
    DeliveryListPresenter mDeliveryListPresenter;
    String mTitle;
    int mPosition;
    AlertDialog.Builder mBuilder;
    String mSearch;
    boolean isInit = false;//真正要显示的View是否已经被初始化（正常加载）

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(MainComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        //mTitle = bundle.getString("Title", "全部");
        mTitle = bundle.getString("Title", "配送");
        mPosition = bundle.getInt("Position", 0);
        this.mDeliveryListPresenter.attachView(this);
        /**
        if (savedInstanceState == null)
            this.mDeliveryListPresenter.loadMessageData(mPosition,mSearch);
         **/
    }

    public void vpLoad() {
        this.mDeliveryListPresenter.loadMessageData(mPosition, mSearch);
    }

    @Override
    protected void lazyData() {
        this.mDeliveryListPresenter.loadMessageData(mPosition, mSearch);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mDeliveryListPresenter.detachView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        this.mRVDelivery.setAdapter(this.mDeliveryAdapter);
        this.mRVDelivery.setEmptyView(R.layout.empty_view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
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
                mDeliveryListPresenter.loadMessageData(mPosition,mSearch);
            }
        });
        mDeliveryAdapter.setOnItemClickListener(new DeliveryAdapter.OnItemClickListener() {
            @Override
            public void onDeliveryItemClicked(OrderBean order) {
                if (mDeliveryListPresenter != null && order != null) {
                    mDeliveryListPresenter.onDeliveryClicked(order);
                }
            }
        });
        mDeliveryAdapter.setOnComplaintClickListener(new DeliveryAdapter.OnComplaintClickListener() {
            @Override
            public void onDeliveryComplaintClicked(OrderBean order) {
                if (mDeliveryListPresenter != null && order != null) {
                    mDeliveryListPresenter.OnComplaintClicked(order);
                }
            }
        });
        mDeliveryAdapter.setOnCallClickListener(new DeliveryAdapter.OnCallClickListener() {
            @Override
            public void onDeliveryCallClicked(final OrderBean order) {
                if(mBuilder == null){
                    mBuilder = new AlertDialog.Builder(getActivity());
                }
                mBuilder.setTitle(getActivity().getString(R.string.msg_prompt));
                mBuilder.setMessage(getActivity().getString(R.string.dl_call_prompt_msg)+":"+order.getOrdTel());
                mBuilder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeliveryListPresenter.contactOrder(order);
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+order.getOrdTel()));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                mBuilder.setNegativeButton(R.string.button_cancel, null);
                mBuilder.show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case LogisticApi.RESULT_OK:
                switch (requestCode){
                    case LogisticApi.INTENT_REQUEST_ORDER_DETAIL_CODE:
                        OrderBean order = data.getParcelableExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY);
                        this.mDeliveryAdapter.updateOrder(order);
                        break;
                    case LogisticApi.INTENT_REQUEST_ORDER_COMPLAINT_CODE:
                        order = data.getParcelableExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY);
                        this.mDeliveryAdapter.updateOrderComplaint(order);
                        break;
                }
                break;
            case LogisticApi.RESULT_DEL:
                switch (requestCode){
                    case LogisticApi.INTENT_REQUEST_ORDER_DETAIL_CODE:
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

    public void resetFragmentData(String search) {
        mSearch = search;
        isInit = false;
    }
}
