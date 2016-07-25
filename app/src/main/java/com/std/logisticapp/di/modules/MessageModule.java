package com.std.logisticapp.di.modules;

import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Maik on 2016/5/12.
 */
@Module
public class MessageModule {
    private MessageBean message;

    public MessageModule() {}

    public MessageModule(MessageBean message) {
        this.message = message;
    }

    @Provides
    @PerActivity
    MessageBean provideMessage() {
        return this.message;
    }
}
