package com.std.logisticapp.di.components;

import android.content.Context;

import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.di.modules.ApplicationModule;
import com.std.logisticapp.di.modules.LogisticApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Maik on 2016/4/21.
 */
@Singleton
@Component(modules = {ApplicationModule.class, LogisticApiModule.class})
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);
    Context getContext();
}
