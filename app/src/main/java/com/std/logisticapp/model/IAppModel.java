package com.std.logisticapp.model;

import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.bean.VersionBean;

import com.squareup.okhttp.ResponseBody;
import rx.Observable;

/**
 * Created by Maik on 2016/5/3.
 */
public interface IAppModel {

    Observable<ResultBean<VersionBean>> getVersion();
    //Observable<ResponseBody> downloadAPK();
}
