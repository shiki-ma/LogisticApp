package com.std.logisticapp.di.components;

import com.std.logisticapp.di.PerActivity;
import com.std.logisticapp.di.modules.ActivityModule;
import com.std.logisticapp.ui.fragment.OrderReceiptFragment;

import dagger.Component;

/**
 * Created by Maik on 2016/5/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface OrderReceiptComponent extends ActivityComponent {
    void inject(OrderReceiptFragment orderReceiptFragment);
}
