package com.std.logisticapp.di.components;

import com.std.logisticapp.di.PerActivity;
import com.std.logisticapp.di.modules.ActivityModule;
import com.std.logisticapp.di.modules.DeliveryModule;
import com.std.logisticapp.di.modules.MessageModule;
import com.std.logisticapp.ui.fragment.DeliveryAppointmentFragment;
import com.std.logisticapp.ui.fragment.DeliveryComplaintFragment;
import com.std.logisticapp.ui.fragment.DeliveryComplaintListFragment;
import com.std.logisticapp.ui.fragment.DeliveryDetailFragment;
import com.std.logisticapp.ui.fragment.DeliverySignFragment;
import com.std.logisticapp.ui.fragment.MessageDetailFragment;
import com.std.logisticapp.ui.fragment.MessageListFragment;

import dagger.Component;

/**
 * Created by Maik on 2016/5/9.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, DeliveryModule.class})
public interface DeliveryComponent extends ActivityComponent {
    void inject(DeliveryDetailFragment deliveryDetailFragment);
    void inject(DeliveryAppointmentFragment deliveryAppointmentFragment);
    void inject(DeliveryComplaintFragment deliveryComplaintFragment);
    void inject(DeliverySignFragment deliverySignFragment);
    void inject(DeliveryComplaintListFragment deliveryComplaintListFragment);
}
