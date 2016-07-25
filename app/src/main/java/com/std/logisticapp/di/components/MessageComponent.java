package com.std.logisticapp.di.components;

import com.std.logisticapp.di.PerActivity;
import com.std.logisticapp.di.modules.ActivityModule;
import com.std.logisticapp.di.modules.MessageModule;
import com.std.logisticapp.ui.fragment.MessageDetailFragment;
import com.std.logisticapp.ui.fragment.MessageListFragment;

import dagger.Component;

/**
 * Created by Maik on 2016/5/9.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MessageModule.class})
public interface MessageComponent extends ActivityComponent {
    void inject(MessageListFragment messageListFragment);
    void inject(MessageDetailFragment messageDetailFragment);
}
