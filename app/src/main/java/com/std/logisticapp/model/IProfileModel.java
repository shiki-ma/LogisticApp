package com.std.logisticapp.model;

import com.std.logisticapp.bean.ProfileBean;
import com.std.logisticapp.bean.ResultBean;

import rx.Observable;

/**
 * Created by Maik on 2016/5/18.
 */
public interface IProfileModel {
    Observable<ResultBean<ProfileBean>> getProfileData(String expressId);
}
