package com.std.logisticapp.di.components;

import android.app.Activity;

import com.std.logisticapp.di.PerActivity;
import com.std.logisticapp.di.modules.ActivityModule;

import dagger.Component;

/**
 * Created by Maik on 2016/4/25.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
