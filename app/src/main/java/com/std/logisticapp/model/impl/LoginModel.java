package com.std.logisticapp.model.impl;

import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.UserBean;
import com.std.logisticapp.logistic.LogisticMain;
import com.std.logisticapp.model.ILoginModel;

import rx.Observable;

/**
 * Created by Maik on 2016/4/28.
 */
public class LoginModel implements ILoginModel {
    private static final LoginModel instance = new LoginModel();

    public static LoginModel getInstance() {
        return instance;
    }

    private LoginModel() {
    }

    @Override
    public Observable<ResultBean<UserBean>> login(String usercode, String password) {
        return LogisticMain.getInstance().getLogisticService().login(usercode, password);
    }
}
