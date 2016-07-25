package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.bean.ProfileBean;
import com.std.logisticapp.core.mvp.MvpView;

/**
 * Created by Maik on 2016/5/18.
 */
public interface ProfileView extends MvpView {
    void renderProfileData(ProfileBean profile);
}
