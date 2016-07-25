package com.std.logisticapp.core.mvp;

/**
 * Created by Maik on 2016/4/29.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);
    void detachView();
}
