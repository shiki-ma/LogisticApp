package com.std.logisticapp.ui.activity;

import android.os.Bundle;

import com.std.logisticapp.R;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.di.HasComponent;
import com.std.logisticapp.di.components.DaggerMessageComponent;
import com.std.logisticapp.di.components.DaggerOrderReceiptComponent;
import com.std.logisticapp.di.components.MessageComponent;
import com.std.logisticapp.di.components.OrderReceiptComponent;
import com.std.logisticapp.ui.fragment.OrderReceiptFragment;

/**
 * Created by Maik on 2016/5/16.
 */
public class OrderReceiptActivity extends BaseActivity implements HasComponent<OrderReceiptComponent> {
    private OrderReceiptComponent orderReceiptComponent;

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
        initializeInjector();
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, new OrderReceiptFragment());
        }
    }

    @Override
    protected void initData() {

    }

    private void initializeInjector() {
        this.orderReceiptComponent = DaggerOrderReceiptComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public OrderReceiptComponent getComponent() {
        return this.orderReceiptComponent;
    }
}
