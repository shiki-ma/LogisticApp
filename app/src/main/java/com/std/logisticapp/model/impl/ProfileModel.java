package com.std.logisticapp.model.impl;

import com.std.logisticapp.bean.ProfileBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.logistic.LogisticMain;
import com.std.logisticapp.model.IProfileModel;

import rx.Observable;

/**
 * Created by Maik on 2016/5/18.
 */
public class ProfileModel implements IProfileModel {
    private static final ProfileModel instance = new ProfileModel();

    public static ProfileModel getInstance() {
        return instance;
    }

    private ProfileModel() {
    }

    @Override
    public Observable<ResultBean<ProfileBean>> getProfileData(String expressId) {
        return LogisticMain.getInstance().getLogisticService().getProfileData(expressId);
    }
}
