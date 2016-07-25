package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.core.mvp.MvpView;

/**
 * Created by Maik on 2016/4/27.
 */
public interface LoginView extends MvpView {
    String getUserCode();
    void setUserCode(String userCode);
    String getPasswd();
    void setPasswd(String passwd);
    Boolean getRemember();
    void setRemeber(Boolean isRemeber);
    void showLoginProgress();
    void hideLoginProgress();
    void enterMain();
}
