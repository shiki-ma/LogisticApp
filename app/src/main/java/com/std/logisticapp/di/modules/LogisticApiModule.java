package com.std.logisticapp.di.modules;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.logistic.LogisticService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Maik on 2016/4/27.
 */
@Module
public class LogisticApiModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(7000, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(LogisticApi.BASE_URL)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    protected LogisticService provideLogisticService(Retrofit retrofit) {
        return retrofit.create(LogisticService.class);
    }
}
