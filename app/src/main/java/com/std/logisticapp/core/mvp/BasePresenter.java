package com.std.logisticapp.core.mvp;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Maik on 2016/4/29.
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {
    private T mMvpView;
    public CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(T mvpView) {
        this.mMvpView = mvpView;
        this.mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
        this.mCompositeSubscription.unsubscribe();
        this.mCompositeSubscription = null;
    }

    public T getMvpView() {
        return mMvpView;
    }
}
