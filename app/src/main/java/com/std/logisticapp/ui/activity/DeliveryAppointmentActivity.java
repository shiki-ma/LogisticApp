package com.std.logisticapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.di.HasComponent;
import com.std.logisticapp.di.components.DaggerDeliveryComponent;
import com.std.logisticapp.di.components.DeliveryComponent;
import com.std.logisticapp.di.modules.DeliveryModule;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.ui.fragment.DeliveryAppointmentFragment;
import com.std.logisticapp.ui.fragment.DeliveryDetailFragment;

/**
 * Created by Eric on 2016/5/25.
 */
public class DeliveryAppointmentActivity extends BaseActivity implements HasComponent<DeliveryComponent> {
    private static final String INSTANCE_STATE_PARAM_ORDER = "org.shiki.STATE_PARAM_ORDER_ID";
    private DeliveryComponent mDeliveryComponent;
    private OrderBean mOrder;

    public static Intent getCallingIntent(Context context, OrderBean order) {
        Intent callingIntent = new Intent(context, DeliveryAppointmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY, order);
        callingIntent.putExtras(bundle);
        return callingIntent;
    }

    public static Intent getOutCallingIntent(OrderBean order) {
        Intent outcallingIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY, order);
        outcallingIntent.putExtras(bundle);
        return outcallingIntent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putParcelable(INSTANCE_STATE_PARAM_ORDER, this.mOrder);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.mOrder = getIntent().getParcelableExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY);
            addFragment(R.id.fragmentContainer, new DeliveryAppointmentFragment());
        } else {
            this.mOrder = savedInstanceState.getParcelable(INSTANCE_STATE_PARAM_ORDER);
        }
        initializeInjector();
    }

    private void initializeInjector() {
        this.mDeliveryComponent = DaggerDeliveryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .deliveryModule(new DeliveryModule(this.mOrder))
                .build();
    }

    @Override
    protected void initData() {

    }

    @Override
    public DeliveryComponent getComponent() {
        return mDeliveryComponent;
    }
}
