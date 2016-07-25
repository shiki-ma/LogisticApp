package com.std.logisticapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.std.logisticapp.R;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.di.HasComponent;
import com.std.logisticapp.di.components.DaggerDeliveryComponent;
import com.std.logisticapp.di.components.DeliveryComponent;
import com.std.logisticapp.ui.fragment.DeliveryComplaintFragment;
import com.std.logisticapp.ui.fragment.DeliveryComplaintListFragment;

/**
 * Created by Eric on 2016/5/25.
 */
public class DeliveryComplaintListActivity extends BaseActivity implements HasComponent<DeliveryComponent> {
    private DeliveryComponent mDeliveryComponent;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, DeliveryComplaintListActivity.class);
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
        initializeInjector();
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, new DeliveryComplaintListFragment());
        }

    }

    private void initializeInjector() {
        this.mDeliveryComponent = DaggerDeliveryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
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
