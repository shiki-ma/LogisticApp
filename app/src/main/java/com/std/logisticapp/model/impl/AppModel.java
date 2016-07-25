package com.std.logisticapp.model.impl;

import com.std.logisticapp.LogisticApplication;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.VersionBean;
import com.std.logisticapp.logistic.LogisticMain;
import com.std.logisticapp.logistic.LogisticService;
import com.std.logisticapp.model.IAppModel;

import com.squareup.okhttp.ResponseBody;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Maik on 2016/5/3.
 */
public class AppModel implements IAppModel {
    private static final AppModel instance = new AppModel();

    public static AppModel getInstance() {
        return instance;
    }

    private AppModel() {
        //LogisticApplication.getApplicationComponent().inject(this);
    }

    @Override
    public Observable<ResultBean<VersionBean>> getVersion() {
        return LogisticMain.getInstance().getLogisticService().getVersion();
    }

//    @Override
//    public Observable<ResponseBody> downloadAPK() {
//        return LogisticMain.getInstance().getLogisticService().receiptOrders("");
//    }
}
