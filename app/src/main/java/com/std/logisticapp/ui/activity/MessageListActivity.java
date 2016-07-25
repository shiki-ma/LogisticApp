package com.std.logisticapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.std.logisticapp.R;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.di.HasComponent;
import com.std.logisticapp.di.components.DaggerMessageComponent;
import com.std.logisticapp.di.components.MessageComponent;
import com.std.logisticapp.ui.fragment.MessageListFragment;

/**
 * Created by Maik on 2016/5/9.
 */
public class MessageListActivity extends BaseActivity implements HasComponent<MessageComponent> {
    private MessageComponent messageComponent;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MessageListActivity.class);
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
            addFragment(R.id.fragmentContainer, new MessageListFragment());
        }
    }

    @Override
    protected void initData() {

    }

    private void initializeInjector() {
        this.messageComponent = DaggerMessageComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public MessageComponent getComponent() {
        return messageComponent;
    }
}
