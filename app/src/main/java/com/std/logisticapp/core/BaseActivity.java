package com.std.logisticapp.core;

import android.support.v4.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.std.logisticapp.LogisticApplication;
import com.std.logisticapp.di.components.ApplicationComponent;
import com.std.logisticapp.di.modules.ActivityModule;

import butterknife.ButterKnife;

/**
 * Created by Maik on 2016/4/21.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setContentView(this.getLayoutId());
        ButterKnife.bind(this);

        this.initToolbar(savedInstanceState);
        this.initViews(savedInstanceState);
        this.initData();
        this.initListeners();
        this.getApplicationComponent().inject(this);
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    protected abstract int getLayoutId();

    protected abstract void initToolbar(Bundle savedInstanceState);

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initData();

    protected void initListeners() {
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((LogisticApplication)getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
