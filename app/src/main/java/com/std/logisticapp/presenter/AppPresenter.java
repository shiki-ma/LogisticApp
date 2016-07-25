package com.std.logisticapp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.shiki.okttp.OkHttpUtils;
import com.shiki.okttp.callback.FileCallback;
import com.shiki.utils.ApkUtils;
import com.shiki.utils.DeviceUtils;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.VersionBean;
import com.std.logisticapp.core.mvp.BasePresenter;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.model.impl.AppModel;
import com.std.logisticapp.presenter.iview.AppView;

import com.squareup.okhttp.ResponseBody;
import com.std.logisticapp.ui.activity.SplashActivity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Maik on 2016/5/3.
 */
public class AppPresenter extends BasePresenter<AppView> {
    private AppModel appModel;

    public AppPresenter() {
        this.appModel = AppModel.getInstance();
    }

    public void checkUpdate() {
        this.mCompositeSubscription.add(Observable.timer(2, TimeUnit.SECONDS)
                .flatMap(new Func1<Long, Observable<ResultBean<VersionBean>>>() {
                    @Override
                    public Observable<ResultBean<VersionBean>> call(Long aLong) {
                        return appModel.getVersion();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean<VersionBean>>() {

                    @Override
                    public void onCompleted() {
                        AppPresenter.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        AppPresenter.this.getMvpView().enterHome();
                    }

                    @Override
                    public void onNext(ResultBean<VersionBean> resultBean) {
                        if (resultBean.getStatusCode().equals(LogisticApi.SUCCESS_DATA)) {
                            Integer versionId = resultBean == null ? 0 : resultBean.getResultData().getVersionCode();
                            if (AppPresenter.this.getMvpView().getAppVersion() < versionId) {
                                AppPresenter.this.getMvpView().showUpdateDialog("发现新版本:" + resultBean.getResultData().getVersionName(), resultBean.getResultData().getVersionDesc(), resultBean.getResultData().getVersionUrl());
                            } else {
                                AppPresenter.this.getMvpView().enterHome();
                            }
                        } else {
                            AppPresenter.this.getMvpView().enterHome();
                        }
                    }
                }));
    }
}
