package com.std.logisticapp;

import android.app.Application;

import com.anupcowkur.reservoir.Reservoir;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.std.logisticapp.di.components.ApplicationComponent;
import com.std.logisticapp.di.components.DaggerApplicationComponent;
import com.std.logisticapp.di.modules.ApplicationModule;
import com.std.logisticapp.di.modules.LogisticApiModule;
import com.std.logisticapp.logistic.LogisticApi;

/**
 * Created by Maik on 2016/4/19.
 */
public class LogisticApplication extends Application {
    private static LogisticApplication instance = new LogisticApplication();
    private static ApplicationComponent applicationComponent;
    public boolean log = true;
    public static final long CACHE_DATA_MAX_SIZE = 1024 * 1024 * 3L;
    public Gson gson;

    public static LogisticApplication getInstance() {
        return instance;
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fresco.initialize(this);
        Logger.init();
        initInjector();
        initGson();
        initReservoir();
    }

    private void initInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .logisticApiModule(new LogisticApiModule())
                .build();
    }

    private void initReservoir() {
        try {
            Reservoir.init(this, CACHE_DATA_MAX_SIZE, this.gson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGson() {
        this.gson = new GsonBuilder().setDateFormat(LogisticApi.LOGISTIC_DATA_FORMAT).create();
    }
}
