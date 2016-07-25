package com.std.logisticapp.model;

import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;

import rx.Observable;

/**
 * Created by Maik on 2016/4/25.
 */
public interface ILoginModel {
    Observable<ResultBean<UserBean>> login(String usercode, String password);
}
