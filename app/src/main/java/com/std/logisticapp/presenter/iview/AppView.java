package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.core.mvp.MvpView;

/**
 * Created by Maik on 2016/5/3.
 */
public interface AppView extends MvpView {

    void setAppVersion(String versionId);
    Integer getAppVersion();
    void showUpdateDialog(String title, String desc, String url);
    void enterHome();
}
