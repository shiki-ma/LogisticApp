package com.std.logisticapp.di.components;

import com.std.logisticapp.di.PerActivity;
import com.std.logisticapp.di.modules.ActivityModule;
import com.std.logisticapp.di.modules.DeliveryModule;
import com.std.logisticapp.ui.fragment.DeliveryListFragment;
import com.std.logisticapp.ui.fragment.HomeFragment;
import com.std.logisticapp.ui.fragment.ProfileFragment;

import dagger.Component;

/**
 * Created by Maik on 2016/5/17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface MainComponent extends ActivityComponent {
    void inject(ProfileFragment profileFragment);
    void inject(DeliveryListFragment deliveryListFragment);
    void inject(HomeFragment homeFragment);
}
